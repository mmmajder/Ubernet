package com.example.ubernet.controller;

import com.example.ubernet.dto.CommentDTO;
import com.example.ubernet.model.Comment;
import com.example.ubernet.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentsController {
    private final CommentService commentService;

    @GetMapping("/get-comments/{userEmail}")
    public List<Comment> getComments(@PathVariable String userEmail) {
        return commentService.getComments(userEmail);
    }

    @PostMapping("/add-comment")
    public void addComment(@RequestBody CommentDTO commentDTO) {
        commentService.addComment(commentDTO);
    }
}
