package com.example.ubernet.controller;

import com.example.ubernet.model.DriverInconsistency;
import com.example.ubernet.model.DriverNotification;
import com.example.ubernet.model.Ride;
import com.example.ubernet.service.DriverInconsistencyService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/driver-inconsistency", produces = MediaType.APPLICATION_JSON_VALUE)
public class DriverInconsistencyController {

    private final DriverInconsistencyService driverInconsistencyService;

    @PostMapping("/{customerEmail}")
    public ResponseEntity<DriverInconsistency> createDriverInconsistencyComplaint(@PathVariable String customerEmail, @RequestBody Long rideId) {
        DriverInconsistency driverInconsistency = driverInconsistencyService.createDriverInconsistencyComplaint(customerEmail, rideId);
        return ResponseEntity.ok(driverInconsistency);
    }

    @PutMapping("/check-inconsistency")
    public ResponseEntity<List<Ride>> getReportable() {
        List<Ride> reportableRides = driverInconsistencyService.getReportableRides();
        return ResponseEntity.ok(reportableRides);
    }

}
