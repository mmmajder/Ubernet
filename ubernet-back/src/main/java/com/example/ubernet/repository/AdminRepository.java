package com.example.ubernet.repository;

import com.example.ubernet.model.Admin;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Override
    <S extends Admin> Optional<S> findOne(Example<S> example);
}
