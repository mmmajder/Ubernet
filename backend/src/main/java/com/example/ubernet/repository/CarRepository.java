package com.example.ubernet.repository;

import com.example.ubernet.model.Car;
import com.example.ubernet.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT car FROM Car car WHERE car.isAvailable=true and car.driver.driverDailyActivity.isActive=true")
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="0")})
    List<Car> findActiveAvailableCars();

    @Query(value = "SELECT car FROM Car car WHERE car.driver.driverDailyActivity.isActive=true")
    List<Car> findActiveCars();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT car FROM Car car WHERE car.isAvailable=false and car.driver.driverDailyActivity.isActive=true")
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="0")})
    List<Car> findActiveNonAvailableCars();


    Car findByDriver(Driver driver);


//    @Query(value = "SELECT car FROM Car car WHERE car.isAvailable=false and car.driver.driverDailyActivity.isActive=true " +
//            "and car.currentRide.shouldGetRouteToClient=true")
//    List<Car> findActiveNonAvailableCarsNewRoute();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT car FROM Car car WHERE car.isAvailable=false and car.driver.driverDailyActivity.isActive=true " +
            "and car.navigation.secondRide is null")
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="0")})
    List<Car> getActiveNotAvailableNotReservedCars();

}
