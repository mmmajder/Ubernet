package com.example.ubernet.repository;

import com.example.ubernet.model.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {

    Page<Ride> findByDriverEmailAndCustomersEmail(String driverEmail, String customerEmail, Pageable pageable);

    Page<Ride> findByDriverEmail(String driverEmail, Pageable pageable);

    Page<Ride> findByCustomersEmail(String customerEmail, Pageable pageable);

    Page<Ride> findAll(Pageable pageable);

    @Query(value = "SELECT ride FROM Ride ride INNER JOIN ride.payment p INNER JOIN p.customers c where c.url=:url")
    Ride getRideByCustomerPaymentURL(String url);

    @Query(value = "SELECT ride FROM Ride ride WHERE ride.driver.car.id=:carId AND ride.rideState=com.example.ubernet.model.enums.RideState.WAITING")
    List<Ride> findRideWhereStatusIsWaitingForCarId(long carId);

    @Query(value = "SELECT ride FROM Ride ride WHERE ride.isReservation=true AND ride.rideState=com.example.ubernet.model.enums.RideState.RESERVED")
    List<Ride> getReservedRides();

    @Query(value = "SELECT ride FROM Ride ride WHERE ride.driver.car.id=:carId AND ride.rideState=com.example.ubernet.model.enums.RideState.TRAVELLING")
    Ride findRideWhereStatusIsTravelingForCarId(long carId);

    @Query(value = "SELECT ride FROM Ride ride WHERE ride.scheduledStart is not null AND ride.rideState=com.example.ubernet.model.enums.RideState.REQUESTED")
    List<Ride> getReservedRidesThatWereNotPayedAndScheduledTimePassed();

    @Query(value = "SELECT ride FROM Ride ride WHERE ride.driver.email=:email AND (ride.rideState=com.example.ubernet.model.enums.RideState.WAITING OR ride.rideState=com.example.ubernet.model.enums.RideState.TRAVELLING)")
    List<Ride> findRideFromDriverEmail(String email);

    @Query(value = "SELECT ride FROM Ride ride JOIN ride.customers c WHERE c.email=:email AND (ride.rideState=com.example.ubernet.model.enums.RideState.WAITING OR ride.rideState=com.example.ubernet.model.enums.RideState.TRAVELLING OR ride.rideState=com.example.ubernet.model.enums.RideState.REQUESTED OR ride.rideState=com.example.ubernet.model.enums.RideState.RESERVED)")
    Ride findActiveRideForCustomer(String email);

    @Query(value = "SELECT ride FROM Ride ride WHERE ride.driver.email=:driverEmail AND ride.rideState=com.example.ubernet.model.enums.RideState.FINISHED AND (ride.actualStart BETWEEN :startOfTheDay AND :endOfTheDay)")
    List<Ride> findRideByDriverEmailAndDateRange(String driverEmail, LocalDateTime startOfTheDay, LocalDateTime endOfTheDay);

    @Query(value = "SELECT ride FROM Ride ride WHERE (ride.actualStart BETWEEN :startOfTheDay AND :endOfTheDay) AND ride.rideState=com.example.ubernet.model.enums.RideState.FINISHED ")
    List<Ride> findRideByDateRange(LocalDateTime startOfTheDay, LocalDateTime endOfTheDay);

    @Query(value = "SELECT ride FROM Ride ride JOIN ride.customers c WHERE c.email=:customerEmail AND (ride.actualStart BETWEEN :startOfTheDay AND :endOfTheDay) AND ride.rideState=com.example.ubernet.model.enums.RideState.FINISHED ")
    List<Ride> findRideByCustomersEmailAndDateRange(String customerEmail, LocalDateTime startOfTheDay, LocalDateTime endOfTheDay);
}
