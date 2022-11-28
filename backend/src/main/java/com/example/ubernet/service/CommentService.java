package com.example.ubernet.service;

import com.example.ubernet.model.Comment;
import com.example.ubernet.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class CommentService {
    private CommentRepository commentRepository;

    public List<Comment> getComments(String userEmail) {
        return commentRepository.findByUserEmail(userEmail);
    }

    public void addComment(String userEmail, String adminEmail, String content) {
        commentRepository.save(Comment.builder()
                .userEmail(userEmail)
                .adminEmail(adminEmail)
                .content(content)
                .time(LocalDateTime.now())
                .build());
    }
}
