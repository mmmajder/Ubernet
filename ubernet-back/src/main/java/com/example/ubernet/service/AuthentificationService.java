package com.example.ubernet.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.example.ubernet.dto.JwtAuthenticationRequest;
import com.example.ubernet.model.Role;
import com.example.ubernet.model.User;
import com.example.ubernet.model.UserAuth;
import com.example.ubernet.model.enums.UserRole;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;

@Service
public class AuthentificationService {
    private final UserService userService;
    private final EmailService emailService;
    private final AdminService adminService;
    private final UserAuthService userAuthService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthentificationService(PasswordEncoder passwordEncoder, UserService userService,
                                   EmailService emailService, AuthenticationManager authenticationManager, AdminService adminService, UserAuthService userAuthService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
        this.adminService = adminService;
        this.userAuthService = userAuthService;
    }

    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsBlocked(false);
        user.setUserAuth(getUserAuth());
        user = addRoles(user);

//        user = this.userService.save(user);
        user = adminService.save(user);

        //TODO send email when needed
//        if (user.getRole() == Role.DRIVER) {
//            // SEND MAIL
//            try {
//                emailService.sendCreateProfileAsync(user);
//            } catch (UnsupportedEncodingException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (MessagingException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
        return user;
    }

    private UserAuth getUserAuth() {
        UserAuth userAuth = new UserAuth();

        String randomCode = RandomString.make(64);
        userAuth.setVerificationCode(randomCode);
        userAuth.setIsEnabled(true);
        userAuth.setLastPasswordSet(new Timestamp(System.currentTimeMillis()));
        userAuth.setIsPasswordReset(false);
        userAuthService.save(userAuth);
        return userAuth;
    }

    private User addRoles(User user) {
        List<Role> roles = new ArrayList<Role>();
        UserRole userRole = user.getRole();
        roles.add((Role) userService.findRolesByUserType("ROLE_USER"));

        if (userRole == UserRole.ADMIN) {
            roles.add((Role) userService.findRolesByUserType("ROLE_ADMIN"));
        } else if (userRole == UserRole.DRIVER) {
            roles.add((Role) userService.findRolesByUserType("ROLE_DRIVER"));
        } else {
            roles.add((Role) userService.findRolesByUserType("ROLE_CUSTOMER"));
        }
        user.getUserAuth().setRoles(roles);
        return user;
    }

    public User login(JwtAuthenticationRequest authenticationRequest) {
        Authentication authentication;
        try {
            System.out.println(passwordEncoder.encode(authenticationRequest.getPassword()));
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (AuthenticationException e) {
            return null;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        if (!user.isEnabled() || user.getDeleted()) {
            return null;
        }
        return user;
    }

    public boolean isPasswordReseted(User user) {
        return user.getUserAuth().getIsPasswordReset();
    }

}

