package com.example.ubernet.model;

import com.example.ubernet.model.enums.RideState;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    private RideState rideState;

    @OneToOne
    @OrderBy("checkPoints")
    private Route route;

    @JsonBackReference
    @OneToOne
    private Payment payment;
    @OneToOne
    private Driver driver;
    private LocalDateTime scheduledStart;
    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;
    private LocalDateTime reservationTime;
    @ManyToMany
    private Set<Customer> customers;
    @OneToMany
    private Set<Review> carReviews;
    @OneToMany
    private Set<Review> driverReviews;

    private Boolean deleted = false;
}
