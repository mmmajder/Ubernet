package com.example.ubernet.repository;

import com.example.ubernet.model.RideAlternatives;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RideAlternativesRepository extends JpaRepository<RideAlternatives, Long> {

    @Query(value = "SELECT rideAlternatives FROM RideAlternatives rideAlternatives WHERE rideAlternatives.ride.id=:rideId")
    RideAlternatives getRideAlternativesByRideId(long rideId);
}
