package com.example.ubernet.service;

import com.example.ubernet.model.Role;
import com.example.ubernet.model.User;
import com.example.ubernet.repository.RoleRepository;
import com.example.ubernet.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user==null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return user;
        }
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Role findRolesByUserType(String name) {
        return roleRepository.findByName(name);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
