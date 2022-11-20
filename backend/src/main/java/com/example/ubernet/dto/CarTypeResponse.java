package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarTypeResponse {
    private long id;
    private String name;
    private Double priceForType;
    private Boolean deleted;
}
