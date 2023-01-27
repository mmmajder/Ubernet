package com.example.ubernet.service;

import com.example.ubernet.dto.CreateUserDTO;
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
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
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
    private final PasswordEncoder passwordEncoder;

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

    public Customer createCustomer(CreateUserDTO createUserDTO) {
        Customer customer = new Customer();
        customer.setEmail(createUserDTO.getEmail());
        customer.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
        customer.setName(createUserDTO.getName());
        customer.setSurname(createUserDTO.getSurname());
        customer.setCity(createUserDTO.getCity());
        customer.setPhoneNumber(createUserDTO.getPhoneNumber());
        customer.setDeleted(false);
        customer.setUserAuth(createCustomerAuth());
        customer.setRole(UserRole.CUSTOMER);
        customer.setBlocked(false);
        customer.setNumberOfTokens(0);
        customer.setActive(false);
        customer.setRole(UserRole.CUSTOMER);
        customer.setNumberOfTokens(0);
        return save(customer);
    }

    private UserAuth createCustomerAuth() {
        UserAuth userAuth = new UserAuth();
        String randomCode = RandomString.make(64);
        userAuth.setVerificationCode(randomCode);
        userAuth.setLastPasswordSet(new Timestamp(System.currentTimeMillis()));
        userAuth.setRoles(getCustomerRoles());
        userAuth.setIsEnabled(false);
        userAuthService.save(userAuth);
        return userAuth;
    }

    private List<Role> getCustomerRoles() {
        return List.of(userService.findRolesByUserType("ROLE_USER"), userService.findRolesByUserType("ROLE_CUSTOMER"));
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

    public void checkIfCustomersCanPay(Double avgPrice, Customer customer) {
        if (customer.getNumberOfTokens() < avgPrice)
            throw new BadRequestException("You do not have enough money to pay for ride");
    }

    public void addToFavoriteRoutes(String rideId) {

    }

    public void removeFromFavoriteRoutes(String rideId) {
    }
}
