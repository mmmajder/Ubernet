package com.example.ubernet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    private Double time;
    private Double km;
    private Double price;
    @OneToMany
    private List<Place> checkPoints;
    private Boolean deleted = false;

    public String stationListConcatenated() {
        return checkPoints.stream().map(Place::getName)
                .collect(Collectors.joining(" -> "));
    }

    public List<String> stationList() {
        return checkPoints.stream().map(Place::getName).collect(Collectors.toList());
    }
}
