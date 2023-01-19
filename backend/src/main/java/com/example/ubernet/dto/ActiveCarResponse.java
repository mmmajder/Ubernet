package com.example.ubernet.dto;

import com.example.ubernet.model.CurrentRide;
import com.example.ubernet.model.Position;
import com.example.ubernet.model.PositionInTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.OneToOne;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveCarResponse {
    private long carId;
    private String driverEmail;
    private CurrentRide currentRide;
    private Position currentPosition;
    private CurrentRide approachFirstRide;
    private CurrentRide firstRide;
    private CurrentRide approachSecondRide;
    private CurrentRide secondRide;
}
