package com.example.ubernet.repository;

import com.example.ubernet.model.DriverNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DriverNotificationRepository extends JpaRepository<DriverNotification, Long> {
    @Query(value = "SELECT driverNotification FROM DriverNotification driverNotification WHERE driverNotification.ride.driver.email=:email AND driverNotification.isFinished=false")
    List<DriverNotification> getActiveRideDriverNotifications(String email);
}
