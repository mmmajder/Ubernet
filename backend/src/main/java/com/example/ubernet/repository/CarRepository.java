package com.example.ubernet.repository;

import com.example.ubernet.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    @Query(value = "SELECT car FROM Car car WHERE car.isAvailable=true and car.driver.driverDailyActivity.isActive=true")
    List<Car> findActiveAvailableCars();

    @Query(value = "SELECT car FROM Car car WHERE car.driver.driverDailyActivity.isActive=true")
    List<Car> findActiveCars();
}
