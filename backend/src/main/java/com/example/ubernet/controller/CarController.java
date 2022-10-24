package com.example.ubernet.controller;

import com.example.ubernet.dto.CarResponse;
import com.example.ubernet.dto.CreateCarDTO;
import com.example.ubernet.dto.JwtAuthenticationRequest;
import com.example.ubernet.dto.LoginResponseDTO;
import com.example.ubernet.model.Car;
import com.example.ubernet.service.AuthentificationService;
import com.example.ubernet.service.CarService;
import com.example.ubernet.utils.DTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/car", produces = MediaType.APPLICATION_JSON_VALUE)
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/create-car")
    public ResponseEntity<CarResponse> createCar(@Valid @RequestBody CreateCarDTO createCarDTO) {

        Car car = carService.createCar(createCarDTO);
        if (car == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(DTOMapper.getCarResponse(car));
    }
}
