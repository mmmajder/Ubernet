package com.example.ubernet.service;

import com.example.ubernet.dto.CreateDriverDTO;
import com.example.ubernet.dto.DriverDto;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.UserRole;
import com.example.ubernet.repository.DriverRepository;
import com.example.ubernet.utils.EntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DriverService {
    private final DriverRepository driverRepository;
    private final DriverDailyActivityService driverDailyActivityService;
    private final UserService userService;
    private final CarService carService;
    private final CarTypeService carTypeService;
    private final PositionService positionService;
    private final PasswordEncoder passwordEncoder;

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

    public Driver save(User user) {
        return driverRepository.save(EntityMapper.mapToDriver(user));
    }

    public Driver saveDriver(CreateDriverDTO dto, UserAuth userAuth) {
        Driver driver = new Driver();
        driver.setUserAuth(userAuth);
        driver.setName(dto.getName());
        driver.setBlocked(false);
        driver.setCity(dto.getCity());
        driver.setPhoneNumber(dto.getPhoneNumber());
        driver.setPassword(passwordEncoder.encode(dto.getPassword()));
        driver.setSurname(dto.getSurname());
        driver.setEmail(dto.getEmail());
        driver.setRole(UserRole.DRIVER);
        driver.setDeleted(false);
        DriverDailyActivity dailyActivity = new DriverDailyActivity();
        dailyActivity.setIsActive(false);
        dailyActivity.setDeleted(false);
        dailyActivity.setTotalDuration(0);
        dailyActivity.setLastTimeSetActive(null);
        driverDailyActivityService.save(dailyActivity);
        driver.setDriverDailyActivity(dailyActivity);

        Car car = new Car();
        car.setIsAvailable(true);
        car.setAllowsBaby(dto.getAllowsBaby());
        car.setAllowsPet(dto.getAllowsPet());
        car.setPlates(dto.getPlates());
        car.setCarType(carTypeService.findCarTypeByName(dto.getCarType()));
        car.setName(dto.getCarName());
        car.setPosition(positionService.save(new Position(45.267136, 19.833549)));
        driver.setCar(car);

        carService.save(car);
        car.setDriver(driver);
        driverRepository.save(driver);
        carService.save(car);

        return driver;
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

    public List<DriverDto> getDrivers() {
        List<DriverDto> drivers = new ArrayList<>();
        for (Driver driver : driverRepository.findAll()) {
            drivers.add(DriverDto.builder()
                    .email(driver.getEmail())
                    .name(driver.getName())
                    .surname(driver.getSurname())
                    .city(driver.getCity())
                    .phoneNumber(driver.getPhoneNumber())
                    .isWorking(driver.getDriverDailyActivity().getIsActive())
                    .blocked(driver.getBlocked())
                    .build());
        }
        return drivers;
    }

    public ArrayList<String> getDriversEmails() {
        return (ArrayList<String>) driverRepository.findAll().stream().map(Driver::getEmail).collect(Collectors.toList());
    }
}
