package com.example.ubernet.controller;

import com.example.ubernet.dto.*;
import com.example.ubernet.model.User;
import com.example.ubernet.service.AuthentificationService;
import com.example.ubernet.service.UserService;
import com.example.ubernet.utils.DTOMapper;
import com.example.ubernet.utils.TokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private final TokenUtils tokenUtils;
    private final UserService userService;
    private final AuthentificationService authentificationService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    public AuthController(TokenUtils tokenUtils, UserService userService, AuthentificationService authentificationService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.tokenUtils = tokenUtils;
        this.userService = userService;
        this.authentificationService = authentificationService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
        Authentication authentication;
        System.out.println(passwordEncoder.encode(authenticationRequest.getPassword()));
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        User user = (User) authentication.getPrincipal();
        if(!user.isEnabled() || user.getDeleted() || authentificationService.isPasswordReseted(user)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();
        return ResponseEntity.ok(new LoginResponseDTO(new UserTokenState(jwt, expiresIn), user.getRole()));
    }

    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody CreateUserDTO userDTO) {

        User existUser = this.userService.findByEmail(userDTO.getEmail());
        if (existUser!=null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User user = DTOMapper.getUser(userDTO);

        user = authentificationService.addUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

//    @PutMapping("/verify/{code}")
//    public ResponseEntity<UserVerificationResponseDTO> verify(@PathVariable String code) {
//        User user = authentificationService.verify(code);
//        if (user == null) {
//            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
//        }
//        UserVerificationResponseDTO dto = DTOMapper.getUserVerificationResponseDTO(user);
//        return new ResponseEntity<>(dto, HttpStatus.OK);
//    }
}
