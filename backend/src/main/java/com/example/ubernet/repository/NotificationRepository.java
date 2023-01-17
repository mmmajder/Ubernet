package com.example.ubernet.repository;

import com.example.ubernet.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(value = "SELECT notification FROM Notification notification WHERE notification.receiverEmail=:userEmail")
    List<Notification> findAllByEmail(String userEmail);
}
