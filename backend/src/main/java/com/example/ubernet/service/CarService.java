package com.example.ubernet.service;

import com.example.ubernet.dto.*;
import com.example.ubernet.model.*;
import com.example.ubernet.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CarService {
    private final CarRepository carRepository;
    private final UserService userService;
    private final CarTypeService carTypeService;
    private final PositionService positionService;
    private final CurrentRideService currentRideService;

    public Car createCar(CreateCarDTO createCarDTO) {
        User user = userService.findByEmail(createCarDTO.getEmail());
        if (user == null) {
            return null;
        }
        return createCar(createCarDTO, (Driver) user);
    }

    public Car save(Car car) {
        return carRepository.save(car);
    }

    private Car createCar(CreateCarDTO createCarDTO, Driver user) {
        Car car = new Car();
        car.setCarType(getCarType(createCarDTO));
        car.setDeleted(false);
        car.setIsAvailable(false);
        user.setCar(car);
        car.setDriver((Driver) userService.findByEmail(user.getEmail()));
//        car.setPosition(null);
        save(car);
        userService.save(user);
        return car;
    }

    private CarType getCarType(CreateCarDTO createCarDTO) {
        CarType carType = new CarType();
        carType.setPriceForType(createCarDTO.getPriceForType());
        carType.setName(createCarDTO.getName());
        carType.setDeleted(false);
        carTypeService.save(carType);
        return carType;
    }

    public Car findById(long id) {
        return carRepository.findById(id).orElse(null);
    }

    public List<Car> getActiveAvailableCars() {
        List<Car> cars = carRepository.findActiveAvailableCars();
//        List<ActiveCarResponse> carResponses = new ArrayList<>();
//        for (Car car : cars) {
//            carResponses.add(getActiveAvailableCar(car));
//        }
        return cars;
    }

    private ActiveCarResponse getActiveAvailableCar(Car car) {
        ActiveCarResponse activeAvailableCarResponse = new ActiveCarResponse();
        activeAvailableCarResponse.setCarId(car.getId());
        activeAvailableCarResponse.setDriverEmail(car.getDriver().getEmail());
        activeAvailableCarResponse.setCurrentRide(car.getCurrentRide());
        activeAvailableCarResponse.setCurrentPosition(car.getPosition());
        return activeAvailableCarResponse;
    }

    public Car setNewDestination(SetNewDestinationDTO setNewDestinationDTO) {
        Optional<Car> optionalCar = carRepository.findById(setNewDestinationDTO.getCarId());
        if (optionalCar.isEmpty()) {
            return null;
        }
        return setDestination(setNewDestinationDTO, optionalCar.get());
    }

    private Car setDestination(SetNewDestinationDTO setNewDestinationDTO, Car car) {
        for (Position position : setNewDestinationDTO.getNewDestinations()) {
            positionService.save(position);
        }
        CurrentRide currentRide = new CurrentRide();
        currentRide.setDestinations(setNewDestinationDTO.getNewDestinations());
        currentRide.setTimeOfStartOfRide(LocalDateTime.now());

        currentRideService.save(currentRide);
//        car.setPosition(car.getCurrentRide().getPositions().get(0).getPosition());
//        car.getCurrentRide().setDestinations(setNewDestinationDTO.getNewDestinations());
        car.setCurrentRide(currentRide);
        System.out.println("SET");
        System.out.println(car.getId());
        return carRepository.save(car);
    }

//    public Car setFuturePositions(FuturePositionsDTO futurePositionsDTO) {
//        Car car = findById(futurePositionsDTO.getCarId());
//        car.setFuturePositions(futurePositionsDTO.getPositions());
//        return save(car);
//    }

    public List<Car> getActiveCars() {
        return carRepository.findActiveCars();
    }

    public ActiveCarResponse reachedDestination(Long carId) {
        Car car = findById(carId);
//        if (car == null) {
//            return null;
//        }
//        car.setPosition(car.getDestinations().get(0));
//        if (car.getDestinations().size() != 1) {
//            car.getDestinations().remove(0);
//        }
//        save(car);
        return getActiveAvailableCar(car);
    }

    public ActiveCarResponse getPosition(Long carId) {
        Car car = findById(carId);
        if (car == null) {
            return null;
        }
        return getActiveAvailableCar(car);
    }

    public Car getCarByDriverEmail(String email){
        Driver driver = (Driver) userService.findByEmail(email);
        Car car = carRepository.findByDriver(driver);

        return car;
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
    public Car setNewCurrentRide(List<Position> positions, Long carId) {
        Car car = findById(carId);
        if (car == null) {
            return null;
        }
        positionService.savePositions(positions);
        car.setCurrentRide(createNewCurrentRide(positions));
        return save(car);
    }

    public CurrentRide createNewCurrentRide(List<Position> positions) {
        CurrentRide currentRide = new CurrentRide();
        currentRide.setDeleted(false);
//        currentRide.setPositions(positions);
        currentRide.setTimeOfStartOfRide(LocalDateTime.now());
        currentRide.setDestinations(positions);
        currentRideService.save(currentRide);
        return currentRide;
    }

    public Car setNewPosition(Long carId) {
        Car car = findById(carId);
        if (car == null) {
            return null;
        }
        if (car.getCurrentRide() == null) {
            return null;
        }
        car.getCurrentRide().getDestinations().remove(0);
        save(car);
        if (car.getCurrentRide().getDestinations().size() == 0) {
            return null;
        }
        car.setPosition(car.getCurrentRide().getDestinations().get(0));
        currentRideService.save(car.getCurrentRide());
        return save(car);
    }

    public List<Car> setNewPositions() {
        List<Car> cars = getActiveAvailableCars();
        for (Car car : cars) {
            setNewPosition(car.getId());
        }
        return cars;
    }
}
