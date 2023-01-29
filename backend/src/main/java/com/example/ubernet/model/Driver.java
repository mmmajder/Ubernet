package com.example.ubernet.model;

import com.example.ubernet.model.enums.AuthProvider;
import com.example.ubernet.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends User {
    @OneToOne
    private Car car;
    @ManyToOne
    private DriverDailyActivity driverDailyActivity;
    private boolean requestedProfileChanges;
    public Driver(String email, String name, String surname, Car car) {
        super(email, name, surname);
        this.car = car;
    }

    public Driver(String email) {
        super(email);
    }
}
