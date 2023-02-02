package com.example.ubernet.service;

import com.example.ubernet.dto.*;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;
    @Mock
    private UserService userService;
    @Mock
    private DriverService driverService;
    @Mock
    private DriverRepository driverRepository;
    @Captor
    private ArgumentCaptor<Car> carArgumentCaptor;
//    @Captor
//    private ArgumentCaptor<Ride> rideArgumentCaptor;

    @InjectMocks
    private CarService carService;

    private final Long CAR_ID = 1L;
    private final Long CAR_ID2 = 2L;

    private final String DRIVER_EMAIL = "a@gmail.com";


    @Test
    @DisplayName("Should save car")
    public void shouldSaveCar() {
        carService.save(new Car());
        verify(carRepository, times(1)).save(carArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Should find car by id")
    public void shouldFindCarById() {
        Car car = createCar(CAR_ID);
        Mockito.when(carRepository.findById(CAR_ID)).thenReturn(Optional.of(car));
        Car found = carService.findById(CAR_ID);
        assertEquals(car.getId(), found.getId());
    }

    @Test
    @DisplayName("Should get active available cars")
    public void shouldGetActiveAvailableCars() {
        Car car = createCar(CAR_ID);
        Car car2 = createCar(CAR_ID2);
        Mockito.when(carRepository.findActiveAvailableCars()).thenReturn(List.of(car, car2));
        List<Car> found = carService.getActiveAvailableCars();
        assertEquals(2, found.size());
    }

    @Test
    @DisplayName("Should find active available cars lock")
    public void shouldFindActiveAvailableCarsLock() {
        Car car = createCar(CAR_ID);
        Car car2 = createCar(CAR_ID2);
        Mockito.when(carRepository.findActiveAvailableCarsLock()).thenReturn(List.of(car, car2));
        List<Car> found = carService.findActiveAvailableCarsLock();
        assertEquals(2, found.size());
    }

    @Test
    @DisplayName("Should get active cars")
    public void shouldGetActiveCars() {
        Car car = createCar(CAR_ID);
        Car car2 = createCar(CAR_ID2);
        Mockito.when(carRepository.findActiveCars()).thenReturn(List.of(car, car2));
        List<Car> found = carService.getActiveCars();
        assertEquals(2, found.size());
    }

    @Test
    @DisplayName("Should get position of car")
    public void shouldGetPositionOfCar() {
        Car car = createCar(CAR_ID);
        Position position = new Position(12.65, 32.23);
        car.setPosition(position);
        car.setDriver(new Driver());
        Mockito.when(carRepository.findById(CAR_ID)).thenReturn(Optional.of(car));
        ActiveCarResponse found = carService.getPosition(CAR_ID);
        assertEquals(position, found.getCurrentPosition());
    }

    @Test
    @DisplayName("Should get car by driver email")
    public void shouldGetCarByDriverEmail() {
        Car car = createCar(CAR_ID);
        Position position = new Position(12.65, 32.23);
        car.setPosition(position);
        Driver driver = createDriver(DRIVER_EMAIL);
        car.setDriver(driver);
        Mockito.when(userService.findByEmail(DRIVER_EMAIL)).thenReturn(driver);
        Mockito.when(carRepository.findByDriver(driver)).thenReturn(car);
        Car found = carService.getCarByDriverEmail(DRIVER_EMAIL);
        assertEquals(position, found.getPosition());
        assertEquals(driver, found.getDriver());
    }

    @Test
    @DisplayName("Should get car by active driver email")
    public void shouldGetCarByActiveDriverEmail() {
        Car car = createCar(CAR_ID);
        Position position = new Position(12.65, 32.23);
        car.setPosition(position);
        Driver driver = createDriver(DRIVER_EMAIL);
        driver.getDriverDailyActivity().setIsActive(true);
        car.setDriver(driver);
        Mockito.when(userService.findByEmail(DRIVER_EMAIL)).thenReturn(driver);
        Mockito.when(carRepository.findByDriver(driver)).thenReturn(car);
        Car found = carService.getCarByDriverEmail(DRIVER_EMAIL);
        assertEquals(position, found.getPosition());
        assertEquals(driver, found.getDriver());
    }

    @Test
    @DisplayName("Should get null for inactive driver email")
    public void shouldGetNullForActiveDriverEmail() {
        Car car = createCar(CAR_ID);
        Position position = new Position(12.65, 32.23);
        car.setPosition(position);
        Driver driver = createDriver(DRIVER_EMAIL);
        driver.getDriverDailyActivity().setIsActive(false);
        car.setDriver(driver);
        Mockito.when(userService.findByEmail(DRIVER_EMAIL)).thenReturn(driver);
        Car found = carService.getCarByDriverEmail(DRIVER_EMAIL);
        assertNull(found);
    }

    @Test
    @DisplayName("Should update car")
    public void shouldUpdateCar() {
        Mockito.when(carRepository.findById(any())).thenReturn(Optional.of(new Car()));
        Mockito.when(carRepository.save(any())).thenReturn(any());
        carService.updateCar(new CarResponseNoDriver());
        verify(carRepository, times(1)).save(carArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Should get closest free car")
    public void shouldGetClosestFreeCar() {
        Car car = createCloserCar();
        Car car2 = createFurtherCar();
        LatLngDTO latLngDTO = new LatLngDTO(0.0, 0.0);
        Mockito.when(carRepository.findActiveAvailableCarsLock()).thenReturn(List.of(car, car2));
        Mockito.when(driverService.driverIsLoggedForMoreThan8HoursInLast24Hours(car.getDriver())).thenReturn(false);
        Mockito.when(driverService.driverIsLoggedForMoreThan8HoursInLast24Hours(car2.getDriver())).thenReturn(false);
        Car found = carService.getClosestFreeCar(latLngDTO, false, false, createCarTypeCabrio());
        assertEquals(car.getPosition(), found.getPosition());
    }

    @Test
    @DisplayName("Should get closest free car that allows child")
    public void shouldGetClosestFreeCarThatHasChildSeat() {
        Car car = createCloserCar();
        car.setAllowsBaby(false);
        Car car2 = createFurtherCar();
        car2.setAllowsBaby(true);
        LatLngDTO latLngDTO = new LatLngDTO(0.0, 0.0);
        Mockito.when(carRepository.findActiveAvailableCarsLock()).thenReturn(List.of(car, car2));
        Car found = carService.getClosestFreeCar(latLngDTO, false, true, createCarTypeCabrio());
        assertEquals(car2.getPosition(), found.getPosition());
    }

    @Test
    @DisplayName("Should get closest free car that allows pet")
    public void shouldGetClosestFreeCarThatAllowsPets() {
        Car car = createCloserCar();
        car.setAllowsPet(false);
        Car car2 = createFurtherCar();
        car2.setAllowsPet(true);
        LatLngDTO latLngDTO = new LatLngDTO(0.0, 0.0);
        Mockito.when(carRepository.findActiveAvailableCarsLock()).thenReturn(List.of(car, car2));
        Car found = carService.getClosestFreeCar(latLngDTO, true, false, createCarTypeCabrio());
        assertEquals(car2.getPosition(), found.getPosition());
    }

    @Test
    @DisplayName("Should get closest free car that allows pet and has children seat")
    public void shouldGetClosestFreeCarThatAllowsPetsAndChildren() {
        Car car = createCloserCar();
        car.setAllowsPet(false);
        car.setAllowsBaby(false);
        Car car2 = createFurtherCar();
        car2.setAllowsPet(true);
        car2.setAllowsBaby(true);
        LatLngDTO latLngDTO = new LatLngDTO(0.0, 0.0);
        Mockito.when(carRepository.findActiveAvailableCarsLock()).thenReturn(List.of(car, car2));
        Car found = carService.getClosestFreeCar(latLngDTO, true, true, createCarTypeCabrio());
        assertEquals(car2.getPosition(), found.getPosition());
    }

    @Test
    @DisplayName("Should get null because no car satisfies pet or child requirement")
    public void shouldGetNullBecauseNoCarSatisfiesPetOrChildRequirement() {
        Car car = createCloserCar();
        car.setAllowsPet(false);
        car.setAllowsBaby(false);
        Car car2 = createFurtherCar();
        car2.setAllowsPet(false);
        car2.setAllowsPet(false);
        LatLngDTO latLngDTO = new LatLngDTO(0.0, 0.0);
        Mockito.when(carRepository.findActiveAvailableCarsLock()).thenReturn(List.of(car, car2));
        Car found = carService.getClosestFreeCar(latLngDTO, true, true, createCarTypeCabrio());
        assertNull(found);
    }

    @Test
    @DisplayName("Should get null because no car satisfies type")
    public void shouldGetNullBecauseNoCarSatisfiesType() {
        Car car = createCloserCar();
        Car car2 = createFurtherCar();
        LatLngDTO latLngDTO = new LatLngDTO(0.0, 0.0);
        Mockito.when(carRepository.findActiveAvailableCarsLock()).thenReturn(List.of(car, car2));
        Car found = carService.getClosestFreeCar(latLngDTO, false, false, createCarTypeVan());
        assertNull(found);
    }

    @Test
    @DisplayName("Should get closest car that matches car type")
    public void shouldGetClosestCarThatMatchesCarType() {
        Car car = createCloserCar();
        Car car2 = createFurtherCar();
        car2.setCarType(createCarTypeVan());
        LatLngDTO latLngDTO = new LatLngDTO(0.0, 0.0);
        Mockito.when(carRepository.findActiveAvailableCarsLock()).thenReturn(List.of(car, car2));
        Car found = carService.getClosestFreeCar(latLngDTO, false, false, createCarTypeVan());
        assertEquals(car2, found);
    }

    @Test
    @DisplayName("Should get closest car that did not work over 8 hours")
    public void shouldGetClosestCarThatDidNotWorkOver8Hours() {
        Car car = createCloserCar();
        Car car2 = createFurtherCar();
        LatLngDTO latLngDTO = new LatLngDTO(0.0, 0.0);
        Mockito.when(carRepository.findActiveAvailableCarsLock()).thenReturn(List.of(car, car2));
        Mockito.when(driverService.driverIsLoggedForMoreThan8HoursInLast24Hours(car.getDriver())).thenReturn(true);
        Mockito.when(driverService.driverIsLoggedForMoreThan8HoursInLast24Hours(car2.getDriver())).thenReturn(false);
        Car found = carService.getClosestFreeCar(latLngDTO, false, false, createCarTypeCabrio());
        assertEquals(car2.getPosition(), found.getPosition());
    }

    @Test
    @DisplayName("Should get null because all drivers worked over 8 hours in last 24h")
    public void shouldGetNullBecauseAllDriversWorkedOver8Hours() {
        Car car = createCloserCar();
        Car car2 = createFurtherCar();
        LatLngDTO latLngDTO = new LatLngDTO(0.0, 0.0);
        Mockito.when(carRepository.findActiveAvailableCarsLock()).thenReturn(List.of(car, car2));
        Mockito.when(driverService.driverIsLoggedForMoreThan8HoursInLast24Hours(car.getDriver())).thenReturn(true);
        Mockito.when(driverService.driverIsLoggedForMoreThan8HoursInLast24Hours(car2.getDriver())).thenReturn(true);
        Car found = carService.getClosestFreeCar(latLngDTO, false, false, createCarTypeCabrio());
        assertNull(found);
    }

    @Test
    @DisplayName("Should get null because all available drivers are blocked")
    public void shouldGetNullBecauseAllAvailableDriversAreBlocked() {
        Car car = createCloserCar();
        car.getDriver().setBlocked(true);
        Car car2 = createFurtherCar();
        car2.getDriver().setBlocked(true);
        LatLngDTO latLngDTO = new LatLngDTO(0.0, 0.0);
        Mockito.when(carRepository.findActiveAvailableCarsLock()).thenReturn(List.of(car, car2));
        Mockito.when(driverService.driverIsLoggedForMoreThan8HoursInLast24Hours(car.getDriver())).thenReturn(false);
        Mockito.when(driverService.driverIsLoggedForMoreThan8HoursInLast24Hours(car2.getDriver())).thenReturn(false);
        Car found = carService.getClosestFreeCar(latLngDTO, false, false, createCarTypeCabrio());
        assertNull(found);
    }

    @Test
    @DisplayName("Should get closest car that was not blocked")
    public void shouldGetClosestCarThatWasNotBlocked() {
        Car car = createCloserCar();
        car.getDriver().setBlocked(true);
        Car car2 = createFurtherCar();
        LatLngDTO latLngDTO = new LatLngDTO(0.0, 0.0);
        Mockito.when(carRepository.findActiveAvailableCarsLock()).thenReturn(List.of(car, car2));
        Mockito.when(driverService.driverIsLoggedForMoreThan8HoursInLast24Hours(car.getDriver())).thenReturn(true);
        Mockito.when(driverService.driverIsLoggedForMoreThan8HoursInLast24Hours(car2.getDriver())).thenReturn(false);
        Car found = carService.getClosestFreeCar(latLngDTO, false, false, createCarTypeCabrio());
        assertEquals(car2.getPosition(), found.getPosition());
    }

    @Test
    @DisplayName("Should get BadRequestException for non existing driver")
    public void shouldGetBadRequestExceptionForNonExistingDriver() {
        Mockito.when(driverRepository.findByEmail(DRIVER_EMAIL)).thenThrow(BadRequestException.class);
        assertThrows(BadRequestException.class, () -> carService.getNavigationDisplayForDriver(DRIVER_EMAIL));
    }

    @Test
    @DisplayName("Should get empty navigation display for driver")
    public void shouldGetEmptyNavigationDisplayForDriver() {
        Driver driver = createDriverWithEmptyCarNavigation();
        NavigationDisplay navigationDisplay = new NavigationDisplay();
        Mockito.when(driverRepository.findByEmail(DRIVER_EMAIL)).thenReturn(driver);
        NavigationDisplay found = carService.getNavigationDisplayForDriver(DRIVER_EMAIL);
        assertEquals(navigationDisplay, found);
    }

    @Test
    @DisplayName("Should get first ride navigation display for driver")
    public void shouldGetFirstRideNavigationDisplayForDriver() {
        Driver driver = createDriverWithEmptyCarNavigation();
        driver.getCar().getNavigation().setApproachFirstRide(new CurrentRide());
        driver.getCar().getNavigation().setFirstRide(new CurrentRide());
        Mockito.when(driverRepository.findByEmail(DRIVER_EMAIL)).thenReturn(driver);
        NavigationDisplay found = carService.getNavigationDisplayForDriver(DRIVER_EMAIL);
        assertEquals(driver.getCar().getNavigation().getApproachFirstRide(), found.getFirstApproach());
        assertEquals(driver.getCar().getNavigation().getFirstRide(), found.getFirstRide());
    }

    @Test
    @DisplayName("Should get first and second ride navigation display for driver")
    public void shouldGetFirstAndSecondRideNavigationDisplayForDriver() {
        Driver driver = createDriverWithEmptyCarNavigation();
        driver.getCar().getNavigation().setApproachFirstRide(new CurrentRide());
        driver.getCar().getNavigation().setFirstRide(new CurrentRide());
        driver.getCar().getNavigation().setApproachSecondRide(new CurrentRide());
        driver.getCar().getNavigation().setSecondRide(new CurrentRide());
        Mockito.when(driverRepository.findByEmail(DRIVER_EMAIL)).thenReturn(driver);
        NavigationDisplay found = carService.getNavigationDisplayForDriver(DRIVER_EMAIL);
        assertEquals(driver.getCar().getNavigation().getApproachFirstRide(), found.getFirstApproach());
        assertEquals(driver.getCar().getNavigation().getFirstRide(), found.getFirstRide());
        assertEquals(driver.getCar().getNavigation().getApproachSecondRide(), found.getSecondApproach());
        assertEquals(driver.getCar().getNavigation().getSecondRide(), found.getSecondRide());

    }

    private Driver createDriverWithEmptyCarNavigation() {
        Driver driver = new Driver();
        Car car = new Car();
        Navigation navigation = new Navigation();
        car.setNavigation(navigation);
        driver.setCar(car);
        return driver;
    }

    private Car createFurtherCar() {
        Car car2 = createCar(CAR_ID2);
        car2.setPosition(new Position(2.0, 2.0));
        car2.setCarType(createCarTypeCabrio());
        car2.setDriver(createDriver("b@gmail.com"));
        return car2;
    }

    private Car createCloserCar() {
        Car car = createCar(CAR_ID);
        car.setPosition(new Position(1.0, 1.0));
        car.setCarType(createCarTypeCabrio());
        car.setDriver(createDriver("a@gmail.com"));
        return car;
    }

    private CarType createCarTypeVan() {
        CarType carType = new CarType();
        carType.setPriceForType(100.00);
        carType.setName("Van");
        return carType;
    }
    private CarType createCarTypeCabrio() {
        CarType carType = new CarType();
        carType.setPriceForType(100.00);
        carType.setName("Cabrio");
        return carType;
    }

    private Driver createDriver(String driverEmail) {
        Driver driver = new Driver();
        driver.setEmail(driverEmail);
        driver.setDriverDailyActivity(new DriverDailyActivity());
        driver.setBlocked(false);
        return driver;
    }

    private Car createCar(Long carId) {
        Car car = new Car();
        car.setId(carId);
        car.setDriver(createDriver());
        return car;
    }

    private Driver createDriver() {
        Driver driver = new Driver();
        driver.setBlocked(false);
        return driver;
    }
}
