package com.example.ubernet.service;

import com.example.ubernet.model.Customer;
import com.example.ubernet.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
}
