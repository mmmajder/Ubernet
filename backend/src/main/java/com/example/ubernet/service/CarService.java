package com.example.ubernet.service;

import com.example.ubernet.dto.ActiveCarResponse;
import com.example.ubernet.dto.CarResponseNoDriver;
import com.example.ubernet.dto.LatLngDTO;
import com.example.ubernet.dto.NavigationDisplay;
import com.example.ubernet.exception.NotFoundException;
import com.example.ubernet.model.Car;
import com.example.ubernet.model.CarType;
import com.example.ubernet.model.Driver;
import com.example.ubernet.model.Navigation;
import com.example.ubernet.repository.CarRepository;
import com.example.ubernet.repository.DriverRepository;
import com.example.ubernet.utils.DTOMapper;
import com.example.ubernet.utils.MapUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class CarService {
    private final CarRepository carRepository;
    private final UserService userService;

    private final DriverRepository driverRepository;
    private final DriverService driverService;


    public Car save(Car car) {
        return carRepository.save(car);
    }

    public Car findById(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    public List<Car> getActiveAvailableCars() {
        return carRepository.findActiveAvailableCars();
    }

    public List<Car> findActiveAvailableCarsLock() {
        return carRepository.findActiveAvailableCarsLock();
    }

    @Transactional
    public List<Car> getActiveCars() {
        return carRepository.findActiveCars();
    }

    public ActiveCarResponse getPosition(Long carId) {
        Car car = findById(carId);
        if (car == null) {
            return null;
        }
        return DTOMapper.getActiveAvailableCar(car);
    }

    public Car getCarByDriverEmail(String email) {
        Driver driver = (Driver) userService.findByEmail(email);
        return carRepository.findByDriver(driver);
    }

    public Car getCarByActiveDriverEmail(String driverEmail) {
        Driver driver = (Driver) userService.findByEmail(driverEmail);
        if (!driver.getDriverDailyActivity().getIsActive()) {
            return null;
        }
        return carRepository.findByDriver(driver);
    }

    public Car updateCar(CarResponseNoDriver carResponseNoDriver) {
        System.out.println("uusao u update u servisu");
        Car car = findById(carResponseNoDriver.getId());
        car.setName(carResponseNoDriver.getName());
        car.setPlates(carResponseNoDriver.getPlates());
        car.setCarType(carResponseNoDriver.getCarType());
        car.setAllowsBaby(carResponseNoDriver.getAllowsBaby());
        car.setAllowsPet(carResponseNoDriver.getAllowsPet());
        return save(car);
    }


    //New
    public Car getClosestFreeCar(LatLngDTO firstPositionOfRide, boolean hasPet, boolean hasChild, CarType carType) {
        List<Car> fittingAvailableCars = findActiveAvailableCarsLock();
        return getClosestCar(firstPositionOfRide, fittingAvailableCars, hasPet, hasChild, carType);
    }

    private Car getClosestCar(LatLngDTO latLngDTO, List<Car> cars, boolean hasPet, boolean hasChild, CarType carType) {
        Car closestCar = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Car car : cars) {
            if (checkIfCarIsNotAppropriate(hasPet, hasChild, carType, car)) continue;
            double distance = MapUtils.calculateDistance(car.getPosition().getX(), car.getPosition().getY(), latLngDTO.getLng(), latLngDTO.getLat()); // switch
            if (distance < minDistance) {
                closestCar = car;
                minDistance = distance;
            }
        }
        return closestCar;
    }

    private boolean carDoesNotSatisfyOrder(Car car, boolean hasPet, boolean hasChild, CarType carType) {
        if (hasPet && !car.getAllowsPet()) return true;
        if (hasChild && !car.getAllowsBaby()) return true;
        return !car.getCarType().getName().equals(carType.getName());
    }

    public Car getClosestCarWhenAllAreNotAvailable(LatLngDTO firstPositionOfRide, boolean hasPet, boolean hasChild, CarType carType) {
        List<Car> activeNotReservedCars = carRepository.getActiveNotAvailableNotReservedCars();
        return getClosestCarAllNotAvailable(firstPositionOfRide, activeNotReservedCars, hasPet, hasChild, carType);
    }

    private Car getClosestCarAllNotAvailable(LatLngDTO latLngDTO, List<Car> cars, boolean hasPet, boolean hasChild, CarType carType) {
        Car closestCar = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Car car : cars) {
            if (checkIfCarIsNotAppropriate(hasPet, hasChild, carType, car)) continue;
            int numberOfPositionsFirstRide = car.getNavigation().getFirstRide().getPositions().size();
            double lastPositionFirstRideX = car.getNavigation().getFirstRide().getPositions().get(numberOfPositionsFirstRide - 1).getPosition().getX();
            double lastPositionFirstRideY = car.getNavigation().getFirstRide().getPositions().get(numberOfPositionsFirstRide - 1).getPosition().getY();
            double distance = MapUtils.calculateDistance(lastPositionFirstRideX, lastPositionFirstRideY, latLngDTO.getLng(), latLngDTO.getLat()); // switched
            if (distance < minDistance) {
                closestCar = car;
                minDistance = distance;
            }
        }
        return closestCar;
    }

    private boolean checkIfCarIsNotAppropriate(boolean hasPet, boolean hasChild, CarType carType, Car car) {
        if (carDoesNotSatisfyOrder(car, hasPet, hasChild, carType)) return true;
        if (driverService.driverIsLoggedForMoreThan8HoursInLast24Hours(car.getDriver())) return true;
        return car.getDriver().getBlocked();
    }


    public NavigationDisplay getNavigationDisplayForDriver(String email) {
        NavigationDisplay navigationDisplay = new NavigationDisplay();
        Driver driver = driverRepository.findByEmail(email);
        if (driver == null) throw new NotFoundException("Driver with this email does not exist");
        Navigation navigation = driver.getCar().getNavigation();
        if (navigation.getApproachFirstRide() != null) {
            navigationDisplay.setFirstApproach(navigation.getApproachFirstRide());
        }
        if (navigation.getFirstRide() != null && !navigation.getFirstRide().isFreeRide()) {
            navigationDisplay.setFirstRide(navigation.getFirstRide());
        }
        if (navigation.getApproachSecondRide() != null) {
            navigationDisplay.setSecondApproach(navigation.getApproachSecondRide());
        }
        if (navigation.getSecondRide() != null) {
            navigationDisplay.setSecondRide(navigation.getSecondRide());
        }
        return navigationDisplay;
    }
}
