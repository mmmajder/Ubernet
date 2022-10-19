package com.example.ubernet.dto;

import com.example.ubernet.model.enums.UserRole;

public class LoginResponseDTO {
    public UserTokenState token;
    public UserRole userRole;

    public LoginResponseDTO() {

    }

    public LoginResponseDTO(UserTokenState token, UserRole userRole) {
        this.token = token;
        this.userRole = userRole;
    }
}
