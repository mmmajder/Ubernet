package com.example.ubernet.repository;

import com.example.ubernet.model.Message;
import com.example.ubernet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByClientEmail(String clientEmail);
}
