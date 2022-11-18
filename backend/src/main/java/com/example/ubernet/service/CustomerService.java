package com.example.ubernet.service;

import com.example.ubernet.dto.LoginSocialDTO;
import com.example.ubernet.model.Customer;
import com.example.ubernet.model.Role;
import com.example.ubernet.model.UserAuth;
import com.example.ubernet.model.enums.UserRole;
import com.example.ubernet.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserService userService;
    private final UserAuthService userAuthService;

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer findByEmail(String clientEmail) {
        return (Customer) userService.findByEmail(clientEmail);
    }


    public Customer createCustomerSocialLogin(LoginSocialDTO loginSocialDTO) {
        UserAuth userAuth = new UserAuth();
        userAuth.setRoles(addCustomerRoles());
        userAuth.setIsEnabled(true);
        userAuth.setDeleted(false);
        userAuth.setLastPasswordSet(new Timestamp(System.currentTimeMillis()));
        userAuthService.save(userAuth);
        Customer customer = new Customer();
        customer.setEmail(loginSocialDTO.getEmail());
        customer.setName(loginSocialDTO.getName().split("\\s+")[0]);
        customer.setUserAuth(userAuth);
        customer.setSurname(loginSocialDTO.getLastName());
        customer.setIsBlocked(false);
        customer.setRole(UserRole.CUSTOMER);
        save(customer);
        return customer;
    }

    private List<Role> addCustomerRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(userService.findRolesByUserType("ROLE_USER"));
        roles.add(userService.findRolesByUserType("ROLE_CUSTOMER"));
        return roles;
    }
}
