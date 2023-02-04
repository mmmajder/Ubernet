package com.example.ubernet.controller;

import com.example.ubernet.dto.DriverChangeRequest;
import com.example.ubernet.dto.DriverChangeResponse;
import com.example.ubernet.dto.DriverDto;
import com.example.ubernet.dto.DriverResponse;
import com.example.ubernet.model.Driver;
import com.example.ubernet.model.ProfileChangesRequest;
import com.example.ubernet.service.DriverService;
import com.example.ubernet.utils.DTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/driver", produces = MediaType.APPLICATION_JSON_VALUE)
public class DriverController {
    private final DriverService driverService;

    @GetMapping("/get-drivers")
    public List<DriverDto> getDrivers() {
        return driverService.getDrivers();
    }

    @GetMapping("/getDriversEmails")
    public ArrayList<String> getDriversEmails() {
        return driverService.getDriversEmails();
    }

    @PutMapping("/toggle-activity/{email}")
    public ResponseEntity<DriverResponse> toggleActivity(@PathVariable String email, @RequestBody boolean driverActive) {
        Driver driver = driverService.toggleActivity(email, driverActive);
        if (driver == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(DTOMapper.getDriverResponse(driver));
    }

    @PutMapping("/update-driver-activity")
    public void updateDriverActivity() {
        driverService.updateDriverActivity();
    }

    @GetMapping("/{email}")
    public ResponseEntity<DriverDto> getDriver(@PathVariable String email) {
        return ResponseEntity.ok(driverService.getDriverByEmail(email));
    }

    @GetMapping("/getFullDriver/{email}")
    public ResponseEntity<DriverChangeResponse> getFullDriver(@PathVariable String email) {
        return ResponseEntity.ok(driverService.getFullDriverByEmail(email));
    }

    @GetMapping("/active-hours/{email}")
    public long getNumberOfActiveHoursInLast24h(@PathVariable String email) {
        return driverService.getNumberOfActiveHoursInLast24h(email);
    }

    @PostMapping("/requestProfileChanges")
    public boolean requestProfileChanges(@RequestBody DriverChangeRequest driverChangeRequest) {
        ProfileChangesRequest request = driverService.createRequest(driverChangeRequest);
        return request != null;
    }

    @GetMapping("/getProfileChangesRequest/{email}")
    public ProfileChangesRequest getProfileChangesRequest(@PathVariable String email) {
        return driverService.getProfileChangesRequest(email);
    }

    @GetMapping("/getProfileChangesRequests")
    public List<ProfileChangesRequest> getProfileChangesRequests() {
        return driverService.getProfileChangesRequests();
    }
}
