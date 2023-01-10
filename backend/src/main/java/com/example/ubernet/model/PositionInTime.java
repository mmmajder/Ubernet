package com.example.ubernet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PositionInTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    private Boolean deleted = false;

    private double secondsPast;

    @OneToOne
    private Position position;

    @Override
    public String toString() {
        return "PositionInTime{" +
                "id=" + id +
                ", deleted=" + deleted +
                ", secondsPast=" + secondsPast +
                ", position=" + position +
                '}';
    }
}
