package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideHistoryRequestParam {
    String sortKind;
    String sortOrder;
    String driverEmail;
    String customerEmail;
    int pageNumber;
    int pageSize;
}
