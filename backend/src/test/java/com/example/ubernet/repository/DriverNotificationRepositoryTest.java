package com.example.ubernet.repository;

import com.example.ubernet.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ActiveProfiles("test")
public class DriverNotificationRepositoryTest {
    @Autowired
    private DriverNotificationRepository driverNotificationRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Driver driver = new Driver();
    private Car car = new Car();
    private Ride ride = new Ride();
    private DriverNotification driverNotification = new DriverNotification();

    public void setup(boolean isFinished){
        driver.setEmail("driverrrfast@gmail.com");
        car.setDriver(driver);
        ride.setDriver(driver);
        driverNotification.setId(1);
        driverNotification.setRide(ride);
        driverNotification.setFinished(isFinished);
    }

    @Test
    public void shouldReturnCurrentRideWhenSaving() {
        DriverNotification driverNotification = new DriverNotification();
        driverNotification.setId(1L);

        DriverNotification savedDriverNotification = driverNotificationRepository.save(driverNotification);

        assertEquals(driverNotification.getId(), savedDriverNotification.getId());
    }

    @Test
    public void shouldReturnDriverNotificationsWhenGettingActiveRideAndIsNotFinished() {
        setup(false);

        testEntityManager.persistAndFlush(driver);
        testEntityManager.persistAndFlush(car);
        testEntityManager.persistAndFlush(ride);
        testEntityManager.merge(driverNotification);
        testEntityManager.flush();

        List<DriverNotification> driverNotifications = driverNotificationRepository.getActiveRideDriverNotifications(driver.getEmail());

        assertEquals(1, driverNotifications.size());
        assertEquals(1, driverNotifications.get(0).getId());
        assertEquals(driver.getEmail(), driverNotifications.get(0).getRide().getDriver().getEmail());
    }

    @Test
    public void shouldReturnEmptyDriverNotificationsWhenGettingActiveRideAndIsFinished() {
        setup(true);

        testEntityManager.persistAndFlush(driver);
        testEntityManager.persistAndFlush(car);
        testEntityManager.persistAndFlush(ride);
        testEntityManager.merge(driverNotification);
        testEntityManager.flush();

        List<DriverNotification> driverNotifications = driverNotificationRepository.getActiveRideDriverNotifications(driver.getEmail());

        assertEquals(0, driverNotifications.size());
    }

    @Test
    public void shouldReturnEmptyDriverNotificationsWhenGettingActiveRideByInvalidEmail() {
        setup(true);

        testEntityManager.persistAndFlush(driver);
        testEntityManager.persistAndFlush(car);
        testEntityManager.persistAndFlush(ride);
        testEntityManager.merge(driverNotification);
        testEntityManager.flush();

        List<DriverNotification> driverNotifications = driverNotificationRepository.getActiveRideDriverNotifications("veryveryinvalidemail@gmai.com");

        assertEquals(0, driverNotifications.size());
    }
}
