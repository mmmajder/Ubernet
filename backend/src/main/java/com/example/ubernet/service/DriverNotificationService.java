package com.example.ubernet.service;

import com.example.ubernet.model.Car;
import com.example.ubernet.model.DriverNotification;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.enums.DriverNotificationType;
import com.example.ubernet.repository.DriverNotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

    public void sendNextRideNotificationToDriver(Ride ride) {
        DriverNotification driverNotification = new DriverNotification();
        driverNotification.setDriverNotificationType(DriverNotificationType.APPROACH);
        driverNotification.setRide(ride);
        driverNotification.setFinished(false);
        driverNotificationRepository.save(driverNotification);
        this.simpMessagingService.sendNextRideNotification(ride.getDriver().getEmail(), driverNotification);
    }

    public void resetOldNotificationsForRide(Car car, Ride ride) {
        List<DriverNotification> driverNotifications = driverNotificationRepository.getActiveRideDriverNotifications(car.getDriver().getEmail());
        for (DriverNotification driverNotification : driverNotifications) {
            if (Objects.equals(ride.getId(), driverNotification.getRide().getId())) {
                driverNotification.setFinished(true);
                driverNotificationRepository.save(driverNotification);
            }
        }
    }
}
