package com.example.ubernet.dto;

import com.example.ubernet.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LoginResponseDTO {
    public UserTokenState token;
    public UserRole userRole;
}
