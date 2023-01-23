package com.example.ubernet.service;

import com.example.ubernet.model.CustomerPayment;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RideRequestService {
    private final RideService rideService;
    private final CustomerRepository customerRepository;
    private final SimpMessagingService simpMessagingService;

    public void sendCarsToReservations() {
        List<Ride> rides = rideService.getReservedRidesThatShouldStartIn10Minutes();
        for (Ride ride : rides) {
            System.out.println(ride);
            ride.setRideState(RideState.WAITING);
            rideService.setRidePositions(ride);
        }
    }

    public void returnMoneyNotPayedPassedReservations() {
        List<Ride> rides = rideService.getReservedRidesThatWereNotPayedAndScheduledTimePassed();
        for (Ride ride : rides) {
            ride.setRideState(RideState.CANCELED);
            rideService.save(ride);
            List<CustomerPayment> customerPayments = ride.getPayment().getCustomers();
            for (CustomerPayment payment : customerPayments) {
                if (payment.isPayed()) {
                    double price = payment.getPricePerCustomer();
                    payment.getCustomer().setNumberOfTokens(payment.getCustomer().getNumberOfTokens() + price);
                    customerRepository.save(payment.getCustomer());
                    // todo notify customer about payback
                    simpMessagingService.sendPaybackNotification(payment.getCustomer().getEmail(), payment.getCustomer().getNumberOfTokens());
                    System.out.println("Return money");
                    System.out.println(payment.getCustomer().getNumberOfTokens());
                }
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
