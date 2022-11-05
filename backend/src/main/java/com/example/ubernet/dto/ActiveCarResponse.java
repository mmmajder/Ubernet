package com.example.ubernet.dto;

import com.example.ubernet.model.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveCarResponse {
    private long carId;
    private String driverEmail;
    private Position destination;
    private Position currentPosition;
}
