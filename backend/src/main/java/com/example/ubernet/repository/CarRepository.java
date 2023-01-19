package com.example.ubernet.repository;

import com.example.ubernet.model.Car;
import com.example.ubernet.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    @Query(value = "SELECT car FROM Car car WHERE car.isAvailable=true and car.driver.driverDailyActivity.isActive=true")
    List<Car> findActiveAvailableCars();

    @Query(value = "SELECT car FROM Car car WHERE car.driver.driverDailyActivity.isActive=true")
    List<Car> findActiveCars();

    @Query(value = "SELECT car FROM Car car WHERE car.isAvailable=false and car.driver.driverDailyActivity.isActive=true")
    List<Car> findActiveNonAvailableCars();


    Car findByDriver(Driver driver);


//    @Query(value = "SELECT car FROM Car car WHERE car.isAvailable=false and car.driver.driverDailyActivity.isActive=true " +
//            "and car.currentRide.shouldGetRouteToClient=true")
//    List<Car> findActiveNonAvailableCarsNewRoute();

    @Query(value = "SELECT car FROM Car car WHERE car.isAvailable=false and car.driver.driverDailyActivity.isActive=true " +
            "and car.navigation.secondRide is null")
    List<Car> getActiveNotAvailableNotReservedCars();
}
