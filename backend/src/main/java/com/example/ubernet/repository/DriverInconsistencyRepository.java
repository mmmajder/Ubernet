package com.example.ubernet.repository;

import com.example.ubernet.model.DriverInconsistency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DriverInconsistencyRepository extends JpaRepository<DriverInconsistency, Long> {
    @Query(value = "SELECT driverInconsistency FROM DriverInconsistency driverInconsistency WHERE driverInconsistency.ride.id=:id")
    DriverInconsistency findByRideId(long id);
}
