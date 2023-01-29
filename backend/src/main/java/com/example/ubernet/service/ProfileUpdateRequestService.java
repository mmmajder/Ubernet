package com.example.ubernet.service;

import com.example.ubernet.model.Car;
import com.example.ubernet.model.Driver;
import com.example.ubernet.model.ProfileChangesRequest;
import com.example.ubernet.repository.CarRepository;
import com.example.ubernet.repository.DriverRepository;
import com.example.ubernet.repository.ProfileChangesRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProfileUpdateRequestService {
    private final ProfileChangesRequestRepository profileChangesRequestRepository;
    private final DriverRepository driverRepository;
    private final CarRepository carRepository;
    private final CarTypeService carTypeService;

    public boolean manageProfileUpdateRequest(String driverEmail, boolean accepted) {
        ProfileChangesRequest profileChangesRequest = profileChangesRequestRepository.findByDriverEmail(driverEmail);
        if (accepted) {
            updateDriver(driverEmail, profileChangesRequest);
        }
        Driver driver = driverRepository.findByEmail(driverEmail);
        driver.setRequestedProfileChanges(false);
        driverRepository.save(driver);

        profileChangesRequestRepository.delete(profileChangesRequest);
        return true;
    }

    private void updateDriver(String driverEmail, ProfileChangesRequest request) {
        Driver driver = driverRepository.findByEmail(driverEmail);
        Car car = carRepository.findByDriver(driver);

        driver.setSurname(request.getSurname());
        driver.setPhoneNumber(request.getPhoneNumber());
        driver.setCity(request.getCity());
        driver.setName(request.getName());

        car.setDriver(driver);
        car.setCarType(carTypeService.findCarTypeByName(request.getCarType()));
        car.setName(request.getCarName());
        car.setAllowsPet(request.getAllowsPets());
        car.setAllowsBaby(request.getAllowsBabies());
        car.setPlates(request.getPlates());

        carRepository.save(car);
        driver.setCar(car);
        driverRepository.save(driver);
    }
}
