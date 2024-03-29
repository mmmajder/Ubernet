package com.example.ubernet.repository;

import com.example.ubernet.model.Car;
import com.example.ubernet.model.Customer;
import com.example.ubernet.model.Driver;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.enums.RideState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    public void shouldReturnRidesWhenFindingRidesWhichStatusIsWaitingForCarId() {
        Car car = new Car(null, "NS 123 BA");
        Driver driver = new Driver("test-driver@gmail.com", "Jova", "Jovic", car);
        Ride ride = new Ride(null, RideState.WAITING, driver);
        car = testEntityManager.persist(car);
        testEntityManager.persist(driver);
        testEntityManager.persist(ride);
        testEntityManager.flush();

        List<Ride> foundRides = rideRepository.findRidesWhichStatusIsWaitingForCarId(car.getId());
        assertEquals(driver.getEmail(), foundRides.get(0).getDriver().getEmail());
        assertEquals(RideState.WAITING, foundRides.get(0).getRideState());
        assertEquals(1, foundRides.size());
    }

    @Test
    public void shouldReturnNullWhenFindingRideWhichStatusIsWaitingForCarId() {
        List<Ride> rides = rideRepository.findRidesWhichStatusIsWaitingForCarId(1000L);
        assertEquals(0, rides.size()) ;
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
        assertEquals(3, foundRides.size());
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
    public void shouldReturnRideWhenFindingByValidId() {
        Ride ride = rideRepository.findById(1L).orElse(null);

        assertEquals(1L, ride.getId());
    }

    @Test
    public void shouldReturnNullWhenFindingByInvalidId() {
        Ride ride = rideRepository.findById(695412L).orElse(null);

        assertNull(ride);
    }

    @Test
    public void shouldReturnRideWhenFindingByValidEmailAndActiveRide() {
        Customer customer = new Customer();
        customer.setEmail("asdecascac@gmail.com");
        Ride ride = new Ride();
        ride.setId(1L);
        ride.setRideState(RideState.TRAVELLING);
        ride.setCustomers(List.of(customer));

        testEntityManager.merge(customer);
        testEntityManager.merge(ride);
        testEntityManager.flush();

        Ride activeRide = rideRepository.findActiveRideForCustomer("asdecascac@gmail.com");

        assertEquals(1L, activeRide.getId());
    }

    @Test
    public void shouldReturnNullWhenFindingByValidEmailAndInactiveRide() {
        Customer customer = new Customer();
        customer.setEmail("asdecascac@gmail.com");
        Ride ride = new Ride();
        ride.setId(555L);
        ride.setRideState(RideState.FINISHED);
        ride.setCustomers(List.of(customer));

        testEntityManager.merge(customer);
        testEntityManager.merge(ride);
        testEntityManager.flush();

        Ride activeRide = rideRepository.findActiveRideForCustomer("asdecascac@gmail.com");

        assertNull(activeRide);
    }

    @Test
    public void shouldReturnRideWhenSaving() {
        Ride ride = new Ride();
        ride.setId(1L);

        Ride savedRide = rideRepository.save(ride);

        assertEquals(ride.getId(), savedRide.getId());
    }
}
