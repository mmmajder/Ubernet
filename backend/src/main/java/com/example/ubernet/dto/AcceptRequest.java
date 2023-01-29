package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcceptRequest {
    String driverEmail;
    boolean accepted;
}
