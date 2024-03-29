package com.example.ubernet.service;

import com.example.ubernet.dto.*;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.model.enums.UserRole;
import com.example.ubernet.repository.*;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateRideServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private RideRepository rideRepository;
    @Mock
    private RideRequestRepository rideRequestRepository;
    @Mock
    private RideService rideService;
    @Mock
    private DriverNotificationService driverNotificationService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private CustomerService customerService;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CustomerPaymentRepository customerPaymentRepository;
    @Mock
    private PositionService positionService;
    @Mock
    private EmailService emailService;
    @Mock
    private CarTypeService carTypeService;
    @Mock
    private RouteRepository routeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PlaceRepository placeRepository;
    @Mock
    private UserService userService;
    @Mock
    private SimpMessagingService simpMessagingService;
    @Captor
    private ArgumentCaptor<Notification> notificationArgumentCaptor;
    @InjectMocks
    private CreateRideService createRideService;

    private final String CUSTOMER_EMAIL = "aca@gmail.com";
    private final String CUSTOMER2_EMAIL = "customer@gmail.com";
    private final String CAR_TYPE_CABRIO = "Cabrio";
    private final Long RIDE_ID = 1L;

    @Test
    @DisplayName("Should throw BadRequestException when customer is active")
    public void shouldThrowBadRequestExceptionWhenActiveCustomerTriesToCreateRide() {
        CreateRideDTO createRideDTO = new CreateRideDTO();
        Customer activeCustomer = new Customer(CUSTOMER_EMAIL, true);
        createRideDTO.setPassengers(List.of(activeCustomer.getEmail()));
        Mockito.when(customerRepository.findByEmail(CUSTOMER_EMAIL)).thenThrow(BadRequestException.class);
        assertThrows(BadRequestException.class, () -> {
            createRideService.createRide(createRideDTO);
        });
    }

    @Test
    @DisplayName("Should throw BadRequestException when customer is blocked")
    public void shouldThrowBadRequestExceptionWhenBlockedCustomerTriesToCreateRide() {
        CreateRideDTO createRideDTO = new CreateRideDTO();
        Customer activeCustomer = new Customer(CUSTOMER_EMAIL, true, false);
        createRideDTO.setPassengers(List.of(activeCustomer.getEmail()));
        Mockito.when(customerRepository.findByEmail(CUSTOMER_EMAIL)).thenThrow(BadRequestException.class);
        assertThrows(BadRequestException.class, () -> {
            createRideService.createRide(createRideDTO);
        });
    }

    @Test
    @DisplayName("Should return ride with RESERVED state")
    public void shouldReturnRideWithReservedState() {
        CreateRideDTO createRideDTO = createRideDTO();
        createRideDTO.setReservation(true);
        createRideDTO.setReservationTime(getReservationTimeIn1Hour());
        createRideDTO.setPassengers(List.of(CUSTOMER2_EMAIL));
        Mockito.when(customerRepository.findByEmail(CUSTOMER2_EMAIL)).thenReturn(createCustomer(CUSTOMER2_EMAIL));
        Mockito.when(carTypeService.findCarTypeByName(CAR_TYPE_CABRIO)).thenReturn(createCarTypeCabrio());
        mockPositionsAndPlaceSave();
        Mockito.when(customerService.getCustomersByEmails(List.of(CUSTOMER2_EMAIL))).thenReturn(List.of(createCustomer(CUSTOMER2_EMAIL)));
        Ride foundRide = createRideService.createRide(createRideDTO);
        assertEquals(RideState.RESERVED, foundRide.getRideState());
    }


    @Test
    @DisplayName("Should return ride with WAITING state for one customer")
    public void shouldReturnRideWithWaitingState() {
        CreateRideDTO createRideDTO = createRideDTO();
        createRideDTO.setPassengers(List.of(CUSTOMER2_EMAIL));
        Mockito.when(customerRepository.findByEmail(CUSTOMER2_EMAIL)).thenReturn(createCustomer(CUSTOMER2_EMAIL));
        Mockito.when(carTypeService.findCarTypeByName(CAR_TYPE_CABRIO)).thenReturn(createCarTypeCabrio());
        mockPositionsAndPlaceSave();
        Mockito.when(rideService.getCarForRide(getCoordinates().get(0), false, false, "Cabrio")).thenReturn(createCar(createDriver()));
        Mockito.when(customerService.getCustomersByEmails(List.of(CUSTOMER2_EMAIL))).thenReturn(List.of(createCustomer(CUSTOMER2_EMAIL)));
        Ride foundRide = createRideService.createRide(createRideDTO);
        assertEquals(RideState.WAITING, foundRide.getRideState());
    }

    @Test
    @DisplayName("Should return ride with REQUESTED state for multiple passengers")
    public void shouldReturnRideWithRequestedStateWhenMultiplePassengers() {
        CreateRideDTO createRideDTO = createRideDTO();
        createRideDTO.setPassengers(List.of(CUSTOMER2_EMAIL, CUSTOMER_EMAIL));
        Mockito.when(customerRepository.findByEmail(CUSTOMER2_EMAIL)).thenReturn(createCustomer(CUSTOMER2_EMAIL));
        Mockito.when(carTypeService.findCarTypeByName(CAR_TYPE_CABRIO)).thenReturn(createCarTypeCabrio());
        mockPositionsAndPlaceSave();
        Mockito.when(customerService.getCustomersByEmails(List.of(CUSTOMER2_EMAIL, CUSTOMER_EMAIL))).thenReturn(List.of(createCustomer(CUSTOMER2_EMAIL), createCustomer(CUSTOMER_EMAIL)));
        Ride foundRide = createRideService.createRide(createRideDTO);
        assertEquals(RideState.REQUESTED, foundRide.getRideState());
    }


    @Test
    @DisplayName("Should throw BadRequestExceptionWhenInvitedPassengerIsBlocked")
    public void shouldThrowBadRequestWhenInvitedPassengerIsBlocked() {
        CreateRideDTO createRideDTO = createRideDTO();
        createRideDTO.setPassengers(List.of(CUSTOMER2_EMAIL, CUSTOMER_EMAIL));
        Mockito.when(customerRepository.findByEmail(CUSTOMER2_EMAIL)).thenReturn(createCustomer(CUSTOMER2_EMAIL));
        Customer customer = createCustomer(CUSTOMER_EMAIL);
        customer.setBlocked(true);
        Mockito.when(customerService.getCustomersByEmails(createRideDTO.getPassengers())).thenReturn(List.of(createCustomer(CUSTOMER2_EMAIL), customer));
        Mockito.when(carTypeService.findCarTypeByName(CAR_TYPE_CABRIO)).thenReturn(createCarTypeCabrio());
        mockPositionsAndPlaceSave();
        assertThrows(BadRequestException.class, () -> {
            createRideService.createRide(createRideDTO);
        });
    }

    @Test
    @DisplayName("Should init ride when customer request ride for himself and ride is not reservation")
    public void shouldInitRideWhenCustomerOrdersRideForOnlyHimselfAndNotReservation() throws MessagingException {
        CreateRideDTO createRideDTO = createRideDTO();
        Driver driver = createDriver();
        Car car = createCar(driver);
        driver.setCar(car);
        Customer customer = createCustomer(CUSTOMER2_EMAIL);
        createRideDTO.setPassengers(List.of(CUSTOMER2_EMAIL));
        Mockito.when(customerRepository.findByEmail(CUSTOMER2_EMAIL)).thenReturn(customer);
        Mockito.when(carTypeService.findCarTypeByName(CAR_TYPE_CABRIO)).thenReturn(createCarTypeCabrio());
        mockPositionsAndPlaceSave();
        Mockito.when(rideService.getCarForRide(getCoordinates().get(0), false, false, "Cabrio")).thenReturn(car);
        Mockito.doNothing().when(emailService).sendEmailToOtherPassengers(any(), anyList());
        Mockito.when(customerService.getCustomersByEmails(List.of(CUSTOMER2_EMAIL))).thenReturn(List.of(customer));
        Ride foundRide = createRideService.createRide(createRideDTO);
        assertNotNull(foundRide.getDriver());
        assertNotNull(foundRide.getDriver().getCar());
        assertTrue(foundRide.getDriver().getDriverDailyActivity().getIsActive());
    }

    @Test
    @DisplayName("Should init ride when customer request ride for himself and ride is not reservation")
    public void shouldCreateNotificationsForPassengersThanAreInvitedToSplitFare() {
        List<Customer> customers = List.of(createCustomer("a@gmail.com"), createCustomer("p@gmail.com"), createCustomer("c@gmail.com"));
        createRideService.notifyCustomers(customers, RIDE_ID);
        verify(notificationService, times(customers.size() - 1)).save(notificationArgumentCaptor.capture());
    }

    private Car createCar(Driver driver) {
        Car car = new Car();
        car.setDriver(driver);
        return car;
    }

    private Driver createDriver() {
        Driver driver = new Driver();
        driver.setDriverDailyActivity(createDriverDailyActivity());
        return driver;
    }

    private DriverDailyActivity createDriverDailyActivity() {
        DriverDailyActivity driverDailyActivity = new DriverDailyActivity();
        driverDailyActivity.setIsActive(true);
        return driverDailyActivity;
    }


    private void mockPositionsAndPlaceSave() {
        for (PlaceDTO place : createRoute()) {
            Position position = new Position();
            position.setX(place.getPosition().getX());
            position.setY(place.getPosition().getY());
            Mockito.when(positionService.save(position)).thenReturn(position);
            Place p = new Place(place.getName(), position);
            Mockito.when(placeRepository.save(p)).thenReturn(p);
        }
    }

    private CarType createCarTypeCabrio() {
        CarType carType = new CarType();
        carType.setPriceForType(100.00);
        carType.setName("Cabrio");
        return carType;
    }

    private Customer createCustomer(String email) {
        Customer customer = new Customer();
        customer.setBlocked(false);
        customer.setEmail(email);
        customer.setRole(UserRole.CUSTOMER);
        customer.setSurname("Customeric");
        customer.setName("Customer");
        customer.setPassword("customer");
        customer.setNumberOfTokens(1000);
        customer.setActive(false);
        return customer;
    }

    public CreateRideDTO createRideDTO() {
        CreateRideDTO createRideDTO = new CreateRideDTO();
        createRideDTO.setCarType("Cabrio");
        createRideDTO.setCoordinates(getCoordinates());
        createRideDTO.setInstructions(getInstructions());
        createRideDTO.setHasChild(false);
        createRideDTO.setHasPet(false);
        createRideDTO.setPassengers(List.of(CUSTOMER2_EMAIL));
        createRideDTO.setTotalDistance(83.4);
        createRideDTO.setTotalTime(21.9);
        createRideDTO.setReservation(false);
        createRideDTO.setRoute(createRoute());
        createRideDTO.setPayment(createPaymentSinglePerson());
        return createRideDTO;
    }


    private List<PlaceDTO> createRoute() {
        List<PlaceDTO> route = new ArrayList<>();
        PositionDTO position1 = new PositionDTO(19.8451756, 45.2551338);
        String name1 = "Novi Sad";
        PositionDTO position2 = new PositionDTO(19.8434958, 45.2562288);
        String name2 = "Prolaz Milosa Hadzica 4, Novi Sad";
        route.add(new PlaceDTO(position1, name1));
        route.add(new PlaceDTO(position2, name2));
        return route;
    }

    private PaymentDTO createPaymentSinglePerson() {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setTotalPrice(210.01);
        paymentDTO.setCustomerThatPayed(CUSTOMER2_EMAIL);
        return paymentDTO;
    }

    private List<LatLngDTO> getCoordinates() {
        List<LatLngDTO> coords = new ArrayList<>();
        coords.add(new LatLngDTO(45.25624, 19.84421));
        coords.add(new LatLngDTO(45.25627, 19.84418));
        coords.add(new LatLngDTO(45.2563, 19.84414));
        coords.add(new LatLngDTO(45.25647, 19.84392));
        coords.add(new LatLngDTO(45.25648, 19.8439));
        coords.add(new LatLngDTO(45.25651, 19.84385));
        coords.add(new LatLngDTO(45.25651, 19.84385));
        coords.add(new LatLngDTO(45.2565, 19.84384));
        coords.add(new LatLngDTO(45.25649, 19.84382));
        coords.add(new LatLngDTO(45.25638, 19.84369));
        coords.add(new LatLngDTO(45.25623, 19.8435));
        coords.add(new LatLngDTO(45.25623, 19.8435));
        return coords;
    }

    private List<InstructionDTO> getInstructions() {
        List<InstructionDTO> instructions = new ArrayList<>();
        instructions.add(new InstructionDTO(41.2, 15.8, "Његошева"));
        instructions.add(new InstructionDTO(42.2, 6.1, "Пролаз Милоша Хаџића"));
        instructions.add(new InstructionDTO(0, 0, "Пролаз Милоша Хаџића"));
        return instructions;
    }

    private String getReservationTimeIn1Hour() {
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(1);
        System.out.println(localDateTime);
        LocalDateTime midn = LocalDateTime.of(2023, 2, 1, 0, 1);
        System.out.println(midn);
        String hour;
        String sufix;
        if (localDateTime.getHour() == 0) {
            hour = "12";
            sufix = "AM";
        } else if (localDateTime.getHour() <= 12) {
            hour = String.valueOf(localDateTime.getHour());
            sufix = "AM";
        } else {
            hour = String.valueOf(localDateTime.getHour() - 12);
            sufix = "PM";
        }
        return hour + ":" + String.format("%02d", localDateTime.getMinute()) + " " + sufix;
    }
}

