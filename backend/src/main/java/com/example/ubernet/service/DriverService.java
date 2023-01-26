package com.example.ubernet.service;

import com.example.ubernet.dto.DriverDto;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.repository.CarRepository;
import com.example.ubernet.repository.DriverRepository;
import com.example.ubernet.repository.DriverActivityPeriodRepository;
import com.example.ubernet.utils.DTOMapper;
import com.example.ubernet.utils.EntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DriverService {
    private final DriverRepository driverRepository;
    private final DriverDailyActivityService driverDailyActivityService;
    private final UserService userService;
    private final CarRepository carRepository;
    private final DriverActivityPeriodRepository intervalRepository;
    private final DriverNotificationService driverNotificationService;

    public Driver toggleActivity(String email, boolean activate) {
        Driver driver = (Driver) userService.findByEmail(email);
        if (driver == null) throw new BadRequestException("Driver with this email does not exist");
        if (activate) activateDriver(driver);
        else deactivateDriver(driver);
        return driver;
    }

    public Driver findByEmail(String email) {
        return driverRepository.findByEmail(email);
    }

    public Driver save(User user) {
        return driverRepository.save(EntityMapper.mapToDriver(user));
    }

    private void activateDriver(Driver driver) {
        DriverDailyActivity driverDailyActivity = driver.getDriverDailyActivity();
        driverDailyActivity.setIsActive(true);
        driverDailyActivity.setLastPeriodStart(LocalDateTime.now());
        driverDailyActivityService.save(driverDailyActivity);
    }

    public void deactivateDriver(Driver driver) {
        if (!driver.getCar().getIsAvailable())
            throw new BadRequestException("You are unable to set inactive during the active ride");
        DriverDailyActivity driverDailyActivity = driver.getDriverDailyActivity();
        driverDailyActivity.setIsActive(false);
        driverDailyActivity.setPeriodsInLast24h(updatePeriodsInLast24h(driverDailyActivity));
        driverDailyActivity.setLastPeriodStart(null);
        driverDailyActivityService.save(driverDailyActivity);
    }

    public void logoutDriver(String email) {
        Driver driver = driverRepository.findByEmail(email);
        deactivateDriver(driver);
        Car car = driver.getCar();
        car.setIsAvailable(false);
        carRepository.save(car);
    }

    private List<DriverActivityPeriod> updatePeriodsInLast24h(DriverDailyActivity driverDailyActivity) {
        List<DriverActivityPeriod> periods = driverDailyActivity.getPeriodsInLast24h();
        DriverActivityPeriod newInterval = new DriverActivityPeriod(driverDailyActivity.getLastPeriodStart(), LocalDateTime.now());
        intervalRepository.save(newInterval);
        if (periods == null) periods = List.of(newInterval);
        else periods.add(newInterval);
        return periods;
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

    public void loginDriver(String email) {
        Driver driver = findByEmail(email);
        Car car = driver.getCar();
        car.setIsAvailable(true);
        carRepository.save(car);
        activateDriver(driver);
    }

    public void updateDriverActivity() {
        List<Driver> activeDrivers = driverRepository.getActiveDrivers();
        for (Driver driver : activeDrivers) {
            updateIntervals(driver.getDriverDailyActivity());
        }
    }

    private void updateIntervals(DriverDailyActivity driverDailyActivity) {
        List<DriverActivityPeriod> periodsInLast24h = driverDailyActivity.getPeriodsInLast24h();
        if (periodsInLast24h == null) return;
        for (DriverActivityPeriod interval : periodsInLast24h) {
            if (interval.getStartOfPeriod().isAfter(LocalDateTime.now().minusDays(1))) break;
            if (interval.getEndOfPeriod().isBefore(LocalDateTime.now().minusDays(1))) periodsInLast24h.remove(interval);
            else {
                interval.setStartOfPeriod(LocalDateTime.now());
                intervalRepository.save(interval);
                break;
            }
        }
        driverDailyActivityService.save(driverDailyActivity);
    }

    public boolean driverIsLoggedForMoreThan8HoursInLast24Hours(Driver driver) {
        DriverDailyActivity driverDailyActivity = driver.getDriverDailyActivity();
        long numberOfActiveSeconds = getNumberOfActiveHoursInLast24h(driverDailyActivity.getPeriodsInLast24h(), driverDailyActivity.getLastPeriodStart());
        driverNotificationService.sendNumberOfWorkingSecondsToDriver(numberOfActiveSeconds, driver.getEmail());
        return numberOfActiveSeconds > 8 * 60 * 60; // h * m * s
    }

    public DriverDto getDriverByEmail(String email) {
        Driver driver = findByEmail(email);
        return DTOMapper.getDriverDTO(driver);
    }

    public long getNumberOfActiveHoursInLast24h(String email) {
        Driver driver = findByEmail(email);
        if (driver == null) throw new BadRequestException("Driver with this email does not exist");
        DriverDailyActivity driverDailyActivity = driver.getDriverDailyActivity();
        return getNumberOfActiveHoursInLast24h(driverDailyActivity.getPeriodsInLast24h(), driverDailyActivity.getLastPeriodStart());
    }

    private long getNumberOfActiveHoursInLast24h(List<DriverActivityPeriod> intervals, LocalDateTime lastPeriodStart) {
        long totalDurationInSeconds = 0;
        for (DriverActivityPeriod interval : intervals) {
            totalDurationInSeconds += interval.getStartOfPeriod().until(interval.getEndOfPeriod(), ChronoUnit.SECONDS);
        }
        if (lastPeriodStart != null)
            totalDurationInSeconds += lastPeriodStart.until(LocalDateTime.now(), ChronoUnit.SECONDS);
        return totalDurationInSeconds;
    }
}
