package com.example.ubernet.repository;

import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class RideRepositoryTest {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void shouldReturnRideWhenFindingRideByPaymentURL() {
        Ride ride = rideRepository.getRideByCustomerPaymentURL("url1");
        assertEquals(1L, ride.getId());
    }

    @Test
    public void shouldReturnNullWhenFindingRideByPaymentURL() {
        Ride ride = rideRepository.getRideByCustomerPaymentURL("wrong-url");
        assertNull(ride);
    }

    @Test
    public void shouldReturnRideWhenFindingRideWhichStatusIsWaitingForCarId() {
        Car car = new Car(null, "NS 123 BA");
        Driver driver = new Driver("test-driver@gmail.com", "Jova", "Jovic", car);
        Ride ride = new Ride(null, RideState.WAITING, driver);
        car = testEntityManager.persist(car);
        testEntityManager.persist(driver);
        testEntityManager.persist(ride);
        testEntityManager.flush();

        Ride foundRide = rideRepository.findRideWhichStatusIsWaitingForCarId(car.getId());
        assertEquals(driver.getEmail(), foundRide.getDriver().getEmail());
        assertEquals(RideState.WAITING, foundRide.getRideState());
    }

    @Test
    public void shouldReturnNullWhenFindingRideWhichStatusIsWaitingForCarId() {
        Ride ride = rideRepository.findRideWhichStatusIsWaitingForCarId(1000L);
        assertNull(ride);
    }

    @Test
    public void shouldReturnRidesWhenFindingReservedRidesWithStatusReserved() {
        Car car1 = new Car(null, "NS 123 BA");
        Ride ride1 = new Ride(null, RideState.RESERVED, (Driver) null);
        ride1.setReservation(true);
        Car car2 = new Car(null, "NS 456 BA");
        Driver driver2 = new Driver("test-driver2@gmail.com", "Laza", "Lazic", car2);
        Ride ride2 = new Ride(null, RideState.WAITING, driver2);
        ride2.setReservation(true);
        testEntityManager.persist(car1);
        testEntityManager.persist(ride1);
        testEntityManager.persist(car2);
        testEntityManager.persist(driver2);
        testEntityManager.persist(ride2);
        testEntityManager.flush();
        List<Ride> foundRide = rideRepository.getReservedWithStatusReservedRides();
        assertEquals(1, foundRide.size());
    }

    @Test
    public void shouldReturnRidesWhenFindingReservedRidesWhichHaveStatusReservedOrWaiting() {
        Car car1 = new Car(null, "NS 123 BA");
        Ride ride1 = new Ride(null, RideState.RESERVED, (Driver) null);
        ride1.setReservation(true);
        Car car2 = new Car(null, "NS 456 BA");
        Driver driver2 = new Driver("test-driver2@gmail.com", "Laza", "Lazic", car2);
        Ride ride2 = new Ride(null, RideState.WAITING, driver2);
        ride2.setReservation(true);
        testEntityManager.persist(car1);
        testEntityManager.persist(ride1);
        testEntityManager.persist(car2);
        testEntityManager.persist(driver2);
        testEntityManager.persist(ride2);
        testEntityManager.flush();
        List<Ride> foundRide = rideRepository.getAcceptedReservationsThatCarDidNotComeYet();
        assertEquals(2, foundRide.size());
    }


    @Test
    public void shouldReturnRideWhenFindingRideWithStatusTravellingByCarId() {
        Car car1 = new Car(null, "NS 123 BA");
        Driver driver1 = new Driver("test-driver2@gmail.com", "Laza", "Lazic", car1);
        Ride ride1 = new Ride(null, RideState.TRAVELLING, driver1);
        car1 = testEntityManager.persist(car1);
        testEntityManager.persist(driver1);
        ride1 = testEntityManager.persist(ride1);
        testEntityManager.flush();
        Ride foundRide = rideRepository.findRideWhereStatusIsTravelingForCarId(car1.getId());
        assertEquals(car1.getId(), foundRide.getDriver().getCar().getId());
        assertEquals(driver1.getEmail(), foundRide.getDriver().getEmail());
        assertEquals(ride1.getId(), foundRide.getId());
    }

    @Test
    public void shouldReturnRidesWhenFindingReservedRidesWithStatusRequestedAndScheduledStartIsNotNull() {
        Ride ride1 = new Ride(null, RideState.REQUESTED, (Driver) null);
        ride1.setScheduledStart(LocalDateTime.now());
        ride1.setReservation(true);
        Ride ride2 = new Ride(null, RideState.REQUESTED, (Driver) null);
        Ride ride3 = new Ride(null, RideState.RESERVED, (Driver) null);
        ride3.setReservation(true);
        ride1 = testEntityManager.persist(ride1);
        testEntityManager.persist(ride2);
        testEntityManager.persist(ride3);
        testEntityManager.flush();
        List<Ride> foundRides = rideRepository.getReservedRidesThatWithStatusRequestedAndScheduledStartIsNotNull();
        assertEquals(1, foundRides.size());
        assertEquals(ride1.getId(), foundRides.get(0).getId());
    }

    @Test
    public void shouldReturnRidesWhenFindingRidesWithStatusWaitingOrTravellingByDriverEmail() {
        Car car1 = new Car(null, "NS 123 BA");
        Driver driver1 = new Driver("test-driver2@gmail.com", "Laza", "Lazic", car1);
        Ride ride1 = new Ride(null, RideState.TRAVELLING, driver1);
        Ride ride2 = new Ride(null, RideState.WAITING, driver1);
        Ride ride3 = new Ride(null, RideState.RESERVED, driver1);
        testEntityManager.persist(car1);
        testEntityManager.persist(driver1);
        ride1 = testEntityManager.persist(ride1);
        testEntityManager.persist(ride2);
        testEntityManager.persist(ride3);
        testEntityManager.flush();
        List<Ride> foundRides = rideRepository.findRidesFromDriverEmail("test-driver2@gmail.com");
        assertEquals(2, foundRides.size());
        assertEquals(ride1.getId(), foundRides.get(0).getId());
    }

    @Test
    public void shouldReturnRideWhenFindingActiveRideByCustomerEmail() {
        Customer customer1 = new Customer("c1");
        Customer customer2 = new Customer("c2");
        Customer customer3 = new Customer("c3");
        Customer customer4 = new Customer("c4");
        Customer customer5 = new Customer("c5");
        Customer customer6 = new Customer("c6");
        testEntityManager.persist(customer1);
        testEntityManager.persist(customer2);
        testEntityManager.persist(customer3);
        testEntityManager.persist(customer4);
        testEntityManager.persist(customer5);
        testEntityManager.persist(customer6);
        Ride ride1 = new Ride(null, RideState.REQUESTED, List.of(customer1));
        Ride ride2 = new Ride(null, RideState.RESERVED, List.of(customer2));
        Ride ride3 = new Ride(null, RideState.WAITING, List.of(customer3));
        Ride ride4 = new Ride(null, RideState.TRAVELLING, List.of(customer4));
        Ride ride5 = new Ride(null, RideState.FINISHED, List.of(customer5));
        Ride ride6 = new Ride(null, RideState.CANCELED, List.of(customer6));
        ride1 = testEntityManager.persist(ride1);
        ride2 = testEntityManager.persist(ride2);
        ride3 = testEntityManager.persist(ride3);
        ride4 = testEntityManager.persist(ride4);
        testEntityManager.persist(ride5);
        testEntityManager.persist(ride6);
        testEntityManager.flush();
        Ride foundRides1 = rideRepository.findActiveRideForCustomer(customer1.getEmail());
        assertEquals(ride1.getId(), foundRides1.getId());
        Ride foundRides2 = rideRepository.findActiveRideForCustomer(customer2.getEmail());
        assertEquals(ride2.getId(), foundRides2.getId());
        Ride foundRides3 = rideRepository.findActiveRideForCustomer(customer3.getEmail());
        assertEquals(ride3.getId(), foundRides3.getId());
        Ride foundRides4 = rideRepository.findActiveRideForCustomer(customer4.getEmail());
        assertEquals(ride4.getId(), foundRides4.getId());
        Ride foundRides5 = rideRepository.findActiveRideForCustomer(customer5.getEmail());
        assertNull(foundRides5);
        Ride foundRides6 = rideRepository.findActiveRideForCustomer(customer6.getEmail());
        assertNull(foundRides6);
    }

    @Test
    public void shouldReturnRidesWhenFindingRidesWithStatusTravelling() {
        Ride ride1 = new Ride(RideState.TRAVELLING);
        Ride ride2 = new Ride(RideState.TRAVELLING);
        Ride ride3 = new Ride(RideState.RESERVED);
        testEntityManager.persist(ride1);
        testEntityManager.persist(ride2);
        testEntityManager.persist(ride3);
        testEntityManager.flush();
        List<Ride> foundRides = rideRepository.findRidesWithStatusTravelling();
        assertEquals(2, foundRides.size());
    }

    @Test
    public void shouldReturnRidesWhenFindingFinishedRidesWithDateOfActualStartBetweenDatesAndByDriverEmail() {
        Driver driver = new Driver("test-driver2@gmail.com");
        Ride ride1 = new Ride(null, RideState.FINISHED, driver);
        ride1.setActualStart(LocalDateTime.now());
        Ride ride2 = new Ride(null, RideState.FINISHED, driver);
        ride2.setActualStart(LocalDateTime.now().minusDays(5));
        Ride ride3 = new Ride(null, RideState.FINISHED, driver);
        ride3.setActualStart(LocalDateTime.now().minusDays(2));
        testEntityManager.persist(driver);
        testEntityManager.persist(ride1);
        testEntityManager.persist(ride2);
        testEntityManager.persist(ride3);
        testEntityManager.flush();
        List<Ride> foundRides = rideRepository.findRideByDriverEmailAndDateRange(driver.getEmail(), LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(1));
        assertEquals(2, foundRides.size());
    }

    @Test
    public void shouldReturnRidesWhenFindingFinishedRidesWithDateOfActualStartBetweenDates() {
        Ride ride1 = new Ride(RideState.FINISHED);
        ride1.setActualStart(LocalDateTime.now());
        Ride ride2 = new Ride(RideState.FINISHED);
        ride2.setActualStart(LocalDateTime.now().minusDays(5));
        Ride ride3 = new Ride(RideState.FINISHED);
        ride3.setActualStart(LocalDateTime.now().minusDays(2));
        testEntityManager.persist(ride1);
        testEntityManager.persist(ride2);
        testEntityManager.persist(ride3);
        testEntityManager.flush();
        List<Ride> foundRides = rideRepository.findRideByDateRange(LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(1));
        assertEquals(2, foundRides.size());
    }




//        Ride ride = new Ride();
//        Customer customer1 = createCustomer1();
//        Customer customer2 = createCustomer2();
//        CustomerPayment customerPayment1 = createCustomerPayment(customer1, "url1");
//        CustomerPayment customerPayment2 = createCustomerPayment(customer2, "url2");

//
//        Customer customer = new Customer();
//        customer.setNumberOfTokens();
//        Student student = new Student(null, STUDENT_FIRST_NAME, STUDENT_LAST_NAME, STUDENT_ID);
//        testEntityManager.persist(student);
//        testEntityManager.flush();

//    private Ride createRide() {
//        Ride ride = new Ride();
//        ride.setId(1000L);
//        ride.setRoute(createRoute());
//        ride.setPayment(createPayment());
//        ride.setDriver(createDriver());
//        return ride;
//    }
//
//    private Driver createDriver() {
//        return null;
//    }
//
//    private Payment createPayment() {
//        return null;
//    }
//
//    private Route createRoute() {
//        return null;
//    }
//
//
//    private CustomerPayment createCustomerPayment(Customer customer, String url) {
//        CustomerPayment customerPayment = new CustomerPayment();
//        customerPayment.setCustomer(customer);
//        customerPayment.setPricePerCustomer(300.00);
//        customerPayment.setUrl(url);
//        customerPayment.setPayed(false);
//        customerPayment.setId(1L);
//        return customerPayment;
//    }
//
//    private Customer createCustomer1() {
//        Customer customer = new Customer();
//        customer.setRole(UserRole.CUSTOMER);
//        customer.setBlocked(false);
//        customer.setDeleted(false);
//        customer.setCity("Novi Sad");
//        customer.setActive(false);
//        customer.setNumberOfTokens(1000);
//        customer.setEmail("pera@gmail.com");
//        customer.setName("Pera");
//        customer.setSurname("Peric");
//        customer.setPhoneNumber("+381 1234657");
//        customer.setUserAuth(createUserAuth());
//        customer.setRole(UserRole.CUSTOMER);
//        customer.setBlocked(false);
//        return customer;
//    }
//
//    private Customer createCustomer2() {
//        Customer customer = new Customer();
//        customer.setRole(UserRole.CUSTOMER);
//        customer.setBlocked(false);
//        customer.setDeleted(false);
//        customer.setCity("Beograd");
//        customer.setActive(false);
//        customer.setNumberOfTokens(2000);
//        customer.setEmail("mika@gmail.com");
//        customer.setName("Mika");
//        customer.setSurname("Mikic");
//        customer.setPhoneNumber("+381 9234657");
//        customer.setUserAuth(createUserAuth());
//        customer.setRole(UserRole.CUSTOMER);
//        customer.setBlocked(false);
//        return customer;
//    }
//
//    private UserAuth createUserAuth() {
//        UserAuth userAuth = new UserAuth();
//        userAuth.setIsEnabled(true);
//        return userAuth;
//    }

}
