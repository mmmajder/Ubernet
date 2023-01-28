package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverChangeRequest {
    private String email;
    private String name;
    private String surname;
    private String city;
    private String phoneNumber;
    private Boolean allowsBabies;
    private Boolean allowsPets;
    private String plates;
    private String carName;
    private String carType;
}
