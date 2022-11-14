package com.example.ubernet.controller;

import com.example.ubernet.dto.DriverResponse;
import com.example.ubernet.model.Driver;
import com.example.ubernet.service.DriverService;
import com.example.ubernet.utils.DTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/driver", produces = MediaType.APPLICATION_JSON_VALUE)
public class DriverController {
    private final DriverService driverService;

    @PutMapping("/toggle-activity/{email}")
    public ResponseEntity<DriverResponse> toggleActivity(@PathVariable String email) {
        Driver driver = driverService.toggleActivity(email);
        if (driver == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(DTOMapper.getDriverResponse(driver));
    }

    @PutMapping("/logout-driver/{email}")
    public ResponseEntity<String> logoutDriver(@PathVariable String email) {
        if (driverService.logoutDriver(email)) {
            return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
        }
        return new ResponseEntity<>("There was a problem logging out", HttpStatus.CONFLICT);
    }
}
