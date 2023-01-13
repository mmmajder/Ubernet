package com.example.ubernet.controller;

import com.example.ubernet.dto.SetNewFreeRideDTO;
import com.example.ubernet.dto.SimpleUser;
import com.example.ubernet.dto.TokensDTO;
import com.example.ubernet.model.Customer;
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
        Customer customer = customerService.findByEmail(email);
        return customer.getNumberOfTokens();
    }

    @PutMapping("/add-tokens/{email}")
    public double addTokens(@PathVariable String email, @RequestBody TokensDTO amount) {
        return customerService.addTokens(email, amount.getTokens());
    }

    @GetMapping("/getCustomersEmails")
    public ArrayList<String> getCustomersEmails() {
        return customerService.getCustomersEmails();
    }

}
