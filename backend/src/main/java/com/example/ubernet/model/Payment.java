package com.example.ubernet.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    private Double totalPrice;
    private Boolean isAcceptedPayment;
    private Boolean deleted = false;
    @OneToMany
    private List<CustomerPayment> customers;
}
