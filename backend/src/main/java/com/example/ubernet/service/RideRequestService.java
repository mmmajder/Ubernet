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

    private List<Customer> getCustomersByEmail(List<String> emails) {
        List<Customer> customers = new ArrayList<>();
        for (String email : emails) {
            customers.add(getCustomerByEmail(email));
        }
        return customers;
    }

    private Customer getCustomerByEmail(String clientEmail) {
        Customer customer = customerRepository.findByEmail(clientEmail);
        if (customer == null) {
            throw new BadRequestException("Client with that email does not exist");
        }
        return customer;
    }
}
