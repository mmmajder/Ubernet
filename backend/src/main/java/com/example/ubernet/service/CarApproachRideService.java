package com.example.ubernet.service;

import com.example.ubernet.dto.CreateRideDTO;
import com.example.ubernet.exception.NotFoundException;
import com.example.ubernet.model.Car;
import com.example.ubernet.model.CurrentRide;
import com.example.ubernet.model.Navigation;
import com.example.ubernet.model.PositionInTime;
import com.example.ubernet.repository.NavigationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class CarApproachRideService {

    private final CarService carService;
    private final CurrentRideService currentRideService;
    private final NavigationRepository navigationRepository;
    private final RideService rideService;

    public void createApproach(Long carId, CreateRideDTO createRideDTO) {
        Car car = carService.findById(carId);
        if (car == null) {
            throw new NotFoundException("Car does not exist");
        }
        List<PositionInTime> positionsInTime = rideService.getRidePositions(createRideDTO.getCoordinates(), createRideDTO.getInstructions());
        createApproachForRide(car.getNavigation(), positionsInTime);
    }

    private void createApproachForRide(Navigation navigation, List<PositionInTime> positionsInTime) {
        CurrentRide approach = new CurrentRide();
        approach.setPositions(positionsInTime);
        approach.setShouldGetRouteToClient(false);
        approach.setFreeRide(false);
        if (navigation.getSecondRide() == null) {
            approach.setStartTime(LocalDateTime.now());
            navigation.setApproachFirstRide(approach);
        } else {
            navigation.setApproachSecondRide(approach);
        }
        currentRideService.save(approach);
        navigationRepository.save(navigation);
    }
}
