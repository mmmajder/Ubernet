package com.example.ubernet.service;

import com.example.ubernet.model.Ride;
import com.example.ubernet.repository.RideRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RideService {
    private final RideRepository rideRepository;

    public Ride findById(Long id) {
        return rideRepository.findById(id).orElse(null);
    }

    public Ride save(Ride ride) {
        return rideRepository.save(ride);
    }
}
