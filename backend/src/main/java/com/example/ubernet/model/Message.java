package com.example.ubernet.model;

import com.example.ubernet.dto.MessageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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

    @ManyToOne
    private User client;
    private String adminEmail;
    private boolean isSentByAdmin; // for easier filtering in repos

    private LocalDateTime time;
    private String content;

    private Boolean isDeleted = false;

    public Message(User client, String adminEmail, boolean isSentByAdmin, String content){
        this.client = client;
        this.adminEmail = adminEmail;
        this.isSentByAdmin = isSentByAdmin;
        this.content = content;
        this.time = LocalDateTime.now();
        this.isDeleted = false;
    }
}
