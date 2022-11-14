package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDTO {
    @Email
    private String clientEmail;
    @Email
    private String adminEmail;
    @NotNull
    private boolean isSentByAdmin;
    @NotEmpty
    private String content;
}