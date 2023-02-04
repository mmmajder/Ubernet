package com.example.ubernet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class DriverDailyActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    @OneToMany
    private List<DriverActivityPeriod> periodsInLast24h;
    private LocalDateTime lastPeriodStart;
    private Boolean isActive;
    private Boolean deleted = false;
}
