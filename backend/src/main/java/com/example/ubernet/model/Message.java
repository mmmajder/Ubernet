package com.example.ubernet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    private String clientEmail;
    private String adminEmail;
    private boolean isSentByAdmin; // for easier filtering in repos

    private LocalDateTime time;
    private String content;

    private Boolean isDeleted = false;

    public Message(String clientEmail, String adminEmail, boolean isSentByAdmin, String content){
        this.clientEmail = clientEmail;
        this.adminEmail = adminEmail;
        this.isSentByAdmin = isSentByAdmin;
        this.content = content;
        this.time = LocalDateTime.now();
        this.isDeleted = false;
    }
}
