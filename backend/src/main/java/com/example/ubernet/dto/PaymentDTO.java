package com.example.ubernet.dto;

import com.example.ubernet.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Double totalPrice;
    private String customerThatPayed;
}
