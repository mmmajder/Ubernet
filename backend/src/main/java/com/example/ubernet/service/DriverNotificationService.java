package com.example.ubernet.service;

import com.example.ubernet.model.DriverNotification;
import com.example.ubernet.repository.DriverNotificationRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DriverNotificationService {
    private final DriverNotificationRepository driverNotificationRepository;

    public DriverNotification save(DriverNotification driverNotification) {
        return this.driverNotificationRepository.save(driverNotification);
    }

    public List<DriverNotification> getActiveRideDriverNotifications(String email) {
        return this.driverNotificationRepository.getActiveRideDriverNotifications(email);
    }
}
