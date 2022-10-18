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
@Table(name = "Users")
public class User {
    @Id
    @Column(unique = true)
    private String email;

    private String name;
    private String surname;
    private String city;
    private String phoneNumber;
    private Boolean deleted=false;
    //    private Image image;
    private Boolean isBLocked;





}

