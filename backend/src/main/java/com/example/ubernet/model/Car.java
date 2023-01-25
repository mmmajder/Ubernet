package com.example.ubernet.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Version;


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

    @OneToOne
    private Navigation navigation;

//    @OneToOne
//    private CurrentRide currentRide;
//    @OneToOne
//    private CurrentRide futureRide;
    @OneToOne
    private CarType carType;
    private Boolean isAvailable;
    @OneToOne
    @JsonIgnore
    private Driver driver;
    private Boolean allowsBaby;
    private Boolean allowsPet;
    private String plates;
    private String name;

    @Version
    private int version;

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", deleted=" + deleted +
                ", position=" + position +
                ", navigation=" + navigation +
                ", carType=" + carType +
                ", isAvailable=" + isAvailable +
                ", driver=" + driver.getName() +
                ", allowsBaby=" + allowsBaby +
                ", allowsPet=" + allowsPet +
                ", plates='" + plates + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
