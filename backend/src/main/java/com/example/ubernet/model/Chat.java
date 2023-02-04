package com.example.ubernet.model;

import com.example.ubernet.dto.MessageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Chat {
    private String clientEmail;
    private String clientFullname;
    private MessageDTO mostRecentMessage;

    public Chat(MessageDTO mostRecentMessage, String name, String lastname){
        this.clientEmail = mostRecentMessage.getClientEmail();
        this.clientFullname = name.concat(" ").concat(lastname);
        this.mostRecentMessage = mostRecentMessage;
    }

    public Chat(Message message, String name, String lastname){
        this.clientEmail = message.getClientEmail();
        this.clientFullname = name.concat(" ").concat(lastname);
        this.mostRecentMessage = new MessageDTO(message);
    }
}