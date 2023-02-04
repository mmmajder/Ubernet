package com.example.ubernet.service;

import com.example.ubernet.exception.NotFoundException;
import com.example.ubernet.model.Car;
import com.example.ubernet.model.CurrentRide;
import com.example.ubernet.model.Position;
import com.example.ubernet.model.PositionInTime;
import com.example.ubernet.repository.NavigationRepository;
import com.example.ubernet.repository.PositionInTimeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class NewFreeRideService {
    private final CarService carService;
    private final PositionService positionService;
    private final PositionInTimeRepository positionInTimeRepository;
    private final CurrentRideService currentRideService;
    private final NavigationRepository navigationRepository;

    public Car setNewFreeRide(List<Position> positions, Long carId) {
        Car car = carService.findById(carId);
        if (car == null) {
            throw new NotFoundException("Car with this id does not exist");
        }
        positionService.savePositions(positions);
        car.getNavigation().setFirstRide(createNewFreeRide(positions));
        navigationRepository.save(car.getNavigation());
        return carService.save(car);
    }

    public CurrentRide createNewFreeRide(List<Position> positions) {
        CurrentRide currentRide = new CurrentRide();
        currentRide.setDeleted(false);
        currentRide.setPositions(createPositionsWithEmptyTime(positions));
        currentRide.setFreeRide(true);
        currentRide.setShouldGetRouteToClient(false);
        currentRide.setStartTime(LocalDateTime.now());
        currentRideService.save(currentRide);
        return currentRide;
    }

    private List<PositionInTime> createPositionsWithEmptyTime(List<Position> positions) {
        List<PositionInTime> positionInTimeList = new ArrayList<>();
        for (Position position : positions) {
            PositionInTime positionInTime = new PositionInTime();
            positionInTime.setPosition(position);
            positionInTimeList.add(positionInTime);
            positionInTimeRepository.save(positionInTime);
        }
        return positionInTimeList;
    }
}
