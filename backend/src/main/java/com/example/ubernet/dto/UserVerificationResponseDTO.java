package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserVerificationResponseDTO {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String city;
    private String phoneNumber;
    private Boolean deleted=false;
}
