package com.example.ubernet.dto;

import com.example.ubernet.model.Message;
import com.example.ubernet.repository.MessageRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageResponse {
    private String clientEmail;
    private String adminEmail;
    private boolean isSentByAdmin;
    private String content;
    private String time; // "dd.MM.yyyy. hh:mm"

    public MessageResponse(Message m){
        this.clientEmail = m.getClient().getEmail();
        this.adminEmail = m.getAdminEmail();
        this.isSentByAdmin = m.isSentByAdmin();
        this.content = m.getContent();
    }
}
