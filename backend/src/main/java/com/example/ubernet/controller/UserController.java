package com.example.ubernet.controller;

import com.example.ubernet.dto.UserEditDTO;
import com.example.ubernet.dto.UserResponse;
import com.example.ubernet.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/")
    public UserResponse getUser(@RequestParam("email") String email) {
        return userService.getUser(email);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserEditDTO> updateProfile(@RequestParam("email") String email, @RequestBody UserEditDTO userEditDTO) {
        userEditDTO = userService.editUser(email, userEditDTO);
        if (userEditDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userEditDTO, HttpStatus.OK);
    }

//    @PutMapping("/profile")
//    public ResponseEntity<UserEditDTO> updateProfile(@RequestParam("email") String email, @RequestBody UserEditDTO userEditDTO) {
//        userEditDTO = userService.editUser(email, userEditDTO);
//        if (userEditDTO == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>(userEditDTO, HttpStatus.OK);
//    }

}
