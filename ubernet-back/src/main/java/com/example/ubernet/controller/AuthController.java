package com.example.ubernet.controller;

import com.example.ubernet.dto.CreateUserDTO;
import com.example.ubernet.dto.JwtAuthenticationRequest;
import com.example.ubernet.dto.LoginResponseDTO;
import com.example.ubernet.model.User;
import com.example.ubernet.service.AuthentificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private final AuthentificationService authentificationService;

    public AuthController(AuthentificationService authentificationService) {
        this.authentificationService = authentificationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) {

        LoginResponseDTO loginResponseDTO = authentificationService.login(authenticationRequest);
        if (loginResponseDTO==null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody CreateUserDTO userDTO) {
        User user = authentificationService.addUser(userDTO);
        if (user==null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
