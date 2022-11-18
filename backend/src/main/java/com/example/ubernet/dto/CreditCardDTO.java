package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreditCardDTO {
    @NotNull
    private String cardNumber;
    @NotNull
    private String expirationDate;
    @NotNull
    private String cvv;
}
