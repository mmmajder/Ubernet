package com.example.ubernet.service;

import com.example.ubernet.dto.MessageDTO;
import com.example.ubernet.dto.MessageResponse;
import com.example.ubernet.model.Message;
import com.example.ubernet.model.User;
import com.example.ubernet.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private  MessageRepository messageRepository;
    @Autowired
    private  UserService userService;

//    public MessageService(MessageRepository messageRepository, UserService userService) {
//        this.messageRepository = messageRepository;
//        this.userService = userService;
//    }

    public MessageService(){}

    public boolean doesClientExist(String email){
        return userService.findByEmail(email) != null;
    }

    public boolean doesAdminExist(String email){
        if (email != null && userService.findByEmail(email) == null)
            return false;
        else
            return true;
    }

    public Message createMessage(MessageDTO messageDTO) {
        User client = userService.findByEmail(messageDTO.getClientEmail());
        String adminEmail = messageDTO.getAdminEmail();
        String clientEmail = client.getEmail();
        Message message = new Message(clientEmail, adminEmail, messageDTO.isSentByAdmin(), messageDTO.getContent());
        save(message);

        return message;
    }

    public List<Message> getClientMessages(String email){
        User user = userService.findByEmail(email);

        return messageRepository.findByClientEmail(email);
    }

    public List<MessageResponse> transformIntoResponses(List<Message> messages, String type){
        return messages.stream().map(m -> transferIntoResponse(m, type)).collect(Collectors.toList());
    }

    public MessageResponse transferIntoResponse(Message m, String type){
        MessageResponse r = new MessageResponse(m, determineType(m, type));
        r.setTime(transformDateTimeToStringForMessages(m.getTime()));

        return r;
    }

    // TODO clean this and change 'right', 'left' into something more meaningful
    private String determineType(Message m, String type) {
        if (type.equals("admin")) {
            if (m.isSentByAdmin())
                type = "right";
            else
                type = "left";
        } else { // sent by a customer or a driver
            if (m.isSentByAdmin())
                type = "left";
            else
                type = "right";
        }

        return type;
    }

    public String transformDateTimeToStringForMessages(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. hh:mm");

        return dateTime.format(formatter);
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }
}
