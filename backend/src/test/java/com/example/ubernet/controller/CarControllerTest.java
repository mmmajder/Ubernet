package com.example.ubernet.controller;

import com.example.ubernet.dto.CarResponse;
import com.example.ubernet.dto.LatLngDTO;
import com.example.ubernet.dto.NavigationDisplay;
import com.example.ubernet.dto.SetNewFreeRideDTO;
import com.example.ubernet.model.Car;
import com.example.ubernet.model.Position;
import com.example.ubernet.repository.CarRepository;
import com.example.ubernet.service.CarService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CarControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarRepository carRepository;

    private final Long CAR_ID_4 = 4L;
    private final Long CAR_ID_INVALID = 444L;

    @Test
    @DisplayName("Should set new free ride to car when car has no future rides or set free ride. When making PUT request to endpoint - /car/new-free-ride")
    public void shouldReturnCarAfterAddingFreeRide() throws JSONException {
        ResponseEntity<CarResponse> responseEntity = restTemplate.exchange("/car/new-free-ride",
                HttpMethod.PUT,
                createSetNewPositionFreeRideRequest(CAR_ID_4),
                new ParameterizedTypeReference<CarResponse>() {
                });
        CarResponse car = responseEntity.getBody();
        assert car != null;
        assertNotNull(car.getCurrentRide());
        assertTrue(car.getCurrentRide().isFreeRide());
    }

    @Test
    @DisplayName("Should throw NotFoundException when id of car is invalid. When making PUT request to endpoint - /car/new-free-ride")
    public void shouldThrowNotFoundExceptionForInvalidCarIdWhenTryingToSetNewFreeRide() throws JSONException {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/car/new-free-ride",
                HttpMethod.PUT,
                createSetNewPositionFreeRideRequest(CAR_ID_INVALID),
                new ParameterizedTypeReference<String>() {
                });
        System.out.println(responseEntity);
        String exceptionMessage = responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should set new free ride to car when car has no future rides or set free ride. When making PUT request to endpoint - /car/new-free-ride")
    public void shouldReturnCarsAfterUpdatePositions() throws JSONException {
        ResponseEntity<NavigationDisplay> responseEntity = restTemplate.exchange("/car/currentRide/" + "customer@gmail.com",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<NavigationDisplay>() {
                });
        NavigationDisplay navigationDisplay = responseEntity.getBody();
        System.out.println("AAAA");
        System.out.println(navigationDisplay);
    }

    private HttpEntity<String> createSetNewPositionFreeRideRequest(Long id) throws JSONException {
        SetNewFreeRideDTO newFreeRideDTO = createSetNewFreeRideDTO();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("carId", id);
        JSONArray positionsArray = new JSONArray();
        for (List<Double> position : newFreeRideDTO.getPositions()) {
            JSONArray innerArray = new JSONArray();
            for (Double value : position) {
                innerArray.put(value);
            }
            positionsArray.put(innerArray);
        }

        jsonObject.put("positions", positionsArray);
        return new HttpEntity<>(jsonObject.toString(), headers);
    }

    private SetNewFreeRideDTO createSetNewFreeRideDTO() {
        SetNewFreeRideDTO setNewFreeRideDTO = new SetNewFreeRideDTO();
        setNewFreeRideDTO.setPositions(getCoordinates());
        return setNewFreeRideDTO;
    }

    private List<List<Double>> getCoordinates() {
        List<List<Double>> coords = new ArrayList<>();
        coords.add(List.of(45.25624, 19.84421));
        coords.add(List.of(45.25627, 19.84418));
        coords.add(List.of(45.25648, 19.8439));
        coords.add(List.of(45.25651, 19.84385));
        coords.add(List.of(45.25651, 19.84385));
        coords.add(List.of(45.2565, 19.84384));
        coords.add(List.of(45.25638, 19.84369));
        coords.add(List.of(45.25623, 19.8435));
        coords.add(List.of(45.25623, 19.8435));
        return coords;
    }
}
