package com.example.ubernet.controller;

import com.example.ubernet.dto.MessageDTO;
import com.example.ubernet.model.Chat;
import com.example.ubernet.model.Message;
import com.example.ubernet.service.MessageService;
import com.example.ubernet.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="*")
@RequestMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

//    @PutMapping(value = "/send")
//    public ResponseEntity<String> sendMessage(@RequestBody MessageDTO messageDTO) {
//        if (!messageService.doesClientExist(messageDTO.getClientEmail()))
//            return new ResponseEntity<>("Sender does not exist.", HttpStatus.BAD_REQUEST);
//        if (!messageService.doesAdminExist(messageDTO.getAdminEmail())) // it is mandatory to be null, if the receiver is admin
//            return new ResponseEntity<>("Receiver does not exist.", HttpStatus.BAD_REQUEST);
//
////        messageService.createMessage(messageDTO);
//        return new ResponseEntity<>("Successfully sent message.", HttpStatus.OK);
//    }

    @GetMapping(value = "/{clientEmail}")
    public ResponseEntity<List<MessageDTO>> getMessagesForClientEmail(@PathVariable String clientEmail) {
        if (!userService.userExist(clientEmail))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Message> messages =  messageService.getClientMessages(clientEmail);
        List<MessageDTO> messageDTOs = messageService.createMessageDTOs(messages);

        return new ResponseEntity<>(messageDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/chats")
    public ResponseEntity<List<Chat>> getChats() {
        List<Chat> chats = messageService.getChats();

        return new ResponseEntity<>(chats, HttpStatus.OK);
    }

//    @GetMapping(value = "admin/{email}")
//    public ResponseEntity<List<MessageDTO>> getMessagesAsAdmin(@PathVariable String email) {
//        if (!userService.userExist(email))
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//
//        List<Message> messages =  messageService.getClientMessages(email);
//        List<MessageDTO> messageDTOs = messageService.createMessageDTOs(messages);
//
//        return new ResponseEntity<>(messageDTOs, HttpStatus.OK);
//    }

//    @GetMapping(value = "/test")
//    public ResponseEntity<StringResponse> getMessages() {
//        StringResponse r = new StringResponse("Success");
//        return new ResponseEntity<>(r, HttpStatus.OK);
//    }
}
