package com.example.ubernet.service;

import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.RideAlternativesRepository;
import com.example.ubernet.repository.RideRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testng.annotations.AfterMethod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StartRideServiceTest {
    @InjectMocks
    StartRideService startRideService;
    @Mock
    RideService rideService;
    @Mock
    DriverNotificationService driverNotificationService;
    @Mock
    CurrentRideService currentRideService;
    @Mock
    RideAlternativesRepository rideAlternativesRepository;
    @Mock
    PositionInTimeService positionInTimeService;
    @Mock
    PositionService positionService;

    private static Long ID = 1111L;
    private static String EMAIL = "email@gmail.com";

    @AfterMethod
    public void verifyNoMoreInteractionsAll(){
        verifyNoMoreInteractions(rideService);
        verifyNoMoreInteractions(driverNotificationService);
        verifyNoMoreInteractions(rideAlternativesRepository);
        verifyNoMoreInteractions(positionInTimeService);
        verifyNoMoreInteractions(currentRideService);
        verifyNoMoreInteractions(positionService);
    }

    @Test
    @DisplayName("Should return Null when finding by invalid ID")
    public void shouldThrowBadRequestForInvalidId() {
        Mockito.when(rideService.findById(ID))
                .thenReturn(null);

        assertThrowsExactly(BadRequestException.class,
                () -> startRideService.startRide(ID));
        verify(rideService).findById(ID);
    }

    @Test
    @DisplayName("Should return Ride and set attributes when finding by valid ID")
    public void shouldReturnRideForValidId() {
        Car car = new Car();
        Driver driver = new Driver();
        driver.setCar(car);
        Ride ride = new Ride();
        ride.setId(ID);
        ride.setDriver(driver);
        Navigation n = new Navigation();
        car.setNavigation(n);
        Position position = new Position();
        PositionInTime positionInTime = new PositionInTime();
        positionInTime.setPosition(position);
        CurrentRide currentRide = new CurrentRide();
        currentRide.setPositions(List.of(positionInTime));
        CurrentRide navCurrentRide = new CurrentRide();
        n.setFirstRide(navCurrentRide);
        PathAlternative pathAlternative = new PathAlternative();
        pathAlternative.setAlternatives(List.of(currentRide));
        RideAlternatives rideAlternatives = new RideAlternatives();
        rideAlternatives.setAlternatives(List.of(pathAlternative));

        Mockito.when(rideService.findById(ID))
                .thenReturn(ride);
        Mockito.when(rideService.save(ride))
                .thenReturn(ride);
        Mockito.when(currentRideService.save(navCurrentRide))
                .thenReturn(navCurrentRide);
        Mockito.doNothing().when(driverNotificationService).resetOldNotificationsForRide(car, ride);
        Mockito.when(rideAlternativesRepository.getRideAlternativesByRideId(ID))
                .thenReturn(rideAlternatives);
        Mockito.when(positionService.save(any(Position.class)))
                .thenReturn(position);
        Mockito.when(positionInTimeService.save(any(PositionInTime.class)))
                .thenReturn(positionInTime);

        Ride rideToVerify = startRideService.startRide(ID);

        assertEquals(rideToVerify.getRideState(), RideState.TRAVELLING);
        Mockito.verify(rideService).findById(ID);
        Mockito.verify(rideService).save(ride);
        Mockito.verify(rideAlternativesRepository).getRideAlternativesByRideId(ID);
        verify(positionService).save(any(Position.class));
        verify(positionInTimeService).save(any(PositionInTime.class));
        Mockito.verify(currentRideService, times(2)).save(navCurrentRide);
        Mockito.verify(driverNotificationService).resetOldNotificationsForRide(car, ride);
    }

    @Test
    @DisplayName("Should return Ride and set attributes when finding by valid ID and no alternative rides")
    public void shouldReturnRideForValidIdAndNoAlternativeRides() {
        Car car = new Car();
        Driver driver = new Driver();
        driver.setCar(car);
        Ride ride = new Ride();
        ride.setId(ID);
        ride.setDriver(driver);
        Navigation n = new Navigation();
        car.setNavigation(n);
        CurrentRide currentRide = new CurrentRide();
        n.setFirstRide(currentRide);
        RideAlternatives rideAlternatives = new RideAlternatives();
        rideAlternatives.setAlternatives(List.of());

        Mockito.when(rideService.findById(ID))
                .thenReturn(ride);
        Mockito.when(rideService.save(ride))
                .thenReturn(ride);
        Mockito.when(currentRideService.save(currentRide))
                .thenReturn(currentRide);
        Mockito.doNothing().when(driverNotificationService).resetOldNotificationsForRide(car, ride);
        Mockito.when(rideAlternativesRepository.getRideAlternativesByRideId(ID))
                .thenReturn(rideAlternatives);

        Ride rideToVerify = startRideService.startRide(ID);

        assertEquals(rideToVerify.getRideState(), RideState.TRAVELLING);
        verify(rideService).findById(ID);
        verify(rideService).save(ride);
        verify(rideAlternativesRepository).getRideAlternativesByRideId(ID);
        verify(currentRideService).save(currentRide);
        verify(driverNotificationService).resetOldNotificationsForRide(car, ride);
    }
}
