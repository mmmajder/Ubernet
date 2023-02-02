package com.example.ubernet.service;

import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.Customer;
import com.example.ubernet.model.CustomerPayment;
import com.example.ubernet.model.Payment;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.CustomerPaymentRepository;
import com.example.ubernet.repository.CustomerRepository;
import com.example.ubernet.repository.PaymentRepository;
import com.example.ubernet.repository.RideRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class AcceptRequestSplitFairService {

    private final CustomerPaymentRepository customerPaymentRepository;
    private final RideRepository rideRepository;
    private final RideService rideService;
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final PaymentService paymentService;

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
            updateRideEveryonePayed(ride);
        }
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
        customerRepository.save(customer);
    }

    private boolean allPassengersPayed(List<CustomerPayment> customerPayments) {
        for (CustomerPayment customerPayment : customerPayments) {
            if (!customerPayment.isPayed()) return false;
        }
        return true;
    }

    private void updateRideEveryonePayed(Ride ride) {
        Payment payment = ride.getPayment();
        payment.setIsAcceptedPayment(true);
        paymentRepository.save(payment);
        ride.setRideState(getRideStateAllPassengersPayed(ride));
        ride.setRequestTime(LocalDateTime.now());
        rideRepository.save(ride);
        this.notificationService.createNotificationForCustomersEveryonePayed(ride);
        if (ride.getRideState() == RideState.WAITING) {
            sendCarToCustomers(ride);
        }
    }

    private RideState getRideStateAllPassengersPayed(Ride ride) {
        if (ride.isReservation()) {
            if (ride.getScheduledStart().isAfter(LocalDateTime.now().plusMinutes(10)))
                return RideState.RESERVED;
        }
        return RideState.WAITING;
    }

    public void sendCarToCustomers(Ride ride) {
        try {
            rideService.setRidePositions(ride);
            notificationService.createNotificationForCustomerInitRide(ride);
        } catch (Exception e) {
            if (ride.isReservation() && ride.getScheduledStart().isAfter(LocalDateTime.now())) return;
            rideService.updateRideStatus(ride, RideState.CANCELED);
            setCustomersActive(ride.getCustomers());
            notificationService.createNotificationForCustomersRideDenied(ride);
            paymentService.returnMoneyToCustomers(ride.getPayment().getCustomers());
        }
    }

    private void setCustomersActive(List<Customer> customers) {
        for (Customer customer : customers) {
            customer.setActive(false);
            customerRepository.save(customer);
        }
    }
}
