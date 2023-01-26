package com.example.ubernet.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class RideToRate {
    private int daysLeftToRate;
    private String rideDate;
    private long rideId;
    private String route;
}
