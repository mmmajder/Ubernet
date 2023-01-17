package com.example.ubernet.service;

import com.example.ubernet.dto.LoginSocialDTO;
import com.example.ubernet.dto.SimpleUser;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.exception.NotFoundException;
import com.example.ubernet.model.Customer;
import com.example.ubernet.model.Role;
import com.example.ubernet.model.User;
import com.example.ubernet.model.UserAuth;
import com.example.ubernet.model.enums.UserRole;
import com.example.ubernet.repository.CustomerRepository;
import com.example.ubernet.utils.EntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserService userService;
    private final UserAuthService userAuthService;

    public Customer save(User user) {
        return customerRepository.save(EntityMapper.mapToCustomer(user));
    }

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
        customer.setName(loginSocialDTO.getFirstName());
        customer.setUserAuth(userAuth);
        customer.setSurname(loginSocialDTO.getLastName());
        customer.setBlocked(false);
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

    public ArrayList<String> getCustomersEmails() {
        return (ArrayList<String>) customerRepository.findAll().stream().map(Customer::getEmail).collect(Collectors.toList());
    }

    public List<Customer> getCustomersByEmails(List<String> customerEmails) {
        List<Customer> customers = new ArrayList<>();
        for (String email : customerEmails) {
            customers.add(getCustomerByEmail(email));
        }
        return customers;
    }

    public Customer getCustomerByEmail(String email) {
        Customer customer = findByEmail(email);
        if (customer == null) {
            throw new NotFoundException("Customer with this email does not have Ubernet account");
        }
        return customer;
    }

    public double addTokens(String email, double tokens) {
        Customer customer = findByEmail(email);
        if (customer == null) {
            throw new NotFoundException("Customer does not exist");
        }
        customer.setNumberOfTokens(customer.getNumberOfTokens() + tokens);
        save(customer);
        return customer.getNumberOfTokens();
    }

    @Transactional
    public void checkIfCustomersCanPay(List<Customer> customers, Double totalPrice, Customer issueCustomer) {
        double avgPrice = totalPrice / (customers.size() + 1);
        StringBuilder errorMessage = new StringBuilder();
        for (Customer customer : customers) {
            if (customer.getNumberOfTokens() < avgPrice) {
                errorMessage.append(customer.getName()).append(" ").append(customer.getSurname());
            }
        }
        if (issueCustomer.getNumberOfTokens()<avgPrice) {
            if (errorMessage.toString().equals("")) {
                throw new BadRequestException("You do not have enough money to pay for ride");
            } else {
                errorMessage.append(" and you");
            }
        }
        if (!errorMessage.toString().equals("")) {
            throw new BadRequestException("Following users do not have enough money: " + errorMessage);
        }
    }
}
