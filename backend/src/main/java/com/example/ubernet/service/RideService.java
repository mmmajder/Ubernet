package com.example.ubernet.service;

import com.example.ubernet.dto.CreateRideDTO;
import com.example.ubernet.dto.InstructionDTO;
import com.example.ubernet.dto.LatLngDTO;
import com.example.ubernet.dto.PlaceDTO;
import com.example.ubernet.exception.NotFoundException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.repository.PlaceRepository;
import com.example.ubernet.repository.RideRepository;
import com.example.ubernet.utils.MapUtils;
import com.example.ubernet.utils.TimeUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
@Transactional
public class RideService {
    private final RideRepository rideRepository;
    private final CarService carService;
    private final CustomerService customerService;
    private final CarTypeService carTypeService;
    private final PositionService positionService;
    private final PositionInTimeService positionInTimeService;
    private final RouteService routeService;
    private final CurrentRideService currentRideService;
    private final PlaceRepository placeRepository;
    public Ride findById(Long id) {
        return rideRepository.findById(id).orElse(null);
    }

    public Ride save(Ride ride) {
        return rideRepository.save(ride);
    }

    public Ride createRide(CreateRideDTO createRideDTO) {
        Car car = carService.getClosestFreeCar(createRideDTO.getCoordinates().get(0));
        if (car == null) {
            throw new NotFoundException("All cars are not free");
        }
        car.setIsAvailable(false);
        CurrentRide currentRide = new CurrentRide();
        currentRide.setPositions(createRideDestinations(createRideDTO));
        currentRide.setShouldGetRouteToClient(true);

        currentRideService.save(currentRide);
        car.setCurrentRide(currentRide);
        carService.save(car);

        Driver driver = car.getDriver();

        Set<Customer> customers = customerService.getCustomersByEmails(createRideDTO.getPassengers());

        Route route = new Route();
        route.setDeleted(false);
        route.setPrice(createRideDTO.getTotalDistance() * 120 / 1000 + carTypeService.findCarTypeByName(createRideDTO.getCarType()).getPriceForType());
        route.setTime(createRideDTO.getTotalTime());
        List<Place> places = new ArrayList<>();
        for (PlaceDTO placeDTO : createRideDTO.getRoute()) {
            Position position = new Position(placeDTO.getPosition().getX(), placeDTO.getPosition().getY());
            positionService.save(position);
            Place place = new Place(placeDTO.getName(), position);
            placeRepository.save(place);
            places.add(place);
        }
        route.setCheckPoints(places);

        routeService.save(route);

        Ride ride = new Ride();
        ride.setDeleted(false);
        ride.setCustomers(customers);
        ride.setReservationTime(TimeUtils.getDateTimeForReservationMaxFiveHoursAdvance(createRideDTO.getReservationTime()));
        ride.setRideState(createRideState(ride.getReservationTime()));
        ride.setDriver(driver);
        ride.setRoute(route);

        save(ride);

        return ride;
    }

    private RideState createRideState(LocalDateTime reservationTime) {
        if (reservationTime.isBefore(LocalDateTime.now().minusMinutes(10))) {
            return RideState.WAITING;
        }
        return RideState.RESERVED;
    }


    private List<PositionInTime> createRideDestinations(CreateRideDTO createRideDTO) {
        List<Double> timeSlots = getTimeSlots(createRideDTO);
        List<PositionInTime> positions = new ArrayList<>();
        for (int i = 0; i < timeSlots.size(); i++) {
            Position position = new Position();
            position.setX(createRideDTO.getCoordinates().get(i).getLng());
            position.setY(createRideDTO.getCoordinates().get(i).getLat());
            PositionInTime positionInTime = new PositionInTime();
            positionInTime.setSecondsPast(timeSlots.get(i));
            positionInTime.setPosition(position);
            positions.add(positionInTime);
            positionService.save(position);
            positionInTimeService.save(positionInTime);
        }
        System.out.println(positions);
        return positions;
    }


    private List<Double> getTimeSlots(CreateRideDTO createRideDTO) {
        List<Integer> distanceSlots = getDistanceSlots(createRideDTO.getCoordinates());
        return calculateTimeSlots(distanceSlots, createRideDTO.getInstructions());
    }

    private List<Double> calculateTimeSlots(List<Integer> distanceSlots, List<InstructionDTO> instructionDTOList) {
        List<Double> timeSlots = new ArrayList<>();
        for (int i = 0; i < distanceSlots.size(); i++) {
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


    public void updateCarRoute(Long carId, CreateRideDTO createRideDTO) {
        Car car = carService.findById(carId);
        if (car == null) {
            throw new NotFoundException("Car does not exist");
        }
        CurrentRide currentRide = car.getCurrentRide();
        List<PositionInTime> newPositions = createRideDestinations(createRideDTO);
        List<PositionInTime> oldPositions = currentRide.getPositions();
        for (PositionInTime positionInTime : oldPositions) {
            positionInTime.setSecondsPast(newPositions.get(newPositions.size() - 1).getSecondsPast() + positionInTime.getSecondsPast());
            positionInTimeService.save(positionInTime);
        }
        currentRide.setPositions(newPositions);
        currentRide.getPositions().addAll(oldPositions);
        currentRide.setShouldGetRouteToClient(false);
        currentRide.setTimeOfStartOfRide(LocalDateTime.now());
        currentRideService.save(currentRide);
    }
}
