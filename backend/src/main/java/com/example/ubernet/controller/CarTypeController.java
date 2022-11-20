package com.example.ubernet.controller;

import com.example.ubernet.dto.CarTypeResponse;
import com.example.ubernet.model.CarType;
import com.example.ubernet.service.CarTypeService;
import com.example.ubernet.utils.DTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/car-type", produces = MediaType.APPLICATION_JSON_VALUE)
public class CarTypeController {
    private final CarTypeService carTypeService;

    @GetMapping("/{name}")
    public ResponseEntity<CarTypeResponse> getCarTypeByName(@PathVariable String name) {
        CarType carType = carTypeService.findCarTypeByName(name);
        if (carType == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(DTOMapper.getCarTypeResponse(carType));
    }
}
