package com.example.ubernet.controller;

import com.example.ubernet.dto.UserEditDTO;
import com.example.ubernet.dto.UserResponse;
import com.example.ubernet.model.User;
import com.example.ubernet.service.UserService;
import com.example.ubernet.utils.DTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public UserResponse getUser(@RequestParam("email") String email) {
        return userService.getUser(email);
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<UserEditDTO> updateProfile(@RequestParam("email") String email, @RequestBody UserEditDTO userEditDTO) {
        userEditDTO = userService.editUser(email, userEditDTO);
        if (userEditDTO==null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userEditDTO, HttpStatus.OK);
    }

}
