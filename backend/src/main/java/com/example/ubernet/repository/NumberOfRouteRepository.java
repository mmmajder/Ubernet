package com.example.ubernet.repository;

import com.example.ubernet.model.NumberOfRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NumberOfRouteRepository extends JpaRepository<NumberOfRoute, Long> {
    NumberOfRoute save(NumberOfRoute numberOfRoute);
}
