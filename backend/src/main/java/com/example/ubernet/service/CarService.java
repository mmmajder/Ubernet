package com.example.ubernet.service;

import com.example.ubernet.dto.*;
import com.example.ubernet.model.*;
import com.example.ubernet.repository.CarRepository;
import com.example.ubernet.utils.DTOMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final UserService userService;
    private final CarTypeService carTypeService;
    private final PositionService positionService;

    public CarService(CarRepository carRepository, UserService userService, CarTypeService carTypeService, PositionService positionService) {
        this.carRepository = carRepository;
        this.userService = userService;
        this.carTypeService = carTypeService;
        this.positionService = positionService;
    }

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
        carType.setAllowsBaby(createCarDTO.getAllowsBaby());
        carType.setAllowsPet(createCarDTO.getAllowsPet());
        carType.setPriceForType(createCarDTO.getPriceForType());
        carType.setName(createCarDTO.getName());
        carType.setDeleted(false);
        carTypeService.save(carType);
        return carType;
    }

    public Car findById(long id) {
        return carRepository.findById(id).orElse(null);
    }

    public List<ActiveCarResponse> getActiveAvailableCars() {
        List<Car> cars = carRepository.findActiveAvailableCars();
        List<ActiveCarResponse> carResponses = new ArrayList<>();
        for (Car car : cars) {
            carResponses.add(getActiveAvailableCar(car));
        }
        return carResponses;
    }

    private ActiveCarResponse getActiveAvailableCar(Car car) {
        ActiveCarResponse activeAvailableCarResponse = new ActiveCarResponse();
        activeAvailableCarResponse.setCarId(car.getId());
        activeAvailableCarResponse.setDriverEmail(car.getDriver().getEmail());
        activeAvailableCarResponse.setDestinations(car.getDestinations());
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
        car.setDestinations(setNewDestinationDTO.getNewDestinations());
        return carRepository.save(car);
    }

//    public Car setFuturePositions(FuturePositionsDTO futurePositionsDTO) {
//        Car car = findById(futurePositionsDTO.getCarId());
//        car.setFuturePositions(futurePositionsDTO.getPositions());
//        return save(car);
//    }

    public List<ActiveCarResponse> getActiveCars() {
        List<Car> cars = carRepository.findActiveCars();
        List<ActiveCarResponse> carResponses = new ArrayList<>();
        for (Car car : cars) {
            carResponses.add(getActiveAvailableCar(car));
        }
        return carResponses;
    }

    public ActiveCarResponse reachedDestination(Long carId) {
        Car car = findById(carId);
        if (car == null) {
            return null;
        }
        car.setPosition(car.getDestinations().get(0));
        if (car.getDestinations().size() != 1) {
            car.getDestinations().remove(0);
        }
        save(car);
        return getActiveAvailableCar(car);
    }
}
