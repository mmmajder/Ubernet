package com.example.ubernet.service;

import com.example.ubernet.dto.LeafletRouteDTO;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.CurrentRide;
import com.example.ubernet.model.PathAlternative;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.RideAlternatives;
import com.example.ubernet.repository.PathAlternativeRepository;
import com.example.ubernet.repository.RideAlternativesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class RideAlternativesService {
    private final RideAlternativesRepository rideAlternativesRepository;
    private final RideService rideService;
    private final PathAlternativeRepository pathAlternativeRepository;

    public void createAlternativesForRide(Long rideId, List<List<LeafletRouteDTO>> alternatives) {
        Ride ride = rideService.findById(rideId);
        RideAlternatives rideAlternatives = new RideAlternatives();
        rideAlternatives.setRide(ride);
        rideAlternatives.setAlternatives(new ArrayList<>());
        if (ride == null) throw new BadRequestException("Ride with this id does not exist");
        for (List<LeafletRouteDTO> alternativePath : alternatives) {
            List<CurrentRide> currentRides = new ArrayList<>();
            for (LeafletRouteDTO variationOfPath : alternativePath) {
                CurrentRide alternative = rideService.createCurrentRide(variationOfPath.getCoordinates(), variationOfPath.getInstructions());
                alternative.setShouldGetRouteToClient(false);
                currentRides.add(alternative);
            }
            PathAlternative pathAlternative = new PathAlternative(currentRides);
            pathAlternativeRepository.save(pathAlternative);
            rideAlternatives.getAlternatives().add(pathAlternative);
        }
        rideAlternativesRepository.save(rideAlternatives);
    }
}
