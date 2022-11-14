package com.example.ubernet.controller;

import com.example.ubernet.dto.*;
import com.example.ubernet.model.User;
import com.example.ubernet.service.AuthentificationService;
import com.example.ubernet.service.UserService;
import com.example.ubernet.utils.DTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private final AuthentificationService authentificationService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> createAuthenticationToken(
            @Valid @RequestBody JwtAuthenticationRequest authenticationRequest) {

        LoginResponseDTO loginResponseDTO = authentificationService.login(authenticationRequest);
        if (loginResponseDTO == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(loginResponseDTO);
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<User> addUser(@Valid @RequestBody CreateUserDTO userDTO) throws MessagingException {
        User user = authentificationService.addUser(userDTO);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/verify/{code}")
    public ResponseEntity<UserResponse> verifyUser(@PathVariable String code) {
        User user = authentificationService.verify(code);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        UserResponse dto = DTOMapper.getUserResponse(user);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/reset-password/{email}")
    public ResponseEntity<String> resetPassword(@PathVariable String email) throws MessagingException {
        if (authentificationService.resetPassword(email)) {
            return new ResponseEntity<>("Successfully reset password", HttpStatus.OK);
        }
        return new ResponseEntity<>("There was a problem in resetting password", HttpStatus.CONFLICT);
    }

    @PutMapping("/change-password/{email}")
    public ResponseEntity<String> changePassword(@PathVariable String email, @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        if (authentificationService.changePassword(email, changePasswordDTO)) {
            return new ResponseEntity<>("Successfully changed password", HttpStatus.OK);
        }
        
        return new ResponseEntity<>("There was a problem in changing password", HttpStatus.CONFLICT);
    }

    @GetMapping("/currently-logged-user")
    public ResponseEntity<UserResponse> loggedUser(Authentication authentication) {
        User user = userService.getLoggedUser(authentication);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserResponse dto = DTOMapper.getUserResponse(user);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}