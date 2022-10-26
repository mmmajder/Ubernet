package com.example.ubernet.repository;

import com.example.ubernet.model.Role;
import com.example.ubernet.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
