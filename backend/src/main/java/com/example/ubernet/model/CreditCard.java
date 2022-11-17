package com.example.ubernet.model;

import com.example.ubernet.dto.CreditCardDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    @ManyToOne
    private User client;

    private String cardNumber;
    private String expirationDate;
    private String cvv;
    private Boolean isActive;

    public CreditCard(String cardNumber, String expirationDate, String cvv){
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.isActive = true;
    }

    public CreditCard(User client, CreditCardDTO creditCardDTO) {
        this.client = client;
        this.cardNumber = creditCardDTO.getCardNumber();
        this.expirationDate = creditCardDTO.getExpirationDate();
        this.cvv = creditCardDTO.getCvv();
        this.isActive = true;
    }
}
