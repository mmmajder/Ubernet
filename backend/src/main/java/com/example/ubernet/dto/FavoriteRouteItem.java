package com.example.ubernet.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteRouteItem {
    List<String> checkPoints;
    Long rideId;
}
