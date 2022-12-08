package com.example.ubernet.controller;

import com.example.ubernet.dto.CarResponse;
import com.example.ubernet.dto.CreateCarDTO;
import com.example.ubernet.dto.CreateRideDTO;
import com.example.ubernet.model.Car;
import com.example.ubernet.model.Ride;
import com.example.ubernet.service.CarService;
import com.example.ubernet.service.RideService;
import com.example.ubernet.utils.DTOMapper;
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

    @PostMapping("/create")
    public ResponseEntity<Ride> createRide(@Valid @RequestBody CreateRideDTO createRideDTO) {
        Ride ride = rideService.createRide(createRideDTO);
        if (ride == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(ride);
    }
}
