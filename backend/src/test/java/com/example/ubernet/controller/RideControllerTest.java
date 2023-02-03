package com.example.ubernet.controller;

import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc //need this in Spring Boot test
public class RideControllerTest {

    private static Long INVALID_ID = 1L;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RideService rideService;
    @MockBean
    StartRideService startRideService;
    @MockBean
    EndRideService endRideService;
    @MockBean
    CarApproachRideService carApproachRideService;
    @MockBean
    AcceptRequestSplitFairService acceptRequestSplitFairService;
    @MockBean
    CreateRideService createRideService;
    @MockBean
    SimpMessagingService simpMessagingService;

    @Test
    public void getByInvalidIdShouldReturnNull() throws Exception {
        Mockito.when(rideService.findById(INVALID_ID))
                .thenReturn(null);

        mockMvc.perform(get("/ride/{id}", INVALID_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void getByValidIdShouldReturnOK() throws Exception {
        Ride ride = new Ride();
        ride.setId(2L);

        Mockito.when(rideService.findById(ride.getId()))
                .thenReturn(ride);

        mockMvc.perform(get("/ride/{id}", ride.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)));
    }

    @Test
    public void startRideInvalidIdShouldReturnBadRequest() throws Exception {
        Mockito.when(startRideService.startRide(INVALID_ID))
                .thenThrow(new BadRequestException("Ride does not exist"));

        mockMvc.perform(put("/ride/start-ride/{rideId}", INVALID_ID))
                .andDo(print())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ride does not exist"));
    }

    @Test
    public void startRideValidIdShouldReturnOK() throws Exception {
        Ride ride = new Ride();
        ride.setId(2L);
        ride.setRideState(RideState.TRAVELLING);
        LocalDateTime now = LocalDateTime.of(2023,2,1,21,34,26,7772);
        ride.setActualStart(now);

        Mockito.when(startRideService.startRide(ride.getId()))
                .thenReturn(ride);

        mockMvc.perform(put("/ride/start-ride/{rideId}", ride.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rideState", is(ride.getRideState().toString())))
                .andExpect(jsonPath("$.actualStart", is(List.of(2023,2,1,21,34,26,7772))))
                .andExpect(jsonPath("$.id", is(2)));
    }

    @Test
    public void endRideInvalidIdShouldReturnBadRequest() throws Exception {
        Mockito.when(endRideService.endRide(INVALID_ID))
                .thenThrow(new BadRequestException("Ride does not exist"));

        mockMvc.perform(put("/ride/end-ride/{rideId}", INVALID_ID))
                .andDo(print())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ride does not exist"));
    }

    @Test
    public void endRideValidIdShouldReturnOK() throws Exception {
        Ride ride = new Ride();
        ride.setId(2L);
        ride.setRideState(RideState.FINISHED);

        Mockito.when(endRideService.endRide(ride.getId()))
                .thenReturn(ride);

        mockMvc.perform(put("/ride/end-ride/{rideId}", ride.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rideState", is(ride.getRideState().toString())))
                .andExpect(jsonPath("$.id", is(2)));
    }

    @Test
    public void getByInvalidCustomerEmailShouldReturnBadRequest() throws Exception {
        String email = "invalidCustomer@gmail.com";
        Mockito.when(rideService.findCurrentRideForClient(email))
                .thenThrow(new BadRequestException("Customer with this email does not exist"));

        mockMvc.perform(get("/ride/find-scheduled-route-navigation-client/{email}", email))
                .andDo(print())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Customer with this email does not exist"));
    }

    @Test
    public void getByValidCustomerEmailButNoRouteShouldReturnNull() throws Exception {
        String email = "customer@gmail.com";
        Mockito.when(rideService.findCurrentRideForClient(email))
                .thenReturn(null);

        mockMvc.perform(get("/ride/find-scheduled-route-navigation-client/{email}", email))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void getByValidCustomerEmailShouldReturnCurrentRide() throws Exception {
        String email = "customer@gmail.com";
        CurrentRide ride = new CurrentRide();
        ride.setId(2L);

        Mockito.when(rideService.findCurrentRideForClient(email))
                .thenReturn(ride);

        mockMvc.perform(get("/ride/find-scheduled-route-navigation-client/{email}", email))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)));
    }

    @Test
    public void getByValidCustomerEmailButNoCurrentRideShouldReturnNull() throws Exception {
        String email = "customer@gmail.com";

        Mockito.when(rideService.findCurrentRideForClient(email))
                .thenReturn(null);

        mockMvc.perform(get("/ride/find-scheduled-route-navigation-client/{email}", email))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void shouldReturnBadRequestForIncorrecUrlWhenAcceptingSplitFare() throws Exception {
        String url = "incorrect.url";
        Mockito.doThrow(new BadRequestException("Url is incorrect")).when(acceptRequestSplitFairService).acceptSplitFare(url);

        mockMvc.perform(put("/ride/accept-request-split-fare/{url}", url))
                .andDo(print())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Url is incorrect"));
    }

    @Test
    public void shouldReturnBadRequestForAlreadyActiveUserWhenAcceptingSplitFare() throws Exception {
        String url = "already.active.customer.url";
        Mockito.doThrow(new BadRequestException("Customer can only have one ride at the time.")).when(acceptRequestSplitFairService).acceptSplitFare(url);

        mockMvc.perform(put("/ride/accept-request-split-fare/{url}", url))
                .andDo(print())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Customer can only have one ride at the time."));
    }

    @Test
    public void shouldReturnBadRequestForAlreadyPayedWhenAcceptingSplitFare() throws Exception {
        String url = "already.payed.customer.url";
        Mockito.doThrow(new BadRequestException("Payment has already been accepted")).when(acceptRequestSplitFairService).acceptSplitFare(url);

        mockMvc.perform(put("/ride/accept-request-split-fare/{url}", url))
                .andDo(print())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Payment has already been accepted"));
    }

    @Test
    public void shouldReturnBadRequestForAlreadyPassedReservationWhenAcceptingSplitFare() throws Exception {
        String url = "already.passed.reservation.url";
        Mockito.doThrow(new BadRequestException("Reservation time has passed! You are not able to get on this ride any more.")).when(acceptRequestSplitFairService).acceptSplitFare(url);

        mockMvc.perform(put("/ride/accept-request-split-fare/{url}", url))
                .andDo(print())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Reservation time has passed! You are not able to get on this ride any more."));
    }
}
