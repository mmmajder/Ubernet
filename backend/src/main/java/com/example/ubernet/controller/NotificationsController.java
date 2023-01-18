package com.example.ubernet.controller;

import com.example.ubernet.model.Notification;
import com.example.ubernet.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/notification", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationsController {

    private final NotificationService notificationService;

    @GetMapping("/{userEmail}")
    public ResponseEntity<List<Notification>> getAllNotificationsByUser(@PathVariable String userEmail) {
        List<Notification> notifications = notificationService.getNotifications(userEmail);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<Notification> getAllNotificationsByUser(@PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/is-opened/{email}")
    public ResponseEntity<Boolean> areNotificationSeen(@PathVariable String email) {
        boolean areOpened = notificationService.areNotificationsSeenForUser(email);
        return ResponseEntity.ok(areOpened);
    }

    @PutMapping("/open/{email}")
    public void openNotificationForCustomer(@PathVariable String email) {
        notificationService.openNotificationForCustomer(email);
    }
}
