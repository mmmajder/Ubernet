package com.example.ubernet.service;

import com.example.ubernet.dto.CreateCarDTO;
import com.example.ubernet.model.Car;
import com.example.ubernet.model.CarType;
import com.example.ubernet.model.Driver;
import com.example.ubernet.model.User;
import com.example.ubernet.repository.CarRepository;
import org.springframework.stereotype.Service;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final UserService userService;
    private final CarTypeService carTypeService;

    public CarService(CarRepository carRepository, UserService userService, CarTypeService carTypeService) {
        this.carRepository = carRepository;
        this.userService = userService;
        this.carTypeService = carTypeService;
    }

    public Car createCar(CreateCarDTO createCarDTO) {
        User user = userService.findByEmail(createCarDTO.getEmail());
        if (user==null) {
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
}
