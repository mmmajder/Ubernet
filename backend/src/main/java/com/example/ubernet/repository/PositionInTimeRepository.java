package com.example.ubernet.repository;

import com.example.ubernet.model.PositionInTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionInTimeRepository extends JpaRepository<PositionInTime, Long> {
}
