package com.example.ubernet.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

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
//    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
    @JsonManagedReference
    @OneToOne
    private Ride ride;
    private Boolean deleted = false;

}
