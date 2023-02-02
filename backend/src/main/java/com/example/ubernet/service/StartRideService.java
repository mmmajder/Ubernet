package com.example.ubernet.service;

import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.RideAlternativesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Service
@Transactional
public class StartRideService {

    private final RideAlternativesRepository rideAlternativesRepository;
    private final DriverNotificationService driverNotificationService;
    private final CurrentRideService currentRideService;
    private final RideService rideService;
    private final PositionService positionService;
    private final PositionInTimeService positionInTimeService;

    public Ride startRide(Long rideId) {
        Ride ride = updateRide(rideId);
        Car car = ride.getDriver().getCar();
        Navigation navigation = tryToNavigateCarToWrongDirection(ride, car.getNavigation());
        navigation.getFirstRide().setStartTime(LocalDateTime.now());
        currentRideService.save(navigation.getFirstRide());
        driverNotificationService.resetOldNotificationsForRide(car, ride);
        return ride;
    }

    private Ride updateRide(Long rideId) {
        Ride ride = rideService.findById(rideId);
        ride.setRideState(RideState.TRAVELLING);
        ride.setActualStart(LocalDateTime.now());
        rideService.save(ride);
        return ride;
    }

    private Navigation tryToNavigateCarToWrongDirection(Ride ride, Navigation navigation) {
        Random rand = new Random();
        RideAlternatives rideAlternatives = rideAlternativesRepository.getRideAlternativesByRideId(ride.getId());
        navigation.getFirstRide().setPositions(new ArrayList<>());
        for (PathAlternative pathAlternative : rideAlternatives.getAlternatives()) {
            double endOfLastPartTime = 0;
            if (navigation.getFirstRide().getPositions().size() != 0)
                endOfLastPartTime = getSecondsOfLastPath(navigation.getFirstRide().getPositions());
            CurrentRide currentRide = pathAlternative.getAlternatives().get(rand.nextInt(pathAlternative.getAlternatives().size()));
            for (PositionInTime positionInTime : currentRide.getPositions()) {
                Position position = new Position();
                position.setX(positionInTime.getPosition().getX());
                position.setY(positionInTime.getPosition().getY());
                positionService.save(position);
                PositionInTime newPositionInTime = new PositionInTime();
                newPositionInTime.setPosition(position);
                newPositionInTime.setSecondsPast(endOfLastPartTime + positionInTime.getSecondsPast());
                positionInTimeService.save(newPositionInTime);
                navigation.getFirstRide().getPositions().add(newPositionInTime);
                currentRideService.save(navigation.getFirstRide());
            }
//                navigation.getFirstRide().getPositions().addAll(currentRide.getPositions());
        }
        return navigation;
    }

    private double getSecondsOfLastPath(List<PositionInTime> positions) {
        return positions.get(positions.size() - 1).getSecondsPast();

    }
}
