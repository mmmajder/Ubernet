package com.example.ubernet.service;

import com.example.ubernet.dto.CreateRideDTO;
import com.example.ubernet.model.CurrentRide;
import com.example.ubernet.repository.CurrentRideRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CurrentRideService {

    private final CurrentRideRepository currentRideRepository;

    public CurrentRide save(CurrentRide currentRide) {
        return this.currentRideRepository.save(currentRide);
    }

    public CurrentRide optimizeByPrice(List<CreateRideDTO> createRideDTOs) {
        CurrentRide cheapest = null;
//        for (CreateRideDTO createRideDTO : createRideDTOs) {
//
//        }
//
//        CurrentRide currentRide = new CurrentRide();
        return cheapest;
    }

//    private double calculatePriceForRoute(CreateRideDTO createRideDTO) {
//
//    }
}
