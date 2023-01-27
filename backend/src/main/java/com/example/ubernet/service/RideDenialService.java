package com.example.ubernet.service;

import com.example.ubernet.dto.CancelRideRequest;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideDenialType;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class RideDenialService {

    private final RideDenialRepository rideDenialRepository;
    private final RideRepository rideRepository;
    private final NavigationRepository navigationRepository;
    private final CarRepository carRepository;
    private final RideService rideService;
    private final DriverNotificationRepository driverNotificationRepository;
    private final SimpMessagingService simpMessagingService;
    private final DriverDailyActivityRepository driverDailyActivityRepository;
    private final NotificationService notificationService;
    private final CustomerRepository customerRepository;

    public RideDenial save(RideDenial rideDenial) {
        return this.rideDenialRepository.save(rideDenial);
    }

    public RideDenial createRideDenial(Long rideId, CancelRideRequest cancelRideRequest) {
        Ride ride = rideService.findById(rideId);
        if (ride == null) throw new BadRequestException("Ride does not exist");
        if (cancelRideRequest.isShouldSetDriverInactive()) {
            rideDenialSetDriverInactive(ride);
            this.notificationService.createNotificationForCustomersCarTechnicalProblem(ride);
        } else {
            rideDenialFreeDriverFromRide(ride);
            setCustomersInactive(ride.getCustomers());
            this.notificationService.createNotificationForCustomersDidNotAppear(ride);
        }
        removeDriverNotifications(ride);
        return createNewRideDenial(cancelRideRequest, ride);
    }

    private void setCustomersInactive(List<Customer> customers) {
        for (Customer customer: customers) {
            customer.setActive(false);
            customerRepository.save(customer);
        }
    }

    private void removeDriverNotifications(Ride ride) {
        List<DriverNotification> driverNotifications = driverNotificationRepository.getActiveRideDriverNotifications(ride.getDriver().getEmail());
        for (DriverNotification driverNotification : driverNotifications) {
            if (driverNotification.getRide().getId() == ride.getId()) {
                driverNotification.setFinished(true);
                driverNotificationRepository.save(driverNotification);
            }
        }
        this.simpMessagingService.declineRide(driverNotifications, ride.getDriver().getEmail());
    }

    private RideDenial createNewRideDenial(CancelRideRequest cancelRideRequest, Ride ride) {
        RideDenial rideDenial = new RideDenial();
        rideDenial.setRide(ride);
        if (cancelRideRequest.isShouldSetDriverInactive()) {
            rideDenial.setRideDenialType(RideDenialType.PROBLEM);
        } else {
            rideDenial.setRideDenialType(RideDenialType.DID_NOT_COME);
        }
        rideDenial.setDeleted(false);
        rideDenial.setReason(cancelRideRequest.getReason());
        return save(rideDenial);
    }

    private void rideDenialFreeDriverFromRide(Ride ride) {
        ride.setRideState(RideState.CANCELED);
        rideRepository.save(ride);
        Car car = ride.getDriver().getCar();
        Navigation navigation = car.getNavigation();
        if (navigation.getSecondRide() == null) {
            navigation.setFirstRide(null);
            navigation.setApproachFirstRide(null);
            navigationRepository.save(navigation);
            car.setIsAvailable(true);
            carRepository.save(car);
        }
    }

    private void rideDenialSetDriverInactive(Ride ride) {
        ride.setRideState(RideState.RESERVED);      // set ride state to reserved, in order to set different car to go to destination
        ride.setReservation(true);
        ride.setScheduledStart(LocalDateTime.now());
        rideRepository.save(ride);
        Driver driver = ride.getDriver();
        driver.getDriverDailyActivity().setIsActive(false);
        driverDailyActivityRepository.save(driver.getDriverDailyActivity());
        Car car = driver.getCar();
        car.setIsAvailable(false);
        Navigation navigation = this.navigationRepository.save(new Navigation());
        car.setNavigation(navigation);
        carRepository.save(car);
    }
}
