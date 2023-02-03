package com.example.ubernet.controller;

import com.example.ubernet.dto.CarResponse;
import com.example.ubernet.dto.NavigationDisplay;
import com.example.ubernet.dto.SetNewFreeRideDTO;
import com.example.ubernet.model.*;
import com.example.ubernet.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class CarControllerIntegrationTest {

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
    private PositionRepository positionRepository;
    @Autowired
    private UserAuthRepository userAuthRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private DriverDailyActivityRepository driverDailyActivityRepository;

    @Test
    @DisplayName("Should return Null for invalid ID when asking for new free ride - PUT /car/new-free-ride")
    public void shouldReturnNotFoundForInvalidCarIdWhenNewFreeRide(){
        HttpEntity<String> request = createSetNewPositionFreeRideRequest(89553L);

        ResponseEntity<String> responseEntity = restTemplate.exchange("/car/new-free-ride",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<String>() {
                });

        String exceptionMessage = responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(exceptionMessage, "Car with this id does not exist");
    }

    @Test
    @DisplayName("Should return Car Response for valid ID when asking for new free ride - PUT /car/new-free-ride")
    public void shouldReturnCarResponseForValidCarIdWhenNewFreeRide(){
        setupShouldReturnCarResponseForValidCarIdWhenNewFreeRide();
        HttpEntity<String> request = createSetNewPositionFreeRideRequest(6L);

        ResponseEntity<CarResponse> responseEntity = restTemplate.exchange("/car/new-free-ride",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<CarResponse>() {
                });

        CarResponse carResponse = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(carResponse);
        assertEquals(carResponse.getDriver().getEmail(), "driverrfast123@2f4u.com");
    }

//    @Test
//    @DisplayName("Should return Car Response for valid ID when asking for new position and available cars exist - PUT /car/new-position")
//    public void shouldReturnCarResponsesWhenAvailableCarsExistNewPosition(){
//        setupShouldReturnCarResponsesWhenAvailableCarsExistNewPosition();
//
//        ResponseEntity<List<CarResponse>> responseEntity = restTemplate.exchange("/car/new-position",
//                HttpMethod.PUT,
//                null,
//                new ParameterizedTypeReference<List<CarResponse>>() {
//                });
//
//        List<CarResponse> carResponses = responseEntity.getBody();
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertNotNull(carResponses);
//        assertEquals(2, carResponses.size());
//    }

    @Test
    @DisplayName("Should throw Not Found for valid ID when asking for new position but no first ride - PUT /car/new-position")
    public void shouldThrowNotFoundExceptionResponsesWhenAvailableCarsExistButNoFirstRide(){
        setupShouldReturnCarResponsesWhenAvailableCarsExistNewPosition();

        ResponseEntity<String> responseEntity = restTemplate.exchange("/car/new-position",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<String>() {
                });

        String carResponses = responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Current ride does not exist", carResponses);
    }

    @Test
    @DisplayName("Should throw NotFoundException for invalid Driver ID when finding current ride - GET /car/currentRide/{email}")
    public void shouldThrowNotFoundExceptionWhenInvalidDriverEmailWhenFindingCurrentRide(){
        ResponseEntity<String> responseEntity = restTemplate.exchange("/car/currentRide/" + "superinvalid@email.com",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                });

        String navD = responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Driver with this email does not exist", navD);
    }

    @Test
    @DisplayName("Should return OK and NavigationDisplay for valid Driver ID when finding current ride - GET /car/currentRide/{email}")
    public void shouldReturnNavigationDisplayWhenValidDriverEmailWhenFindingCurrentRide(){
        setupShouldReturnNavigationDisplayWhenValidDriverEmailWhenFindingCurrentRide();

        ResponseEntity<NavigationDisplay> responseEntity = restTemplate.exchange("/car/currentRide/" + "111driverrfast123@2f4u.com",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<NavigationDisplay>() {
                });

        NavigationDisplay navD = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(navD);
    }

    private HttpEntity<String> createSetNewPositionFreeRideRequest(Long carId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonString = "{\"carId\": " + carId + ", \"positions\":[[45.25624, 19.84421],[45.25627, 19.84418],[45.25648, 19.8439], [45.25651, 19.84385], [45.25651, 19.84385], [45.2565, 19.84384], [45.25638, 19.84369], [45.25623, 19.8435], [45.25623, 19.8435]]}";
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        return request;
    }

    private SetNewFreeRideDTO createSetNewFreeRideDTO() {
        SetNewFreeRideDTO setNewFreeRideDTO = new SetNewFreeRideDTO();
        setNewFreeRideDTO.setPositions(getCoordinates());
        return setNewFreeRideDTO;
    }

    private List<List<Double>> getCoordinates() {
        List<List<Double>> coords = new ArrayList<>();
        coords.add(List.of(45.25624, 19.84421));
        coords.add(List.of(45.25627, 19.84418));
        coords.add(List.of(45.25648, 19.8439));
        coords.add(List.of(45.25651, 19.84385));
        coords.add(List.of(45.25651, 19.84385));
        coords.add(List.of(45.2565, 19.84384));
        coords.add(List.of(45.25638, 19.84369));
        coords.add(List.of(45.25623, 19.8435));
        coords.add(List.of(45.25623, 19.8435));
        return coords;
    }

    private void setupShouldReturnCarResponseForValidCarIdWhenNewFreeRide(){
        Car car = setupCar(6L);
        Driver driver = setupDriver("driverrfast123@2f4u.com");
        driver.setCar(car);
        driverRepository.save(driver);
        car.setDriver(driver);

        Position position = new Position();
        positionRepository.save(position);

        car.setPosition(position);

        Navigation n = new Navigation();
        car.setNavigation(n);

        navigationRepository.save(n);
        carRepository.save(car);
    }

    private void setupShouldReturnCarResponsesWhenAvailableCarsExistNewPosition(){
        Car car1 = setupCar(6L);
        Driver driver1 = setupDriver("111driverrfast123@2f4u.com");
        driver1.setCar(car1);
        DriverDailyActivity driverDailyActivity1 = new DriverDailyActivity();
        driverDailyActivity1.setIsActive(true);
        driverDailyActivityRepository.save(driverDailyActivity1);
        driver1.setDriverDailyActivity(driverDailyActivity1);
        driverRepository.save(driver1);
        car1.setDriver(driver1);
        car1.setIsAvailable(true);

        Car car2 = setupCar(7L);
        Driver driver2 = setupDriver("222driverrfast123@2f4u.com");
        driver2.setCar(car2);
        DriverDailyActivity driverDailyActivity2 = new DriverDailyActivity();
        driverDailyActivity2.setIsActive(true);
        driverDailyActivityRepository.save(driverDailyActivity2);
        driver2.setDriverDailyActivity(driverDailyActivity2);
        driverRepository.save(driver2);
        car2.setDriver(driver2);
        car2.setIsAvailable(true);

        Position position1 = new Position();
        positionRepository.save(position1);
        Position position2 = new Position();
        positionRepository.save(position2);

        car1.setPosition(position1);
        car2.setPosition(position2);

        carRepository.save(car1);
        carRepository.save(car2);
    }

    private void setupShouldReturnNavigationDisplayWhenValidDriverEmailWhenFindingCurrentRide(){
        Car car1 = setupCar(6L);
        Driver driver1 = setupDriver("111driverrfast123@2f4u.com");
        driver1.setCar(car1);

        DriverDailyActivity driverDailyActivity1 = new DriverDailyActivity();
        driverDailyActivity1.setIsActive(true);
        driverDailyActivityRepository.save(driverDailyActivity1);
        driver1.setDriverDailyActivity(driverDailyActivity1);
        driverRepository.save(driver1);
        car1.setDriver(driver1);
        car1.setIsAvailable(true);

        Navigation n = new Navigation();
        car1.setNavigation(n);

        CurrentRide firstRide = new CurrentRide();
        firstRide.setFreeRide(false);
        n.setFirstRide(firstRide);
        currentRideRepository.save(firstRide);
        CurrentRide approachFirstRide = new CurrentRide();
        n.setApproachFirstRide(approachFirstRide);
        currentRideRepository.save(approachFirstRide);

        navigationRepository.save(n);

        carRepository.save(car1);
    }

    private Role setupRole(String roleName){
        Role role = new Role();
        role.setId(2L);
        role.setName(roleName);
        roleRepository.save(role);

        return role;
    }

    private UserAuth setupUserAuth(Role specificRole, Role userRole){
        UserAuth userAuth = new UserAuth();
        userAuth.setRoles(List.of(specificRole, userRole));
        userAuth.setIsEnabled(true);
        userAuthRepository.save(userAuth);

        return userAuth;
    }

    private Driver setupDriver(String email){
        Role roleDriver = setupRole("ROLE_DRIVER");
        Role roleUser = setupRole("ROLE_USER");
        UserAuth userAuth = setupUserAuth(roleDriver, roleUser);

        Driver driver = new Driver();
        driver.setEmail(email);
        driver.setUserAuth(userAuth);
        driverRepository.save(driver);

        return driver;
    }

    private Car setupCar(Long id){
        Car car = new Car();
        car.setId(id);
        carRepository.save(car);

        return car;
    }
}
