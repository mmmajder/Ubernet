package com.example.ubernet.model;

import com.example.ubernet.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    private String text;
    private boolean opened;
    private NotificationType type;
    private String receiverEmail;
    private Long rideId;
    private LocalDateTime timeCreated;
    private String driverEmail;
}
