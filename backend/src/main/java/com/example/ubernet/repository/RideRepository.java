package com.example.ubernet.repository;

import com.example.ubernet.model.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RideRepository extends JpaRepository<Ride, Long> {

    Page<Ride> findByDriverEmailAndCustomersEmail(String driverEmail, String customerEmail, Pageable pageable);
    Page<Ride> findByDriverEmail(String driverEmail, Pageable pageable);
    Page<Ride> findByCustomersEmail(String customerEmail, Pageable pageable);
    Page<Ride> findAll(Pageable pageable);
}