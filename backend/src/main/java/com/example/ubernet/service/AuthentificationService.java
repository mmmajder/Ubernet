package com.example.ubernet.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.example.ubernet.dto.*;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.UserRole;
import com.example.ubernet.utils.DTOMapper;
import com.example.ubernet.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;

import javax.mail.MessagingException;

@AllArgsConstructor
@Service
public class AuthentificationService {
    private final UserService userService;
    private final TokenUtils tokenUtils;
    private final UserAuthService userAuthService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final CustomerService customerService;
    private final DriverService driverService;

    public void addCustomer(CreateUserDTO createUserDTO) throws MessagingException {
        if (userService.findByEmail(createUserDTO.getEmail()) != null)
            throw new BadRequestException("User with this email already exist");
        Customer customer = customerService.createCustomer(createUserDTO);
        emailService.sendRegistrationAsync(customer);
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
        if (user.getRole().equals(UserRole.DRIVER)) {
            driverService.loginDriver(user.getEmail());
        }
        if (!isUserEnabled(user)) return null;
        return createAccessToken(user);
    }

    public LoginResponseDTO loginSocial(LoginSocialDTO loginSocialDTO) {
        User user = userService.findByEmail(loginSocialDTO.getEmail());
        if (user == null) {
            user = customerService.createCustomerSocialLogin(loginSocialDTO);
        }
        if (!isUserEnabled(user)) return null;
        return saveAuthInContext(user);
    }

    private LoginResponseDTO saveAuthInContext(User user) {
//        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        var authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), "");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenUtils.generateToken(userService.findByEmail((String) authentication.getPrincipal()));
        long expiresIn = tokenUtils.getExpiredIn();
        return new LoginResponseDTO(new UserTokenState(token, expiresIn), user.getRole());
    }


    private LoginResponseDTO createAccessToken(User user) {
        String jwt = tokenUtils.generateToken(user);
        long expiresIn = tokenUtils.getExpiredIn();
        return new LoginResponseDTO(new UserTokenState(jwt, expiresIn), user.getRole());
    }

    private boolean isUserEnabled(User user) {
        return user.isEnabled() && !user.getDeleted();
    }

    public boolean changePassword(String email, ChangePasswordDTO changePasswordDTO) {
        User user = userService.findByEmail(email);

        if (!validatePasswordChange(user, changePasswordDTO))
            return false;

        saveNewPassword(user, changePasswordDTO.getNewPassword());

        return true;
    }

    public boolean setPassword(String token, SetPasswordDTO setPasswordDTO) {
        User user = userService.findByResetPasswordCode(token);
        if (user == null) return false;
        if (!setPasswordDTO.getNewPassword().equals(setPasswordDTO.getReEnteredNewPassword())) return false;
        saveNewPassword(user, setPasswordDTO.getNewPassword());
        return true;
    }

    private void saveNewPassword(User user, String setPasswordDTO) {
        user.setPassword(passwordEncoder.encode(setPasswordDTO));
        if (!user.isEnabled()) {
            user.getUserAuth().setIsEnabled(true);
            user.getUserAuth().setLastPasswordSet(new Timestamp(System.currentTimeMillis()));
            user.getUserAuth().setResetPasswordCode(null);
            userAuthService.save(user.getUserAuth());
        }
        userService.save(user);
    }

    private boolean validatePasswordChange(User user, ChangePasswordDTO changePasswordDTO) {
        if (user == null)
            return false;

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getReEnteredNewPassword()))
            return false;

        return passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword());
    }

    public User verify(String verificationCode) {
        User user = userService.findByVerificationCode(verificationCode);
        if (user == null) throw new BadRequestException("Wrong verification code!");
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

        String randomCode = RandomString.make(64);
        user.getUserAuth().setResetPasswordCode(randomCode);
        userAuthService.save(user.getUserAuth());
        userService.save(user);
        emailService.sendEmailResetAsync(user);
        return true;
    }

    public void logoutUser(String token) {
        String email = tokenUtils.getUsernameFromToken(token);
        User user = userService.findByEmail(email);
        if (user == null) throw new BadRequestException("User with this email does not exist");
        if (user.getRole().equals(UserRole.DRIVER)) {
            driverService.logoutDriver(user.getEmail());
        }
    }


    public Driver addDriver(CreateDriverDTO userDTO) {
        if (userService.findByEmail(userDTO.getEmail()) != null) {
            return null;
        }
        User user = DTOMapper.getUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setBlocked(false);
        return driverService.saveDriver(userDTO, getUserAuth(user));
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

    private boolean setIsUserEnabledRegistration(User user) {
        return user.getRole() != UserRole.CUSTOMER;
    }
}
