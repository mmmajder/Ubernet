package com.example.ubernet.repository;

import com.example.ubernet.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserEmail(String userEmail);
}
