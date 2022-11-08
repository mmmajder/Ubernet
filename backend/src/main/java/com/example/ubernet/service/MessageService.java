package com.example.ubernet.service;

import com.example.ubernet.dto.MessageDTO;
import com.example.ubernet.model.Message;
import com.example.ubernet.model.User;
import com.example.ubernet.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    public MessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

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
        Message message = new Message(client, adminEmail, messageDTO.isSentByAdmin(), messageDTO.getContent());
        save(message);

        return message;
    }

    public List<Message> getUserChat(String email){
        User user = userService.findByEmail(email);

        return messageRepository.findByClient(user);
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }
}
