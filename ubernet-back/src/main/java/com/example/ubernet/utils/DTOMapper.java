package com.example.ubernet.utils;

import com.example.ubernet.dto.CreateUserDTO;
import com.example.ubernet.dto.UserVerificationResponseDTO;
import com.example.ubernet.model.User;
import com.example.ubernet.service.UserService;

public class DTOMapper {
    private static UserService userService;

    public static User getUser(CreateUserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setCity(userDTO.getCity());
        user.setEmail(userDTO.getEmail());
        user.setSurname(userDTO.getSurname());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getUserRole());
        return user;
    }

    public static UserVerificationResponseDTO getUserVerificationResponseDTO(User user) {
        UserVerificationResponseDTO userVerificationResponseDTO = new UserVerificationResponseDTO();
        userVerificationResponseDTO.setCity(user.getCity());
        userVerificationResponseDTO.setPassword(user.getPassword());
        userVerificationResponseDTO.setEmail(user.getEmail());
        userVerificationResponseDTO.setSurname(user.getSurname());
        userVerificationResponseDTO.setPhoneNumber(user.getPhoneNumber());
        userVerificationResponseDTO.setName(user.getName());
        return userVerificationResponseDTO;
    }
}
