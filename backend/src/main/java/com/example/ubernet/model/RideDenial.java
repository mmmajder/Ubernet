package com.example.ubernet.model;


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

public class RideDenial {

    @Id
    @Column(unique = true)
    private long id;

    @OneToOne
    private Ride ride;
    private String reason;
    private Boolean deleted = false;

}
