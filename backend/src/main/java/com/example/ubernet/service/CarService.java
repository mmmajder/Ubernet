package com.example.ubernet.service;

import com.example.ubernet.dto.*;
import com.example.ubernet.model.*;
import com.example.ubernet.repository.CarRepository;
import com.example.ubernet.utils.MapUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        return carRepository.findActiveAvailableCars();
    }

    public List<Car> getActiveNonAvailableCars() {
        return carRepository.findActiveNonAvailableCars();
    }

    public List<Car> getActiveNonAvailableCarsNewRoute() {
        return carRepository.findActiveNonAvailableCarsNewRoute();
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

    public Car getCarByDriverEmail(String email) {
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

    public Car setNewPositionAvailableCar(Long carId) {
        Car car = findById(carId);
        if (car == null) {
            return null;
        }
        if (car.getCurrentRide() == null) {
            return null;
        }
        if (car.getCurrentRide().getDestinations().size() == 0) {
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
        List<Car> cars = setNewPositionForAvailableCars();
        cars.addAll(setNewPositionForNonAvailableCars());
        return cars;
    }

    private List<Car> setNewPositionForNonAvailableCars() {
        List<Car> cars = getActiveNonAvailableCars();
        for (Car car : cars) {
            setNewPositionNonAvailableCar(car.getId());
        }
        return cars;
    }

    private Car setNewPositionNonAvailableCar(long carId) {
        Car car = findById(carId);
        if (car == null) {
            return null;
        }
        if (car.getCurrentRide() == null) {
            return null;
        }
        if (car.getCurrentRide().isShouldGetRouteToClient()) {
            return null;
        }
        removeNonAvailableCarPositions(car);
        save(car);
        if (car.getCurrentRide().getPositions().size() == 0) {
            return null;
        }
        car.setPosition(getNextPosition(car.getCurrentRide()).getPosition());
        return save(car);
    }

    private PositionInTime getNextPosition(CurrentRide currentRide) {
        double minTime = Double.POSITIVE_INFINITY;
        PositionInTime next = null;
        for (PositionInTime positionInTime : currentRide.getPositions()) {
            if (positionInTime.getSecondsPast() < minTime) {
                minTime = positionInTime.getSecondsPast();
                next = positionInTime;
            }
        }
        return next;
    }

    private void removeNonAvailableCarPositions(Car car) {
        CurrentRide currentRide = car.getCurrentRide();
        while (true) {
            if (currentRide.getPositions().size() == 0) {
                car.setIsAvailable(true);
                save(car);
                return;
            }
            PositionInTime currentPositionInTime = getNextPosition(car.getCurrentRide());
            double pastTime = currentPositionInTime.getSecondsPast();
            LocalDateTime finishTime = currentRide.getTimeOfStartOfRide().plusSeconds((long) pastTime);
            if (finishTime.isBefore(LocalDateTime.now())) {
                currentRide.getPositions().remove(currentPositionInTime);
                currentRideService.save(currentRide);
            } else {
                currentRideService.save(currentRide);
//                car.setPosition(car.getCurrentRide().getPositions().get(0).getPosition());
//                save(car);
                return;
            }
        }
    }

    private List<Car> setNewPositionForAvailableCars() {
        List<Car> cars = getActiveAvailableCars();
        for (Car car : cars) {
            setNewPositionAvailableCar(car.getId());
        }
        return cars;
    }

    public Car getClosestFreeCar(LatLngDTO latLngDTO) {
        List<Car> activeAvailableCars = getActiveAvailableCars();
        Car closestCar = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Car car : activeAvailableCars) {
            double distance = MapUtils.calculateDistance(car.getPosition().getX(), car.getPosition().getY(), latLngDTO.getLat(), latLngDTO.getLng());
            if (distance < minDistance) {
                closestCar = car;
                minDistance = distance;
            }
        }
        return closestCar;
    }


}
