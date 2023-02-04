package com.example.ubernet.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private String userEmail;
    private String adminEmail;
    private String content;
}
