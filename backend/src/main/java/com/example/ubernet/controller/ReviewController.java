package com.example.ubernet.controller;

import com.example.ubernet.dto.CreateReviewDTO;
import com.example.ubernet.dto.ReviewResponse;
import com.example.ubernet.dto.RideToRate;
import com.example.ubernet.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/review", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("")
    public ResponseEntity<HttpStatus> review(@RequestBody CreateReviewDTO createReviewDTO) {
        ReviewResponse reviewResponse = reviewService.createRideReview(createReviewDTO);
        if (reviewResponse == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getRidesToRate/{customerEmail}")
    public List<RideToRate> getRidesToRate(@PathVariable String customerEmail) {
        return reviewService.getRidesToRate(customerEmail);
    }
}
