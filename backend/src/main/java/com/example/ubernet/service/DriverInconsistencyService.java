package com.example.ubernet.service;

import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.repository.CustomerRepository;
import com.example.ubernet.repository.DriverInconsistencyRepository;
import com.example.ubernet.repository.RideRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class DriverInconsistencyService {

    private DriverInconsistencyRepository driverInconsistencyRepository;
    private CustomerRepository customerRepository;
    private RideRepository rideRepository;
    private NotificationService notificationService;

    private DriverInconsistency save(DriverInconsistency driverInconsistency) {
        return this.driverInconsistencyRepository.save(driverInconsistency);
    }

    public DriverInconsistency createDriverInconsistencyComplaint(String customerEmail, Long rideId) {
        DriverInconsistency driverInconsistency = driverInconsistencyRepository.findByRideId(rideId);
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) throw new BadRequestException("Customer with this email does not exist");
        if (driverInconsistency.getCustomers().size() == 0) {
            driverInconsistency.setCustomers(List.of(customer));
        } else {
            for (Customer customerThatComplained : driverInconsistency.getCustomers()) {
                if (customerThatComplained.getEmail().equals(customerEmail)) throw new BadRequestException("You have already complained!");
            }
            driverInconsistency.getCustomers().add(customer);
        }
        return save(driverInconsistency);
    }

    public List<Ride> getReportableRides() {
        List<Ride> currentRides = rideRepository.findRidesWithStatusTravelling();
        List<Ride> reportableRides = new ArrayList<>();
        for (Ride ride : currentRides) {
            if (driverInconsistencyRepository.findByRideId(ride.getId())!=null) continue;
            Car car = ride.getDriver().getCar();
            Position position = car.getPosition();
            boolean carIsOnRightPath = false;
            for (PositionInTime positionInTime: ride.getRideRequest().getCurrentRide().getPositions()) {
                if (Objects.equals(positionInTime.getPosition().getX(), position.getX())
                        && Objects.equals(positionInTime.getPosition().getY(), position.getY())) {
                    carIsOnRightPath = true;
                    break;
                }
            }
            if (!carIsOnRightPath) {
                for (Customer customer:ride.getCustomers()){
                    this.notificationService.notifyCustomerForDriverInconsistency(customer, ride);
                    DriverInconsistency driverInconsistency = new DriverInconsistency();
                    driverInconsistency.setRide(ride);
                    driverInconsistency.setDeleted(false);
                    save(driverInconsistency);
                }
                reportableRides.add(ride);
            }
        }
        return reportableRides;
    }
}
