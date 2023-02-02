package com.example.ubernet.controller;

import com.example.ubernet.dto.CancelRideRequest;
import com.example.ubernet.model.RideDenial;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GPTTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testExampleController() throws Exception {
//        RideDenial expectedRideDenial = createRideDenial();
        CancelRideRequest cancelRideRequest = createCancelRideRequest();

//        mockMvc.perform(post("/ride-denial/101", cancelRideRequest))
//                .andExpect(status().isOk());
    }

    private CancelRideRequest createCancelRideRequest() {
        return CancelRideRequest.builder()
                .reason("PROBLEM")
                .build();
    }
}