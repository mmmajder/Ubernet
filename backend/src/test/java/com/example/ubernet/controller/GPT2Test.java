package com.example.ubernet.controller;

import com.example.ubernet.dto.CancelRideRequest;
import com.example.ubernet.model.RideDenial;
import com.example.ubernet.service.RideDenialService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.testing.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(RideDenialController.class)
class GPT2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RideDenialService rideDenialService;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    void testCreateRideDenial() throws Exception {
        Long rideId = 1L;
        CancelRideRequest cancelRideRequest = new CancelRideRequest();
        cancelRideRequest.setReason("reason");
        RideDenial rideDenial = new RideDenial();
        rideDenial.setId(1L);
        when(rideDenialService.createRideDenial(eq(rideId), any(CancelRideRequest.class)))
                .thenReturn(rideDenial);

        mockMvc.perform(MockMvcRequestBuilders.post("/ride-denial/" + rideId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(cancelRideRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        Mockito.verify(rideDenialService).createRideDenial(eq(rideId), any(CancelRideRequest.class));
    }
}