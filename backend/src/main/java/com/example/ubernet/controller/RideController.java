package com.example.ubernet.controller;

import com.example.ubernet.dto.CreateRideDTO;
import com.example.ubernet.model.CurrentRide;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/ride", produces = MediaType.APPLICATION_JSON_VALUE)
public class RideController {
    private final RideService rideService;
    private final CreateRideService createRideService;
    private final SimpMessagingService simpMessagingService;
    private final CarApproachRideService carApproachRideService;
    private final AcceptRequestSplitFairService acceptRequestSplitFairService;
    private final StartRideService startRideService;
    private final EndRideService endRideService;
    @PostMapping("/create")
    public ResponseEntity<Ride> createRide(@Valid @RequestBody CreateRideDTO createRideDTO) {
        Ride ride = createRideService.createRide(createRideDTO);
        if (ride == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (ride.getRideState() == RideState.WAITING) {
            this.simpMessagingService.updateRouteForSelectedCar(ride.getDriver().getEmail(), ride);
        }
        createRideService.notifyCustomers(ride.getCustomers(), ride.getId());
        return ResponseEntity.ok(ride);
    }

    @PostMapping("/update-car-route/{carId}")
    public void createApproachForRide(@PathVariable Long carId, @RequestBody CreateRideDTO createRideDTO) {
        carApproachRideService.createApproach(carId, createRideDTO);
    }

    @PutMapping("/accept-request-split-fare/{url}")
    public void acceptSplitFare(@PathVariable String url) {
        acceptRequestSplitFairService.acceptSplitFare(url);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ride> getById(@PathVariable Long id) {
        Ride ride = rideService.findById(id);
        return ResponseEntity.ok(ride);
    }

    @PutMapping("/start-ride/{rideId}")
    public ResponseEntity<Ride> startRide(@PathVariable Long rideId) {
        Ride ride = startRideService.startRide(rideId);
        return ResponseEntity.ok(ride);
    }

    @PutMapping("/end-ride/{rideId}")
    public ResponseEntity<Ride> endRide(@PathVariable Long rideId) {
        Ride ride = endRideService.endRide(rideId);
        return ResponseEntity.ok(ride);
    }

    @GetMapping("/find-scheduled-route-navigation-client/{email}")
    public ResponseEntity<CurrentRide> findScheduledRouteForClient(@PathVariable String email) {
        CurrentRide customersRide = rideService.findCurrentRideForClient(email);
        return ResponseEntity.ok(customersRide);
    }
}
