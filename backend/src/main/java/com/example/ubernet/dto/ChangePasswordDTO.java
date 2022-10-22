package com.example.ubernet.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class ChangePasswordDTO {
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String password;

    public ChangePasswordDTO() {
    }

    public ChangePasswordDTO(String email, String password) {
        this.email = email;
        this.password = password;
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
}
