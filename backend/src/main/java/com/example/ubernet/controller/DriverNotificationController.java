package com.example.ubernet.controller;

import com.example.ubernet.model.DriverNotification;
import com.example.ubernet.service.DriverNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/driver-notification", produces = MediaType.APPLICATION_JSON_VALUE)
public class DriverNotificationController {

    private final DriverNotificationService driverNotificationService;

    @GetMapping("/{userEmail}")
    public ResponseEntity<List<DriverNotification>> getAllNotificationsByUser(@PathVariable String userEmail) {
        List<DriverNotification> notifications = driverNotificationService.getActiveRideDriverNotifications(userEmail);
        return ResponseEntity.ok(notifications);
    }
}
