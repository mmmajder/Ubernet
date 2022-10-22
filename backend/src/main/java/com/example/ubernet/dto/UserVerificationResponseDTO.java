package com.example.ubernet.dto;

import com.example.ubernet.model.UserAuth;
import com.example.ubernet.model.enums.UserRole;

import javax.persistence.OneToOne;

public class UserVerificationResponseDTO {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String city;
    private String phoneNumber;
    private Boolean deleted=false;
    public UserVerificationResponseDTO() {
    }

    public UserVerificationResponseDTO(String email, String password, String name, String surname, String city, String phoneNumber, Boolean deleted) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.deleted = deleted;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

}
