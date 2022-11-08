package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDTO {
    private String clientEmail;
    private String adminEmail;
    private boolean isSentByAdmin;
    private String content;
}