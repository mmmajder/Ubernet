package com.example.ubernet.controller;


import com.example.ubernet.dto.CancelRideRequest;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideDenialType;
import com.example.ubernet.model.enums.UserRole;
import com.example.ubernet.service.RideDenialService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RideDenialController2Test {

    @Value(value = "${local.server.port}")
    private int port;

    @MockBean
    private RideDenialService rideDenialService;

    @Autowired
    private TestRestTemplate restTemplate;

    private final long rideDenialId = 100L;
    private final long rideId = 101L;
    private final String reason = "Problem!";
    private final RideDenialType rideDenialType = RideDenialType.PROBLEM;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void greetingShouldReturnDefaultMessage() throws Exception {

        RideDenial expectedRideDenial = createRideDenial();
        CancelRideRequest cancelRideRequest = createCancelRideRequest();

//        Mockito.when(rideDenialService.createRideDenial(rideDenialId, cancelRideRequest)).thenReturn(expectedRideDenial);

        ResponseEntity<RideDenial> realRideDenial = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/ride-denial/" + rideId, cancelRideRequest, RideDenial.class);
        JsonNode root = objectMapper.readTree(String.valueOf(realRideDenial.getBody()));

        assertEquals(HttpStatus.OK, realRideDenial.getStatusCode());
        assertNotNull(realRideDenial.getBody());
        assertEquals(expectedRideDenial, realRideDenial.getBody());
    }

    private CancelRideRequest createCancelRideRequest() {
        return CancelRideRequest.builder()
                .reason(reason)
                .build();
    }

    private RideDenial createRideDenial() {
        return RideDenial.builder()
                .id(rideDenialId)
                .reason(reason)
                .deleted(false)
                .ride(createRide())
                .rideDenialType(rideDenialType)
                .build();
    }

    private Ride createRide() {
        Ride ride = new Ride();
        ride.setId(rideId);
        ride.setReservation(false);
        ride.setCustomers(List.of(createCustomer()));
        ride.setDriver(createDriver());
        return ride;
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setRole(UserRole.CUSTOMER);
        customer.setBlocked(false);
        customer.setDeleted(false);
        customer.setCity("Novi Sad");
        customer.setActive(false);
        customer.setNumberOfTokens(1000);
        customer.setEmail("pera@gmail.com");
        customer.setName("Pera");
        customer.setSurname("Peric");
        customer.setPhoneNumber("+381 1234657");
        customer.setUserAuth(createUserAuth());
        return customer;
    }

    private Driver createDriver() {
        Driver driver = new Driver();
        driver.setRole(UserRole.DRIVER);
        driver.setBlocked(false);
        driver.setDeleted(false);
        driver.setCity("Novi Sad");
        driver.setEmail("driver@gmail.com");
        driver.setName("Driver");
        driver.setSurname("Driveric");
        driver.setPhoneNumber("+381 1234657");
        driver.setUserAuth(createUserAuth());
        return driver;
    }

    private UserAuth createUserAuth() {
        UserAuth userAuth = new UserAuth();
        userAuth.setIsEnabled(true);
        return userAuth;
    }
}
