package com.example.ubernet.controller;

import com.example.ubernet.dto.SimpleUser;
import com.example.ubernet.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/customer", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/get-customers")
    public List<SimpleUser> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/get-number-of-tokens/{email}")
    public double getNumberOfTokens(@PathVariable String email) {
        return customerService.findByEmail(email).getNumberOfTokens();
    }

    @GetMapping("/getCustomersEmails")
    public ArrayList<String> getCustomersEmails() {
        return customerService.getCustomersEmails();
    }

}
