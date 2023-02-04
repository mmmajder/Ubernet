package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeafletRouteDTO {
    private List<LatLngDTO> coordinates;
    private List<InstructionDTO> instructions;
    private String name;
    private int routesIndex;
    private LeafletRouteSummaryDTO summary;
}
