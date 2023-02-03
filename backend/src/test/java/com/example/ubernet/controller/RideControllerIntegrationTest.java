package com.example.ubernet.controller;

import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.*;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class RideControllerIntegrationTest {

    private final Long INVALID_ID = 111L;
    private final String INVALID_EMAIL = "invalidemail1234@gmail.com";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private NavigationRepository navigationRepository;
    @Autowired
    private CurrentRideRepository currentRideRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private PositionInTimeRepository positionInTimeRepository;
    @Autowired
    private PathAlternativeRepository pathAlternativeRepository;
    @Autowired
    private RideAlternativesRepository rideAlternativesRepository;
    @Autowired
    private UserAuthRepository userAuthRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Should return Null for invalid ID when making GET request to endpoint - /ride/{id}")
    public void shouldReturnNullForInvalidId(){
        ResponseEntity<Ride> responseEntity = restTemplate.exchange("/ride/" + INVALID_ID,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Ride>() {
                });

        Ride ride = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ride, null);
    }

    @Test
    @DisplayName("Should return OK for valid ID when making GET request to endpoint - /ride/{id}")
    public void shouldReturnOkForValidId(){
        ResponseEntity<Ride> responseEntity = restTemplate.exchange("/ride/" + "1",
                HttpMethod.GET,
                null,
                Ride.class);

        Ride ride = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ride.getId(), 1L);
    }

    @Test
    @DisplayName("Should return Bad Request for invalid ID when making PUT request to endpoint - /ride/start-ride/{id}")
    public void shouldReturnBadRequestForInvalidIdWhenStartingRide(){
        ResponseEntity<String> responseEntity = restTemplate.exchange("/ride/start-ride/" + INVALID_ID,
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<String>() {
                });

        String exceptionMessage = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(exceptionMessage, "Ride does not exist");
    }


    private Driver setupDriver(){
        Role roleDriver = new Role();
        roleDriver.setId(2L);
        roleDriver.setName("ROLE_DRIVER");
        roleRepository.save(roleDriver);
        Role roleUser = new Role();
        roleUser.setId(4L);
        roleUser.setName("ROLE_USER");
        roleRepository.save(roleUser);

        UserAuth userAuth = new UserAuth();
        userAuth.setRoles(List.of(roleDriver, roleUser));
        userAuth.setIsEnabled(true);
        userAuthRepository.save(userAuth);

        Driver driver = new Driver();
        driver.setEmail("driverrfast123@2f4u.com");
        driver.setUserAuth(userAuth);
        driverRepository.save(driver);

        return driver;
    }

    private RideAlternatives setupRideAlternatives(Ride ride){
        Position position = new Position();
        positionRepository.save(position);

        PositionInTime positionInTime = new PositionInTime();
        positionInTime.setPosition(position);
        positionInTimeRepository.save(positionInTime);

        CurrentRide currentRide = new CurrentRide();
        currentRide.setPositions(List.of(positionInTime));
        currentRideRepository.save(currentRide);

        PathAlternative pathAlternative = new PathAlternative();
        pathAlternative.setAlternatives(List.of(currentRide));
        pathAlternativeRepository.save(pathAlternative);

        RideAlternatives rideAlternatives = new RideAlternatives();
        rideAlternatives.setAlternatives(List.of(pathAlternative));
        rideAlternatives.setRide(ride);
        rideAlternativesRepository.save(rideAlternatives);

        return rideAlternatives;
    }

    private Car setupCar(){
        Car car = new Car();
        car.setId(4L);
        carRepository.save(car);

        return car;
    }

    private Ride setupRide(){
        Ride ride = new Ride();
        ride.setId(3L);
        rideRepository.save(ride);

        return ride;
    }

    private void setupShouldReturnOkForValidIdWhenStartingRide(){
        Car car = setupCar();
        Driver driver = setupDriver();
        driver.setCar(car);
        driverRepository.save(driver);
        car.setDriver(driver);

        Ride ride = setupRide();
        ride.setDriver(driver);
        ride.setRideState(RideState.WAITING);
        Navigation n = new Navigation();
        car.setNavigation(n);

        CurrentRide navCurrentRide = new CurrentRide();
        n.setFirstRide(navCurrentRide);
        currentRideRepository.save(navCurrentRide);

        setupRideAlternatives(ride);

        navigationRepository.save(n);
        carRepository.save(car);
        rideRepository.save(ride);
    }

    @Test
    @DisplayName("Should return OK for valid ID when making PUT request to endpoint - /ride/start-ride/{rideId}")
    @Rollback
    public void shouldReturnOkForValidIdWhenStartingRide(){
        setupShouldReturnOkForValidIdWhenStartingRide();

        ResponseEntity<Ride> responseEntity = restTemplate.exchange("/ride/start-ride/" + "3",
                HttpMethod.PUT,
                null,
                Ride.class);

        Ride gottenRide = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(gottenRide.getId(), 3L);
        assertEquals(gottenRide.getRideState(), RideState.TRAVELLING);
    }

    @Test
    @DisplayName("Should return Bad Request for invalid ID when making PUT request to endpoint - /ride/end-ride/{id}")
    public void shouldReturnBadRequestForInvalidIdWhenEndingRide(){
        ResponseEntity<String> responseEntity = restTemplate.exchange("/ride/end-ride/" + INVALID_ID,
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<String>() {
                });

        String exceptionMessage = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(exceptionMessage, "Ride does not exist");
    }

    @Test
    @DisplayName("Should return OK for valid ID when making PUT request to endpoint - /ride/end-ride/{rideId}")
    @Rollback
    public void shouldReturnOkForValidIdWhenEndingRide(){
        setupShouldReturnOkForValidIdWhenStartingRide();

        ResponseEntity<Ride> responseEntity = restTemplate.exchange("/ride/end-ride/" + "3",
                HttpMethod.PUT,
                null,
                Ride.class);

        Ride gottenRide = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(gottenRide.getId(), 3L);
        assertEquals(gottenRide.getRideState(), RideState.TRAVELLING);
    }
//
//    @Test
//    public void endRideValidIdShouldReturnOK() throws Exception {
//        Ride ride = new Ride();
//        ride.setId(2L);
//        ride.setRideState(RideState.FINISHED);
//
//        Mockito.when(endRideService.endRide(ride.getId()))
//                .thenReturn(ride);
//
//        mockMvc.perform(put("/ride/end-ride/{rideId}", ride.getId()))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.rideState", is(ride.getRideState().toString())))
//                .andExpect(jsonPath("$.id", is(2)));
//    }
//
    @Test
    @DisplayName("Should return Bad Request for invalid email when making GET request to endpoint - /ride/find-scheduled-route-navigation-client/{email}")
    public void shouldReturnBadRequestForInvalidEmailWhenFindingScheduledRoute(){
        ResponseEntity<String> responseEntity = restTemplate.exchange("/ride/find-scheduled-route-navigation-client/" + INVALID_EMAIL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                });

        String exceptionMessage = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(exceptionMessage, "Customer with this email does not exist");
    }

    @Test
    @DisplayName("Should return Null for valid email but no current route when making GET request to endpoint - /ride/find-scheduled-route-navigation-client/{email}")
    public void shouldReturnNullForValidEmailButNoCurrentRouteWhenFindingScheduledRoute(){
        ResponseEntity<Ride> responseEntity = restTemplate.exchange("/ride/find-scheduled-route-navigation-client/" + "customer@gmail.com",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Ride>() {
                });

        Ride ride = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ride, null);
    }

//    @Test
//    public void getByValidCustomerEmailShouldReturnCurrentRide() throws Exception {
//        String email = "customer@gmail.com";
//        CurrentRide ride = new CurrentRide();
//        ride.setId(2L);
//
//        Mockito.when(rideService.findCurrentRouteForClient(email))
//                .thenReturn(ride);
//
//        mockMvc.perform(get("/ride/find-scheduled-route-navigation-client/{email}", email))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(2)));
//    }
}
