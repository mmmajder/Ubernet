package com.example.ubernet.service;

import com.example.ubernet.dto.CreateRideDTO;
import com.example.ubernet.dto.InstructionDTO;
import com.example.ubernet.dto.LatLngDTO;
import com.example.ubernet.dto.PlaceDTO;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.exception.NotFoundException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.NotificationType;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.CustomerPaymentRepository;
import com.example.ubernet.repository.PaymentRepository;
import com.example.ubernet.repository.PlaceRepository;
import com.example.ubernet.repository.RideRepository;
import com.example.ubernet.utils.MapUtils;
import com.example.ubernet.utils.TimeUtils;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationService notificationService;

    public Ride findById(Long id) {
        return rideRepository.findById(id).orElse(null);
    }

    public Ride save(Ride ride) {
        return rideRepository.save(ride);
    }

    @Transactional
    public Ride createRide(CreateRideDTO createRideDTO) {
        Ride ride = new Ride();
        ride.setRideState(getRideStateCreateRide(createRideDTO));
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
            Car car = getCarForRide(createRideDTO);
            ride.setDriver(car.getDriver());
            // TODO dodeliti vozilu voznju
        }
        return rideRepository.save(ride);

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
    }

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

    private RideState getRideStateCreateRide(CreateRideDTO createRideDTO) {
        if (createRideDTO.isReservation() && createRideDTO.getPassengers().size() == 1) {
            return RideState.RESERVED;
        } else if (!createRideDTO.isReservation() && createRideDTO.getPassengers().size() == 1) {
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
        currentRide.setPositions(createRideDestinations(createRideDTO));
        currentRide.setShouldGetRouteToClient(true);
        currentRide.setNumberOfRoute(createRideDTO.getNumberOfRoute());
        currentRideService.save(currentRide);

        if (car.getCurrentRide() == null || car.getCurrentRide().isFreeRide()) {
            car.setCurrentRide(currentRide);
        } else {
            car.setFutureRide(currentRide);
        }
        carService.save(car);
    }

    private Car getCarForRide(CreateRideDTO createRideDTO) {
        Car car = carService.getClosestFreeCar(createRideDTO);
        if (car == null) {
            car = carService.getClosestCarWhenAllAreNotAvailable(createRideDTO);
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


    private List<PositionInTime> createRideDestinations(CreateRideDTO createRideDTO) {
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
        if (car.getFutureRide() == null) {
            CurrentRide currentRide = car.getCurrentRide();
            List<PositionInTime> newPositions = createRideDestinations(createRideDTO);
            List<PositionInTime> oldPositions = currentRide.getPositions();
            for (PositionInTime positionInTime : oldPositions) {
                positionInTime.setSecondsPast(newPositions.get(newPositions.size() - 1).getSecondsPast() + positionInTime.getSecondsPast());
                positionInTimeService.save(positionInTime);
            }
            currentRide.setPositions(newPositions);
            currentRide.getPositions().addAll(oldPositions);
            currentRide.setShouldGetRouteToClient(false);
            currentRide.setTimeOfStartOfRide(LocalDateTime.now());
            currentRide.setNumberOfRoute(createRideDTO.getNumberOfRoute());
            currentRideService.save(currentRide);
        } else {
            CurrentRide futureRide = car.getFutureRide();
            List<PositionInTime> newPositions = createRideDestinations(createRideDTO);
            List<PositionInTime> oldPositions = futureRide.getPositions();
            for (PositionInTime positionInTime : oldPositions) {
                positionInTime.setSecondsPast(newPositions.get(newPositions.size() - 1).getSecondsPast() + positionInTime.getSecondsPast());
                positionInTimeService.save(positionInTime);
            }
            futureRide.setPositions(newPositions);
            futureRide.getPositions().addAll(oldPositions);
            futureRide.setShouldGetRouteToClient(false);
            futureRide.setTimeOfStartOfRide(LocalDateTime.now());
            futureRide.setNumberOfRoute(createRideDTO.getNumberOfRoute());
            currentRideService.save(futureRide);
        }
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
        if (allPassangersPayed(ride.getPayment().getCustomers())) {
            Payment payment = ride.getPayment();
            payment.setIsAcceptedPayment(true);
            paymentRepository.save(payment);
            ride.setRideState(getRideStateAllPassangersPayed(ride));
            if (ride.getRideState() == RideState.WAITING) {
                //todo inicjalizuj voznju
            }
            //todo notify every one
        }
    }

    private boolean reservationPassed(LocalDateTime scheduledStart) {
        return scheduledStart.isAfter(LocalDateTime.now());
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

    private boolean allPassangersPayed(List<CustomerPayment> customerPayments) {
        for (CustomerPayment customerPayment : customerPayments) {
            if (!customerPayment.isPayed()) return false;
        }
        return true;
    }

    public void notifyCustomers(List<Customer> customers, long rideId) {
        for (int i=1; i< customers.size(); i++) {
            Notification notification = new Notification();
            notification.setOpened(false);
            notification.setText("You have been invited to split fare for ride.");
            notification.setType(NotificationType.SPLIT_FARE);
            notification.setReceiverEmail(customers.get(i).getEmail());
            notification.setRideId(rideId);
            notification.setTimeCreated(LocalDateTime.now());
            notificationService.save(notification);
            this.simpMessagingTemplate.convertAndSend("/notify/split-fare-" + customers.get(i).getEmail(), notification);
        }
    }
}
