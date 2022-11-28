package com.example.ubernet.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Customer extends User {
    private String paymentCredentials; //- pogledati kako se pravilno radi
    private double numberOfTokens;
}
