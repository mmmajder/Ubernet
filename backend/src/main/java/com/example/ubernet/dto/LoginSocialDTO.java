package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LoginSocialDTO {
    private String email;
    private String authToken;
    private String firstName;
    private String id;
    private String idToken;
    private String lastName;
    private String name;
    private String photoUrl;
    private String provider;
}
