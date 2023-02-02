package com.example.ubernet.controller;

import com.example.ubernet.dto.CancelRideRequest;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideDenialType;
import com.example.ubernet.model.enums.UserRole;
import com.example.ubernet.service.RideDenialService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WebMvcTest(controllers = RideDenialController.class)
@ContextConfiguration(classes = RideDenialController.class, loader = AnnotationConfigContextLoader.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
public class RideDenialControllerTest {

    @MockBean
    private RideDenialService rideDenialService;

    @Autowired
    private MockMvc mockMvc;

    private final long rideDenialId = 100L;
    private final long rideId = 101L;
    private final String reason = "Problem!";
    private final RideDenialType rideDenialType = RideDenialType.PROBLEM;

    @Test
    @DisplayName("Should List All Posts When making GET request to endpoint - /api/posts/")
    public void shouldCreatePost() throws Exception {

        Mockito.when(rideDenialService.createRideDenial(rideDenialId, createCancelRideRequest())).thenReturn(createRideDenial());

        mockMvc.perform(get("/ride-denial/" + rideDenialId))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(rideDenialId)))
                .andExpect(jsonPath("$.ride.id", Matchers.is(rideId)))
                .andExpect(jsonPath("$.reason", Matchers.is(reason)))
                .andExpect(jsonPath("$.rideDenialType", Matchers.is(rideDenialType)));
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