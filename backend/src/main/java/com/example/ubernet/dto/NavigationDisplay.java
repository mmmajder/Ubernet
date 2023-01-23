package com.example.ubernet.dto;

import com.example.ubernet.model.CurrentRide;
import com.example.ubernet.model.Route;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NavigationDisplay {
    private CurrentRide firstApproach;
    private Route firstRide;
    private CurrentRide secondApproach;
    private Route secondRide;
}
