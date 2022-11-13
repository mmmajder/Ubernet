package com.example.ubernet.dto;

import com.example.ubernet.model.DriverDailyActivity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponse {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String city;
    private String phoneNumber;
    private DriverDailyActivity driverDailyActivity;
}
