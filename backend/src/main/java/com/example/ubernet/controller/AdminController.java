package com.example.ubernet.controller;

import com.example.ubernet.service.ProfileUpdateRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
    private final ProfileUpdateRequestService profileUpdateRequestService;

    @PutMapping(value = "/manage-profile-update")
    public ResponseEntity<String> manageProfileUpdateRequest(@RequestParam("id") Long id, @RequestParam("isAccepted") Boolean isAccepted) {
        if (profileUpdateRequestService.manageProfileUpdateRequest(id, isAccepted)) {
            return new ResponseEntity<>("Successfully accepted/declined Driver profile edit", HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
