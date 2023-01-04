package com.example.ubernet.dto;

import com.example.ubernet.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDTO {
    private String clientEmail;
    private String adminEmail;
    @NotNull
    private boolean isSentByAdmin;
    @NotEmpty
    private String content;
    @NotNull
//    private LocalDateTime time;

    public MessageDTO(Message message){
        this.clientEmail = message.getClientEmail();
        this.adminEmail = message.getAdminEmail();
        this.isSentByAdmin = message.isSentByAdmin();
        this.content = message.getContent();
//        this.time = message.getTime();
    }
}