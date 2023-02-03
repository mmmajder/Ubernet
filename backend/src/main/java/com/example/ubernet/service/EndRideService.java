package com.example.ubernet.service;

import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.Car;
import com.example.ubernet.model.Customer;
import com.example.ubernet.model.Navigation;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.CustomerRepository;
import com.example.ubernet.repository.NavigationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
public class EndRideService {

    private final RideService rideService;
    private final DriverNotificationService driverNotificationService;
    private final CustomerRepository customerRepository;
    private final NavigationRepository navigationRepository;
    private final CarService carService;
    private final CurrentRideService currentRideService;

    public Ride endRide(Long rideId) {
        Ride ride = rideService.findById(rideId);
        if (ride == null) throw new BadRequestException("Ride does not exist");
        rideService.updateRideStatus(ride, RideState.FINISHED);
        setEndOfRide(ride);
        setCustomersToInactive(ride.getCustomers());
        Car car = updateCarNavigationAfterEndRide(ride);
        driverNotificationService.resetOldNotificationsForRide(car, ride);
        return ride;
    }

    private void setEndOfRide(Ride ride) {
        ride.setActualEnd(LocalDateTime.now());
        rideService.save(ride);
    }

    private void setCustomersToInactive(List<Customer> customers) {
        customers = customers.stream()
                .peek(customer -> customer.setActive(false))
                .collect(Collectors.toList());
        customerRepository.saveAll(customers);
    }

    private Car updateCarNavigationAfterEndRide(Ride ride) {
        Car car = ride.getDriver().getCar();
        Navigation navigation = ride.getDriver().getCar().getNavigation();
        if (navigation.getSecondRide() != null) {
            navigation.setFirstRide(navigation.getSecondRide());
            navigation.setSecondRide(null);
            navigation.setApproachFirstRide(navigation.getApproachSecondRide());
            navigation.setApproachSecondRide(null);
            navigation.getApproachFirstRide().setStartTime(LocalDateTime.now());
            currentRideService.save(navigation.getApproachFirstRide());
        } else {
            navigation.setFirstRide(null);
            car.setIsAvailable(true);
            carService.save(car);
        }
        navigationRepository.save(navigation);
        return car;
    }
}
