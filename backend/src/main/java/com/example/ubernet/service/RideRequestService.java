package com.example.ubernet.service;

import com.example.ubernet.model.CustomerPayment;
import com.example.ubernet.model.Notification;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.enums.RideState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class RideRequestService {
    private final RideService rideService;
    private final NotificationService notificationService;
    private final PaymentService paymentService;
    private final CustomerService customerService;

    @Transactional
    public void sendCarsToRidesInReservedState() {
        List<Ride> rides = rideService.getReservedRidesThatShouldStartIn10Minutes();
        for (Ride ride : rides) {
            ride = rideService.setRidePositions(ride);
            ride.setRideState(RideState.WAITING);
            rideService.save(ride);
            notificationService.createNotificationForCustomerInitRide(ride);
        }
    }

    public void returnMoney() {
        returnMoneyPassedReservations();
        returnMoneyNotPayedPassedReservations();
    }

    public void returnMoneyPassedReservations() {
        List<Ride> rides = rideService.getReservedRidesThatScheduledTimePassed();
        for (Ride ride : rides) {
            cancelRide(ride);
        }
    }

    private void cancelRide(Ride ride) {
        ride.setRideState(RideState.CANCELED);
        rideService.save(ride);
        List<CustomerPayment> customerPayments = ride.getPayment().getCustomers();
        paymentService.returnMoneyToCustomers(customerPayments);
        customerService.deactivateCustomers(ride.getCustomers());
    }

    public void returnMoneyNotPayedPassedReservations() {
        List<Ride> rides = rideService.getReservedRidesThatWereNotPayedAndScheduledTimePassed();
        for (Ride ride : rides) {
            cancelRide(ride);
        }
    }


    public void notifyTimeUntilReservation() {
        List<Ride> ridesWithAcceptedReservation = rideService.getRidesWithAcceptedReservation();
        for (Ride ride : ridesWithAcceptedReservation) {
            createNewReminderNotifications(ride);
        }
    }

    private void createNewReminderNotifications(Ride ride) {
        List<Notification> reminderNotifications = notificationService.getReminderNotificationsForRide(ride);
        long minutesUntil = LocalDateTime.now().until(ride.getScheduledStart(), ChronoUnit.MINUTES);
        if (minutesUntil < 5 && reminderNotifications.size() == 0) return;
        if (minutesUntil <= 15 - reminderNotifications.size() * 5L) {
            notificationService.createNotificationForCustomersReservationReminder(ride, 15 - reminderNotifications.size() * 5L);
        }
    }

//    public RideRequest save(RideRequest rideRequest) {
//        return rideRequestRepository.save(rideRequest);
//    }
//
//    public RideRequest createRideRequest(CreateRideRequestDTO createRideRequestDTO, String clientEmail) {
//        RideRequest rideRequest = new RideRequest();
//        rideRequest.setRide(rideService.createRide(createRideRequestDTO.getRide()));
//        rideRequest.getCustomersThatPayed().add(getCustomerByEmail(clientEmail));
//        rideRequest.setCustomersThatShouldPay(getCustomersByEmail(createRideRequestDTO.getPassengers()));
//        return rideRequestRepository.save(rideRequest);
//    }


}
