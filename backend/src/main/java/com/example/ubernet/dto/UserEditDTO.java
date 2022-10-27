package com.example.ubernet.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserEditDTO {
    private String name;
    private String surname;
    private String city;
    private String phoneNumber;
}
