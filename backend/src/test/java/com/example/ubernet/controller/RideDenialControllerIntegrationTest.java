package com.example.ubernet.controller;

import com.example.ubernet.dto.CancelRideRequest;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.*;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.testng.annotations.BeforeMethod;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class RideDenialControllerIntegrationTest {
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
    private UserAuthRepository userAuthRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private DriverDailyActivityRepository driverDailyActivityRepository;

    @BeforeMethod
    public void setup(){

    }

    private HttpEntity<String> createCancelRequestRequest(boolean shouldSetDriverInactive, String reason){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shouldSetDriverInactive", shouldSetDriverInactive);
        jsonObject.put("reason", reason);

        HttpEntity<String> request = new HttpEntity<String>(jsonObject.toString(), headers);

        return request;
    }

    @Test
    @DisplayName("Should return Bad Request for invalid ID - POST /ride-denial/{id}")
    public void shouldReturnNullForInvalidId(){
        HttpEntity<String> request = createCancelRequestRequest(true, "reason");

        ResponseEntity<String> responseEntity = restTemplate.exchange("/ride-denial/" + INVALID_ID,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });

        String exceptionMessage = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(exceptionMessage, "Ride does not exist");
    }

    @Test
    @DisplayName("Should return RideDenial for valid ID when shoudlSetDriverInactive=true - POST /ride-denial/{id}")
    @Rollback
    public void shouldReturnRideDenialForValidIdWhenSetDriverInactiveIsTrue(){
        setupShouldReturnRideDenialForValidIdWhenSetDriverInactiveIsTrue();
        HttpEntity<String> request = createCancelRequestRequest(true, "reason");

        ResponseEntity<RideDenial> responseEntity = restTemplate.exchange("/ride-denial/" + 4,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<RideDenial>() {
                });

        RideDenial rideDenial = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(rideDenial.getReason(), "reason");
        assertEquals(rideDenial.getRide().getId(), 4L);
    }

    @Test
    @DisplayName("Should return RideDenial for valid ID when shoudlSetDriverInactive=false and navigation does not have second ride - POST /ride-denial/{id}")
    @Rollback
    public void shouldReturnRideDenialForValidIdWhenSetDriverInactiveIsFalseAndNoSecondRide(){
        setupShouldReturnRideDenialForValidIdWhenSetDriverInactiveIsFalseAndNoSecondRide();
        HttpEntity<String> request = createCancelRequestRequest(false, "reason");

        ResponseEntity<RideDenial> responseEntity = restTemplate.exchange("/ride-denial/" + 4,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<RideDenial>() {
                });

        RideDenial rideDenial = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(rideDenial.getReason(), "reason");
        assertEquals(rideDenial.getRide().getId(), 4L);
        assertFalse(rideDenial.getRide().getCustomers().get(0).isActive());
    }

    @Test
    @DisplayName("Should return RideDenial for valid ID when shoudlSetDriverInactive=false and navigation has second ride - POST /ride-denial/{id}")
    @Rollback
    public void shouldReturnRideDenialForValidIdWhenSetDriverInactiveIsFalseAndHasSecondRide(){
        setupShouldReturnRideDenialForValidIdWhenSetDriverInactiveIsFalseAndHasSecondRide();
        HttpEntity<String> request = createCancelRequestRequest(false, "reason");

        ResponseEntity<RideDenial> responseEntity = restTemplate.exchange("/ride-denial/" + 4,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<RideDenial>() {
                });

        RideDenial rideDenial = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(rideDenial.getReason(), "reason");
        assertEquals(rideDenial.getRide().getId(), 4L);
        assertFalse(rideDenial.getRide().getCustomers().get(0).isActive());
    }

    private void setupShouldReturnRideDenialForValidIdWhenSetDriverInactiveIsTrue(){
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

        navigationRepository.save(n);
        carRepository.save(car);
        rideRepository.save(ride);
    }

    private void setupShouldReturnRideDenialForValidIdWhenSetDriverInactiveIsFalseAndNoSecondRide(){
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
        currentRideRepository.save(navCurrentRide);

        navigationRepository.save(n);
        carRepository.save(car);
        rideRepository.save(ride);
    }

    private void setupShouldReturnRideDenialForValidIdWhenSetDriverInactiveIsFalseAndHasSecondRide(){
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
        DriverDailyActivity driverDailyActivity = new DriverDailyActivity();
        driverDailyActivityRepository.save(driverDailyActivity);

        Role roleDriver = setupRole("ROLE_DRIVER");
        Role roleUser = setupRole("ROLE_USER");
        UserAuth userAuth = setupUserAuth(roleDriver, roleUser);

        Driver driver = new Driver();
        driver.setEmail("driverrfast123@2f4u.com");
        driver.setUserAuth(userAuth);
        driver.setDriverDailyActivity(driverDailyActivity);
        driverRepository.save(driver);

        return driver;
    }

    private Car setupCar(){
        Car car = new Car();
        car.setId(6L);
        carRepository.save(car);

        return car;
    }

    private Ride setupRide(){
        Ride ride = new Ride();
        ride.setId(4L);
        rideRepository.save(ride);

        return ride;
    }
}
