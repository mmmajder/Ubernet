package com.example.ubernet.controller;

import com.example.ubernet.dto.*;
import com.example.ubernet.model.Car;
import com.example.ubernet.service.CarService;
import com.example.ubernet.utils.DTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/car", produces = MediaType.APPLICATION_JSON_VALUE)
public class CarController {
    private final CarService carService;
    private final SimpMessagingTemplate simpMessagingTemplate;

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
        List<Car> cars = carService.getActiveCars();
        if (cars == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(DTOMapper.getListActiveCarResponse(cars));
    }

    @GetMapping("/active-available")
    public ResponseEntity<List<ActiveCarResponse>> getActiveAvailableCars() {
        List<Car> cars = carService.getActiveAvailableCars();
        if (cars == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(DTOMapper.getListActiveCarResponse(cars));
    }

    @GetMapping("/active-nonavailable-new-route")
    public ResponseEntity<List<ActiveCarResponse>> getActiveNonAvailableCarsNewRoute() {
        List<Car> cars = carService.getActiveNonAvailableCarsNewRoute();
        if (cars == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(DTOMapper.getListActiveCarResponse(cars));
    }

    @PutMapping("/position")
    public ResponseEntity<ActiveCarResponse> reachedDestination(@RequestBody Long carId) {
        ActiveCarResponse car = carService.reachedDestination(carId);
        if (car == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(car);
    }

    @GetMapping("/{carId}")
    public ResponseEntity<ActiveCarResponse> getCarById(@PathVariable Long carId) {
        ActiveCarResponse car = carService.getPosition(carId);
        if (car == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(car);
    }

    @PutMapping("/update")
    public ResponseEntity<CarResponse> update(@RequestBody CarResponseNoDriver carResponseNoDriver) {
        System.out.println("uusao u update");
        Car car = carService.updateCar(carResponseNoDriver);

        if (car == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        CarResponse resp = new CarResponse(car);

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/driver/{driverEmail}")
    public ResponseEntity<CarResponse> getCarByDriverEmail(@PathVariable String driverEmail) {
        Car car = carService.getCarByDriverEmail(driverEmail);

        if (car == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        CarResponse resp = new CarResponse(car);

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping("/active-driver/{driverEmail}")
    public ResponseEntity<ActiveCarResponse> getActiveCarByDriverEmail(@PathVariable String driverEmail) {
        Car car = carService.getCarByActiveDriverEmail(driverEmail);

        if (car == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(DTOMapper.getActiveCarResponse(car), HttpStatus.OK);
    }

    //New
    @PutMapping("/new-free-ride")
    public ResponseEntity<CarResponse> setNewFreeRide(@RequestBody SetNewFreeRideDTO setNewFreeRideDTO) {
        Car car = carService.setNewFreeRide(DTOMapper.getPositions(setNewFreeRideDTO.getPositions()), setNewFreeRideDTO.getCarId());
        if (car == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(DTOMapper.getCarResponse(car));
    }

    @PutMapping("/new-position")
    public ResponseEntity<List<CarResponse>> setNewPositionForCar() {
        List<Car> cars = carService.setNewPositions();
        List<CarResponse> carsResponse = DTOMapper.getListCarResponse(cars);
        this.simpMessagingTemplate.convertAndSend("/map-updates/update-vehicle-position", carsResponse);
        return ResponseEntity.ok(carsResponse);
    }

}
