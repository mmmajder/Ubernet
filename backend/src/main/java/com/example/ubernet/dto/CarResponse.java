package com.example.ubernet.dto;

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
    private CarType carType;
    private Driver driver;
}
