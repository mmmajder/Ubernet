package com.example.ubernet.utils;

import com.example.ubernet.model.Admin;
import com.example.ubernet.model.User;

public class UserMapper {
    public static Admin mapToAdmin(User user) {
        Admin admin = new Admin();
        admin.setCity(user.getCity());
        admin.setDeleted(user.getDeleted());
        admin.setUserAuth(user.getUserAuth());
        admin.setPassword(user.getPassword());
        admin.setEmail(user.getEmail());
        admin.setIsBlocked(user.getIsBlocked());
        admin.setPhoneNumber(user.getPhoneNumber());
        admin.setName(user.getName());
        admin.setSurname(user.getSurname());
        admin.setRole(user.getRole());
        return admin;
    }
}
