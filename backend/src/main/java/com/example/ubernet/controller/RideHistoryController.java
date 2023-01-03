package com.example.ubernet.controller;

import com.example.ubernet.dto.RideHistoryRequestParam;
import com.example.ubernet.dto.RideHistorySimpleResponse;
import com.example.ubernet.model.Ride;
import com.example.ubernet.service.RideHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/ridesHistory", produces = MediaType.APPLICATION_JSON_VALUE)
public class RideHistoryController {

    RideHistoryService rideHistoryService;

    @PostMapping("/getRides")
    public List<RideHistorySimpleResponse> getRides(@RequestBody RideHistoryRequestParam filter) {
        List<RideHistorySimpleResponse> rides = rideHistoryService.getRides(filter);
        return rides;
    }
}
