package com.example.ubernet.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

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
public class Car {
    @Id
    @Column(unique = true)
    private long id;

    private Boolean deleted=false;
    @OneToOne
    private Position position;
    @OneToOne
    private CarType carType;
    private Boolean isAvailable;
    @OneToOne
    private Driver driver;
}
