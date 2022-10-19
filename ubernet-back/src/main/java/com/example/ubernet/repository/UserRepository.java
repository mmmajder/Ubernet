package com.example.ubernet.repository;

import com.example.ubernet.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Page<User> findAll(Pageable pageable);
    public Optional<User> findByEmail(String username);

    public User save(User user);
}
