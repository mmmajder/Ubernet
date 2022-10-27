package com.example.ubernet.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.example.ubernet.dto.*;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.UserRole;
import com.example.ubernet.utils.DTOMapper;
import com.example.ubernet.utils.TokenUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;

import javax.mail.MessagingException;

@Service
public class AuthentificationService {
    private final UserService userService;
    private final AdminService adminService;
    private final TokenUtils tokenUtils;
    private final UserAuthService userAuthService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;


    public AuthentificationService(PasswordEncoder passwordEncoder, UserService userService, TokenUtils tokenUtils, AuthenticationManager authenticationManager, AdminService adminService, UserAuthService userAuthService, EmailService emailService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.tokenUtils = tokenUtils;
        this.authenticationManager = authenticationManager;
        this.adminService = adminService;
        this.userAuthService = userAuthService;
        this.emailService = emailService;
    }

    public User addUser(CreateUserDTO createUserDTO) throws MessagingException {
        if (userService.findByEmail(createUserDTO.getEmail()) != null) {
            return null;
        }
        User user = saveUser(createUserDTO);
        if (user.getRole() == UserRole.CUSTOMER) {
            emailService.sendRegistrationAsync(user);
        }
        return user;
    }

    private User saveUser(CreateUserDTO createUserDTO) {
        User user = DTOMapper.getUser(createUserDTO);
        user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
        user.setIsBlocked(false);
        user.setUserAuth(getUserAuth(user));
        return adminService.save(user);
    }

    private UserAuth getUserAuth(User user) {
        UserAuth userAuth = new UserAuth();

        String randomCode = RandomString.make(64);
        userAuth.setVerificationCode(randomCode);
        userAuth.setLastPasswordSet(new Timestamp(System.currentTimeMillis()));
        userAuth.setRoles(getRoles(user));
        userAuth.setIsEnabled(setIsUserEnabledRegistration(user));
        userAuthService.save(userAuth);
        return userAuth;
    }

    private boolean setIsUserEnabledRegistration(User user) {
        return user.getRole() != UserRole.CUSTOMER;
    }

    private List<Role> getRoles(User user) {
        List<Role> roles = new ArrayList<>();
        UserRole userRole = user.getRole();
        roles.add(userService.findRolesByUserType("ROLE_USER"));

        if (userRole == UserRole.ADMIN) {
            roles.add(userService.findRolesByUserType("ROLE_ADMIN"));
        } else if (userRole == UserRole.DRIVER) {
            roles.add(userService.findRolesByUserType("ROLE_DRIVER"));
        } else {
            roles.add(userService.findRolesByUserType("ROLE_CUSTOMER"));
        }
        return roles;
    }

    public LoginResponseDTO login(JwtAuthenticationRequest authenticationRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (AuthenticationException e) {
            return null;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        if (!isUserEnabled(user)) return null;
        return createAccessToken(user);
    }

    private LoginResponseDTO createAccessToken(User user) {
        String jwt = tokenUtils.generateToken(user);
        long expiresIn = tokenUtils.getExpiredIn();
        return new LoginResponseDTO(new UserTokenState(jwt, expiresIn), user.getRole());
    }

    private boolean isUserEnabled(User user) {
        return user.isEnabled() && !user.getDeleted();
    }

    public boolean changePassword(ChangePasswordDTO changePasswordDTO) {
        User user = userService.findByEmail(changePasswordDTO.getEmail());
        if (user == null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
        if (!user.isEnabled()) {
            user.getUserAuth().setIsEnabled(true);
            userAuthService.save(user.getUserAuth());
        }
        userService.save(user);
        return true;
    }

    public User verify(String verificationCode) {
        User user = userService.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return null;
        }

        user.getUserAuth().setVerificationCode(null);
        user.getUserAuth().setIsEnabled(true);
        userAuthService.save(user.getUserAuth());
        user = userService.save(user);
        return user;
    }

    public boolean resetPassword(String email) throws MessagingException {
        User user = userService.findByEmail(email);
        if (user == null) {
            return false;
        }
        user.setPassword("");
        user.getUserAuth().setIsEnabled(false);
        user.getUserAuth().setLastPasswordSet(new Timestamp(System.currentTimeMillis()));
        userAuthService.save(user.getUserAuth());
        userService.save(user);
        emailService.sendEmailResetAsync(user);
        return true;
    }


}
