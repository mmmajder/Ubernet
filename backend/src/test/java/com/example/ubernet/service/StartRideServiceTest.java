package com.example.ubernet.service;

import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.RideRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
    RideAlternativesService rideAlternativesRepository;

    private static Long ID = 1111L;
    private static String EMAIL = "email@gmail.com";

    @Test
    @DisplayName("Should return Null when finding by invalid ID")
    public void shouldThrowBadRequestForInvalidId() {
        Mockito.when(rideService.findById(ID))
                .thenReturn(null);

        assertThrowsExactly(BadRequestException.class,
                () -> startRideService.startRide(ID));
        verify(rideService).findById(ID);
        verifyNoMoreInteractions(rideService);
        verifyNoMoreInteractions(driverNotificationService);
        verifyNoMoreInteractions(currentRideService);
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
        CurrentRide currentRide = new CurrentRide();
        Navigation n = new Navigation();


        Mockito.when(rideService.findById(ID))
                .thenReturn(ride);
        Mockito.when(rideService.save(ride))
                .thenReturn(ride);
        Mockito.when(currentRideService.save(currentRide))
                .thenReturn(currentRide);
        Mockito.doNothing().when(driverNotificationService).resetOldNotificationsForRide(car, ride);

        Ride rideToVerify = startRideService.startRide(ID);

        assertEquals(rideToVerify.getRideState(), RideState.TRAVELLING);
        verify(rideService).findById(ID);
        verify(rideService).save(ride);
        verify(currentRideService).save(currentRide);
        verify(driverNotificationService).resetOldNotificationsForRide(car, ride);
        verifyNoMoreInteractions(rideService);
        verifyNoMoreInteractions(driverNotificationService);
        verifyNoMoreInteractions(currentRideService);
    }

//    ride.setRideState(RideState.TRAVELLING);
//        ride.setActualStart(LocalDateTime.now());

//    private Ride updateRide(Long rideId) {
//        Ride ride = rideService.findById(rideId);
//        if (ride == null) throw new BadRequestException("Ride does not exist");
//        ride.setRideState(RideState.TRAVELLING);
//        ride.setActualStart(LocalDateTime.now());
//        rideService.save(ride);
//        return ride;
//    }
}
