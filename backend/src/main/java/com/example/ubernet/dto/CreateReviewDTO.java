package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewDTO {
    private String comment;
    private int driverRating;
    private int carRating;
    private long rideId;
    private String clientEmail;
}
