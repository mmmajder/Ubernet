package com.example.ubernet.service;

import com.example.ubernet.model.DriverDailyActivity;
import com.example.ubernet.repository.DriverDailyActivityRepository;
import org.springframework.stereotype.Service;

@Service
public class DriverDailyActivityService {
    private final DriverDailyActivityRepository driverDailyActivityRepository;

    public DriverDailyActivityService(DriverDailyActivityRepository driverDailyActivityRepository) {
        this.driverDailyActivityRepository = driverDailyActivityRepository;
    }

    public DriverDailyActivity save(DriverDailyActivity driverDailyActivity) {
        return driverDailyActivityRepository.save(driverDailyActivity);
    }

    public DriverDailyActivity findById(long id) {
        return driverDailyActivityRepository.findById(id).orElse(null);
    }
}
