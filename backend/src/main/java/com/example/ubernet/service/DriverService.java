package com.example.ubernet.service;

import com.example.ubernet.model.Driver;
import com.example.ubernet.repository.DriverRepository;
import org.springframework.stereotype.Service;

@Service
public class DriverService {
    private final DriverRepository driverRepository;
    private final DriverDailyActivityService driverDailyActivityService;
    private final UserService userService;

    public DriverService(DriverRepository driverRepository, DriverDailyActivityService driverDailyActivityService, UserService userService) {
        this.driverRepository = driverRepository;
        this.driverDailyActivityService = driverDailyActivityService;
        this.userService = userService;
    }

    public Driver toggleActivity(String email) {
        Driver driver = (Driver) userService.findByEmail(email);
        if (driver==null) {
            return null;
        }
        driver.getDriverDailyActivity().setIsActive(!driver.getDriverDailyActivity().getIsActive());
        driverDailyActivityService.save(driver.getDriverDailyActivity());
        driverRepository.save(driver);
        return driver;
    }
}
