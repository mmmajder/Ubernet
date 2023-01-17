package com.example.ubernet.service;

import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.Customer;
import com.example.ubernet.model.Notification;
import com.example.ubernet.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification save(Notification notification) {
        return this.notificationRepository.save(notification);
    }

    public List<Notification> getNotifications(String userEmail) {
        return notificationRepository.findAllByEmail(userEmail);
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }
}
