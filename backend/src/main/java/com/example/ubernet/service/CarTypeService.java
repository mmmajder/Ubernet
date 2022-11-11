package com.example.ubernet.service;

import com.example.ubernet.model.CarType;
import com.example.ubernet.repository.CarTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class CarTypeService {
    private final CarTypeRepository carTypeRepository;

    public CarTypeService(CarTypeRepository carTypeRepository) {
        this.carTypeRepository = carTypeRepository;
    }

    public CarType save(CarType carType) {
        return carTypeRepository.save(carType);
    }
}
