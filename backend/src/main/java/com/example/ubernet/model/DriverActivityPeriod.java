package com.example.ubernet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DriverActivityPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    private LocalDateTime startOfPeriod;
    private LocalDateTime endOfPeriod;

    public DriverActivityPeriod(LocalDateTime start, LocalDateTime end) {
        this.startOfPeriod = start;
        this.endOfPeriod = end;
    }
}
