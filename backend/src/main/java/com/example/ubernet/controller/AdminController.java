package com.example.ubernet.controller;

import com.example.ubernet.dto.AcceptRequest;
import com.example.ubernet.service.ProfileUpdateRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
    private final ProfileUpdateRequestService profileUpdateRequestService;

    @PostMapping(value = "/acceptProfileChange")
    public ResponseEntity<String> manageProfileUpdateRequest(@RequestBody AcceptRequest request) {
        if (profileUpdateRequestService.manageProfileUpdateRequest(request.getDriverEmail(), request.isAccepted())) {
            return new ResponseEntity<>("Successfully resolved driver's profile edit.", HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
