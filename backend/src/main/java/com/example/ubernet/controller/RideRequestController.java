package com.example.ubernet.controller;

import com.example.ubernet.service.RideRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/ride-request", produces = MediaType.APPLICATION_JSON_VALUE)
public class RideRequestController {

    private final RideRequestService rideRequestService;

    @PutMapping("/send-cars-to-reservations")
    public void sendCarsToReservations() {
        rideRequestService.sendCarsToReservations();
    }

    @PutMapping("/return-money")
    public void returnMoney() {
        rideRequestService.returnMoneyNotPayedPassedReservations();
    }

    @PutMapping("/notify-time-until-reservation")
    public void notifyTimeUntilReservation() {
        rideRequestService.notifyTimeUntilReservation();
    }
}
