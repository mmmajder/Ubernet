package com.example.ubernet.service;

import com.example.ubernet.dto.*;
import com.example.ubernet.exception.NotFoundException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.DriverNotificationType;
import com.example.ubernet.repository.*;
import com.example.ubernet.utils.MapUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class CarService {
    private final CarRepository carRepository;
    private final UserService userService;
    private final CarTypeService carTypeService;
    private final PositionService positionService;
    private final CurrentRideService currentRideService;
    private final PositionInTimeRepository positionInTimeRepository;
    private final NavigationRepository navigationRepository;
    private final SimpMessagingService simpMessagingService;
    private final RideRepository rideRepository;
    private final DriverNotificationRepository driverNotificationRepository;
    private final DriverRepository driverRepository;
    private final NotificationService notificationService;

    public Car createCar(CreateCarDTO createCarDTO) {
        User user = userService.findByEmail(createCarDTO.getEmail());
        if (user == null) {
            return null;
        }
        return createCar(createCarDTO, (Driver) user);
    }

    public Car save(Car car) {
        return carRepository.save(car);
    }

    private Car createCar(CreateCarDTO createCarDTO, Driver user) {
        Car car = new Car();
        car.setCarType(getCarType(createCarDTO));
        car.setDeleted(false);
        car.setIsAvailable(false);
        user.setCar(car);
        car.setDriver((Driver) userService.findByEmail(user.getEmail()));
//        car.setPosition(null);
        save(car);
        userService.save(user);
        return car;
    }

    private CarType getCarType(CreateCarDTO createCarDTO) {
        CarType carType = new CarType();
        carType.setPriceForType(createCarDTO.getPriceForType());
        carType.setName(createCarDTO.getName());
        carType.setDeleted(false);
        carTypeService.save(carType);
        return carType;
    }

    public Car findById(long id) {
        return carRepository.findById(id).orElse(null);
    }

    public List<Car> getActiveAvailableCars() {
        return carRepository.findActiveAvailableCars();
    }

    public List<Car> getActiveNonAvailableCars() {
        return carRepository.findActiveNonAvailableCars();
    }

    private ActiveCarResponse getActiveAvailableCar(Car car) {
        ActiveCarResponse activeAvailableCarResponse = new ActiveCarResponse();
        activeAvailableCarResponse.setCarId(car.getId());
        activeAvailableCarResponse.setDriverEmail(car.getDriver().getEmail());
//        activeAvailableCarResponse.setCurrentRide(car.getCurrentRide());
        activeAvailableCarResponse.setCurrentPosition(car.getPosition());
        return activeAvailableCarResponse;
    }

    @Transactional(readOnly = false)
    public List<Car> getActiveCars() {
        return carRepository.findActiveCars();
    }

    public ActiveCarResponse reachedDestination(Long carId) {
        Car car = findById(carId);
        return getActiveAvailableCar(car);
    }

    public ActiveCarResponse getPosition(Long carId) {
        Car car = findById(carId);
        if (car == null) {
            return null;
        }
        return getActiveAvailableCar(car);
    }

    public Car getCarByDriverEmail(String email) {
        Driver driver = (Driver) userService.findByEmail(email);
        return carRepository.findByDriver(driver);
    }

    public Car getCarByActiveDriverEmail(String driverEmail) {
        Driver driver = (Driver) userService.findByEmail(driverEmail);
        if (!driver.getDriverDailyActivity().getIsActive()) {
            return null;
        }
        return carRepository.findByDriver(driver);
    }

    public Car updateCar(CarResponseNoDriver carResponseNoDriver) {
        System.out.println("uusao u update u servisu");
        Car car = findById(carResponseNoDriver.getId());
        car.setName(carResponseNoDriver.getName());
        car.setPlates(carResponseNoDriver.getPlates());
        car.setCarType(carResponseNoDriver.getCarType());
        car.setAllowsBaby(carResponseNoDriver.getAllowsBaby());
        car.setAllowsPet(carResponseNoDriver.getAllowsPet());
        return save(car);
    }


    //New
    public Car setNewFreeRide(List<Position> positions, Long carId) {
        Car car = findById(carId);
        if (car == null) {
            throw new NotFoundException("Car with this id does not exist");
        }
        positionService.savePositions(positions);
        car.getNavigation().setFirstRide(createNewFreeRide(positions));
        navigationRepository.save(car.getNavigation());
        return save(car);
    }

    public CurrentRide createNewFreeRide(List<Position> positions) {
        CurrentRide currentRide = new CurrentRide();
        currentRide.setDeleted(false);
        currentRide.setPositions(createPositionsWithEmptyTime(positions));
        currentRide.setFreeRide(true);
        ArrayList<NumberOfRoute> numberOfRoutes = new ArrayList<NumberOfRoute>() {
            {
                new NumberOfRoute(0);
            }
        };
        currentRide.setNumberOfRoute(numberOfRoutes);
        currentRide.setShouldGetRouteToClient(false);
        currentRide.setStartTime(LocalDateTime.now());
        currentRideService.save(currentRide);
        return currentRide;
    }

    private List<PositionInTime> createPositionsWithEmptyTime(List<Position> positions) {
        List<PositionInTime> positionInTimeList = new ArrayList<>();
        for (Position position : positions) {
            PositionInTime positionInTime = new PositionInTime();
            positionInTime.setPosition(position);
            positionInTimeList.add(positionInTime);
            positionInTimeRepository.save(positionInTime);
        }
        return positionInTimeList;
    }

    public Car setNewPositionAvailableCar(Long carId) {
        Car car = findById(carId);
        if (car == null) throw new NotFoundException("Car with this id does not exist");
        Navigation navigation = car.getNavigation();
        if (navigation == null) return null;
        if (navigation.getFirstRide() == null) {
            throw new NotFoundException("Current ride does not exist");
        }
        if (navigation.getFirstRide().getPositions().size() == 0) {
            return null;
        }
        navigation.getFirstRide().getPositions().remove(0);
        currentRideService.save(navigation.getFirstRide());
        if (car.getNavigation().getFirstRide().getPositions().size() == 0) {
            return null;
        }
        car.setPosition(car.getNavigation().getFirstRide().getPositions().get(0).getPosition());
        save(car);
        return save(car);
    }

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

    private void setNewPositionNonAvailableCar(long carId) {
        Car car = findById(carId);
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
        save(car);
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
//                car.setPosition(car.getCurrentRide().getPositions().get(0).getPosition());
//                save(car);
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
        Ride ride = rideRepository.findRideWhereStatusIsWaitingForCarId(car.getId()).get(0);
        driverNotification.setRide(ride);
        driverNotification.setFinished(false);
        driverNotificationRepository.save(driverNotification);
        simpMessagingService.sendStartRideNotificationToDriver(car.getDriver().getEmail(), ride, driverNotification);
    }

    private List<Car> setNewPositionForAvailableCars() {
        List<Car> cars = getActiveAvailableCars();
        for (Car car : cars) {
            setNewPositionAvailableCar(car.getId());
        }
        return cars;
    }

    public Car getClosestFreeCar(LatLngDTO firstPositionOfRide, boolean hasPet, boolean hasChild, CarType carType) {
        List<Car> fittingAvailableCars = getActiveAvailableCars();
        return getClosestCar(firstPositionOfRide, fittingAvailableCars, hasPet, hasChild, carType);
    }

    private Car getClosestCar(LatLngDTO latLngDTO, List<Car> cars, boolean hasPet, boolean hasChild, CarType carType) {
        Car closestCar = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Car car : cars) {
            if (carDoesNotSatisfyOrder(car, hasPet, hasChild, carType)) continue;
            double distance = MapUtils.calculateDistance(car.getPosition().getX(), car.getPosition().getY(), latLngDTO.getLng(), latLngDTO.getLat()); // switch
            if (distance < minDistance) {
                closestCar = car;
                minDistance = distance;
            }
        }
        return closestCar;
    }

    private boolean carDoesNotSatisfyOrder(Car car, boolean hasPet, boolean hasChild, CarType carType) {
        if (hasPet && !car.getAllowsPet()) return true;
        if (hasChild && !car.getAllowsBaby()) return true;
        return !car.getCarType().getName().equals(carType.getName());
    }

    public Car getClosestCarWhenAllAreNotAvailable(LatLngDTO firstPositionOfRide, boolean hasPet, boolean hasChild, CarType carType) {
        List<Car> activeNotReservedCars = carRepository.getActiveNotAvailableNotReservedCars();
        return getClosestCarAllNotAvailable(firstPositionOfRide, activeNotReservedCars, hasPet, hasChild, carType);
    }

    private Car getClosestCarAllNotAvailable(LatLngDTO latLngDTO, List<Car> cars, boolean hasPet, boolean hasChild, CarType carType) {
        Car closestCar = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Car car : cars) {
            if (carDoesNotSatisfyOrder(car, hasPet, hasChild, carType)) continue;
            int numberOfPositionsFirstRide = car.getNavigation().getFirstRide().getPositions().size();
            double lastPositionFirstRideX = car.getNavigation().getFirstRide().getPositions().get(numberOfPositionsFirstRide - 1).getPosition().getX();
            double lastPositionFirstRideY = car.getNavigation().getFirstRide().getPositions().get(numberOfPositionsFirstRide - 1).getPosition().getY();
            double distance = MapUtils.calculateDistance(lastPositionFirstRideX, lastPositionFirstRideY, latLngDTO.getLng(), latLngDTO.getLat()); // switched
            if (distance < minDistance) {
                closestCar = car;
                minDistance = distance;
            }
        }
        return closestCar;
    }


    public NavigationDisplay getNavigationDisplayForDriver(String email) {
        NavigationDisplay navigationDisplay = new NavigationDisplay();
        Driver driver = driverRepository.findByEmail(email);
        if (driver == null) throw new NotFoundException("Driver with this email does not exist");
        Navigation navigation = driver.getCar().getNavigation();
        if (navigation.getApproachFirstRide() != null) {
            navigationDisplay.setFirstApproach(navigation.getApproachFirstRide());
        }
        if (navigation.getFirstRide() != null && !navigation.getFirstRide().isFreeRide()) {
//            List<Ride> rides = rideRepository.findRideFromDriverEmail(driver.getEmail());
//            Route route = rides.get(0).getRoute();
            navigationDisplay.setFirstRide(navigation.getFirstRide());
        }
        if (navigation.getApproachSecondRide() != null) {
            navigationDisplay.setSecondApproach(navigation.getApproachSecondRide());
        }
        if (navigation.getSecondRide() != null) {
//            List<Ride> rides = rideRepository.findRideFromDriverEmail(driver.getEmail());
//            Route route = rides.get(1).getRoute();
            navigationDisplay.setSecondRide(navigation.getSecondRide());
        }
        return navigationDisplay;
    }
}
