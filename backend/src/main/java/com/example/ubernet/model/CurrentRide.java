package com.example.ubernet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CurrentRide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    private Boolean deleted = false;

    @OneToMany
    private List<PositionInTime> positions;

    private LocalDateTime timeOfStartOfRide;
    private LocalDateTime timeOfStartOfApproachingRide;
    private boolean isFreeRide;
    private boolean shouldGetRouteToClient;
    private Integer numberOfRoute;
}
