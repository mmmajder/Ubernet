package com.example.ubernet.model;

import com.example.ubernet.model.enums.DriverNotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DriverNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    @ManyToOne
    private Ride ride;
    private DriverNotificationType driverNotificationType;
    private boolean isFinished;

}
