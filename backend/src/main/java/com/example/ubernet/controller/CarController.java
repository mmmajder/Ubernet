package com.example.ubernet.controller;

import com.example.ubernet.dto.*;
import com.example.ubernet.model.Car;
import com.example.ubernet.service.CarService;
import com.example.ubernet.utils.DTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/active")
    public ResponseEntity<List<ActiveCarResponse>> getActiveCars() {
        List<ActiveCarResponse> cars = carService.getActiveCars();
        if (cars == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/active-available")
    public ResponseEntity<List<ActiveCarResponse>> getActiveAvailableCars() {
        List<ActiveCarResponse> cars = carService.getActiveAvailableCars();
        if (cars == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(cars);
    }

    @PutMapping("/new-destination")
    public ResponseEntity<CarResponse> setNewDestinationForAvailableCar(@Valid @RequestBody SetNewDestinationDTO setNewDestinationDTO) {
        Car car = carService.setNewDestination(setNewDestinationDTO);
        if (car == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(DTOMapper.getCarResponse(car));
    }

    @PutMapping("/future-positions")
    public ResponseEntity<String> setFuturePositions(@Valid @RequestBody FuturePositionsDTO futurePositionsDTO) {
        Car car = carService.setFuturePositions(futurePositionsDTO);
        if (car == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Successfully added future positions");
    }


//    @PutMapping("/positions")
//    public ResponseEntity<> updatePositions(@Valid @RequestBody SetNewDestinationDTO setNewDestinationDTO) {
//        Car car = carService.setNewDestination(setNewDestinationDTO);
//        if (car == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        return ResponseEntity.ok(DTOMapper.getCarResponse(car));
//    }

}
