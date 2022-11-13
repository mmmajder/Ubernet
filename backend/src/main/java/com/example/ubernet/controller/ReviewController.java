package com.example.ubernet.controller;

import com.example.ubernet.dto.CreateReviewDTO;
import com.example.ubernet.dto.ReviewResponse;
import com.example.ubernet.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/review", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/review-car")
    public ResponseEntity<ReviewResponse> reviewCar(@RequestBody CreateReviewDTO createReviewDTO) {
        ReviewResponse reviewResponse = reviewService.createCarReview(createReviewDTO);
        if (reviewResponse == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(reviewResponse, HttpStatus.OK);
    }

    @PostMapping("/review-driver")
    public ResponseEntity<ReviewResponse> reviewDriver(@RequestBody CreateReviewDTO createReviewDTO) {
        ReviewResponse reviewResponse = reviewService.createDriverReview(createReviewDTO);
        if (reviewResponse == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(reviewResponse, HttpStatus.OK);
    }

    @GetMapping("/carReviews/{rideId}")
    public ResponseEntity<Set<ReviewResponse>> getCarReviews(@PathVariable Long rideId) {
        Set<ReviewResponse> reviewResponse = reviewService.getCarReviews(rideId);
        if (reviewResponse == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(reviewResponse, HttpStatus.OK);
    }

    @GetMapping("/driverReviews/{rideId}")
    public ResponseEntity<Set<ReviewResponse>> getDriverReviews(@PathVariable Long rideId) {
        Set<ReviewResponse> reviewResponse = reviewService.getDriverReviews(rideId);
        if (reviewResponse == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(reviewResponse, HttpStatus.OK);
    }
}
