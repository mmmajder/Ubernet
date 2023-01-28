package com.example.ubernet.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Driver extends User {
    @OneToOne
    private Car car;
    @ManyToOne
    private DriverDailyActivity driverDailyActivity;
    private boolean requestedProfileChanges;
}
