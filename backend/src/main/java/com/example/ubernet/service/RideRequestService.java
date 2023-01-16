package com.example.ubernet.service;

import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.Customer;
import com.example.ubernet.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RideRequestService {
//    private final RideRequestRepository rideRequestRepository;
    private final CustomerRepository customerRepository;
    private final RideService rideService;

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
