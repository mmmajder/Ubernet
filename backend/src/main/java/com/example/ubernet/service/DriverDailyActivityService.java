package com.example.ubernet.service;

import com.example.ubernet.model.DriverDailyActivity;
import com.example.ubernet.repository.DriverDailyActivityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DriverDailyActivityService {
    private final DriverDailyActivityRepository driverDailyActivityRepository;

    public DriverDailyActivity save(DriverDailyActivity driverDailyActivity) {
        return driverDailyActivityRepository.save(driverDailyActivity);
    }

    public DriverDailyActivity findById(long id) {
        return driverDailyActivityRepository.findById(id).orElse(null);
    }
}
