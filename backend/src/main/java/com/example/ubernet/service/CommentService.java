package com.example.ubernet.service;

import com.example.ubernet.dto.CommentDTO;
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

    public void addComment(CommentDTO commentDTO) {
        commentRepository.save(Comment.builder()
                .userEmail(commentDTO.getUserEmail())
                .adminEmail(commentDTO.getAdminEmail())
                .content(commentDTO.getContent())
                .time(LocalDateTime.now())
                .build());
    }
}
