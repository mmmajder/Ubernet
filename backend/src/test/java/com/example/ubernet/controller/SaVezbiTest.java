package com.example.ubernet.controller;

import com.example.ubernet.dto.CancelRideRequest;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.RideDenial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
////@TestPropertySource(
////        locations = "classpath:application-test.properties")
//@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SaVezbiTest {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldRetriveAllComment() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/ride-denial/1";
        URI uri = new URI(baseUrl);
        ResponseEntity<RideDenial> responseEntity = restTemplate.postForEntity(uri, createCancelRideRequest(), RideDenial.class);

        RideDenial rideDenial = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    private CancelRideRequest createCancelRideRequest() {
        return CancelRideRequest.builder()
                .reason("NZM")
                .build();
    }
}
