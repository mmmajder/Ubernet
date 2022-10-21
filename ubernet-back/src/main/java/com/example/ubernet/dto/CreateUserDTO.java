package com.example.ubernet.dto;

import com.example.ubernet.model.enums.UserRole;

public class CreateUserDTO {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String city;
    private String phoneNumber;
    private boolean isDeleted;
    private UserRole userRole;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CreateUserDTO(boolean isDeleted, String email, String name, String surname, String city, String phoneNumber, UserRole userRole, String password) {
        this.isDeleted = isDeleted;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
        this.password = password;
    }

    public CreateUserDTO() {
    }
}
