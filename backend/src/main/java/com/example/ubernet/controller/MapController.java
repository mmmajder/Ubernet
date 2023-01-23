package com.example.ubernet.controller;

import com.example.ubernet.dto.CarResponse;
import com.example.ubernet.dto.CreateRideDTO;
import com.example.ubernet.dto.SetNewFreeRideDTO;
import com.example.ubernet.model.Car;
import com.example.ubernet.model.CurrentRide;
import com.example.ubernet.service.CurrentRideService;
import com.example.ubernet.utils.DTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<CurrentRide> optimizeByPrice(@Valid @RequestBody List<CreateRideDTO> createRideDTOs) {
        CurrentRide currentRide = currentRideService.optimizeByPrice(createRideDTOs);
        return ResponseEntity.ok(currentRide);
    }
}
