package com.example.ubernet.repository;

import com.example.ubernet.model.CurrentRide;
import com.example.ubernet.model.Ride;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class CurrentRideRepositoryTest {

    @Autowired
    private CurrentRideRepository currentRideRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void shouldReturnCurrentRideWhenSaving() {
        CurrentRide ride = new CurrentRide();
        ride.setId(1L);

        CurrentRide savedRide = currentRideRepository.save(ride);

        assertEquals(ride.getId(), savedRide.getId());
    }
}
