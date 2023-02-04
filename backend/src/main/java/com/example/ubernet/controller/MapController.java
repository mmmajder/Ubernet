package com.example.ubernet.controller;

import com.example.ubernet.dto.LeafletRouteDTO;
import com.example.ubernet.model.CurrentRide;
import com.example.ubernet.service.CurrentRideService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/map", produces = MediaType.APPLICATION_JSON_VALUE)
public class MapController {

    private final CurrentRideService currentRideService;

    @PutMapping("/optimize-by-price")
    public ResponseEntity<List<LeafletRouteDTO>> optimizeByPrice(@Valid @RequestBody List<List<LeafletRouteDTO>> createRideDTOs) {
        List<LeafletRouteDTO> shortestRoute = currentRideService.optimizeByPrice(createRideDTOs);
        return ResponseEntity.ok(shortestRoute);
    }

    @PutMapping("/optimize-by-time")
    public ResponseEntity<List<LeafletRouteDTO>> optimizeByTime(@Valid @RequestBody List<List<LeafletRouteDTO>> createRideDTOs) {
        List<LeafletRouteDTO> shortestRoute = currentRideService.optimizeByTime(createRideDTOs);
        return ResponseEntity.ok(shortestRoute);
    }
}
