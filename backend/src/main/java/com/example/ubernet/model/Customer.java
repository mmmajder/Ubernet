package com.example.ubernet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends User {
    private double numberOfTokens;
    private boolean isActive;
    @JsonIgnore
    @OneToMany
    private List<Ride> favoriteRoutes;

    public Customer(String email) {
        super(email);
    }

    public Customer(String email, boolean isActive) {
        super(email);
        this.isActive = isActive;
    }

    public Customer(String email, Boolean blocked, boolean isActive) {
        super(email, blocked);
        this.isActive = isActive;
    }
}
