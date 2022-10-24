package com.example.ubernet.controller;

import com.example.ubernet.dto.CarResponse;
import com.example.ubernet.dto.CreateCarDTO;
import com.example.ubernet.dto.DriverResponse;
import com.example.ubernet.model.Car;
import com.example.ubernet.model.Driver;
import com.example.ubernet.service.DriverService;
import com.example.ubernet.utils.DTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/driver", produces = MediaType.APPLICATION_JSON_VALUE)
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PutMapping("/toggle-activity/{email}")
    public ResponseEntity<DriverResponse> toggleActivity(@PathVariable String email) {
        Driver driver = driverService.toggleActivity(email);
        if (driver == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(DTOMapper.getDriverResponse(driver));
    }
}
