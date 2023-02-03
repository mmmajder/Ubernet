package com.example.ubernet.repository;

import com.example.ubernet.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(value = "SELECT notification FROM Notification notification WHERE notification.receiverEmail=:userEmail AND (NOT notification.type=com.example.ubernet.model.enums.NotificationType.CAR_POSITION)")
    List<Notification> findAllByEmail(String userEmail);
    @Query(value = "SELECT notification FROM Notification notification WHERE notification.rideId=:rideId and notification.type=com.example.ubernet.model.enums.NotificationType.REMINDER")
    List<Notification> getReminderNotificationsForRideId(Long rideId);
}
