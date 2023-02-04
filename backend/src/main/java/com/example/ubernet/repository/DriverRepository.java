package com.example.ubernet.repository;

import com.example.ubernet.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Driver findByEmail(String email);
    @Query(value = "SELECT driver FROM Driver driver WHERE driver.driverDailyActivity.isActive=true")
    List<Driver> getActiveDrivers();
}
