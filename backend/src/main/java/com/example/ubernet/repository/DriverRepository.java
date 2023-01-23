package com.example.ubernet.repository;

import com.example.ubernet.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Driver findByEmail(String email);
}
