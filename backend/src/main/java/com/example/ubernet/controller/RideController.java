package com.example.ubernet.controller;

import com.example.ubernet.dto.CreateRideDTO;
import com.example.ubernet.model.Ride;
import com.example.ubernet.service.RideService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/ride", produces = MediaType.APPLICATION_JSON_VALUE)
public class RideController {
    private final RideService rideService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/create")
    public ResponseEntity<Ride> createRide(@Valid @RequestBody CreateRideDTO createRideDTO) {
        Ride ride = rideService.createRide(createRideDTO);
        if (ride == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        this.simpMessagingTemplate.convertAndSend("/map-updates/update-route-for-selected-car", ride);
        return ResponseEntity.ok(ride);
    }

    @PostMapping("/update-car-route/{carId}")
    public void updateCarRoute(@PathVariable Long carId, @RequestBody CreateRideDTO createRideDTO) {
        rideService.updateCarRoute(carId, createRideDTO);
    }
}
