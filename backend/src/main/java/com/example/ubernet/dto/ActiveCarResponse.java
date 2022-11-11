package com.example.ubernet.dto;

import com.example.ubernet.model.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveCarResponse {
    private long carId;
    private String driverEmail;
    private List<Position> destinations;
    private Position currentPosition;
}
