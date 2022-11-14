package com.example.ubernet.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
