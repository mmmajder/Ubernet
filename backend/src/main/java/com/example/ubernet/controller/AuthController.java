package com.example.ubernet.controller;

import com.example.ubernet.dto.ChangePasswordDTO;
import com.example.ubernet.dto.CreateUserDTO;
import com.example.ubernet.dto.JwtAuthenticationRequest;
import com.example.ubernet.dto.LoginResponseDTO;
import com.example.ubernet.model.User;
import com.example.ubernet.service.AuthentificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private final AuthentificationService authentificationService;

    public AuthController(AuthentificationService authentificationService) {
        this.authentificationService = authentificationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> createAuthenticationToken(
            @Valid @RequestBody JwtAuthenticationRequest authenticationRequest) {

        LoginResponseDTO loginResponseDTO = authentificationService.login(authenticationRequest);
        if (loginResponseDTO==null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(loginResponseDTO);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<User> addUser(@Valid @RequestBody CreateUserDTO userDTO) {
        User user = authentificationService.addUser(userDTO);
        if (user==null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        if (authentificationService.changePassword(changePasswordDTO)){
            return new ResponseEntity<>("Successfully changed password", HttpStatus.OK);
        }
        return new ResponseEntity<>("There was a problem in changing of password", HttpStatus.CONFLICT);
    }
}
