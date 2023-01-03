package com.example.ubernet.repository;

import com.example.ubernet.model.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RideRepository extends JpaRepository<Ride, Long> {

//    @Query(value = "SELECT * FROM RIDES WHERE DRIVER.EMAIL = ",
//            countQuery = "SELECT count(*) FROM USERS WHERE LASTNAME = ?1",
//            nativeQuery = true)
    Page<Ride> findByDriverEmail(String driverEmail, Pageable pageable);
}