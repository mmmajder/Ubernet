package com.example.ubernet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SQLDelete(sql
        = "UPDATE person "
        + "SET deleted = true "
        + "WHERE username = ? and version = ?")
@Where(clause = "deleted = false")
public class DriverDailyActivity {

    @Id
    @Column(unique = true)
    private long id;
    private double totalDuration;
    private LocalDateTime lastTimeSetActive;
    private Boolean isActive;
    private Boolean deleted = false;
}
