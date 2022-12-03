package com.example.ubernet.service;

import com.example.ubernet.dto.LoginSocialDTO;
import com.example.ubernet.dto.SimpleUser;
import com.example.ubernet.model.Customer;
import com.example.ubernet.model.Role;
import com.example.ubernet.model.User;
import com.example.ubernet.model.UserAuth;
import com.example.ubernet.model.enums.UserRole;
import com.example.ubernet.repository.CustomerRepository;
import com.example.ubernet.utils.EntityMapper;
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

    public Customer save(User user) {
        return customerRepository.save(EntityMapper.mapToCustomer(user));
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
        customer.setName(loginSocialDTO.getFirstName());
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
