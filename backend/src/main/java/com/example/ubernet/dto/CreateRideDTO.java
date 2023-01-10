package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRideDTO {
    @NotEmpty
    private List<LatLngDTO> coordinates;
    @NotEmpty
    private List<InstructionDTO> instructions;
    @NotEmpty
    private String carType;
    private boolean hasChild;
    private boolean hasPet;
    private List<String> passengers;
    @Min(value = 0)
    private double totalDistance;
    @Min(value = 0)
    private double totalTime;
    private String reservationTime;
}
