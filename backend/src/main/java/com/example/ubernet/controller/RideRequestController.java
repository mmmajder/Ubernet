package com.example.ubernet.controller;

import com.example.ubernet.dto.CreateRideDTO;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.service.RideRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/ride-request", produces = MediaType.APPLICATION_JSON_VALUE)
public class RideRequestController {

    private final RideRequestService rideRequestService;

    @PutMapping("/send-cars-to-reservations")
    public void sendCarsToReservations() {
        rideRequestService.sendCarsToReservations();
    }
}
