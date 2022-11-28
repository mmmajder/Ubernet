package com.example.ubernet.utils;

import com.example.ubernet.model.Admin;
import com.example.ubernet.model.Customer;
import com.example.ubernet.model.Driver;
import com.example.ubernet.model.User;

public class EntityMapper {
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

    public static Customer mapToCustomer(User user) {
        Customer customer = new Customer();
        customer.setPassword(user.getPassword());
        customer.setCity(user.getCity());
        customer.setName(user.getName());
        customer.setPhoneNumber(user.getPhoneNumber());
        customer.setDeleted(user.getDeleted());
        customer.setSurname(user.getSurname());
        customer.setEmail(user.getEmail());
        customer.setUserAuth(user.getUserAuth());
        customer.setRole(user.getRole());
        customer.setIsBlocked(user.getIsBlocked());
        return customer;
    }
    public static Driver mapToDriver(User user) {
        Driver driver = new Driver();
        driver.setPassword(user.getPassword());
        driver.setCity(user.getCity());
        driver.setName(user.getName());
        driver.setPhoneNumber(user.getPhoneNumber());
        driver.setDeleted(user.getDeleted());
        driver.setSurname(user.getSurname());
        driver.setEmail(user.getEmail());
        driver.setUserAuth(user.getUserAuth());
        driver.setRole(user.getRole());
        driver.setIsBlocked(user.getIsBlocked());
        return driver;
    }

}
