package com.example.ubernet.controller;

import com.example.ubernet.dto.LeafletRouteDTO;
import com.example.ubernet.model.RideAlternatives;
import com.example.ubernet.service.RideAlternativesService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/ride-alternatives", produces = MediaType.APPLICATION_JSON_VALUE)
public class RideAlternativeController {

    private RideAlternativesService rideAlternativesService;

    @PostMapping("/{rideId}")
    public void addAlternativesOfRide(@PathVariable Long rideId, @RequestBody List<List<LeafletRouteDTO>> alternatives) {
        rideAlternativesService.createAlternativesForRide(rideId, alternatives);
    }
}
