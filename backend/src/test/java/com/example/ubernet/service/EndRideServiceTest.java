package com.example.ubernet.service;

import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.CustomerRepository;
import com.example.ubernet.repository.NavigationRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testng.annotations.AfterMethod;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EndRideServiceTest {
    @InjectMocks
    EndRideService endRideService;
    @Mock
    RideService rideService;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    CarService carService;
    @Mock
    NavigationRepository navigationRepository;
    @Mock
    DriverNotificationService driverNotificationService;
    @Mock
    CurrentRideService currentRideService;

    private static Long ID = 1111L;
    Car car = new Car();
    Driver driver = new Driver();
    Customer c = new Customer();
    Ride ride = new Ride();



    @AfterMethod
    public void verifyNoMoreInteractionsAll(){
        verifyNoMoreInteractions(rideService);
    }

    private void setup(Navigation navigation){
        c.setActive(true);
        car.setNavigation(navigation);
        driver.setCar(car);
        ride.setId(ID);
        ride.setRideState(RideState.FINISHED);
        ride.setCustomers(List.of(c));
        ride.setDriver(driver);
    }

    @Test
    @DisplayName("Should return Null when finding by invalid ID")
    public void shouldThrowBadRequestForInvalidId() {
        Mockito.when(rideService.findById(ID))
                .thenReturn(null);

        assertThrowsExactly(BadRequestException.class,
                () -> endRideService.endRide(ID));
        verify(rideService).findById(ID);
    }

    @Test
    @DisplayName("Should return Ride with inactive Customers when finding by valid ID")
    public void shouldReturnRideWithInactiveCustomersWhenEndingByValidId() {
        Navigation navigation =  new Navigation();
        navigation.setSecondRide(null);
        setup(navigation);

        Mockito.when(rideService.findById(ID))
                .thenReturn(ride);
        Mockito.doNothing().when(rideService).updateRideStatus(ride, RideState.FINISHED);
        Mockito.when(customerRepository.saveAll(ArgumentMatchers.<Customer>anyList()))
                .thenReturn(List.of(c));
        Mockito.when(carService.save(car))
                .thenReturn(car);
        Mockito.when(navigationRepository.save(any(Navigation.class)))
                .thenReturn(navigation);
        Mockito.doNothing().when(driverNotificationService).resetOldNotificationsForRide(car, ride);

        Ride endedRide = endRideService.endRide(ID);

        assertEquals(endedRide.getRideState(), RideState.FINISHED);
        assertFalse(c.isActive());
        verify(rideService).findById(ID);
        verify(rideService).updateRideStatus(ride, RideState.FINISHED);
        verify(customerRepository).saveAll(ArgumentMatchers.<Customer>anyList());
        verify(carService).save(car);
        verify(navigationRepository).save(any(Navigation.class));
        verifyNoInteractions(currentRideService);
    }

    @Test
    @DisplayName("Should return Ride which continues when finding by valid ID and has second ride")
    public void shouldReturnRideWhichContinuesWhenEndingByValidIdAndHasSecondRide() {
        Navigation navigation =  new Navigation();
        CurrentRide secondRide = new CurrentRide();
        navigation.setSecondRide(secondRide);
        navigation.setApproachSecondRide(secondRide);
        setup(navigation);

        Mockito.when(rideService.findById(ID))
                .thenReturn(ride);
        Mockito.doNothing().when(rideService).updateRideStatus(ride, RideState.FINISHED);
        Mockito.when(customerRepository.saveAll(ArgumentMatchers.<Customer>anyList()))
                .thenReturn(List.of(c));
        Mockito.when(navigationRepository.save(any(Navigation.class)))
                .thenReturn(navigation);
        Mockito.when(currentRideService.save(secondRide))
                .thenReturn(secondRide);
        Mockito.doNothing().when(driverNotificationService).resetOldNotificationsForRide(car, ride);

        Ride endedRide = endRideService.endRide(ID);

        assertEquals(navigation.getFirstRide(), secondRide);
        assertNull(navigation.getSecondRide());
//        assertTrue(c.isActive());
        verify(rideService).findById(ID);
        verify(rideService).updateRideStatus(ride, RideState.FINISHED);
        verify(customerRepository).saveAll(ArgumentMatchers.<Customer>anyList());
        verify(currentRideService).save(any(CurrentRide.class));
        verify(navigationRepository).save(any(Navigation.class));
        verifyNoInteractions(carService);
    }

}
