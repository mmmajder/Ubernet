package com.example.ubernet.service;

import com.example.ubernet.dto.UserEditDTO;
import com.example.ubernet.dto.UserResponse;
import com.example.ubernet.model.ProfileUpdateRequest;
import com.example.ubernet.model.Role;
import com.example.ubernet.model.User;
import com.example.ubernet.model.enums.AuthProvider;
import com.example.ubernet.model.enums.UserRole;
import com.example.ubernet.repository.ProfileUpdateRequestRepository;
import com.example.ubernet.repository.RoleRepository;
import com.example.ubernet.repository.UserRepository;
import com.example.ubernet.utils.DTOMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileUpdateRequestRepository profileUpdateRequestRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, ProfileUpdateRequestRepository profileUpdateRequestRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.profileUpdateRequestRepository = profileUpdateRequestRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        return user.get();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Role findRolesByUserType(String name) {
        return roleRepository.findByName(name);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public UserEditDTO editUser(String emailOfUserToEdit, UserEditDTO userEditRequest) {
        Optional<User> user = userRepository.findByEmail(emailOfUserToEdit);
        if (user.isEmpty()) {
            return null;
        }
        setNewUserProperties(user.get(), userEditRequest);
        return DTOMapper.getUserEditDTO(user.get());
    }

    private void setNewUserProperties(User user, UserEditDTO userEditRequest) {
        if (user.getRole() == UserRole.CUSTOMER || user.getRole() == UserRole.ADMIN) {
            updateUser(user, userEditRequest);
        } else if (user.getRole() == UserRole.DRIVER) {
            createDriverUpdateRequest(user, userEditRequest);
        }

    }

    private void createDriverUpdateRequest(User user, UserEditDTO userEditRequest) {
        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest();
        profileUpdateRequest.setUser(user);
        profileUpdateRequest.setName(Optional.ofNullable(userEditRequest.getName()).orElse(user.getName()));
        profileUpdateRequest.setSurname(Optional.ofNullable(userEditRequest.getSurname()).orElse(user.getSurname()));
        profileUpdateRequest.setCity(Optional.ofNullable(userEditRequest.getCity()).orElse(user.getCity()));
        profileUpdateRequest.setPhoneNumber(Optional.ofNullable(userEditRequest.getPhoneNumber()).orElse(user.getPhoneNumber()));
        profileUpdateRequest.setProcessed(false);
        profileUpdateRequest.setRequestTime(new Timestamp(System.currentTimeMillis()));
        profileUpdateRequestRepository.save(profileUpdateRequest);
    }

    public void updateUser(User user, UserEditDTO userEditRequest) {
        user.setName(Optional.ofNullable(userEditRequest.getName()).orElse(user.getName()));
        user.setSurname(Optional.ofNullable(userEditRequest.getSurname()).orElse(user.getSurname()));
        user.setCity(Optional.ofNullable(userEditRequest.getCity()).orElse(user.getCity()));
        user.setPhoneNumber(Optional.ofNullable(userEditRequest.getPhoneNumber()).orElse(user.getPhoneNumber()));
        save(user);
    }

    private void setEmail(User user, String newEmail) throws Exception {
        if (newEmail == null) {
            return;
        }
        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new Exception("Mail already exists");
        }
        user.setEmail(newEmail);
    }

    public void processOAuthPostLogin(String email) {
        Optional<User> existUser = userRepository.findByEmail(email);

        if (existUser.isEmpty()) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setProvider(AuthProvider.GOOGLE);
            newUser.getUserAuth().setIsEnabled(true);

            userRepository.save(newUser);
        }
    }


    public User findByVerificationCode(String verificationCode) {
        return userRepository.findByVerificationCode(verificationCode).orElse(null);
    }

    public User getLoggedUser(Authentication authentication) {
        if(authentication == null) {
            return null;
        }
        return findByEmail(authentication.getName());
    }

    public UserResponse getUser(String email) {
        User user = findByEmail(email);
        if (user!=null) {
            return DTOMapper.getUserResponse(user);
        }
        return null;
    }
}
