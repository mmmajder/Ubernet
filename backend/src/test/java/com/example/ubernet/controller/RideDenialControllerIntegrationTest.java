package com.example.ubernet.controller;

import com.example.ubernet.dto.CancelRideRequest;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.RideDenial;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.testng.annotations.BeforeMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class RideDenialControllerIntegrationTest {
    private final Long INVALID_ID = 111L;
    private final String INVALID_EMAIL = "invalidemail1234@gmail.com";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeMethod
    public void setup(){

    }

    private HttpEntity<String> createCancelRequestRequest(boolean shouldSetDriverInactive, String reason){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shouldSetDriverInactive", shouldSetDriverInactive);
        jsonObject.put("reason", reason);

        HttpEntity<String> request = new HttpEntity<String>(jsonObject.toString(), headers);

        return request;
    }

    @Test
    @DisplayName("Should return Bad Request for invalid ID when making POST request to endpoint - /ride-denial//{id}")
    public void shouldReturnNullForInvalidId(){
        HttpEntity<String> request = createCancelRequestRequest(true, "reason");

        ResponseEntity<String> responseEntity = restTemplate.exchange("/ride-denial/" + INVALID_ID,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });

        String exceptionMessage = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(exceptionMessage, "Ride does not exist");
    }

//    @Test
//    @DisplayName("Should return Bad Request for invalid ID when making POST request to endpoint - /ride-denial//{id}")
//    public void shouldReturnRideDenialForValidId(){
//        HttpEntity<String> request = createCancelRequestRequest(true, "reason");
//
//        ResponseEntity<RideDenial> responseEntity = restTemplate.exchange("/ride-denial/" + 1,
//                HttpMethod.POST,
//                request,
//                new ParameterizedTypeReference<RideDenial>() {
//                });
//
//        RideDenial rideDenial = responseEntity.getBody();
//
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//        assertEquals(rideDenial.getReason(), "reason");
//    }
}
