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

public class DriverInconsistency {

    @Id
    @Column(unique = true)
    private long id;

    @OneToOne
    private Ride ride;

    private Boolean deleted = false;

}
