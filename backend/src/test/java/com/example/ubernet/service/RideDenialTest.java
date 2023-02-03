package com.example.ubernet.service;

import com.example.ubernet.dto.CancelRideRequest;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RideDenialTest {

    @Mock
    private RideDenialRepository rideDenialRepository;
    @Mock
    private RideRepository rideRepository;
    @Mock
    private NavigationRepository navigationRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private RideService rideService;
    @Mock
    private DriverNotificationRepository driverNotificationRepository;
    @Mock
    private SimpMessagingService simpMessagingService;
    @Mock
    private DriverDailyActivityRepository driverDailyActivityRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private RideDenialService rideDenialService;

    private Long RIDE_ID = 1L;

    @Test
    @DisplayName("Ride does not exist")
    public void nonExistingRide() {
        when(rideService.findById(RIDE_ID)).thenReturn(null);
        assertThrows(BadRequestException.class, () -> rideDenialService.createRideDenial(RIDE_ID, any()));
    }

    @Test
    @DisplayName("Should set driver inactive")
    public void shouldSetDriverInactive() {
        Ride ride = createRide();
        List<DriverNotification> driverNotifications = new ArrayList<>();
        CancelRideRequest cancelRideRequest = createCancelRideRequest(true);
        when(rideService.findById(RIDE_ID)).thenReturn(ride);
        when(driverNotificationRepository.getActiveRideDriverNotifications("driver@gmail.com")).thenReturn(driverNotifications);

        rideDenialService.createRideDenial(RIDE_ID, cancelRideRequest);

        verify(notificationService).createNotificationForCustomersCarTechnicalProblem(ride);
        verify(simpMessagingService).declineRide(driverNotifications, ride.getDriver().getEmail());
    }

    @Test
    @DisplayName("Shouldn't set driver inactive")
    public void shouldntSetDriverInactive() {
        Ride ride = createRide();
        List<DriverNotification> driverNotifications = new ArrayList<>();
        CancelRideRequest cancelRideRequest = createCancelRideRequest(false);
        when(rideService.findById(RIDE_ID)).thenReturn(ride);
        when(driverNotificationRepository.getActiveRideDriverNotifications("driver@gmail.com")).thenReturn(driverNotifications);

        rideDenialService.createRideDenial(RIDE_ID, cancelRideRequest);

        verify(notificationService).createNotificationForCustomersDidNotAppear(ride);
        verify(simpMessagingService).declineRide(driverNotifications, ride.getDriver().getEmail());
    }

    private Ride createRide() {
        Ride ride = new Ride();
        ride.setId(RIDE_ID);
        ride.setDriver(createDriver());
        ride.setCustomers(new ArrayList<>());
        return ride;
    }

    private CancelRideRequest createCancelRideRequest(boolean driverInactive) {
        return CancelRideRequest.builder()
                .shouldSetDriverInactive(driverInactive)
                .reason("reason")
                .build();
    }

    private Car createCar() {
        Car car = new Car();
        car.setPlates("NS 123 BA");
        car.setCarType(new CarType(null, "Cabrio", 200.00, false));
        car.setIsAvailable(true);
        car.setAllowsBaby(false);
        car.setAllowsPet(false);
        car.setNavigation(new Navigation());
        return car;
    }

    private Driver createDriver() {
        Driver driver = new Driver();
        driver.setEmail("driver@gmail.com");
        driver.setDriverDailyActivity(new DriverDailyActivity());
        driver.setBlocked(false);
        driver.setCar(createCar());
        return driver;
    }

}
