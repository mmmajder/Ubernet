package com.example.ubernet.service;

import com.example.ubernet.dto.LeafletRouteDTO;
import com.example.ubernet.model.CurrentRide;
import com.example.ubernet.repository.CurrentRideRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CurrentRideService {

    private final CurrentRideRepository currentRideRepository;

    public CurrentRide save(CurrentRide currentRide) {
        return this.currentRideRepository.save(currentRide);
    }

    public List<LeafletRouteDTO> optimizeByPrice(List<List<LeafletRouteDTO>> createRideDTOs) {
        List<LeafletRouteDTO> cheapestRoute = new ArrayList<>();
        for (List<LeafletRouteDTO> path : createRideDTOs) {
            LeafletRouteDTO cheapestInPath = getCheapest(path);
            cheapestRoute.add(cheapestInPath);
        }
        return cheapestRoute;
    }

    public List<LeafletRouteDTO> optimizeByTime(List<List<LeafletRouteDTO>> createRideDTOs) {
        List<LeafletRouteDTO> shortestByTimeRoute = new ArrayList<>();
        for (List<LeafletRouteDTO> path : createRideDTOs) {
            LeafletRouteDTO cheapestInPath = getShortestByTime(path);
            shortestByTimeRoute.add(cheapestInPath);
        }
        return shortestByTimeRoute;
    }

    private LeafletRouteDTO getCheapest(List<LeafletRouteDTO> path) {
        double minDistance = Double.POSITIVE_INFINITY;
        LeafletRouteDTO cheapestPath = path.get(0);
        for (LeafletRouteDTO alternative : path) {
            if (alternative.getSummary().getTotalDistance() < minDistance) {
                minDistance = alternative.getSummary().getTotalDistance();
                cheapestPath = alternative;
            }
        }
        return cheapestPath;
    }

    private LeafletRouteDTO getShortestByTime(List<LeafletRouteDTO> path) {
        double minTime = Double.POSITIVE_INFINITY;
        LeafletRouteDTO shortestPathByTime = path.get(0);
        for (LeafletRouteDTO alternative : path) {
            if (alternative.getSummary().getTotalTime() < minTime) {
                minTime = alternative.getSummary().getTotalTime();
                shortestPathByTime = alternative;
            }
        }
        return shortestPathByTime;
    }

//    private double calculatePriceForRoute(CreateRideDTO createRideDTO) {
//
//    }
}
