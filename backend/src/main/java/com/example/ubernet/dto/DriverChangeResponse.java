package com.example.ubernet.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverChangeResponse {
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
    private boolean alreadyRequestedChanges;
}
