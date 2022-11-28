package com.example.ubernet.service;

import com.example.ubernet.controller.CustomerController;
import com.example.ubernet.dto.SimpleUser;
import com.example.ubernet.model.Customer;
import com.example.ubernet.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserService userService;

    public Customer findById(long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer findByEmail(String clientEmail) {
        return (Customer) userService.findByEmail(clientEmail);
    }

    public List<SimpleUser> getCustomers() {
        List<SimpleUser> simpleUsers = new ArrayList<>();
        for (Customer customer : customerRepository.findAll()) {
            simpleUsers.add(SimpleUser.builder().email(customer.getEmail()).name(customer.getName()).surname(customer.getSurname()).build());
        }
        return simpleUsers;
    }

    public void createCustomer(Customer user) {
        user.setNumberOfTokens(0);
        save(user);
    }
}
