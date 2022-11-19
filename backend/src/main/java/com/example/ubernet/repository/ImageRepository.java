package com.example.ubernet.repository;

import com.example.ubernet.model.CreditCard;
import com.example.ubernet.model.Image;
import com.example.ubernet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, String> {
    Image findByUserAndIsActive(User user, boolean isActive);
}