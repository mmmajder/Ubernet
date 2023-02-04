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

public class CarType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;
    private String name;
    private Double priceForType;
    private Boolean deleted = false;
}
