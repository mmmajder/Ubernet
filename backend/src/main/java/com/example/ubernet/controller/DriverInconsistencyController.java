package com.example.ubernet.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/driver-inconsistency", produces = MediaType.APPLICATION_JSON_VALUE)
public class DriverInconsistencyController {

//    private final DriverInconsistencyService driverInconsistencyService;

    
}
