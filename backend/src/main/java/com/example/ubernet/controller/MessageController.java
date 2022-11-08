package com.example.ubernet.controller;

import com.example.ubernet.dto.MessageDTO;
import com.example.ubernet.model.Message;
import com.example.ubernet.service.MessageService;
import com.example.ubernet.service.ProfileUpdateRequestService;
import com.example.ubernet.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;


    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PutMapping(value = "/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessageDTO messageDTO) {
        if (!messageService.doesClientExist(messageDTO.getClientEmail()))
            return new ResponseEntity<>("Sender does not exist.", HttpStatus.BAD_REQUEST);
        if (!messageService.doesAdminExist(messageDTO.getAdminEmail())) // it is mandatory to be null, if the receiver is admin
            return new ResponseEntity<>("Receiver does not exist.", HttpStatus.BAD_REQUEST);

        messageService.createMessage(messageDTO);
        return new ResponseEntity<>("Successfully sent message.", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessages(@RequestParam String email) {
        if (!userService.doesUserExist(email))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Message> chat =  messageService.getUserChat(email);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

}