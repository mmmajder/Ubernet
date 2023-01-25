package com.example.ubernet.service;

import com.example.ubernet.controller.DriverInconsistencyController;
import com.example.ubernet.dto.LeafletRouteDTO;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.repository.CustomerRepository;
import com.example.ubernet.repository.DriverInconsistencyRepository;
import com.example.ubernet.repository.RideRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Service
public class DriverInconsistencyService {

    private DriverInconsistencyRepository driverInconsistencyRepository;
    private CustomerRepository customerRepository;
    private RideRepository rideRepository;

    private DriverInconsistency save(DriverInconsistency driverInconsistency) {
        return this.driverInconsistencyRepository.save(driverInconsistency);
    }


    public DriverInconsistency createDriverInconsistency(String customerEmail, Ride ride) {
        DriverInconsistency driverInconsistency = driverInconsistencyRepository.findByRideId(ride.getId());
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) throw new BadRequestException("Customer with this email does not exist");
        if (driverInconsistency == null) {
            driverInconsistency = new DriverInconsistency();
            driverInconsistency.setRide(ride);
            driverInconsistency.setDeleted(false);
            driverInconsistency.setCustomers(Arrays.asList(customer));
        } else {
            driverInconsistency.getCustomers().add(customer);
        }
        return save(driverInconsistency);
    }

    public List<Ride> getReportableRides() {
        List<Ride> currentRides = rideRepository.findCurrentRides();
        List<Ride> reportableRides = new ArrayList<>();
        for (Ride ride : currentRides) {
            List<NumberOfRoute> numberOfRoutes = ride.getRoute().getNumberOfRoute();
            List<NumberOfRoute> carNumberOfRoutes = ride.getDriver().getCar().getNavigation().getFirstRide().getNumberOfRoute();
            if (!routeChoicesSame(numberOfRoutes, carNumberOfRoutes)) {
                reportableRides.add(ride);
            }
        }
        return reportableRides;
    }

    private boolean routeChoicesSame(List<NumberOfRoute> numberOfRoutes, List<NumberOfRoute> carNumberOfRoutes) {
        if (numberOfRoutes.size() != carNumberOfRoutes.size()) return false;
        for (int i = 0; i < numberOfRoutes.size(); i++) {
            if (numberOfRoutes.get(i) != carNumberOfRoutes.get(i)) return false;
        }
        return true;
    }
}
