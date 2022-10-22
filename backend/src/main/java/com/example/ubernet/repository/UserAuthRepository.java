package com.example.ubernet.repository;

import com.example.ubernet.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
}
