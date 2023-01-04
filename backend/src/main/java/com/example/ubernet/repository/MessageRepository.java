package com.example.ubernet.repository;

import com.example.ubernet.model.Message;
import com.example.ubernet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByClientEmail(String clientEmail);

    @Query("SELECT DISTINCT message.clientEmail FROM Message message")
    List<String> findUniqueClientEmails();

    Message findFirstByClientEmailAndIsDeletedOrderByIdDesc(String clientEmail, boolean isDeleted);
    Message findFirstByClientEmailAndIsDeletedOrderByTimeDesc(String clientEmail, boolean isDeleted);
}
