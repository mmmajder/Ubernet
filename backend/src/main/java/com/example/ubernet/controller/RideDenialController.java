package com.example.ubernet.controller;

import com.example.ubernet.dto.CancelRideRequest;
import com.example.ubernet.model.RideDenial;
import com.example.ubernet.service.RideDenialService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/ride-denial", produces = MediaType.APPLICATION_JSON_VALUE)
public class RideDenialController {

    private final RideDenialService rideDenialService;

    @PostMapping("/{rideId}")
    public ResponseEntity<RideDenial> createRideDenial(@PathVariable Long rideId, @RequestBody CancelRideRequest cancelRideRequest) {
        RideDenial rideDenial = rideDenialService.createRideDenial(rideId, cancelRideRequest);
        return ResponseEntity.ok(rideDenial);
    }
}
