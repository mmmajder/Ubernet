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
    private String profileImage;
    private String type;

    public MessageResponse(Message m, String type){
        this.clientEmail = m.getClientEmail();
        this.adminEmail = m.getAdminEmail();
        this.isSentByAdmin = m.isSentByAdmin();
        this.content = m.getContent();
        this.profileImage = "assets/taxi.jpg";
        this.type = type;
    }
}
