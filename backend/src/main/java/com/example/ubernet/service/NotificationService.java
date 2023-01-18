package com.example.ubernet.service;

import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.Customer;
import com.example.ubernet.model.Notification;
import com.example.ubernet.repository.CustomerRepository;
import com.example.ubernet.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final CustomerRepository customerRepository;
    public Notification save(Notification notification) {
        return this.notificationRepository.save(notification);
    }

    public List<Notification> getNotifications(String userEmail) {
        return notificationRepository.findAllByEmail(userEmail);
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public boolean areNotificationsSeenForUser(String email) {
        for (Notification notification: getNotifications(email)) {
            if (!notification.isOpened()) {
                return false;
            }
        }
        return true;
    }

    public void openNotificationForCustomer(String email) {
        for (Notification notification: getNotifications(email)) {
            if (!notification.isOpened()) {
                notification.setOpened(true);
                save(notification);
            }
        }
    }
}
