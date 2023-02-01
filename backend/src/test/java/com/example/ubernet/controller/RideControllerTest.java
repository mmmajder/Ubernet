package com.example.ubernet.controller;

import com.example.ubernet.dto.CreateRideDTO;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.exception.NotFoundException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@ActiveProfiles("test")
//@ExtendWith(MockitoExtension.class)
//@WebMvcTest(RideController.class)
//@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
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
        Mockito.when(rideService.findCurrentRouteForClient(email))
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
        Mockito.when(rideService.findCurrentRouteForClient(email))
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

        Mockito.when(rideService.findCurrentRouteForClient(email))
                .thenReturn(ride);

        mockMvc.perform(get("/ride/find-scheduled-route-navigation-client/{email}", email))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)));
    }

    @Test
    public void getByValidCustomerEmailButNoCurrentRideShouldReturnNull() throws Exception {
        String email = "customer@gmail.com";

        Mockito.when(rideService.findCurrentRouteForClient(email))
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
