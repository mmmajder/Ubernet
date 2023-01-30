package com.example.ubernet.service;

import com.example.ubernet.exception.NotFoundException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.DriverNotificationType;
import com.example.ubernet.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class UpdateCarPositionService {

    private final CurrentRideService currentRideService;
    private final NotificationService notificationService;
    private final NavigationRepository navigationRepository;
    private final SimpMessagingService simpMessagingService;
    private final RideRepository rideRepository;
    private final DriverNotificationRepository driverNotificationRepository;
    private final CarService carService;
    private final CarRepository carRepository;

    public List<Car> setNewPositions() {
        List<Car> cars = setNewPositionForAvailableCars();
        cars.addAll(setNewPositionForNonAvailableCars());
        return cars;
    }

    private List<Car> setNewPositionForNonAvailableCars() {
        List<Car> cars = getActiveNonAvailableCars();
        for (Car car : cars) {
            setNewPositionNonAvailableCar(car.getId());
        }
        return cars;
    }

    public List<Car> getActiveNonAvailableCars() {
        return carRepository.findActiveNonAvailableCars();
    }

    private void setNewPositionNonAvailableCar(long carId) {
        Car car = carService.findById(carId);
        if (car == null) {
            throw new NotFoundException("Car with this id does not exist");
        }
        Navigation navigation = car.getNavigation();
        if (navigation == null) return;
        if (navigation.getApproachFirstRide() != null) {
            removeNonAvailableCarPositions(car, navigation.getApproachFirstRide());
            notificationService.createNotificationForCustomerTimeUntilRide(car);
        } else if (navigation.getFirstRide() != null && navigation.getFirstRide().getStartTime() != null) {
            removeNonAvailableCarPositions(car, navigation.getFirstRide());
        }
        carService.save(car);
    }

    private void removeNonAvailableCarPositions(Car car, CurrentRide currentRide) {
        while (true) {
            if (currentRide.getPositions().size() == 0) {
                if (car.getNavigation().getApproachFirstRide() != null && car.getNavigation().getApproachFirstRide().getPositions().size() == 0) {
                    car.getNavigation().setApproachFirstRide(null);
                    navigationRepository.save(car.getNavigation());
                    carRepository.save(car);
                    createNotificationForDriverToStartRide(car);
                    notificationService.createNotificationForCustomersCarReachedDestination(car);
                } else if (car.getNavigation().getFirstRide().getPositions().size() == 0) {
                    car.getNavigation().setFirstRide(null);
                    navigationRepository.save(car.getNavigation());
                    carRepository.save(car);
                    createNotificationForDriverToEndRide(car);
                }
                return;
            }
            PositionInTime currentPositionInTime = getNextPosition(currentRide);
            double pastTime = currentPositionInTime.getSecondsPast();
            LocalDateTime finishTime = currentRide.getStartTime().plusSeconds((long) pastTime);
            if (finishTime.isBefore(LocalDateTime.now())) {
                currentRide.getPositions().remove(currentPositionInTime);
                currentRideService.save(currentRide);
                if (currentRide.getPositions().size() != 0) {
                    car.setPosition(currentRide.getPositions().get(0).getPosition());
                    carRepository.save(car);
                }
            } else {
                currentRideService.save(currentRide);
                return;
            }
        }
    }

    private void createNotificationForDriverToEndRide(Car car) {
        DriverNotification driverNotification = new DriverNotification();
        driverNotification.setDriverNotificationType(DriverNotificationType.END);
        Ride ride = rideRepository.findRideWhereStatusIsTravelingForCarId(car.getId());
        driverNotification.setRide(ride);
        driverNotification.setFinished(false);
        driverNotificationRepository.save(driverNotification);
        simpMessagingService.sendEndRideNotification(car.getDriver().getEmail(), driverNotification);
    }

    private void createNotificationForDriverToStartRide(Car car) {
        DriverNotification driverNotification = new DriverNotification();
        driverNotification.setDriverNotificationType(DriverNotificationType.START);
        Ride ride = rideRepository.findRideWhichStatusIsWaitingForCarId(car.getId());
        driverNotification.setRide(ride);
        driverNotification.setFinished(false);
        driverNotificationRepository.save(driverNotification);
        simpMessagingService.sendStartRideNotificationToDriver(car.getDriver().getEmail(), ride, driverNotification);
    }

    private List<Car> setNewPositionForAvailableCars() {
        List<Car> cars = carRepository.findActiveAvailableCarsLock();
        for (Car car : cars) {
            setNewPositionAvailableCar(car.getId());
        }
        return cars;
    }

    public void setNewPositionAvailableCar(Long carId) {
        Car car = carService.findById(carId);
        if (car == null) throw new NotFoundException("Car with this id does not exist");
        Navigation navigation = car.getNavigation();
        if (navigation == null) return;
        if (navigation.getFirstRide() == null) {
            throw new NotFoundException("Current ride does not exist");
        }
        if (navigation.getFirstRide().getPositions().size() == 0) {
            return;
        }
        navigation.getFirstRide().getPositions().remove(0);
        currentRideService.save(navigation.getFirstRide());
        if (car.getNavigation().getFirstRide().getPositions().size() == 0) {
            return;
        }
        car.setPosition(car.getNavigation().getFirstRide().getPositions().get(0).getPosition());
        carService.save(car);
    }

    private PositionInTime getNextPosition(CurrentRide currentRide) {
        double minTime = Double.POSITIVE_INFINITY;
        PositionInTime next = null;
        for (PositionInTime positionInTime : currentRide.getPositions()) {
            if (positionInTime.getSecondsPast() < minTime) {
                minTime = positionInTime.getSecondsPast();
                next = positionInTime;
            }
        }
        return next;
    }

}
