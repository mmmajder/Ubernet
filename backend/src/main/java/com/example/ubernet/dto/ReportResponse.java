package com.example.ubernet.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class ReportResponse {
    List<Integer> numberOfRides;
    List<Double> numberOfKm;
    List<Double> money;
    Double averageMoneyPerDay;
    Double totalSum;
}
