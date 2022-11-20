package com.example.ubernet.service;

import com.example.ubernet.model.CarType;
import com.example.ubernet.repository.CarTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CarTypeService {
    private final CarTypeRepository carTypeRepository;

    public CarType save(CarType carType) {
        return carTypeRepository.save(carType);
    }

    public CarType findCarTypeByName(String name) {
        return carTypeRepository.findCarTypeByName(name);
    }
}
