package com.example.ubernet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProfileChangesRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    @OneToOne
    private Driver driver;

    private boolean processed;
    private LocalDateTime requestTime;

    private String name;
    private String surname;
    private String city;
    private String phoneNumber;
    private Boolean allowsBabies;
    private Boolean allowsPets;
    private String plates;
    private String carName;
    private String carType;
}
