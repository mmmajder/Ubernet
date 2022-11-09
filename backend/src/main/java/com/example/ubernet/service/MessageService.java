package com.example.ubernet.service;

import com.example.ubernet.dto.MessageDTO;
import com.example.ubernet.dto.MessageResponse;
import com.example.ubernet.model.Message;
import com.example.ubernet.model.User;
import com.example.ubernet.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Message> getClientMessages(String email){
        User user = userService.findByEmail(email);

        return messageRepository.findByClient(user);
    }

    public List<MessageResponse> transformIntoResponses(List<Message> messages){
        return messages.stream().map(m -> transferIntoResponse(m)).collect(Collectors.toList());
    }

    public MessageResponse transferIntoResponse(Message m){
        MessageResponse r = new MessageResponse(m);
        r.setTime(transformDateTimeToStringForMessages(m.getTime()));

        return r;
    }

    public String transformDateTimeToStringForMessages(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. hh:mm");

        return dateTime.format(formatter);
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }
}
