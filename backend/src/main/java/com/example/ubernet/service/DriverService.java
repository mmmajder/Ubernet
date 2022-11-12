package com.example.ubernet.service;

import com.example.ubernet.model.Car;
import com.example.ubernet.model.Driver;
import com.example.ubernet.model.DriverDailyActivity;
import com.example.ubernet.model.User;
import com.example.ubernet.repository.DriverRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DriverService {
    private final DriverRepository driverRepository;
    private final DriverDailyActivityService driverDailyActivityService;
    private final UserService userService;
    private final CarService carService;

    public Driver toggleActivity(String email) {
        Driver driver = (Driver) userService.findByEmail(email);
        if (driver == null) {
            return null;
        }
        driver.getDriverDailyActivity().setIsActive(!driver.getDriverDailyActivity().getIsActive());
        driverDailyActivityService.save(driver.getDriverDailyActivity());
        //TODO setPosition()
        driverRepository.save(driver);
        return driver;
    }

    public Driver save(Driver driver) {
        return driverRepository.save(driver);
    }

    public boolean logoutDriver(String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return false;
        }
        Driver driver = (Driver) user;
        DriverDailyActivity driverDailyActivity = driverDailyActivityService.findById(driver.getDriverDailyActivity().getId());
        driverDailyActivity.setIsActive(false);
        Car car = carService.findById(driver.getCar().getId());
        car.setPosition(null);
        carService.save(car);
        driverDailyActivityService.save(driverDailyActivity);
        save(driver);
        return true;
    }
}
