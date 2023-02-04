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
public class PathAlternative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    @OneToMany
    private List<CurrentRide> alternatives;

    public PathAlternative(List<CurrentRide> alternatives) {
        this.alternatives = alternatives;
    }
}
