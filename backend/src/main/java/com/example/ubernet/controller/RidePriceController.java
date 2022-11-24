package com.example.ubernet.controller;

import com.example.ubernet.dto.PriceEstimationDTO;
import com.example.ubernet.service.RidePriceService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/ride-price", produces = MediaType.APPLICATION_JSON_VALUE)
public class RidePriceController {

    private final RidePriceService ridePriceService;

    @PutMapping()
    public ResponseEntity<Double> calculateEstimatedPrice(@RequestBody PriceEstimationDTO priceEstimationDTO) {
        double price = ridePriceService.calculateEstimatedPrice(priceEstimationDTO);
        return ResponseEntity.ok(price);
    }
}
