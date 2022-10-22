package com.example.ubernet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Map {
    @Id
    @Column(unique = true)
    private long id;

//    @OneToMany
//    private HashSet<Car> activeCars;

    private Boolean deleted = false;

}
