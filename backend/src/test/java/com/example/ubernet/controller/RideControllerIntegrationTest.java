package com.example.ubernet.controller;

import com.example.ubernet.dto.*;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RideRequestRepository rideRequestRepository;
    @Autowired
    private DriverDailyActivityRepository driverDailyActivityRepository;

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
    @Order(1)
    public void shouldReturnOkForValidIdWhenEndingRideAndHasNoSecondRide(){
        setupShouldReturnOkForValidIdWhenEndingRideAndHasNoSecondRide();

        ResponseEntity<Ride> responseEntity = restTemplate.exchange("/ride/end-ride/" + "3",
                HttpMethod.PUT,
                null,
                Ride.class);

        Ride gottenRide = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(gottenRide.getId(), 3L);
        assertEquals(gottenRide.getRideState(), RideState.FINISHED);
        assertFalse(gottenRide.getCustomers().get(0).isActive());
        assertNull(gottenRide.getDriver().getCar().getNavigation().getFirstRide());
    }

    @Test
    @DisplayName("Should return OK for valid ID when making PUT request to endpoint - /ride/end-ride/{rideId}")
    @Rollback
    public void shouldReturnOkForValidIdWhenEndingRideAndHasSecondRide(){
        setupShouldReturnOkForValidIdWhenEndingRideAndHasSecondRide();

        ResponseEntity<Ride> responseEntity = restTemplate.exchange("/ride/end-ride/" + "3",
                HttpMethod.PUT,
                null,
                Ride.class);

        Ride gottenRide = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(gottenRide.getId(), 3L);
        assertEquals(gottenRide.getRideState(), RideState.FINISHED);
        assertFalse(gottenRide.getCustomers().get(0).isActive());
        assertNotNull(gottenRide.getDriver().getCar().getNavigation().getFirstRide());
        assertNull(gottenRide.getDriver().getCar().getNavigation().getSecondRide());
    }

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

    @Test
    @DisplayName("Should return OK for valid ID when making PUT request to endpoint - /ride/find-scheduled-route-navigation-client/{email}")
    @Rollback
    public void shouldReturnOkForValidEmailButAndHasCurrentRouteWhenFindingScheduledRoute(){
        setupShouldReturnOkForValidEmailButAndHasCurrentRouteWhenFindingScheduledRoute();

        ResponseEntity<CurrentRide> responseEntity = restTemplate.exchange("/ride/find-scheduled-route-navigation-client/" + "customedcustomer@2f4u.com",
                HttpMethod.GET,
                null,
                CurrentRide.class);

        CurrentRide gottenRide = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(gottenRide.getId(), 1L);
    }

    // zakazivanje vvv
    @Test
    @DisplayName("Should return Ride and OK  when making  request to endpoint - POST /ride/create")
    public void shouldReturnRideAndOKForValidCreateRideDTO(){
        setupShouldReturnRideAndOKForValidCreateRideDTO();
        HttpEntity<String> request = createCreateRideDTO();

        ResponseEntity<Ride> responseEntity = restTemplate.exchange("/ride/create" ,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<Ride>() {
                });

        Ride ride = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(ride);
    }

    @Test
    @DisplayName("Should throw BadRequest for active user when making  request to endpoint - POST /ride/create")
    public void shouldThrowBadRequestForValidCreateRideDTOAndActiveCustomer(){
        setupShouldThrowBadRequestForValidCreateRideDTOAndActiveCustomer();
        HttpEntity<String> request = createCreateRideDTO();

        ResponseEntity<String> responseEntity = restTemplate.exchange("/ride/create" ,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });

        String message = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(message, "Active customer can not request another ride!");
    }

    @Test
    @DisplayName("Should throw BadRequest for blocked user when making  request to endpoint - POST /ride/create")
    public void shouldThrowBadRequestForValidCreateRideDTOAndBlockedCustomer(){
        setupShouldThrowBadRequestForValidCreateRideDTOAndBlockedCustomer();
        HttpEntity<String> request = createCreateRideDTO();

        ResponseEntity<String> responseEntity = restTemplate.exchange("/ride/create" ,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });

        String message = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(message, "You are blocked by admin and can not request ride!");
    }

    private HttpEntity<String> createCreateRideDTO() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonString = """
                {
                    "coordinates": [{"lat": 19.586, "lng": 18.451}],
                    "instructions": [{"distance": 20, "time": 20, "road": "cool road"}],
                    "carType": "Cabrio",
                    "hasChild": true,
                    "hasPet": false,
                    "passengers": ["customedcustomer@2f4u.com"],
                    "totalDistance": 15,
                    "totalTime": 15,
                    "reservationTime": "3:30",
                    "route": [],
                    "payment": { "totalPrice": 150, "customerThatPayed": "customedcustomer@2f4u.com"},
                    "reservation": false
                }
                """;

        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        return request;
    }

    private void setupShouldThrowBadRequestForValidCreateRideDTOAndBlockedCustomer(){
        Customer customer = setupCustomer();
        customer.setActive(false);
        customer.setBlocked(true);
        customer.setNumberOfTokens(99999);
        customerRepository.save(customer);
    }

    private void setupShouldThrowBadRequestForValidCreateRideDTOAndActiveCustomer(){
        Customer customer = setupCustomer();
        customer.setActive(true);
        customer.setBlocked(false);
        customer.setNumberOfTokens(99999);
        customerRepository.save(customer);
    }

    private void setupShouldReturnRideAndOKForValidCreateRideDTO(){
        Customer customer = setupCustomer();
        customer.setActive(false);
        customer.setBlocked(false);
        customer.setNumberOfTokens(99999);
        customerRepository.save(customer);

        Car car1 = setupCar();
        Driver driver1 = setupDriver();
        driver1.setBlocked(false);
        driver1.setCar(car1);
        DriverDailyActivity driverDailyActivity1 = new DriverDailyActivity();
        driverDailyActivity1.setIsActive(true);
        driverDailyActivityRepository.save(driverDailyActivity1);
        driver1.setDriverDailyActivity(driverDailyActivity1);
        driverRepository.save(driver1);
        car1.setDriver(driver1);
        car1.setIsAvailable(true);
        car1.setAllowsBaby(true);
        CarType carType = new CarType();
        carType.setId(1L);
        carType.setName("Cabrio");
        carType.setPriceForType(200.0);
        carType.setDeleted(false);
        car1.setCarType(carType);

        Position position1 = new Position();
        position1.setX(5.0);
        position1.setY(5.0);
        positionRepository.save(position1);

        car1.setPosition(position1);

        driverRepository.save(driver1);
        carRepository.save(car1);
    }

    private void setupShouldReturnOkForValidEmailButAndHasCurrentRouteWhenFindingScheduledRoute(){
        Car car = setupCar();
        Driver driver = setupDriver();
        driver.setCar(car);
        driverRepository.save(driver);
        car.setDriver(driver);

        Customer customer = setupCustomer();

        Ride ride = setupRide();
        ride.setDriver(driver);
        ride.setRideState(RideState.TRAVELLING);
        ride.setCustomers(List.of(customer));

        CurrentRide currentRide = new CurrentRide();
        currentRide.setId(1L);
        currentRideRepository.save(currentRide);

        RideRequest rideRequest = new RideRequest();
        rideRequest.setCurrentRide(currentRide);
        rideRequestRepository.save(rideRequest);
        ride.setRideRequest(rideRequest);

        carRepository.save(car);
        rideRepository.save(ride);
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

    private void setupShouldReturnOkForValidIdWhenEndingRideAndHasNoSecondRide(){
        Car car = setupCar();
        Driver driver = setupDriver();
        driver.setCar(car);
        driverRepository.save(driver);
        car.setDriver(driver);

        Customer customer = setupCustomer();

        Ride ride = setupRide();
        ride.setDriver(driver);
        ride.setRideState(RideState.TRAVELLING);
        ride.setCustomers(List.of(customer));
        Navigation n = new Navigation();
        car.setNavigation(n);

        CurrentRide navCurrentRide = new CurrentRide();
        n.setFirstRide(navCurrentRide);
        n.setSecondRide(null);
        currentRideRepository.save(navCurrentRide);

        setupRideAlternatives(ride);

        navigationRepository.save(n);
        carRepository.save(car);
        rideRepository.save(ride);
    }

    private void setupShouldReturnOkForValidIdWhenEndingRideAndHasSecondRide(){
        Car car = setupCar();
        Driver driver = setupDriver();
        driver.setCar(car);
        driverRepository.save(driver);
        car.setDriver(driver);

        Customer customer = setupCustomer();

        Ride ride = setupRide();
        ride.setDriver(driver);
        ride.setRideState(RideState.TRAVELLING);
        ride.setCustomers(List.of(customer));
        Navigation n = new Navigation();
        car.setNavigation(n);

        CurrentRide navCurrentRide = new CurrentRide();
        n.setFirstRide(navCurrentRide);
        CurrentRide secondRide = new CurrentRide();
        n.setSecondRide(secondRide);
        n.setApproachSecondRide(secondRide);
        currentRideRepository.save(navCurrentRide);
        currentRideRepository.save(secondRide);

        setupRideAlternatives(ride);

        navigationRepository.save(n);
        carRepository.save(car);
        rideRepository.save(ride);
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

    private Customer setupCustomer(){
        Role roleCustomer = setupRole("ROLE_CUSTOMER");
        Role roleUser = setupRole("ROLE_USER");
        UserAuth userAuth = setupUserAuth(roleCustomer, roleUser);

        Customer c = new Customer();
        c.setEmail("customedcustomer@2f4u.com");
        c.setUserAuth(userAuth);
        customerRepository.save(c);

        return c;
    }

    private Driver setupDriver(){
        Role roleDriver = setupRole("ROLE_DRIVER");
        Role roleUser = setupRole("ROLE_USER");
        UserAuth userAuth = setupUserAuth(roleDriver, roleUser);

        Driver driver = new Driver();
        driver.setEmail("driverrfast123@2f4u.com");
        driver.setUserAuth(userAuth);
        driverRepository.save(driver);

        return driver;
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
}
