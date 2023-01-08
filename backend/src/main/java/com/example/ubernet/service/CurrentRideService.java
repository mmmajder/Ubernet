package com.example.ubernet.service;

import com.example.ubernet.model.CurrentRide;
import com.example.ubernet.repository.CurrentRideRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CurrentRideService {

    private final CurrentRideRepository currentRideRepository;

    public CurrentRide save(CurrentRide currentRide) {
        return this.currentRideRepository.save(currentRide);
    }
}
