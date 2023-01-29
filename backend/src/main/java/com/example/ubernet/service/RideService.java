package com.example.ubernet.service;

import com.example.ubernet.dto.*;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.exception.NotFoundException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.DriverNotificationType;
import com.example.ubernet.model.enums.NotificationType;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.*;
import com.example.ubernet.utils.DTOMapper;
import com.example.ubernet.utils.MapUtils;
import com.example.ubernet.utils.TimeUtils;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
public class RideService {
    private final RideRepository rideRepository;
    private final CarService carService;
    private final CustomerService customerService;
    private final CarTypeService carTypeService;
    private final PositionService positionService;
    private final PositionInTimeService positionInTimeService;
    private final RouteRepository routeRepository;
    private final CurrentRideService currentRideService;
    private final PlaceRepository placeRepository;
    private final EmailService emailService;
    private final CustomerPaymentRepository customerPaymentRepository;
    private final PaymentRepository paymentRepository;
    private final SimpMessagingService simpMessagingService;
    private final NotificationService notificationService;
    private final NavigationRepository navigationRepository;
    private final DriverNotificationRepository driverNotificationRepository;
    private final RideRequestRepository rideRequestRepository;
    private final CustomerRepository customersRepository;
    private final RideAlternativesRepository rideAlternativesRepository;

    @Transactional
    public Ride findById(Long id) {
        return rideRepository.findById(id).orElse(null);
    }

    public Ride save(Ride ride) {
        return rideRepository.save(ride);
    }

    @Transactional
    public Ride createRide(CreateRideDTO createRideDTO) {
        Customer customer = customerService.findByEmail(createRideDTO.getPassengers().get(0));
        if (customer.isActive()) throw new BadRequestException("Active customer can not request another ride!");
        if (customer.getBlocked()) throw new BadRequestException("You are blocked by admin and can not request ride!");
        Ride ride = new Ride();
        ride.setRideState(getRideStateCreateRide(createRideDTO.isReservation(), createRideDTO.getPassengers().size()));
        ride.setRoute(getRouteCreateRide(createRideDTO));
        ride.setRequestTime(LocalDateTime.now());
        ride.setCustomers(getCustomersCreateRide(createRideDTO.getPassengers()));
        ride.setDeleted(false);
        ride.setReservation(createRideDTO.isReservation());
        if (createRideDTO.isReservation()) {
            ride.setScheduledStart(TimeUtils.getDateTimeForReservationMaxFiveHoursMin15MinutesAdvance(createRideDTO.getReservationTime()));
        }
        if (ride.getRideState() == RideState.WAITING) {
            initRide(createRideDTO, ride);
            rideRepository.save(ride);
        }
        rideRepository.save(ride);
        ride.setRideRequest(createRideRequest(createRideDTO));
        ride.setPayment(getPaymentCreateRide(createRideDTO));
        return rideRepository.save(ride);
    }

    private RideRequest createRideRequest(CreateRideDTO createRideDTO) {
        RideRequest rideRequest = new RideRequest();
        rideRequest.setCurrentRide(createCurrentRide(createRideDTO.getCoordinates(), createRideDTO.getInstructions()));
        rideRequest.setCarType(createRideDTO.getCarType());
        rideRequest.setHasChild(createRideDTO.isHasChild());
        rideRequest.setHasPet(createRideDTO.isHasPet());
        return rideRequestRepository.save(rideRequest);
    }

    private void initRide(CreateRideDTO createRideDTO, Ride ride) {
        Car car = getCarForRide(createRideDTO.getCoordinates().get(0), createRideDTO.isHasPet(), createRideDTO.isHasChild(), createRideDTO.getCarType());
        ride.setDriver(car.getDriver());
        CurrentRide currentRide = createCurrentRide(createRideDTO.getCoordinates(), createRideDTO.getInstructions());
        addNavigation(car, currentRide);
        rideRepository.save(ride);
        sendNextRideNotificationToDriver(ride);
        this.notificationService.createNotificationForCustomerInitRide(ride);
    }

    private void sendNextRideNotificationToDriver(Ride ride) {
        DriverNotification driverNotification = new DriverNotification();
        driverNotification.setDriverNotificationType(DriverNotificationType.APPROACH);
        driverNotification.setRide(ride);
        driverNotification.setFinished(false);
        driverNotificationRepository.save(driverNotification);
        this.simpMessagingService.sendNextRideNotification(ride.getDriver().getEmail(), driverNotification);
    }

    public CurrentRide createCurrentRide(List<LatLngDTO> coordinates, List<InstructionDTO> instructions) {
        CurrentRide currentRide = new CurrentRide();
        currentRide.setPositions(createRidePositions(coordinates, instructions));
        currentRide.setShouldGetRouteToClient(true);
        currentRide.setFreeRide(false);
        return currentRideService.save(currentRide);
    }

    private void addNavigation(Car car, CurrentRide currentRide) {
        Navigation navigation = car.getNavigation();
        if (navigation == null) {
            navigation = new Navigation();
        }
        car.setNavigation(navigation);
        car.setIsAvailable(false);
        carService.save(car);
        if (navigation.getFirstRide() == null || navigation.getFirstRide().isFreeRide()) {
            navigation.setFirstRide(currentRide);
        } else {
            navigation.setSecondRide(currentRide);
        }
        navigationRepository.save(navigation);
    }

    private List<Customer> getCustomersCreateRide(List<String> passengers) {
        List<Customer> customers = customerService.getCustomersByEmails(passengers);
        for (Customer customer : customers) {
            if (customer.getBlocked()) throw new BadRequestException("Some of you friends are blocked and you can not request ride for them!");
        }
        return customers;
    }

    private Payment getPaymentCreateRide(CreateRideDTO createRideDTO) {
        Payment payment = new Payment();
        payment.setDeleted(false);
        payment.setTotalPrice(createRideDTO.getPayment().getTotalPrice());
        payment.setIsAcceptedPayment(createRideDTO.getPassengers().size() == 1);
        payment.setCustomers(getCustomerPaymentsCreateRide(createRideDTO));
        paymentRepository.save(payment);
        return payment;
    }

    private List<CustomerPayment> getCustomerPaymentsCreateRide(CreateRideDTO createRideDTO) {
        List<CustomerPayment> customerPayments = new ArrayList<>();
        List<Customer> customers = customerService.getCustomersByEmails(createRideDTO.getPassengers());
        double avgPrice = createRideDTO.getPayment().getTotalPrice() / customers.size();
        customerPayments.add(createCustomerPaymentForIssueCustomerCreateRide(createRideDTO, avgPrice));
        customerPayments.addAll(createCustomerPaymentForInvitedCustomersAndSendMails(customers, avgPrice));
        return customerPayments;
    }

    private List<CustomerPayment> createCustomerPaymentForInvitedCustomersAndSendMails(List<Customer> customers, double avgPrice) {
        List<CustomerPayment> customerPayments = new ArrayList<>();
        for (int i = 1; i < customers.size(); i++) {
            CustomerPayment customerPayment = new CustomerPayment();
            customerPayment.setCustomer(customers.get(i));
            customerPayment.setPayed(false);
            customerPayment.setPricePerCustomer(avgPrice);
            customerPayment.setUrl(RandomString.make(64));
            customerPaymentRepository.save(customerPayment);
            customerPayments.add(customerPayment);
        }
        try {
            emailService.sendEmailToOtherPassangers(customerPayments);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return customerPayments;
    }

    private CustomerPayment createCustomerPaymentForIssueCustomerCreateRide(CreateRideDTO createRideDTO, double avgPrice) {
        Customer issueCustomer = customerService.getCustomerByEmail(createRideDTO.getPayment().getCustomerThatPayed());
        customerService.checkIfCustomersCanPay(avgPrice, issueCustomer);
        issueCustomer.setNumberOfTokens(issueCustomer.getNumberOfTokens() - avgPrice);
        issueCustomer.setActive(true);
        customerService.save(issueCustomer);

        CustomerPayment customerPayment = new CustomerPayment();
        customerPayment.setCustomer(issueCustomer);
        customerPayment.setPayed(true);
        customerPayment.setPricePerCustomer(avgPrice);
        customerPaymentRepository.save(customerPayment);
        return customerPayment;
    }

    private Route getRouteCreateRide(CreateRideDTO createRideDTO) {
        Route route = new Route();
        route.setDeleted(false);
        route.setPrice(createRideDTO.getTotalDistance() / 1000 * 120 + carTypeService.findCarTypeByName(createRideDTO.getCarType()).getPriceForType());
        route.setTime(createRideDTO.getTotalTime());
        route.setKm(createRideDTO.getTotalDistance() / 1000);
        List<Place> places = new ArrayList<>();
        for (PlaceDTO placeDTO : createRideDTO.getRoute()) {
            Position position = new Position(placeDTO.getPosition().getX(), placeDTO.getPosition().getY());
            positionService.save(position);
            Place place = new Place(placeDTO.getName(), position);
            placeRepository.save(place);
            places.add(place);
        }
        route.setCheckPoints(places);
        routeRepository.save(route);
        return route;
    }

    private RideState getRideStateCreateRide(boolean isReservation, int numberOfPassengers) {
        if (isReservation && numberOfPassengers == 1) {
            return RideState.RESERVED;
        } else if (!isReservation && numberOfPassengers == 1) {
            return RideState.WAITING;
        }
        return RideState.REQUESTED;
    }


    private Car getCarForRide(LatLngDTO firstPositionOfRide, boolean hasPet, boolean hasChild, String carTypeName) {
        CarType carType = carTypeService.findCarTypeByName(carTypeName);
        Car car = carService.getClosestFreeCar(firstPositionOfRide, hasPet, hasChild, carType);
        if (car == null) {
            car = carService.getClosestCarWhenAllAreNotAvailable(firstPositionOfRide, hasPet, hasChild, carType);
            if (car == null)
                throw new NotFoundException("All cars are not free");
        }
        return car;
    }

    private List<PositionInTime> createRidePositions(List<LatLngDTO> coordinates, List<InstructionDTO> instructions) {
        List<Double> timeSlots = getTimeSlots(coordinates, instructions);
        List<PositionInTime> positions = new ArrayList<>();
        for (int i = 0; i < timeSlots.size(); i++) {
            Position position = new Position();
            position.setX(coordinates.get(i).getLng());
            position.setY(coordinates.get(i).getLat());
            PositionInTime positionInTime = new PositionInTime();
            positionInTime.setSecondsPast(timeSlots.get(i));
            positionInTime.setPosition(position);
            positions.add(positionInTime);
            positionService.save(position);
            positionInTimeService.save(positionInTime);
        }
        System.out.println(positions);
        return positions;
    }


    private List<Double> getTimeSlots(List<LatLngDTO> coordinates, List<InstructionDTO> instructions) {
        List<Integer> distanceSlots = getDistanceSlots(coordinates);
        return calculateTimeSlots(distanceSlots, instructions);
    }

    private List<Double> calculateTimeSlots(List<Integer> distanceSlots, List<InstructionDTO> instructionDTOList) {
        List<Double> timeSlots = new ArrayList<>();
        for (int i = 0; i < distanceSlots.size(); i++) {
            if (instructionDTOList.size()<=i) break;
            double time = instructionDTOList.get(i).getTime() / distanceSlots.get(i);
            for (int j = 0; j < distanceSlots.get(i); j++) {
                if (timeSlots.size() == 0) {
                    timeSlots.add(time);
                } else {
                    timeSlots.add(timeSlots.get(timeSlots.size() - 1) + time);
                }
            }
        }
        return timeSlots;
    }

    private List<Integer> getDistanceSlots(List<LatLngDTO> coordinatesList) {
        int numberOfCoordinates = 1;
        List<Integer> distanceSlots = new ArrayList<>();
        for (int i = 1; i < coordinatesList.size(); i++) {
            LatLngDTO prevCoordinate = coordinatesList.get(i - 1);
            LatLngDTO coordinate = coordinatesList.get(i);
            double distance = MapUtils.calculateDistance(prevCoordinate.getLat(), prevCoordinate.getLng(), coordinate.getLat(), coordinate.getLng());
            if (distance == 0) {
                distanceSlots.add(numberOfCoordinates);
                numberOfCoordinates = 0;
            }
            numberOfCoordinates += 1;
        }
        return distanceSlots;
    }

    @Transactional
    public void updateCarRoute(Long carId, CreateRideDTO createRideDTO) {
        Car car = carService.findById(carId);
        if (car == null) {
            throw new NotFoundException("Car does not exist");
        }
        List<PositionInTime> positionsInTime = createRidePositions(createRideDTO.getCoordinates(), createRideDTO.getInstructions());
        CurrentRide approach = new CurrentRide();
        approach.setPositions(positionsInTime);
        approach.setShouldGetRouteToClient(false);
        approach.setFreeRide(false);
        if (car.getNavigation().getSecondRide() == null) {
            approach.setStartTime(LocalDateTime.now());
            car.getNavigation().setApproachFirstRide(approach);
        } else {
            car.getNavigation().setApproachSecondRide(approach);
        }
        currentRideService.save(approach);
        navigationRepository.save(car.getNavigation());
    }

    @Transactional
    public void acceptSplitFare(String url) {
        CustomerPayment customerPayment = customerPaymentRepository.findByUrl(url);
        if (customerPayment == null) throw new BadRequestException("Url is incorrect");
        if (customerPayment.getCustomer().isActive())
            throw new BadRequestException("Customer can only have one ride at the time.");
        if (customerPayment.isPayed()) throw new BadRequestException("Payment has already been accepted");
        Ride ride = rideRepository.getRideByCustomerPaymentURL(url);
        if (ride.isReservation() && reservationPassed(ride.getScheduledStart())) {
            throw new BadRequestException("Reservation time has passed! You are not able to get on this ride any more.");
        }
        acceptSplitFarePay(customerPayment);
        if (allPassengersPayed(ride.getPayment().getCustomers())) {
            Payment payment = ride.getPayment();
            payment.setIsAcceptedPayment(true);
            paymentRepository.save(payment);
            ride.setRideState(getRideStateAllPassangersPayed(ride));
            ride.setRequestTime(LocalDateTime.now());
            save(ride);
            this.notificationService.createNotificationForCustomersEveryonePayed(ride);

            if (ride.getRideState() == RideState.WAITING) {
                setRidePositions(ride);
            }
        }
    }


    public void setRidePositions(Ride ride) {
        RideRequest rideRequest = ride.getRideRequest();
        Car car = getCarForRide(DTOMapper.positionToLatLng(rideRequest.getCurrentRide().getPositions().get(0).getPosition()), rideRequest.isHasPet(), rideRequest.isHasChild(), rideRequest.getCarType());
        ride.setDriver(car.getDriver());
        addNavigation(car, rideRequest.getCurrentRide());
        rideRepository.save(ride);
        this.simpMessagingService.updateRouteForSelectedCar(ride.getDriver().getEmail(), ride);
        sendNextRideNotificationToDriver(ride);
        notificationService.createNotificationForCustomerInitRide(ride);
    }

    private boolean reservationPassed(LocalDateTime scheduledStart) {
        return scheduledStart.isBefore(LocalDateTime.now());
    }

    private void acceptSplitFarePay(CustomerPayment customerPayment) {
        Customer customer = customerPayment.getCustomer();
        double price = customerPayment.getPricePerCustomer();
        if (customer.getNumberOfTokens() < price) throw new BadRequestException("You do not have enough tokes.");
        customerPayment.setPayed(true);
        customerPaymentRepository.save(customerPayment);
        customer.setNumberOfTokens(customer.getNumberOfTokens() - price);
        customer.setActive(true);
        customerService.save(customer);
    }

    private RideState getRideStateAllPassangersPayed(Ride ride) {
        if (ride.isReservation()) {
            if (ride.getScheduledStart().isAfter(LocalDateTime.now().plusMinutes(10)))
                return RideState.RESERVED;
        }
        return RideState.WAITING;
    }

    private boolean allPassengersPayed(List<CustomerPayment> customerPayments) {
        for (CustomerPayment customerPayment : customerPayments) {
            if (!customerPayment.isPayed()) return false;
        }
        return true;
    }

    public void notifyCustomers(List<Customer> customers, long rideId) {
        for (int i = 1; i < customers.size(); i++) {
            Notification notification = new Notification();
            notification.setOpened(false);
            notification.setText("You have been invited to split fare for ride.");
            notification.setType(NotificationType.SPLIT_FARE);
            notification.setReceiverEmail(customers.get(i).getEmail());
            notification.setRideId(rideId);
            notification.setTimeCreated(LocalDateTime.now());
            notificationService.save(notification);
            this.simpMessagingService.notifyCustomersSplitFair(customers.get(i).getEmail(), notification);
        }
    }

    public List<Ride> getReservedRidesThatShouldStartIn10Minutes() {
        List<Ride> rides = this.rideRepository.getReservedWithStatusReservedRides();
        List<Ride> res = new ArrayList<>();
        for (Ride ride : rides) {
            if (ride.getScheduledStart().isBefore(LocalDateTime.now().plusMinutes(10))) {
                res.add(ride);
            }
        }
        return res;
    }

    public List<Ride> getRidesWithAcceptedReservation() {
        return this.rideRepository.getAcceptedReservationsThatCarDidNotComeYet();
    }

    @Transactional
    public Ride startRide(Long rideId) {
        Ride ride = findById(rideId);
        ride.setRideState(RideState.TRAVELLING);
        ride.setActualStart(LocalDateTime.now());
        save(ride);
        Car car = ride.getDriver().getCar();
        Navigation navigation = car.getNavigation();
        Random rand = new Random();
        double randNum = rand.nextDouble();
        System.out.println("Random generated value: " + randNum);
        if (randNum > 0) {
            System.out.println("Setting possibly wrong");
            RideAlternatives rideAlternatives = rideAlternativesRepository.getRideAlternativesByRideId(ride.getId());
            navigation.getFirstRide().setPositions(new ArrayList<>());
            for (PathAlternative pathAlternative : rideAlternatives.getAlternatives()) {
                CurrentRide currentRide = pathAlternative.getAlternatives().get(rand.nextInt(pathAlternative.getAlternatives().size()));
                for (PositionInTime positionInTime : currentRide.getPositions()) {
                    Position position = new Position();
                    position.setX(positionInTime.getPosition().getX());
                    position.setY(positionInTime.getPosition().getY());
                    positionService.save(position);
                    PositionInTime newPositionInTime = new PositionInTime();
                    newPositionInTime.setPosition(position);
                    newPositionInTime.setSecondsPast(positionInTime.getSecondsPast());
                    positionInTimeService.save(newPositionInTime);
                    navigation.getFirstRide().getPositions().add(newPositionInTime);
//                    currentRideService.save(navigation.getFirstRide());
                }
//                navigation.getFirstRide().getPositions().addAll(currentRide.getPositions());
            }
        }
        navigation.getFirstRide().setStartTime(LocalDateTime.now());
        currentRideService.save(navigation.getFirstRide());

        resetOldNotificationsForRide(car, ride);
        return ride;
    }

    private void resetOldNotificationsForRide(Car car, Ride ride) {
        List<DriverNotification> driverNotifications = driverNotificationRepository.getActiveRideDriverNotifications(car.getDriver().getEmail());
        for (DriverNotification driverNotification : driverNotifications) {
            if (ride.getId() == driverNotification.getRide().getId()) {
                driverNotification.setFinished(true);
                driverNotificationRepository.save(driverNotification);
            }
        }
    }

    @Transactional
    public Ride endRide(Long rideId) {
        Ride ride = findById(rideId);
        if (ride == null) throw new BadRequestException("Ride does not exist");
        ride.setRideState(RideState.FINISHED);
        save(ride);
        setCustomersToInactive(ride.getCustomers());
        Car car = ride.getDriver().getCar();
        Navigation navigation = ride.getDriver().getCar().getNavigation();
        if (navigation.getSecondRide() != null) {
            navigation.setFirstRide(navigation.getSecondRide());
            navigation.setSecondRide(null);
            navigation.setApproachFirstRide(navigation.getApproachSecondRide());
            navigation.setApproachSecondRide(null);
            navigation.getApproachFirstRide().setStartTime(LocalDateTime.now());
            currentRideService.save(navigation.getApproachFirstRide());
        } else {
            navigation.setFirstRide(null);
            car.setIsAvailable(true);
            carService.save(car);
        }
        navigationRepository.save(navigation);
        resetOldNotificationsForRide(car, ride);
        return ride;
    }

    private void setCustomersToInactive(List<Customer> customers) {
        customers = customers.stream()
                .peek(customer -> customer.setActive(false))
                .collect(Collectors.toList());
        customersRepository.saveAll(customers);
    }

    public List<Ride> getReservedRidesThatWereNotPayedAndScheduledTimePassed() {
        List<Ride> rides = rideRepository.getReservedRidesThatWithStatusRequestedAndScheduledStartIsNotNull();
        rides.removeIf(ride -> ride.getScheduledStart().isAfter(LocalDateTime.now()));
        return rides;
    }

    @Transactional
    public CurrentRide findCurrentRouteForClient(String email) {
        Customer customer = customerService.findByEmail(email);
        if (customer == null) throw new BadRequestException("Customer with this email does not exist");
        Ride activeRide = rideRepository.findActiveRideForCustomer(email);
        if (activeRide == null) return null;
        return activeRide.getRideRequest().getCurrentRide();
    }


}
