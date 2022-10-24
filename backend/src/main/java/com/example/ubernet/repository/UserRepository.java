package com.example.ubernet.repository;

import com.example.ubernet.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String username);

    @Query(value = "SELECT user FROM User user WHERE user.userAuth.verificationCode = :code")
    public Optional<User> findByVerificationCode(String code);
}
