package com.example.ubernet.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Customer extends User {
    private String paymentCredentials; //- pogledati kako se pravilno radi
}
