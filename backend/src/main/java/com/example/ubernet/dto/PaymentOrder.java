package com.example.ubernet.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentOrder {
    private double price;
    private String currency;
    private String method;
    private String intent;
    private String description;
}
