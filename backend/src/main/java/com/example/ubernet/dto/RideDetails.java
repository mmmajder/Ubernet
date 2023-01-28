package com.example.ubernet.dto;

import com.example.ubernet.model.Place;
import com.example.ubernet.model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RideDetails {
    private long id;
    private List<Place> checkPoints;
    private Double totalPrice;
    private SimpleUser driver;
    private List<SimpleUser> customers;
    private String scheduledStart;
    private String actualStart;
    private String actualEnd;
    private String reservationTime;
    private Set<Review> reviews;
}
