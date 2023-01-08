package com.example.ubernet.handler;

import com.example.ubernet.dto.MessageDTO;
import com.example.ubernet.dto.MessageFromClient;
import com.example.ubernet.model.Message;
import com.example.ubernet.model.User;
import com.example.ubernet.model.enums.UserRole;
import com.example.ubernet.service.MessageService;
import com.example.ubernet.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@Component
@ServerEndpoint(value = "/chatWebSocket/{username}")
//@Configurable(preConstruction = true, autowire = Autowire.BY_TYPE, dependencyCheck = false)
//@EnableSpringConfigured
public class ChatWebSocket {
//    private static HashMap<String, Session> sessions = new HashMap<>();

    private static HashMap<String, Session> adminSessions = new HashMap<>();
    private static HashMap<String, Session> userSessions = new HashMap<>();

    private  UserService userService;
    private   MessageService messageService;

    public ChatWebSocket(){
        ApplicationContext ctx =  ContextForChat.getApplicationContext();
        System.out.println(ctx);

        if (ctx != null){
            this.userService = (UserService) ContextForChat.getApplicationContext().getBean("userService");
            this.messageService = (MessageService) ContextForChat.getApplicationContext().getBean("messageService");
        }
    }

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        User u = userService.findByEmail(username);
        if (u.getRole() == UserRole.ADMIN){
            adminSessions.put(username, session);
        } else {
            userSessions.put(username, session);
        }

        System.out.println(u.getRole() + "  " + username + " connected on " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        ObjectMapper o = new ObjectMapper();
        MessageFromClient m = o.readValue(message, MessageFromClient.class);
        Message createdMessage = messageService.createMessage(m);
        messageService.save(createdMessage);
        MessageDTO messageDTO = messageService.createMessageDTO(createdMessage);
        sendMessageToSessions(messageDTO);
    }

    private void sendMessageToSessions(MessageDTO m) throws IOException {
        ObjectMapper o = new ObjectMapper();
        o.findAndRegisterModules();

        if (m.isSentByAdmin()){
            sendToCustomerSession(m, o);
            sendToAdminSessionsExceptToTheAdminThatSentTheMessage(m,o);
        } else { // sent by customer or by driver
            sendToAllAdminSessions(m, o);
        }
    }

    private void sendToCustomerSession(MessageDTO m, ObjectMapper o) throws IOException {
        Session receiverSession = userSessions.get(m.getClientEmail());
        if (receiverSession != null) {
            receiverSession.getBasicRemote().sendText(o.writeValueAsString(m));
            System.out.println("Poruka poslata sa " + m.getAdminEmail() + " i ide ka " + m.getClientEmail());
        }
    }

    private void sendToAdminSessionsExceptToTheAdminThatSentTheMessage(MessageDTO m, ObjectMapper o) throws IOException {
        for (String adminEmail: adminSessions.keySet()) {
            if (!m.getAdminEmail().equals(adminEmail)){
                Session adminSession = adminSessions.get(adminEmail);
                adminSession.getBasicRemote().sendText(o.writeValueAsString(m));
                System.out.println("Poruka poslata sa " + m.getAdminEmail() + " i ide ka " + adminEmail);
            }
        }
    }

    private void sendToAllAdminSessions(MessageDTO m, ObjectMapper o) throws IOException {
        for (Session adminSession: adminSessions.values()) {
            adminSession.getBasicRemote().sendText(o.writeValueAsString(m));
            System.out.println("Poruka poslata sa " + m.getClientEmail() + " i ide ka " + m.getAdminEmail());
        }
    }

    @OnClose
    public void onClose(@PathParam("username") String username, Session session, CloseReason closeReason) {
        adminSessions.remove(username);
        userSessions.remove(username);
        System.out.println(username + " disconected from" + session.getId());
    }
}
