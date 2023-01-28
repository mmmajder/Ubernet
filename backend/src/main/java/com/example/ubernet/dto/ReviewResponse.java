package com.example.ubernet.dto;

import com.example.ubernet.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private String comment;
    private int carRating;
    private int driverRating;
    private Customer customer;
}
