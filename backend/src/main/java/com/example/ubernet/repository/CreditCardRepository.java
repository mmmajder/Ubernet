package com.example.ubernet.repository;

import com.example.ubernet.model.CreditCard;
import com.example.ubernet.model.Message;
import com.example.ubernet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    List<CreditCard> findByClient(User client);
    CreditCard findByClientAndIsActive(User client, boolean isActive);
}