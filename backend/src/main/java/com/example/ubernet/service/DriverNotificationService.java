package com.example.ubernet.service;

import com.example.ubernet.model.DriverNotification;
import com.example.ubernet.model.enums.DriverNotificationType;
import com.example.ubernet.repository.DriverNotificationRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DriverNotificationService {
    private final DriverNotificationRepository driverNotificationRepository;
    private final SimpMessagingService simpMessagingService;

    public DriverNotification save(DriverNotification driverNotification) {
        return this.driverNotificationRepository.save(driverNotification);
    }

    public List<DriverNotification> getActiveRideDriverNotifications(String email) {
        return this.driverNotificationRepository.getActiveRideDriverNotifications(email);
    }

    public void createNotificationForDriverThatIsActiveTooMuch(String email) {
        DriverNotification driverNotification = new DriverNotification();
        driverNotification.setDriverNotificationType(DriverNotificationType.END_OF_SHIFT);
        driverNotification.setRide(null);
        driverNotification.setFinished(false);
        save(driverNotification);
        simpMessagingService.endShiftDriverNotification(driverNotification, email);
    }

    public void sendNumberOfWorkingSecondsToDriver(long numberOfActiveSeconds, String email) {
        simpMessagingService.sendNumberOfWorkingSecondsToDriver(numberOfActiveSeconds, email);
    }
}
