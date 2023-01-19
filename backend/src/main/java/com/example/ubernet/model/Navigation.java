package com.example.ubernet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Navigation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    private Boolean deleted = false;
    @OneToOne
    private CurrentRide approachFirstRide;
    @OneToOne
    private CurrentRide firstRide;
    @OneToOne
    private CurrentRide approachSecondRide;
    @OneToOne
    private CurrentRide secondRide;
}
