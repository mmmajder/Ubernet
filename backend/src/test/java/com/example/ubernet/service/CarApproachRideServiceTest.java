package com.example.ubernet.service;

import com.example.ubernet.dto.CreateRideDTO;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CarApproachRideServiceTest {


    @Mock
    private CarService carService;
    @Mock
    private RideService rideService;
    @Mock
    private CurrentRideService currentRideService;
    @Mock
    private NavigationRepository navigationRepository;
    @Captor
    private ArgumentCaptor<Navigation> navigationArgumentCaptor;
    @Captor
    private ArgumentCaptor<CurrentRide> currentRideArgumentCaptor;

    @InjectMocks
    private CarApproachRideService carApproachRideService;

    private final Long CAR_ID = 1L;

    @Test
    @DisplayName("Should throw BadRequestException for non existing car")
    public void shouldThrowBadRequestExceptionForNonExistingUrl() {
        Mockito.when(carService.findById(CAR_ID)).thenThrow(BadRequestException.class);
        assertThrows(BadRequestException.class, () -> carApproachRideService.createApproach(CAR_ID, any()));
    }

    @Test
    @DisplayName("Should set approach for first ride.")
    public void shouldCreateApproachForFirstRide() {
        Car car = createCar(CAR_ID);
        car.getNavigation().setSecondRide(null);
        List<PositionInTime> positionInTimeList = getPositionsInTime();
        CreateRideDTO createRideDTO = new CreateRideDTO();
        Mockito.when(carService.findById(CAR_ID)).thenReturn(car);
        Mockito.when(rideService.getRidePositions(any(), any())).thenReturn(positionInTimeList);
        carApproachRideService.createApproach(CAR_ID, createRideDTO);
        assertFalse(car.getNavigation().getApproachFirstRide().isShouldGetRouteToClient());
        assertFalse(car.getNavigation().getApproachFirstRide().isFreeRide());
        assertNotNull(car.getNavigation().getApproachFirstRide().getStartTime());
        assertEquals(positionInTimeList, car.getNavigation().getApproachFirstRide().getPositions());
        verify(currentRideService, times(1)).save(currentRideArgumentCaptor.capture());
        verify(navigationRepository, times(1)).save(navigationArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Should set approach for second ride.")
    public void shouldCreateApproachForSecondRide() {
        Car car = createCar(CAR_ID);
        car.getNavigation().setSecondRide(new CurrentRide());
        List<PositionInTime> positionInTimeList = getPositionsInTime();
        CreateRideDTO createRideDTO = new CreateRideDTO();
        Mockito.when(carService.findById(CAR_ID)).thenReturn(car);
        Mockito.when(rideService.getRidePositions(any(), any())).thenReturn(positionInTimeList);
        carApproachRideService.createApproach(CAR_ID, createRideDTO);
        assertFalse(car.getNavigation().getApproachSecondRide().isShouldGetRouteToClient());
        assertFalse(car.getNavigation().getApproachSecondRide().isFreeRide());
        assertNull(car.getNavigation().getApproachSecondRide().getStartTime());
        assertEquals(positionInTimeList, car.getNavigation().getApproachSecondRide().getPositions());
        verify(currentRideService, times(1)).save(currentRideArgumentCaptor.capture());
        verify(navigationRepository, times(1)).save(navigationArgumentCaptor.capture());
    }

    private List<PositionInTime> getPositionsInTime() {
        List<PositionInTime> positionsInTime = new ArrayList<>();
        List<Position> positions = getCoordinates();
        double seconds = 1.0;
        for (Position position : positions) {
            positionsInTime.add(new PositionInTime(seconds, position));
            seconds += 1;
        }
        return positionsInTime;
    }

    private List<Position> getCoordinates() {
        List<Position> coords = new ArrayList<>();
        coords.add(new Position(19.84421, 45.25624));
        coords.add(new Position(19.84418, 45.25627));
        coords.add(new Position(19.84414, 45.2563));
        coords.add(new Position(19.84392, 45.25647));
        coords.add(new Position(19.8439, 45.25648));
        coords.add(new Position(19.84385, 45.25651));
        coords.add(new Position(19.84385, 45.25651));
        coords.add(new Position(19.84384, 45.2565));
        coords.add(new Position(19.84382, 45.25649));
        coords.add(new Position(19.84369, 45.25638));
        coords.add(new Position(19.8435, 45.25623));
        coords.add(new Position(19.8435, 45.25623));
        return coords;
    }

    private Car createCar(Long id) {
        Car car = new Car();
        car.setId(id);
        car.setNavigation(new Navigation());
        return car;
    }
}
