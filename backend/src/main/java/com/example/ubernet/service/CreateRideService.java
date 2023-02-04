package com.example.ubernet.service;

import com.example.ubernet.dto.CreateRideDTO;
import com.example.ubernet.dto.PlaceDTO;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.NotificationType;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.*;
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
public class CreateRideService {

    private final CustomerRepository customerRepository;
    private final RideRepository rideRepository;
    private final RideRequestRepository rideRequestRepository;
    private final RideService rideService;
    private final DriverNotificationService driverNotificationService;
    private final NotificationService notificationService;
    private final CustomerService customerService;
    private final PaymentRepository paymentRepository;
    private final CustomerPaymentRepository customerPaymentRepository;
    private final PositionService positionService;
    private final EmailService emailService;
    private final CarTypeService carTypeService;
    private final RouteRepository routeRepository;
    private final PlaceRepository placeRepository;
    private final SimpMessagingService simpMessagingService;

    @Transactional
    public Ride createRide(CreateRideDTO createRideDTO) {
        Customer customer = customerRepository.findByEmail(createRideDTO.getPassengers().get(0));
        if (customer.isActive()) throw new BadRequestException("Active customer can not request another ride!");
        if (customer.getBlocked()) throw new BadRequestException("You are blocked by admin and can not request ride!");
        return getNewRide(createRideDTO);
    }

    private Ride getNewRide(CreateRideDTO createRideDTO) {
        Ride ride = new Ride();
        ride.setRideState(getRideState(createRideDTO.isReservation(), createRideDTO.getPassengers().size()));
        ride.setRoute(getRoute(createRideDTO));
        ride.setRequestTime(LocalDateTime.now());
        ride.setCustomers(getCustomers(createRideDTO.getPassengers()));
        ride.setDeleted(false);
        ride.setReservation(createRideDTO.isReservation());
        if (createRideDTO.isReservation()) {
            ride.setScheduledStart(TimeUtils.getDateTimeForReservationMaxFiveHoursMin15MinutesAdvance(createRideDTO.getReservationTime()));
        }
        ride.setRideRequest(createRideRequest(createRideDTO));
        ride.setPayment(getPayment(createRideDTO));
        if (ride.getRideState() == RideState.WAITING) {
            initRide(createRideDTO, ride);
            rideRepository.save(ride);
        }
        rideRepository.save(ride);
        return ride;
    }

    private RideRequest createRideRequest(CreateRideDTO createRideDTO) {
        RideRequest rideRequest = new RideRequest();
        rideRequest.setCurrentRide(rideService.createCurrentRide(createRideDTO.getCoordinates(), createRideDTO.getInstructions()));
        rideRequest.setCarType(createRideDTO.getCarType());
        rideRequest.setHasChild(createRideDTO.isHasChild());
        rideRequest.setHasPet(createRideDTO.isHasPet());
        return rideRequestRepository.save(rideRequest);
    }

    private void initRide(CreateRideDTO createRideDTO, Ride ride) {
        Car car = rideService.getCarForRide(createRideDTO.getCoordinates().get(0), createRideDTO.isHasPet(), createRideDTO.isHasChild(), createRideDTO.getCarType());
        ride.setDriver(car.getDriver());
        CurrentRide currentRide = rideService.createCurrentRide(createRideDTO.getCoordinates(), createRideDTO.getInstructions());
        rideService.addNavigation(car, currentRide);
        rideRepository.save(ride);
        driverNotificationService.sendNextRideNotificationToDriver(ride);
        notificationService.createNotificationForCustomerInitRide(ride);
    }

    private List<Customer> getCustomers(List<String> passengers) {
        List<Customer> customers = customerService.getCustomersByEmails(passengers);
        for (Customer customer : customers) {
            if (customer.getBlocked())
                throw new BadRequestException("Some of you friends are blocked and you can not request ride for them!");
        }
        return customers;
    }

    private Payment getPayment(CreateRideDTO createRideDTO) {
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
        customerPayments.add(createCustomerPaymentForIssueCustomer(createRideDTO, avgPrice));
        customerPayments.addAll(createCustomerPaymentForInvitedCustomersAndSendMails(customers, avgPrice));
        return customerPayments;
    }

    private CustomerPayment createCustomerPaymentForIssueCustomer(CreateRideDTO createRideDTO, double avgPrice) {
        Customer issueCustomer = customerRepository.findByEmail(createRideDTO.getPayment().getCustomerThatPayed());
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
            emailService.sendEmailToOtherPassengers(customers.get(0), customerPayments);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return customerPayments;
    }

    private Route getRoute(CreateRideDTO createRideDTO) {
        Route route = new Route();
        route.setDeleted(false);
        route.setPrice(createRideDTO.getTotalDistance() / 1000 * 120 + carTypeService.findCarTypeByName(createRideDTO.getCarType()).getPriceForType());
        route.setTime(createRideDTO.getTotalTime());
        route.setKm(createRideDTO.getTotalDistance() / 1000);
        route.setCheckPoints(getCheckpoints(createRideDTO.getRoute()));
        routeRepository.save(route);
        return route;
    }

    private List<Place> getCheckpoints(List<PlaceDTO> route) {
        List<Place> places = new ArrayList<>();
        for (PlaceDTO placeDTO : route) {
            Position position = new Position(placeDTO.getPosition().getX(), placeDTO.getPosition().getY());
            positionService.save(position);
            Place place = new Place(placeDTO.getName(), position);
            placeRepository.save(place);
            places.add(place);
        }
        return places;
    }

    private RideState getRideState(boolean isReservation, int numberOfPassengers) {
        if (isReservation && numberOfPassengers == 1) {
            return RideState.RESERVED;
        } else if (!isReservation && numberOfPassengers == 1) {
            return RideState.WAITING;
        }
        return RideState.REQUESTED;
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
}
