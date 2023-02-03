package com.example.ubernet.service;

import com.example.ubernet.dto.InstructionDTO;
import com.example.ubernet.dto.LatLngDTO;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.exception.NotFoundException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.ubernet.model.CurrentRide;
import com.example.ubernet.model.Customer;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.RideRequest;
import com.example.ubernet.repository.RideRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.util.Optional;

import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class RideServiceTest {
    @Mock
    private CarTypeService carTypeService;
    @Mock
    private CarService carService;
    @Mock
    private CarRepository carRepository;
    @Mock
    private PositionRepository positionRepository;
    @Mock
    private PositionInTimeRepository positionInTimeRepository;
    @Mock
    private NavigationRepository navigationRepository;
    @Mock
    private RideRepository rideRepository;
    @Mock
    private CustomerService customerService;
    @InjectMocks
    private RideService rideService;

    @Captor
    private ArgumentCaptor<Car> carArgumentCaptor;
    @Captor
    private ArgumentCaptor<Navigation> navigationArgumentCaptor;

    @Captor
    private ArgumentCaptor<Position> positionArgumentCaptor;
    @Captor
    private ArgumentCaptor<PositionInTime> positionInTimeArgumentCaptor;

    private final LatLngDTO FIRST_POSITION_OF_RIDE = new LatLngDTO(19.8451756, 45.2551338);
    private final Boolean NO_PET = false;
    private final Boolean NO_CHILD = false;
    private final CarType CABRIO_CAR_TYPE = new CarType(null, "Cabrio", 200.00, false);

    @Test
    @DisplayName("Should return closest free car.")
    public void shouldReturnClosestFreeCarWithActiveDriver() {
        Car closestCar = createFreeCabrioNoPetNoChild();
        Mockito.when(carTypeService.findCarTypeByName("Cabrio")).thenReturn(CABRIO_CAR_TYPE);
        Mockito.when(carService.getClosestFreeCar(FIRST_POSITION_OF_RIDE, NO_PET, NO_CHILD, CABRIO_CAR_TYPE)).thenReturn(closestCar);
        Car foundCar = rideService.getCarForRide(FIRST_POSITION_OF_RIDE, NO_PET, NO_CHILD, CABRIO_CAR_TYPE.getName());
        assertEquals(foundCar.getPlates(), closestCar.getPlates());
    }

    @Test
    @DisplayName("Should return closest not free car.")
    public void shouldReturnClosestNotFreeCarWithActiveDriver() {
        Car closestCar = createNotFreeCabrioNoPetNoChild();
        Mockito.when(carTypeService.findCarTypeByName("Cabrio")).thenReturn(CABRIO_CAR_TYPE);
        Mockito.when(carService.getClosestFreeCar(FIRST_POSITION_OF_RIDE, NO_PET, NO_CHILD, CABRIO_CAR_TYPE)).thenReturn(null);
        Mockito.when(carService.getClosestCarWhenAllAreNotAvailable(FIRST_POSITION_OF_RIDE, NO_PET, NO_CHILD, CABRIO_CAR_TYPE)).thenReturn(closestCar);
        Car foundCar = rideService.getCarForRide(FIRST_POSITION_OF_RIDE, NO_PET, NO_CHILD, "Cabrio");
        assertEquals(foundCar.getPlates(), closestCar.getPlates());
    }

    @Test
    @DisplayName("Should throw Not found exception for finding closest car.")
    public void shouldThrowNotFoundExceptionForSearchingForClosestActiveCarWithActiveDriver() {
        createNotFreeCabrioNoPetNoChild();
        Mockito.when(carTypeService.findCarTypeByName("Cabrio")).thenReturn(CABRIO_CAR_TYPE);
        Mockito.when(carService.getClosestFreeCar(FIRST_POSITION_OF_RIDE, NO_PET, NO_CHILD, CABRIO_CAR_TYPE)).thenReturn(null);
        Mockito.when(carService.getClosestCarWhenAllAreNotAvailable(FIRST_POSITION_OF_RIDE, NO_PET, NO_CHILD, CABRIO_CAR_TYPE)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> rideService.getCarForRide(FIRST_POSITION_OF_RIDE, NO_PET, NO_CHILD, "Cabrio"));
    }

    @Test
    @DisplayName("Should navigation to car. Set first ride to current.")
    public void shouldAddNavigationAndSetFirstRideBecauseNavigationIsNull() {
        Car closestCar = createNotFreeCabrioNoPetNoChild();
        CurrentRide currentRide = createNotFreeCurrentRide();
        closestCar.setNavigation(new Navigation());
        rideService.addNavigation(closestCar, currentRide);

        assertEquals(currentRide, closestCar.getNavigation().getFirstRide());
        verify(carRepository, times(1)).save(carArgumentCaptor.capture());
        verify(navigationRepository, times(1)).save(navigationArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Should navigation to car. Set first ride to current.")
    public void shouldAddNavigationAndSetFirstRideBecauseFirstRideIsFree() {
        Car closestCar = createNotFreeCabrioNoPetNoChild();
        CurrentRide currentRide = createNotFreeCurrentRide();
        Navigation navigation = new Navigation();
        navigation.setFirstRide(createFreeCurrentRide());
        closestCar.setNavigation(navigation);
        rideService.addNavigation(closestCar, currentRide);
        assertEquals(currentRide, closestCar.getNavigation().getFirstRide());
        verify(carRepository, times(1)).save(carArgumentCaptor.capture());
        verify(navigationRepository, times(1)).save(navigationArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Should navigation to car. Set second ride to current.")
    public void shouldAddNavigationAndSetSecondRideBecauseFirstRideIsNotFree() {
        Car closestCar = createNotFreeCabrioNoPetNoChild();
        CurrentRide currentRide = createNotFreeCurrentRide();
        Navigation navigation = new Navigation();
        navigation.setFirstRide(createNotFreeCurrentRide());
        closestCar.setNavigation(navigation);
        rideService.addNavigation(closestCar, currentRide);
        assertEquals(currentRide, closestCar.getNavigation().getSecondRide());
        verify(carRepository, times(1)).save(carArgumentCaptor.capture());
        verify(navigationRepository, times(1)).save(navigationArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Should create list of position in time for coordinates.")
    public void shouldCreateListOfPositionInTimeForCoordinatesAndInstructions() {
        List<LatLngDTO> coordinates = getCoordinatesLatLng();
        List<InstructionDTO> instructions = getInstructions();

        List<PositionInTime> found = rideService.getRidePositions(coordinates, instructions);

        for (int i = 0; i < found.size(); i++) {
            assertEquals(found.get(i).getPosition().getX(), coordinates.get(i).getLng());
            assertEquals(found.get(i).getPosition().getY(), coordinates.get(i).getLat());
        }
        verify(positionRepository, times(coordinates.size() - 1)).save(positionArgumentCaptor.capture());
        verify(positionInTimeRepository, times(coordinates.size() - 1)).save(positionInTimeArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Should set ride status")
    public void shouldSetRideStatus() {
        Ride ride = createRide();
        Ride res = createRide();
        res.setRideState(RideState.WAITING);
        Mockito.when(rideRepository.save(ride)).thenReturn(res);
        rideService.updateRideStatus(ride, RideState.WAITING);
        assertEquals(RideState.WAITING, ride.getRideState());
    }

    @Test
    @DisplayName("Should get rides with accepted reservation")
    public void shouldGetRidesWithAcceptedReservation() {
        Mockito.when(rideRepository.getAcceptedReservationsThatCarDidNotComeYet()).thenReturn(List.of(createRide()));
        List<Ride> foundRides = rideService.getRidesWithAcceptedReservation();
        assertEquals(1, foundRides.size());
    }

    @Test
    @DisplayName("Should get rides that are reservations with split fare and time for start of ride passed.")
    public void shouldGetReservedRidesThatWereNotPayedAndScheduledTimePassed() {
        Mockito.when(rideRepository.getReservedRidesThatWithStatusRequestedAndScheduledStartIsNotNull()).thenReturn(List.of(createRideScheduledTimePassed(11), createRideScheduledTimeNotPassed(12)));
        List<Ride> foundRides = rideService.getReservedRidesThatWereNotPayedAndScheduledTimePassed();
        assertEquals(1, foundRides.size());
        assertTrue(foundRides.get(0).getScheduledStart().isBefore(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should get rides that are with passed scheduled time.")
    public void shouldGetReservedRidesThatScheduledTimePassed() {
        Mockito.when(rideRepository.getReservedRidesThatWithStatusRequestedAndScheduledStartIsNotNull()).thenReturn(List.of(createRideScheduledTimePassed(10), createRideScheduledTimeNotPassed(11)));
        List<Ride> foundRides = rideService.getReservedRidesThatWereNotPayedAndScheduledTimePassed();
        assertEquals(1, foundRides.size());
        assertTrue(foundRides.get(0).getScheduledStart().isBefore(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should get reserved rides that should start in 10 minutes.")
    public void shouldGetReservedRidesThatShouldStartIn10Minutes() {
        Mockito.when(rideRepository.getReservedWithStatusReservedRides()).thenReturn(List.of(createRideScheduledTimePassed(7), createRideScheduledTimePassed(15)));
        List<Ride> foundRides = rideService.getReservedRidesThatShouldStartIn10Minutes();
        assertEquals(1, foundRides.size());
        assertTrue(foundRides.get(0).getScheduledStart().isAfter(LocalDateTime.now().minusMinutes(10)));
    }

    @Test
    @DisplayName("Should get current ride for client.")
    public void shouldGetCurrentRideForClient() {
        String email = "a@gmail.com";
        Customer customer = new Customer(email);
        Ride ride = createRide();
        ride.setCustomers(List.of(customer));
        RideRequest rideRequest = new RideRequest();
        rideRequest.setCurrentRide(createNotFreeCurrentRide());
        ride.setRideRequest(rideRequest);
        Mockito.when(customerService.findByEmail(email)).thenReturn(customer);
        Mockito.when(rideRepository.findActiveRideForCustomer(email)).thenReturn(ride);
        CurrentRide foundCurrentRide = rideService.findCurrentRideForClient(email);
        assertEquals(rideRequest.getCurrentRide(), foundCurrentRide);
    }

    @Test
    @DisplayName("Should throw  Bad request exception for non existing customer with email.")
    public void shouldThrowBadRequestExceptionWrongEmail() {
        Mockito.when(customerService.findByEmail(any())).thenReturn(null);
        assertThrows(BadRequestException.class, () -> rideService.findCurrentRideForClient(any()));
    }

    @Test
    @DisplayName("Should return null for non existing active ride for customer.")
    public void shouldReturnNullForNoActiveRide() {
        String email = "a@gmail.com";
        Customer customer = new Customer(email);
        Mockito.when(customerService.findByEmail(email)).thenReturn(customer);
        Mockito.when(rideRepository.findActiveRideForCustomer(any())).thenReturn(null);
        assertNull(rideService.findCurrentRideForClient(email));
    }

    private Ride createRideScheduledTimePassed(long time) {
        Ride ride = new Ride();
        ride.setScheduledStart(LocalDateTime.now().plusMinutes(time));
        return ride;
    }

    private Ride createRideScheduledTimeNotPassed(long time) {
        Ride ride = new Ride();
        ride.setScheduledStart(LocalDateTime.now().minusMinutes(time));
        return ride;
    }

    private Ride createRide() {
        return new Ride();
    }

    private List<LatLngDTO> getCoordinatesLatLng() {
        List<LatLngDTO> coords = new ArrayList<>();
        coords.add(new LatLngDTO(45.25624, 19.84421));
        coords.add(new LatLngDTO(45.25627, 19.84418));
        coords.add(new LatLngDTO(45.25648, 19.8439));
        coords.add(new LatLngDTO(45.25651, 19.84385));   // change of instruction
        coords.add(new LatLngDTO(45.25651, 19.84385));
        coords.add(new LatLngDTO(45.2565, 19.84384));
        coords.add(new LatLngDTO(45.25638, 19.84369));
        coords.add(new LatLngDTO(45.25623, 19.8435));    // change of instruction
        coords.add(new LatLngDTO(45.25623, 19.8435));
        return coords;
    }

    private List<InstructionDTO> getInstructions() {
        List<InstructionDTO> instructions = new ArrayList<>();
        instructions.add(new InstructionDTO(41.2, 16.0, "Његошева"));
        instructions.add(new InstructionDTO(42.2, 6, "Пролаз Милоша Хаџића"));
        instructions.add(new InstructionDTO(0, 0, "Пролаз Милоша Хаџића"));
        return instructions;
    }

    private CurrentRide createFreeCurrentRide() {
        CurrentRide currentRide = createNotFreeCurrentRide();
        currentRide.setFreeRide(true);
        return currentRide;
    }

    private CurrentRide createNotFreeCurrentRide() {
        CurrentRide currentRide = new CurrentRide();
        currentRide.setFreeRide(false);
        currentRide.setShouldGetRouteToClient(true);
        currentRide.setPositions(createPositionsInTime());
        return currentRide;
    }

    private List<PositionInTime> createPositionsInTime() {
        List<PositionInTime> positionInTimeList = new ArrayList<>();
        double seconds = 0.0;
        for (Position position : getCoordinates()) {
            positionInTimeList.add(new PositionInTime(seconds, position));
            seconds += 1;
        }
        return positionInTimeList;
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


    private Car createNotFreeCabrioNoPetNoChild() {
        Car car = createFreeCabrioNoPetNoChild();
        car.setIsAvailable(false);
        return car;
    }

    private Car createFreeCabrioNoPetNoChild() {
        Car car = new Car();
        car.setPlates("NS 123 BA");
        car.setCarType(new CarType(null, "Cabrio", 200.00, false));
        car.setIsAvailable(true);
        car.setAllowsBaby(false);
        car.setAllowsPet(false);
        return car;
    }

    private static Long ID = 1111L;
    private static String EMAIL = "email@gmail.com";

    @Test
    @DisplayName("Should return Null when finding by invalid ID")
    public void shouldReturnNullWhenFindingByInvalidId() {
        Optional<Ride> o = Optional.empty();
        Mockito.when(rideRepository.findById(ID))
                .thenReturn(o);

        Ride ride = rideService.findById(ID);
        assertNull(ride);
        verify(rideRepository).findById(ID);
        verifyNoMoreInteractions(rideRepository);
    }

    @Test
    @DisplayName("Should return Ride when finding by valid ID")
    public void shouldReturnRideWhenFindingByValidId() {
        Ride r = new Ride();
        r.setId(ID);
        Optional<Ride> o = Optional.of(r);
        Mockito.when(rideRepository.findById(ID))
                .thenReturn(o);

        Ride ride = rideService.findById(ID);
        assertEquals(ride.getId(), ID);
        verify(rideRepository).findById(ID);
        verifyNoMoreInteractions(rideRepository);
    }

    @Test
    @DisplayName("Should throw Bad Request Null when finding by invalid email")
    public void shouldReturnNullWhenFindingCustomerByInvalidEmail() {
        Mockito.when(customerService.findByEmail(EMAIL))
                .thenReturn(null);

        assertThrowsExactly(BadRequestException.class,
                () -> rideService.findCurrentRideForClient(EMAIL));
        verify(customerService).findByEmail(EMAIL);
        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(rideRepository);
    }

    @Test
    @DisplayName("Should throw Bad Request when finding by valid email but no active ride")
    public void shouldReturnNullWhenFindingActiveRideByValidEmailButNoActiveRide() {
        Customer c = new Customer();
        c.setEmail(EMAIL);

        Mockito.when(customerService.findByEmail(EMAIL))
                .thenReturn(c);
        Mockito.when(rideRepository.findActiveRideForCustomer(EMAIL))
                .thenReturn(null);

        CurrentRide activeRide = rideService.findCurrentRideForClient(EMAIL);

        assertNull(activeRide);
        verify(customerService).findByEmail(EMAIL);
        verify(rideRepository).findActiveRideForCustomer(EMAIL);
        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(rideRepository);
    }

    @Test
    @DisplayName("Should return CurrentRide when finding by valid email and has active ride")
    public void shouldReturnCurrentRideWhenFindingByValidEmailAndHasActiveRide() {
        Customer c = new Customer();
        c.setEmail(EMAIL);

        CurrentRide currentRide = new CurrentRide();
        currentRide.setId(ID);
        RideRequest rideRequest = new RideRequest();
        rideRequest.setCurrentRide(currentRide);
        Ride activeRide = new Ride();
        activeRide.setRideRequest(rideRequest);

        Mockito.when(customerService.findByEmail(EMAIL))
                .thenReturn(c);
        Mockito.when(rideRepository.findActiveRideForCustomer(EMAIL))
                .thenReturn(activeRide);

        CurrentRide cRide = rideService.findCurrentRideForClient(EMAIL);

        assertEquals(cRide.getId(), activeRide.getRideRequest().getCurrentRide().getId());
        verify(customerService).findByEmail(EMAIL);
        verify(rideRepository).findActiveRideForCustomer(EMAIL);
        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(rideRepository);
    }
}
