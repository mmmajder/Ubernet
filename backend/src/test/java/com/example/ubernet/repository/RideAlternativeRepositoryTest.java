package com.example.ubernet.repository;

import com.example.ubernet.model.Admin;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.RideAlternatives;
import com.example.ubernet.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import com.example.ubernet.repository.RideAlternativesRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ActiveProfiles("test")
public class RideAlternativeRepositoryTest {

    @Autowired
    private RideAlternativesRepository rideAlternativesRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void shouldReturnRideAlternativesWhenFindingByValidId() {
        Ride ride = new Ride();
        ride.setId(3L);
        RideAlternatives rideAlternatives =  new RideAlternatives();
        rideAlternatives.setId(1L);
        rideAlternatives.setRide(ride);

        testEntityManager.merge(ride);
        testEntityManager.merge(rideAlternatives);
        testEntityManager.flush();

        RideAlternatives foundRideAlternatives = rideAlternativesRepository.getRideAlternativesByRideId(ride.getId());

        assertEquals(ride.getId(), foundRideAlternatives.getRide().getId());
        assertEquals(rideAlternatives.getId(), foundRideAlternatives.getId());
    }

    @Test
    public void shouldReturnNulWhenFindingByInvalidId() {
        Ride ride = new Ride();
        ride.setId(3L);
        RideAlternatives rideAlternatives =  new RideAlternatives();
        rideAlternatives.setId(1L);
        rideAlternatives.setRide(ride);

        testEntityManager.merge(ride);
        testEntityManager.merge(rideAlternatives);
        testEntityManager.flush();

        RideAlternatives foundRideAlternatives = rideAlternativesRepository.getRideAlternativesByRideId(6849L);

        assertNull(foundRideAlternatives);
    }
}
