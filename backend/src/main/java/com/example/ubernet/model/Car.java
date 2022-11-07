package com.example.ubernet.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    private Boolean deleted=false;
    @OneToOne
    private Position position;
    @OneToMany
    private List<Position> destinations;
    @OneToOne
    private CarType carType;
    private Boolean isAvailable;
    @OneToOne
    @JsonIgnore
    private Driver driver;

}
