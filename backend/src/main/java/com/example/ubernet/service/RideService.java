package com.example.ubernet.service;

import com.example.ubernet.dto.CreateRideDTO;
import com.example.ubernet.dto.InstructionDTO;
import com.example.ubernet.dto.LatLngDTO;
import com.example.ubernet.dto.PlaceDTO;
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
    private final RouteService routeService;
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

    public Ride findById(Long id) {
        return rideRepository.findById(id).orElse(null);
    }

    public Ride save(Ride ride) {
        return rideRepository.save(ride);
    }

    @Transactional
    public Ride createRide(CreateRideDTO createRideDTO) {
        Ride ride = new Ride();
        ride.setRideState(getRideStateCreateRide(createRideDTO.isReservation(), createRideDTO.getPassengers().size()));
        ride.setRoute(getRouteCreateRide(createRideDTO));
        ride.setPayment(getPaymentCreateRide(createRideDTO));
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
//        else {
        rideRepository.save(ride);
        ride.setRideRequest(createRideRequest(createRideDTO));
//        }
        return rideRepository.save(ride);
    }

    private RideRequest createRideRequest(CreateRideDTO createRideDTO) {
        RideRequest rideRequest = new RideRequest();
        rideRequest.setCurrentRide(createCurrentRide(createRideDTO));
        rideRequest.setCarType(createRideDTO.getCarType());
        rideRequest.setHasChild(createRideDTO.isHasChild());
        rideRequest.setHasPet(createRideDTO.isHasPet());
        return rideRequestRepository.save(rideRequest);
    }

    private void initRide(CreateRideDTO createRideDTO, Ride ride) {
        Car car = getCarForRide(createRideDTO.getCoordinates().get(0), createRideDTO.isHasPet(), createRideDTO.isHasChild());
        ride.setDriver(car.getDriver());
        CurrentRide currentRide = createCurrentRide(createRideDTO);
        addNavigation(car, currentRide);
        rideRepository.save(ride);
        sendNextRideNotificationToDriver(ride);
    }

    private void sendNextRideNotificationToDriver(Ride ride) {
        DriverNotification driverNotification = new DriverNotification();
        driverNotification.setDriverNotificationType(DriverNotificationType.APPROACH);
        driverNotification.setRide(ride);
        driverNotification.setFinished(false);
        driverNotificationRepository.save(driverNotification);
        this.simpMessagingService.sendNextRideNotification(ride.getDriver().getEmail(), driverNotification);
    }

    private CurrentRide createCurrentRide(CreateRideDTO createRideDTO) {
        CurrentRide currentRide = new CurrentRide();
        currentRide.setPositions(createRidePositions(createRideDTO));
        currentRide.setShouldGetRouteToClient(true);
        currentRide.setNumberOfRoute(createRideDTO.getNumberOfRoute());
        currentRide.setFreeRide(false);
        return currentRideService.save(currentRide);
    }

    private void addNavigation(Car car, CurrentRide currentRide) {
//        private void setCurrentOfFutureRide(CreateRideDTO createRideDTO, Car car) {
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

//        List<Customer> customers = customerService.getCustomersByEmails(createRideDTO.getPassengers());
//        Customer issueCustomer = customerService.getCustomerByEmail(createRideDTO.getPayment().getCustomerThatPayed());
//        customerService.checkIfCustomersCanPay(customers, createRideDTO.getPayment().getTotalPrice() / customers.size(), issueCustomer);
//        double price = createRideDTO.getPayment().getTotalPrice() / (customers.size() + 1);
//        issueCustomer.setNumberOfTokens(issueCustomer.getNumberOfTokens() - price);
//        customerService.save(issueCustomer);

//        Payment payment = new Payment();
//        List<CustomerPayment> customerPayments = new ArrayList<>();
//        CustomerPayment customerPayment = new CustomerPayment();
//        customerPayment.setCustomer(issueCustomer);
//        customerPayment.setPayed(true);
//        customerPaymentRepository.save(customerPayment);
//        customerPayments.add(customerPayment);
//        for (Customer customer : customers) {
//            CustomerPayment customersPayment = new CustomerPayment();
//            customersPayment.setCustomer(customer);
//            customersPayment.setPayed(false);
//            customersPayment.setUrl(RandomString.make(64));
//            customerPaymentRepository.save(customersPayment);
//            customerPayments.add(customersPayment);
//        }
//        for (CustomerPayment pay : customerPayments) {
//            pay.setPricePerCustomer(createRideDTO.getPayment().getTotalPrice() / customerPayments.size());
//        }
//        payment.setCustomers(customerPayments);
//        payment.setDeleted(false);
//        payment.setTotalPrice(createRideDTO.getPayment().getTotalPrice());
//        payment.setIsAcceptedPayment(createRideDTO.getPassengers().size() == 0);
//        paymentRepository.save(payment);
//        try {
//            emailService.sendEmailToOtherPassangers(payment.getCustomers());
//
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }

//        Ride ride = new Ride();
//        ride.setCustomers(customers);
//        rideService.save(ride);
//        return ride;
//        Car car = getCarForRide(createRideDTO);
//        car.setIsAvailable(false);
//        setCurrentOfFutureRide(createRideDTO, car);
//        return setRide(createRideDTO, car);

    private List<Customer> getCustomersCreateRide(List<String> passengers) {
        return customerService.getCustomersByEmails(passengers);
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
//        payment.setCustomers(customerPayments);
//
//        paymentRepository.save(payment);
//        try {
//            emailService.sendEmailToOtherPassangers(payment.getCustomers());
//
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
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
        List<Place> places = new ArrayList<>();
        for (PlaceDTO placeDTO : createRideDTO.getRoute()) {
            Position position = new Position(placeDTO.getPosition().getX(), placeDTO.getPosition().getY());
            positionService.save(position);
            Place place = new Place(placeDTO.getName(), position);
            placeRepository.save(place);
            places.add(place);
        }
        route.setCheckPoints(places);
        routeService.save(route);
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

    private Ride setRide(CreateRideDTO createRideDTO, Car car) {
        Route route = setRoute(createRideDTO);
        List<Customer> customers = customerService.getCustomersByEmails(createRideDTO.getPassengers());
        Driver driver = car.getDriver();
        Ride ride = new Ride();
        ride.setDeleted(false);
        ride.setCustomers(customers);
        ride.setRequestTime(TimeUtils.getDateTimeForReservationMaxFiveHoursMin15MinutesAdvance(createRideDTO.getReservationTime()));
        ride.setRideState(createRideState(ride.getRequestTime()));
        ride.setDriver(driver);
        ride.setRoute(route);
        save(ride);
        return ride;
    }

    private Route setRoute(CreateRideDTO createRideDTO) {
        Route route = new Route();
        route.setDeleted(false);
        route.setPrice(createRideDTO.getTotalDistance() * 120 / 1000 + carTypeService.findCarTypeByName(createRideDTO.getCarType()).getPriceForType());
        route.setTime(createRideDTO.getTotalTime());
        List<Place> places = new ArrayList<>();
        for (PlaceDTO placeDTO : createRideDTO.getRoute()) {
            Position position = new Position(placeDTO.getPosition().getX(), placeDTO.getPosition().getY());
            positionService.save(position);
            Place place = new Place(placeDTO.getName(), position);
            placeRepository.save(place);
            places.add(place);
        }
        route.setCheckPoints(places);

        routeService.save(route);
        return route;
    }

    private void setCurrentOfFutureRide(CreateRideDTO createRideDTO, Car car) {
        CurrentRide currentRide = new CurrentRide();
        currentRide.setPositions(createRidePositions(createRideDTO));
        currentRide.setShouldGetRouteToClient(true);
        currentRide.setNumberOfRoute(createRideDTO.getNumberOfRoute());
        currentRideService.save(currentRide);

//        if (car.getCurrentRide() == null || car.getCurrentRide().isFreeRide()) {
//            car.setCurrentRide(currentRide);
//        } else {
//            car.setFutureRide(currentRide);
//        }
        carService.save(car);
    }

    private Car getCarForRide(LatLngDTO firstPositionOfRide, boolean hasPet, boolean hasChild) {
        Car car = carService.getClosestFreeCar(firstPositionOfRide, hasPet, hasChild);
        if (car == null) {
            car = carService.getClosestCarWhenAllAreNotAvailable(firstPositionOfRide, hasPet, hasChild);
            if (car == null)
                throw new NotFoundException("All cars are not free");
        }
        return car;
    }

    private RideState createRideState(LocalDateTime reservationTime) {
        if (reservationTime.isBefore(LocalDateTime.now().minusMinutes(10))) {
            return RideState.WAITING;
        }
        return RideState.RESERVED;
    }


    private List<PositionInTime> createRidePositions(CreateRideDTO createRideDTO) {
        List<Double> timeSlots = getTimeSlots(createRideDTO);
        List<PositionInTime> positions = new ArrayList<>();
        for (int i = 0; i < timeSlots.size(); i++) {
            Position position = new Position();
            position.setX(createRideDTO.getCoordinates().get(i).getLng());
            position.setY(createRideDTO.getCoordinates().get(i).getLat());
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


    private List<Double> getTimeSlots(CreateRideDTO createRideDTO) {
        List<Integer> distanceSlots = getDistanceSlots(createRideDTO.getCoordinates());
        return calculateTimeSlots(distanceSlots, createRideDTO.getInstructions());
    }

    private List<Double> calculateTimeSlots(List<Integer> distanceSlots, List<InstructionDTO> instructionDTOList) {
        List<Double> timeSlots = new ArrayList<>();
        for (int i = 0; i < distanceSlots.size(); i++) {
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


    public void updateCarRoute(Long carId, CreateRideDTO createRideDTO) {
        Car car = carService.findById(carId);
        if (car == null) {
            throw new NotFoundException("Car does not exist");
        }
        List<PositionInTime> positionsInTime = createRidePositions(createRideDTO);
        CurrentRide approach = new CurrentRide();
        approach.setPositions(positionsInTime);
        approach.setShouldGetRouteToClient(false);
        approach.setFreeRide(false);
        approach.setStartTime(LocalDateTime.now());
        approach.setNumberOfRoute(createRideDTO.getNumberOfRoute());
        currentRideService.save(approach);
        if (car.getNavigation().getSecondRide() == null) {
            car.getNavigation().setApproachFirstRide(approach);
        } else {
            car.getNavigation().setApproachSecondRide(approach);
        }
        navigationRepository.save(car.getNavigation());
    }

    public void acceptSplitFare(String url) {
        CustomerPayment customerPayment = customerPaymentRepository.findByUrl(url);
        if (customerPayment == null) throw new BadRequestException("Url is incorrect");
        if (customerPayment.isPayed()) throw new BadRequestException("Payment has already been accepted");
        Ride ride = rideRepository.getRideByCustomerPaymentURL(url);
        if (ride.isReservation() && reservationPassed(ride.getScheduledStart())) {
            throw new BadRequestException("Reservation time has passed! You are not able to get on this ride any more.");
        }
        acceptSplitFarePay(customerPayment);
        //todo check if all payed
        if (allPassengersPayed(ride.getPayment().getCustomers())) {
            Payment payment = ride.getPayment();
            payment.setIsAcceptedPayment(true);
            paymentRepository.save(payment);
            ride.setRideState(getRideStateAllPassangersPayed(ride));
            save(ride);
            if (ride.getRideState() == RideState.WAITING) {
                //todo inicjalizuj voznju
                setRidePositions(ride);
            }
            //todo notify every one
        }
    }

    public void setRidePositions(Ride ride) {
        RideRequest rideRequest = ride.getRideRequest();
        Car car = getCarForRide(DTOMapper.positionToLatLng(rideRequest.getCurrentRide().getPositions().get(0).getPosition()), rideRequest.isHasPet(), rideRequest.isHasChild());
        ride.setDriver(car.getDriver());
        addNavigation(car, rideRequest.getCurrentRide());
        rideRepository.save(ride);
        this.simpMessagingService.updateRouteForSelectedCar(ride.getDriver().getEmail(), ride);
        sendNextRideNotificationToDriver(ride);
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
        List<Ride> rides = this.rideRepository.getReservedRides();
        List<Ride> res = new ArrayList<>();
        for (Ride ride : rides) {
            if (ride.getScheduledStart().isBefore(LocalDateTime.now().plusMinutes(10))) {
                res.add(ride);
            }
        }
        return res;
    }
}
