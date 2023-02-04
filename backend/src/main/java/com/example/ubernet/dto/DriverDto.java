package com.example.ubernet.dto;

import com.example.ubernet.model.DriverDailyActivity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverDto {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String city;
    private String phoneNumber;
    public boolean isWorking;
    private boolean blocked;
    private DriverDailyActivity driverDailyActivity;
    private boolean requestedChanges;
}
