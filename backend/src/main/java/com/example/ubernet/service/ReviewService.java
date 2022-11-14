package com.example.ubernet.service;

import com.example.ubernet.dto.CreateReviewDTO;
import com.example.ubernet.dto.ReviewResponse;
import com.example.ubernet.model.Customer;
import com.example.ubernet.model.Review;
import com.example.ubernet.model.Ride;
import com.example.ubernet.repository.ReviewRepository;
import com.example.ubernet.utils.DTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RideService rideService;
    private final CustomerService customerService;

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public ReviewResponse createCarReview(CreateReviewDTO createReviewDTO) {
        Ride ride = rideService.findById(createReviewDTO.getRideId());
        if (ride == null) {
            return null;
        }
        if (pastThreeDays(ride)) {
            return null;
        }
        Review review = createReview(createReviewDTO);
        saveNewCarReview(ride, review);
        return DTOMapper.getReviewResponse(review);
    }

    private boolean pastThreeDays(Ride ride) {
        LocalDateTime endOfRideTime = ride.getActualEnd();
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.DAYS.between(endOfRideTime, now) > 3;
    }

    private void saveNewCarReview(Ride ride, Review review) {
        Set<Review> rides = ride.getCarReviews();
        rides.add(review);
        ride.setCarReviews(rides);
        rideService.save(ride);
    }

    private Review createReview(CreateReviewDTO createReviewDTO) {
        Review review = new Review();
        review.setComment(createReviewDTO.getComment());
        Customer customer = customerService.findByEmail(createReviewDTO.getClientEmail());
        review.setCustomer(customer);
        review.setRating(createReviewDTO.getRating());
        save(review);
        return review;
    }

    public ReviewResponse createDriverReview(CreateReviewDTO createReviewDTO) {
        Ride ride = rideService.findById(createReviewDTO.getRideId());
        if (ride == null) {
            return null;
        }
        Review review = createReview(createReviewDTO);
        saveNewDriverReview(ride, review);
        return DTOMapper.getReviewResponse(review);
    }

    private void saveNewDriverReview(Ride ride, Review review) {
        Set<Review> rides = ride.getDriverReviews();
        rides.add(review);
        ride.setDriverReviews(rides);
        rideService.save(ride);
    }

    public Set<ReviewResponse> getCarReviews(Long rideId) {
        Ride ride = rideService.findById(rideId);
        Set<ReviewResponse> reviews = new HashSet<>();
        for (Review review : ride.getCarReviews()) {
            reviews.add(DTOMapper.getReviewResponse(review));
        }
        return reviews;
    }

    public Set<ReviewResponse> getDriverReviews(Long rideId) {
        Ride ride = rideService.findById(rideId);
        Set<ReviewResponse> reviews = new HashSet<>();
        for (Review review : ride.getDriverReviews()) {
            reviews.add(DTOMapper.getReviewResponse(review));
        }
        return reviews;
    }
}
