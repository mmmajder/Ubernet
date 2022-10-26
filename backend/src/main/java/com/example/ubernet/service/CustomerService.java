package com.example.ubernet.service;

import com.example.ubernet.model.Customer;
import com.example.ubernet.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer findById(long id) {
        return customerRepository.findById(id).orElse(null);
    }
}
