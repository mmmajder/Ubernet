package com.example.ubernet.dto;

import com.example.ubernet.model.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRideDTO {
    private List<Position> destinations;
    private String carType;
    private boolean hasChild;
    private boolean hasPet;
    private List<String> friends;
}
