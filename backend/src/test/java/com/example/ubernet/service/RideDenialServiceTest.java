package com.example.ubernet.service;

import com.example.ubernet.dto.CancelRideRequest;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.DriverNotificationType;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RideDenialServiceTest {

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
    @Captor
    private ArgumentCaptor<Ride> rideArgumentCaptor;
    @Captor
    private ArgumentCaptor<Navigation> navigationArgumentCaptor;
    @Captor
    private ArgumentCaptor<Car> carArgumentCaptor;
    @Captor
    private ArgumentCaptor<DriverDailyActivity> driverDailyActivityArgumentCaptor;
    @InjectMocks
    private RideDenialService rideDenialService;

    private final Long RIDE_ID = 1L;
    private final String DRIVER_EMAIL = "driver@gmail.com";
    private final String CAR_PLATES = "NS 123 BA";

    @Test
    @DisplayName("Ride does not exist")
    public void nonExistingRide() {
        when(rideService.findById(RIDE_ID)).thenReturn(null);
        assertThrows(BadRequestException.class, () -> rideDenialService.createRideDenial(RIDE_ID, any()));
    }

    @Test
    @DisplayName("Should set driver inactive - no notifications")
    public void shouldSetDriverInactive() {
        Ride ride = createRide();
        List<DriverNotification> driverNotifications = new ArrayList<>();
        CancelRideRequest cancelRideRequest = createCancelRideRequest(true);
        when(rideService.findById(RIDE_ID)).thenReturn(ride);
        when(driverNotificationRepository.getActiveRideDriverNotifications(DRIVER_EMAIL)).thenReturn(driverNotifications);

        rideDenialService.createRideDenial(RIDE_ID, cancelRideRequest);

        verify(notificationService).createNotificationForCustomersCarTechnicalProblem(ride);
        verify(simpMessagingService).declineRide(driverNotifications, ride.getDriver().getEmail());

        verify(rideRepository, times(1)).save(rideArgumentCaptor.capture());
        verify(driverDailyActivityRepository, times(1)).save(driverDailyActivityArgumentCaptor.capture());
        verify(navigationRepository, times(1)).save(navigationArgumentCaptor.capture());
        verify(carRepository, times(1)).save(carArgumentCaptor.capture());
        verify(driverNotificationRepository, times(1)).getActiveRideDriverNotifications(DRIVER_EMAIL);

        assertEquals(RIDE_ID, rideArgumentCaptor.getValue().getId());
        assertEquals(CAR_PLATES, carArgumentCaptor.getValue().getPlates());
    }

    @Test
    @DisplayName("Should set driver inactive - has 2 notifications")
    public void shouldSetDriverInactive2() {
        Ride ride = createRide();
        List<DriverNotification> driverNotifications = createDriverNotifications(ride);

        CancelRideRequest cancelRideRequest = createCancelRideRequest(true);
        when(rideService.findById(RIDE_ID)).thenReturn(ride);
        when(driverNotificationRepository.getActiveRideDriverNotifications(DRIVER_EMAIL)).thenReturn(driverNotifications);

        rideDenialService.createRideDenial(RIDE_ID, cancelRideRequest);

        verify(notificationService).createNotificationForCustomersCarTechnicalProblem(ride);
        verify(simpMessagingService).declineRide(driverNotifications, ride.getDriver().getEmail());

        verify(rideRepository, times(1)).save(rideArgumentCaptor.capture());
        verify(driverDailyActivityRepository, times(1)).save(driverDailyActivityArgumentCaptor.capture());
        verify(navigationRepository, times(1)).save(navigationArgumentCaptor.capture());
        verify(carRepository, times(1)).save(carArgumentCaptor.capture());
        verify(driverNotificationRepository, times(1)).getActiveRideDriverNotifications(DRIVER_EMAIL);
        verify(driverNotificationRepository, times(2)).save(any());

        assertEquals(RIDE_ID, rideArgumentCaptor.getValue().getId());
        assertEquals(CAR_PLATES, carArgumentCaptor.getValue().getPlates());
    }

    @Test
    @DisplayName("Shouldn't set driver inactive - driver has second ride")
    public void shouldntSetDriverInactive() {
        Ride ride = createRide();
        List<DriverNotification> driverNotifications = new ArrayList<>();
        CancelRideRequest cancelRideRequest = createCancelRideRequest(false);
        ride.getDriver().getCar().setNavigation(createNavigation());

        when(rideService.findById(RIDE_ID)).thenReturn(ride);
        when(driverNotificationRepository.getActiveRideDriverNotifications(DRIVER_EMAIL)).thenReturn(driverNotifications);

        rideDenialService.createRideDenial(RIDE_ID, cancelRideRequest);

        verify(notificationService).createNotificationForCustomersDidNotAppear(ride);
        verify(simpMessagingService).declineRide(driverNotifications, ride.getDriver().getEmail());
        verify(simpMessagingService).updateRouteForSelectedCar(DRIVER_EMAIL, ride);

        verify(rideRepository, times(1)).save(rideArgumentCaptor.capture());
        assertEquals(RIDE_ID, rideArgumentCaptor.getValue().getId());
        assertEquals(RideState.CANCELED, rideArgumentCaptor.getValue().getRideState());

        verify(carRepository, times(0)).save(carArgumentCaptor.capture());
        verify(navigationRepository, times(1)).save(navigationArgumentCaptor.capture());
        assertNull(navigationArgumentCaptor.getValue().getSecondRide());
        assertNull(navigationArgumentCaptor.getValue().getApproachSecondRide());
    }

    @Test
    @DisplayName("Shouldn't set driver inactive - driver doesn't have second ride")
    public void shouldntSetDriverInactive2() {
        Ride ride = createRide();
        List<DriverNotification> driverNotifications = new ArrayList<>();
        CancelRideRequest cancelRideRequest = createCancelRideRequest(false);

        when(rideService.findById(RIDE_ID)).thenReturn(ride);
        when(driverNotificationRepository.getActiveRideDriverNotifications(DRIVER_EMAIL)).thenReturn(driverNotifications);

        rideDenialService.createRideDenial(RIDE_ID, cancelRideRequest);

        verify(notificationService).createNotificationForCustomersDidNotAppear(ride);
        verify(simpMessagingService).declineRide(driverNotifications, ride.getDriver().getEmail());

        verify(rideRepository, times(1)).save(rideArgumentCaptor.capture());
        assertEquals(RIDE_ID, rideArgumentCaptor.getValue().getId());
        assertEquals(RideState.CANCELED, rideArgumentCaptor.getValue().getRideState());

        verify(navigationRepository, times(1)).save(navigationArgumentCaptor.capture());
        assertNull(navigationArgumentCaptor.getValue().getFirstRide());
        assertNull(navigationArgumentCaptor.getValue().getApproachFirstRide());
        verify(carRepository, times(1)).save(carArgumentCaptor.capture());
    }

    private Ride createRide() {
        Ride ride = new Ride();
        ride.setId(RIDE_ID);
        ride.setDriver(createDriver());
        ride.setCustomers(new ArrayList<>());
        return ride;
    }

    private List<DriverNotification> createDriverNotifications(Ride ride) {
        List<DriverNotification> driverNotifications = new ArrayList<>();

        DriverNotification driverNotification = new DriverNotification();
        driverNotification.setRide(ride);
        driverNotification.setFinished(true);
        driverNotification.setDriverNotificationType(DriverNotificationType.START);
        driverNotifications.add(driverNotification);

        DriverNotification driverNotification2 = new DriverNotification();
        driverNotification2.setRide(ride);
        driverNotification2.setFinished(true);
        driverNotification2.setDriverNotificationType(DriverNotificationType.END_OF_SHIFT);
        driverNotifications.add(driverNotification2);

        return driverNotifications;
    }

    private CancelRideRequest createCancelRideRequest(boolean driverInactive) {
        return CancelRideRequest.builder()
                .shouldSetDriverInactive(driverInactive)
                .reason("reason")
                .build();
    }

    private Car createCar() {
        Car car = new Car();
        car.setPlates(CAR_PLATES);
        car.setCarType(new CarType(null, "Cabrio", 200.00, false));
        car.setIsAvailable(true);
        car.setAllowsBaby(false);
        car.setAllowsPet(false);
        car.setNavigation(new Navigation());
        return car;
    }

    private Navigation createNavigation() {
        Navigation navigation = new Navigation();
        navigation.setSecondRide(new CurrentRide());
        return navigation;
    }

    private Driver createDriver() {
        Driver driver = new Driver();
        driver.setEmail(DRIVER_EMAIL);
        driver.setDriverDailyActivity(new DriverDailyActivity());
        driver.setBlocked(false);
        driver.setCar(createCar());
        driver.getCar().setDriver(driver);
        return driver;
    }

}
