package com.example.ubernet.service;

import com.example.ubernet.dto.PriceEstimationDTO;
import com.example.ubernet.model.CarType;
import org.springframework.stereotype.Service;

@Service
public class RidePriceService {

    private final CarTypeService carTypeService;

    public RidePriceService(CarTypeService carTypeService) {
        this.carTypeService = carTypeService;
    }

    public double calculateEstimatedPrice(PriceEstimationDTO priceEstimationDTO) {
        CarType carType = carTypeService.findCarTypeByName(priceEstimationDTO.getCarType());
        return calculatePrice(priceEstimationDTO.getEstimatedLengthInKm(), carType.getPriceForType());
    }

    public double calculatePrice(double lengthInKM, double pricePerCarType) {
        return 120 * lengthInKM + pricePerCarType;
    }
}
