package com.example.ubernet.service;

import com.example.ubernet.dto.MessageDTO;
import com.example.ubernet.dto.MessageFromClient;
import com.example.ubernet.model.Message;
import com.example.ubernet.model.Chat;
import com.example.ubernet.model.User;
import com.example.ubernet.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserService userService;

    public MessageService() {
    }

    public boolean clientExist(String email) {
        return userService.findByEmail(email) != null;
    }

    public boolean adminExist(String email) {
        return email == null || userService.findByEmail(email) != null;
    }

    public Message createMessage(MessageFromClient messageFromClient) {
        Message message = new Message(messageFromClient);
        save(message);

        return message;
    }

    public MessageDTO createMessageDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO(message);
        String time = transformDateTimeToStringForMessages(message.getTime());
        messageDTO.setTime(time);

        return messageDTO;
    }

    public List<MessageDTO> createMessageDTOs(List<Message> messages) {
        return messages.stream().map(this::createMessageDTO).collect(Collectors.toList());
    }

    public List<Message> getClientMessages(String email) {
        return messageRepository.findByClientEmail(email).stream().filter(m -> !m.getIsDeleted()).collect(Collectors.toList());
    }

    public List<Chat> getChats() {
        List<Message> mostRecentMessages = findMostRecentMessageForEachClient();
        mostRecentMessages.sort(Comparator.comparing(Message::getTime).reversed());

        return transformIntoChats(mostRecentMessages);
    }

    private List<Message> findMostRecentMessageForEachClient() {
        List<String> uniqueEmails = messageRepository.findUniqueClientEmails();
        List<Message> mostRecentMessages = new ArrayList<>();

        for (String e : uniqueEmails) {
            Message mostRecentMessage = getMessegeWithTheMostRecentTimeByClientEmail(e);
            mostRecentMessages.add(mostRecentMessage);
        }

        return mostRecentMessages;
    }

    private List<Chat> transformIntoChats(List<Message> mostRecentMessages) {
        List<Chat> chats = new ArrayList<>();

        for (Message m : mostRecentMessages) {
            User u = userService.findByEmail(m.getClientEmail());
            Chat chat = new Chat(m, u.getName(), u.getSurname());
            chats.add(chat);
        }

        return chats;
    }

    private Message getMessegeWithHighestIdByClientEmail(String clientEmail) {
        return messageRepository.findFirstByClientEmailAndIsDeletedOrderByIdDesc(clientEmail, false);
    }

    private Message getMessegeWithTheMostRecentTimeByClientEmail(String clientEmail) {
        return messageRepository.findFirstByClientEmailAndIsDeletedOrderByTimeDesc(clientEmail, false);
    }

    public String transformDateTimeToStringForMessages(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. hh:mm");
        return dateTime.format(formatter);
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }
}
