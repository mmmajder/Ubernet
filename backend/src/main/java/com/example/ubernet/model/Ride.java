package com.example.ubernet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Ride {
    @Id
    @Column(unique = true)
    private long id;

    @OneToOne
    private Route route;

    @OneToOne
    private Payment payment;
    @OneToOne
    private Driver driver;
    private LocalDateTime start;
    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;
    private LocalDateTime reservationTime;
    @OneToMany
    private Set<Customer> customers;
    @OneToMany
    private Set<Rating> carRatings;
    @OneToMany
    private Set<Rating> driverRatings;

    private Boolean deleted = false;

}
