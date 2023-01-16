package com.example.ubernet.utils;

import com.example.ubernet.dto.*;
import com.example.ubernet.model.*;

import java.util.ArrayList;
import java.util.List;

public class DTOMapper {
    public static User getUser(CreateUserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setCity(userDTO.getCity());
        user.setEmail(userDTO.getEmail());
        user.setSurname(userDTO.getSurname());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getUserRole());
        return user;
    }

    public static UserVerificationResponseDTO getUserVerificationResponseDTO(User user) {
        UserVerificationResponseDTO userVerificationResponseDTO = new UserVerificationResponseDTO();
        userVerificationResponseDTO.setCity(user.getCity());
        userVerificationResponseDTO.setPassword(user.getPassword());
        userVerificationResponseDTO.setEmail(user.getEmail());
        userVerificationResponseDTO.setSurname(user.getSurname());
        userVerificationResponseDTO.setPhoneNumber(user.getPhoneNumber());
        userVerificationResponseDTO.setName(user.getName());
        return userVerificationResponseDTO;
    }

    public static UserEditDTO getUserEditDTO(User user) {
        UserEditDTO userEditDTO = new UserEditDTO();
        userEditDTO.setCity(user.getCity());
        userEditDTO.setName(user.getName());
        userEditDTO.setSurname(user.getSurname());
        userEditDTO.setPhoneNumber(user.getPhoneNumber());
        return userEditDTO;
    }

    public static UserResponse getUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setCity(user.getCity());
        userResponse.setEmail(user.getEmail());
        userResponse.setName(user.getName());
        userResponse.setRole(user.getRole());
        userResponse.setSurname(user.getSurname());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setBlocked(user.getBlocked());
        return userResponse;
    }

    public static CarResponse getCarResponse(Car car) {
        CarResponse carResponse = new CarResponse();
        carResponse.setCarType(car.getCarType());
        carResponse.setDriver(car.getDriver());
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setX(car.getPosition().getX());
        positionDTO.setY(car.getPosition().getY());
        carResponse.setPosition(positionDTO);
        return carResponse;
    }

    public static DriverResponse getDriverResponse(Driver driver) {
        DriverResponse driverResponse = new DriverResponse();
        driverResponse.setDriverDailyActivity(driver.getDriverDailyActivity());
        driverResponse.setCity(driver.getCity());
        driverResponse.setEmail(driver.getEmail());
        driverResponse.setPassword(driver.getPassword());
        driverResponse.setName(driver.getName());
        driverResponse.setSurname(driver.getSurname());
        driverResponse.setPhoneNumber(driver.getPhoneNumber());
        return driverResponse;
    }

    public static ReviewResponse getReviewResponse(Review review) {
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setComment(review.getComment());
        reviewResponse.setRating(review.getRating());
        reviewResponse.setCustomer(review.getCustomer());
        return reviewResponse;
    }

    public static CarTypeResponse getCarTypeResponse(CarType carType) {
        CarTypeResponse carTypeResponse = new CarTypeResponse();
        carTypeResponse.setDeleted(carType.getDeleted());
        carTypeResponse.setPriceForType(carType.getPriceForType());
        carTypeResponse.setId(carType.getId());
        carTypeResponse.setName(carType.getName());
        return carTypeResponse;
    }

    public static List<CarTypeResponse> getCarTypesResponse(List<CarType> carTypes) {
        List<CarTypeResponse> carTypeResponses = new ArrayList<>();
        for (CarType carType :
                carTypes) {
            carTypeResponses.add(DTOMapper.getCarTypeResponse(carType));
        }
        return carTypeResponses;
    }

    public static List<ActiveCarResponse> getListActiveCarResponse(List<Car> cars) {
        List<ActiveCarResponse> carResponses = new ArrayList<>();
        for (Car car : cars) {
            carResponses.add(getActiveCarResponse(car));
        }
        return carResponses;
    }

    public static ActiveCarResponse getActiveCarResponse(Car car) {
        ActiveCarResponse activeAvailableCarResponse = new ActiveCarResponse();
        activeAvailableCarResponse.setCarId(car.getId());
        activeAvailableCarResponse.setDriverEmail(car.getDriver().getEmail());
        activeAvailableCarResponse.setCurrentPosition(car.getPosition());
        activeAvailableCarResponse.setCurrentRide(car.getCurrentRide());
        return activeAvailableCarResponse;
    }

    public static List<Position> getPositions(List<List<Double>> positionsDTO) {
        List<Position> positions = new ArrayList<>();
        for (List<Double> pos :
                positionsDTO) {
            Position position = new Position();
            position.setX(pos.get(0));
            position.setY(pos.get(1));
            positions.add(position);
        }
        return positions;
    }

    public static List<CarResponse> getListCarResponse(List<Car> cars) {
        List<CarResponse> carResponses = new ArrayList<>();
        for (Car car :
                cars) {
            carResponses.add(getCarResponse(car));
        }
        return carResponses;
    }
}
