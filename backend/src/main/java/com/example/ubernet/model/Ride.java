package com.example.ubernet.model;

import com.example.ubernet.model.enums.RideState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
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

    @OneToOne
    private Payment payment;
    @OneToOne
    private Driver driver;
    private LocalDateTime scheduledStart;  // has only if it is reservation!!!
    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;
    private LocalDateTime requestTime;
    @ManyToMany
    private List<Customer> customers;
    @OneToMany
    private Set<Review> reviews;
    private boolean isReservation;

    private Boolean deleted = false;

    @OneToOne
    private RideRequest rideRequest;
}
