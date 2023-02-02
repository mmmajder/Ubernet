package com.example.ubernet.service;

import com.example.ubernet.dto.CreateRideDTO;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.CurrentRide;
import com.example.ubernet.model.Customer;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.RideRequest;
import com.example.ubernet.repository.RideRepository;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testng.annotations.BeforeMethod;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RideServiceTest {

    @InjectMocks
    RideService rideService;
    @Mock
    RideRepository rideRepository;
    @Mock
    CustomerService customerService;

    private static Long ID = 1111L;
    private static String EMAIL = "email@gmail.com";

    @Test
    @DisplayName("Should return Null when finding by invalid ID")
    public void shouldReturnNullWhenFindingByInvalidId() {
        Optional<Ride> o = Optional.empty();
        Mockito.when(rideRepository.findById(ID))
                .thenReturn(o);

        Ride ride = rideService.findById(ID);
        assertNull(ride);
        verify(rideRepository).findById(ID);
        verifyNoMoreInteractions(rideRepository);
    }

    @Test
    @DisplayName("Should return Ride when finding by valid ID")
    public void shouldReturnRideWhenFindingByValidId() {
        Ride r = new Ride();
        r.setId(ID);
        Optional<Ride> o = Optional.of(r);
        Mockito.when(rideRepository.findById(ID))
                .thenReturn(o);

        Ride ride = rideService.findById(ID);
        assertEquals(ride.getId(), ID);
        verify(rideRepository).findById(ID);
        verifyNoMoreInteractions(rideRepository);
    }

    @Test
    @DisplayName("Should throw Bad Request Null when finding by invalid email")
    public void shouldReturnNullWhenFindingCustomerByInvalidEmail() {
        Mockito.when(customerService.findByEmail(EMAIL))
                .thenReturn(null);

        assertThrowsExactly(BadRequestException.class,
                () -> rideService.findCurrentRouteForClient(EMAIL));
        verify(customerService).findByEmail(EMAIL);
        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(rideRepository);
    }

    @Test
    @DisplayName("Should throw Bad Request when finding by valid email but no active ride")
    public void shouldReturnNullWhenFindingActiveRideByValidEmailButNoActiveRide() {
        Customer c = new Customer();
        c.setEmail(EMAIL);

        Mockito.when(customerService.findByEmail(EMAIL))
                .thenReturn(c);
        Mockito.when(rideRepository.findActiveRideForCustomer(EMAIL))
                .thenReturn(null);

        CurrentRide activeRide = rideService.findCurrentRouteForClient(EMAIL);

        assertNull(activeRide);
        verify(customerService).findByEmail(EMAIL);
        verify(rideRepository).findActiveRideForCustomer(EMAIL);
        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(rideRepository);
    }

    @Test
    @DisplayName("Should return CurrentRide when finding by valid email and has active ride")
    public void shouldReturnCurrentRideWhenFindingByValidEmailAndHasActiveRide() {
        Customer c = new Customer();
        c.setEmail(EMAIL);

        CurrentRide currentRide = new CurrentRide();
        currentRide.setId(ID);
        RideRequest rideRequest = new RideRequest();
        rideRequest.setCurrentRide(currentRide);
        Ride activeRide = new Ride();
        activeRide.setRideRequest(rideRequest);

        Mockito.when(customerService.findByEmail(EMAIL))
                .thenReturn(c);
        Mockito.when(rideRepository.findActiveRideForCustomer(EMAIL))
                .thenReturn(activeRide);

        CurrentRide cRide = rideService.findCurrentRouteForClient(EMAIL);

        assertEquals(cRide.getId(), activeRide.getRideRequest().getCurrentRide().getId());
        verify(customerService).findByEmail(EMAIL);
        verify(rideRepository).findActiveRideForCustomer(EMAIL);
        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(rideRepository);
    }
}
