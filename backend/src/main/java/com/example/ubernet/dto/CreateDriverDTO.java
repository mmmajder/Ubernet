package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateDriverDTO {
    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String name;
    @NotEmpty
    private String surname;
    @NotEmpty
    private String city;
    @NotEmpty
    private String phoneNumber;
    private Boolean allowsBaby;
    private Boolean allowsPet;
    private String plates;
    private String carName;
    private String carType;
}
