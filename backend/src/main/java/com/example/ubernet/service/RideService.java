package com.example.ubernet.service;

import com.example.ubernet.dto.*;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.exception.NotFoundException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.*;
import com.example.ubernet.utils.DTOMapper;
import com.example.ubernet.utils.MapUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class RideService {
    private final RideRepository rideRepository;
    private final CarService carService;
    private final CarRepository carRepository;
    private final CustomerService customerService;
    private final CarTypeService carTypeService;
    private final PositionRepository positionRepository;
    private final PositionInTimeRepository positionInTimeRepository;
    private final CurrentRideService currentRideService;
    private final SimpMessagingService simpMessagingService;
    private final NavigationRepository navigationRepository;
    private final DriverNotificationService driverNotificationService;

    @Transactional
    public Ride findById(Long id) {
        return rideRepository.findById(id).orElse(null);
    }

    public CurrentRide createCurrentRide(List<LatLngDTO> coordinates, List<InstructionDTO> instructions) {
        CurrentRide currentRide = new CurrentRide();
        currentRide.setPositions(getRidePositions(coordinates, instructions));
        currentRide.setShouldGetRouteToClient(true);
        currentRide.setFreeRide(false);
        return currentRideService.save(currentRide);
    }

    public void addNavigation(Car car, CurrentRide currentRide) {
        Navigation navigation = car.getNavigation();
        if (navigation == null) {
            navigation = new Navigation();
        }
        car.setNavigation(navigation);
        car.setIsAvailable(false);
        carRepository.save(car);
        if (navigation.getFirstRide() == null || navigation.getFirstRide().isFreeRide()) {
            navigation.setFirstRide(currentRide);
        } else {
            navigation.setSecondRide(currentRide);
        }
        navigationRepository.save(navigation);
    }

    public Ride save(Ride ride) {
        return rideRepository.save(ride);
    }

    public Car getCarForRide(LatLngDTO firstPositionOfRide, boolean hasPet, boolean hasChild, String carTypeName) {
        CarType carType = carTypeService.findCarTypeByName(carTypeName);
        Car car = carService.getClosestFreeCar(firstPositionOfRide, hasPet, hasChild, carType);
        if (car == null) {
            car = carService.getClosestCarWhenAllAreNotAvailable(firstPositionOfRide, hasPet, hasChild, carType);
            if (car == null)
                throw new NotFoundException("All cars are not free");
        }
        return car;
    }

    public List<PositionInTime> getRidePositions(List<LatLngDTO> coordinates, List<InstructionDTO> instructions) {
        List<Double> timeSlots = getTimeSlots(coordinates, instructions);
        List<PositionInTime> positions = new ArrayList<>();
        for (int i = 0; i < timeSlots.size(); i++) {
            Position position = new Position();
            position.setX(coordinates.get(i).getLng());
            position.setY(coordinates.get(i).getLat());
            PositionInTime positionInTime = new PositionInTime();
            positionInTime.setSecondsPast(timeSlots.get(i));
            positionInTime.setPosition(position);
            positions.add(positionInTime);
            positionRepository.save(position);
            positionInTimeRepository.save(positionInTime);
        }
        System.out.println(positions);
        return positions;
    }


    private List<Double> getTimeSlots(List<LatLngDTO> coordinates, List<InstructionDTO> instructions) {
        List<Integer> distanceSlots = getDistanceSlots(coordinates);
        return calculateTimeSlots(distanceSlots, instructions);
    }

    private List<Double> calculateTimeSlots(List<Integer> distanceSlots, List<InstructionDTO> instructionDTOList) {
        List<Double> timeSlots = new ArrayList<>();
        for (int i = 0; i < distanceSlots.size(); i++) {
            if (instructionDTOList.size() <= i) break;
            double time = instructionDTOList.get(i).getTime() / distanceSlots.get(i);
            for (int j = 0; j < distanceSlots.get(i); j++) {
                if (timeSlots.size() == 0) {
                    timeSlots.add(time);
                } else {
                    timeSlots.add(timeSlots.get(timeSlots.size() - 1) + time);
                }
            }
        }
        return timeSlots;
    }

    private List<Integer> getDistanceSlots(List<LatLngDTO> coordinatesList) {
        int numberOfCoordinates = 1;
        List<Integer> distanceSlots = new ArrayList<>();
        for (int i = 1; i < coordinatesList.size(); i++) {
            LatLngDTO prevCoordinate = coordinatesList.get(i - 1);
            LatLngDTO coordinate = coordinatesList.get(i);
            double distance = MapUtils.calculateDistance(prevCoordinate.getLat(), prevCoordinate.getLng(), coordinate.getLat(), coordinate.getLng());
            if (distance == 0) {
                distanceSlots.add(numberOfCoordinates);
                numberOfCoordinates = 0;
            }
            numberOfCoordinates += 1;
        }
        return distanceSlots;
    }

    public Ride setRidePositions(Ride ride) {
        RideRequest rideRequest = ride.getRideRequest();
        Car car = getCarForRide(DTOMapper.positionToLatLng(rideRequest.getCurrentRide().getPositions().get(0).getPosition()), rideRequest.isHasPet(), rideRequest.isHasChild(), rideRequest.getCarType());
        ride.setDriver(car.getDriver());
        addNavigation(car, rideRequest.getCurrentRide());
        rideRepository.save(ride);
        this.simpMessagingService.updateRouteForSelectedCar(ride.getDriver().getEmail(), ride);
        driverNotificationService.sendNextRideNotificationToDriver(ride);
        return ride;
    }

    public List<Ride> getRidesWithAcceptedReservation() {
        return this.rideRepository.getAcceptedReservationsThatCarDidNotComeYet();
    }

    public void updateRideStatus(Ride ride, RideState rideState) {
        ride.setRideState(rideState);
        save(ride);
    }

    public List<Ride> getReservedRidesThatWereNotPayedAndScheduledTimePassed() {
        List<Ride> rides = rideRepository.getReservedRidesThatWithStatusRequestedAndScheduledStartIsNotNull();
        List<Ride> passedReservations = new ArrayList<>();
        for (Ride ride : rides) {
            if (ride.getScheduledStart().isBefore(LocalDateTime.now())) {
                passedReservations.add(ride);
            }
        }
        return passedReservations;
    }

    public List<Ride> getReservedRidesThatScheduledTimePassed() {
        List<Ride> rides = rideRepository.getReservedRidesThatWithStatusReservedAndScheduledStartIsNotNull();
        List<Ride> passedRides = new ArrayList<>();
        for (Ride ride : rides) {
            if (ride.getScheduledStart().isBefore(LocalDateTime.now())) {
                passedRides.add(ride);
            }
        }
        return passedRides;
    }

    public List<Ride> getReservedRidesThatShouldStartIn10Minutes() {
        List<Ride> rides = this.rideRepository.getReservedWithStatusReservedRides();
        List<Ride> res = new ArrayList<>();
        for (Ride ride : rides) {
            if (ride.getScheduledStart().isBefore(LocalDateTime.now().plusMinutes(10))) {
                res.add(ride);
            }
        }
        return res;
    }

    @Transactional
    public CurrentRide findCurrentRideForClient(String email) {
        Customer customer = customerService.findByEmail(email);
        if (customer == null) throw new BadRequestException("Customer with this email does not exist");
        Ride activeRide = rideRepository.findActiveRideForCustomer(email);
        if (activeRide == null) return null;
        return activeRide.getRideRequest().getCurrentRide();
    }
}
