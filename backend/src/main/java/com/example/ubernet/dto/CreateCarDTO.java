package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCarDTO {
//    @NotEmpty
//    private Position position;
    @NotEmpty
    private String name;
    @Min(0)
    private Double priceForType;
    @NotEmpty
    private String email;
}
