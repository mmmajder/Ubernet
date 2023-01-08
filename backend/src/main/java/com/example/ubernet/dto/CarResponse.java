package com.example.ubernet.dto;

import com.example.ubernet.model.Car;
import com.example.ubernet.model.CarType;
import com.example.ubernet.model.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.OneToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarResponse {
    private long id;
    private CarType carType;
    private String driverEmail;
    private String plates;
    private String name;
    private Boolean allowsBaby;
    private Boolean allowsPet;
    private Driver driver;
    private PositionDTO position;

    public CarResponse(Car car){
        this.id = car.getId();
        this.carType = car.getCarType();
        this.driverEmail = car.getDriver().getEmail();
        this.name = car.getName();
        this.plates = car.getPlates();
        this.allowsBaby = car.getAllowsBaby();
        this.allowsPet = car.getAllowsPet();
        this.driver = car.getDriver();
    }
}
