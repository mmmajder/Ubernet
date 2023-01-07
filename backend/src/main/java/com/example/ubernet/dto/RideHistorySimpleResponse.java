package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RideHistorySimpleResponse {
    Long id;
    String route;
    Double price;
    String start;
    String end;
}