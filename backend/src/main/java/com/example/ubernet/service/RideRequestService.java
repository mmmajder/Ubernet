package com.example.ubernet.service;

import com.example.ubernet.model.CustomerPayment;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.CustomerRepository;
import com.example.ubernet.repository.RideRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class RideRequestService {
    private final RideService rideService;
    private final NotificationService notificationService;
    private final PaymentService paymentService;
    @Transactional
    public void sendCarsToReservations() {
        List<Ride> rides = rideService.getReservedRidesThatShouldStartIn10Minutes();
        for (Ride ride : rides) {
            System.out.println(ride);
            ride.setRideState(RideState.WAITING);
            rideService.setRidePositions(ride);
            notificationService.createNotificationForCustomerInitRide(ride);
        }
    }

    public void returnMoneyNotPayedPassedReservations() {
        List<Ride> rides = rideService.getReservedRidesThatWereNotPayedAndScheduledTimePassed();
        for (Ride ride : rides) {
            ride.setRideState(RideState.CANCELED);
            rideService.save(ride);
            List<CustomerPayment> customerPayments = ride.getPayment().getCustomers();
            paymentService.returnMoneyToCustomers(customerPayments);
        }
    }



    public void notifyTimeUntilReservation() {
        List<Ride> ridesWithAcceptedReservation = rideService.getRidesWithAcceptedReservation();
        List<Long> minutesForNotification = Arrays.asList(15L, 10L, 5L);
        for (Ride ride : ridesWithAcceptedReservation) {
            long minutesUntil = LocalDateTime.now().until(ride.getScheduledStart(), ChronoUnit.MINUTES);
            if (minutesForNotification.contains(minutesUntil)) {
                notificationService.createNotificationForCustomersReservationReminder(ride, minutesUntil);
            }
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
