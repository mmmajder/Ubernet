package com.example.ubernet.service;

import com.example.ubernet.model.Route;
import com.example.ubernet.repository.RouteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RouteService {
    private final RouteRepository routeRepository;

    public Route save(Route route) {
        return routeRepository.save(route);
    }
}
