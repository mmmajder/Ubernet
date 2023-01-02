package com.example.ubernet.handler;

import com.example.ubernet.dto.MessageFromClient;
import com.example.ubernet.model.Message;
import com.example.ubernet.model.User;
import com.example.ubernet.model.enums.UserRole;
import com.example.ubernet.service.MessageService;
import com.example.ubernet.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CodePointLength;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.persistence.ManyToOne;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint(value = "/chatWebSocket/{username}")
//@Configurable(preConstruction = true, autowire = Autowire.BY_TYPE, dependencyCheck = false)
//@EnableSpringConfigured
public class ChatWebSocket {
//    private static HashMap<String, Session> sessions = new HashMap<>();

    private static HashMap<String, Session> adminSessions = new HashMap<>();
    private static HashMap<String, Session> userSessions = new HashMap<>();

//    @Autowired
    private  UserService userService;
//    @Autowired
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
        System.out.println("PORUKA NA SERVERU");
        System.out.println(message);
        System.out.println(m);

        if (m.isSentByAdmin()){
            // send to user, show to other admins
            System.out.println(m.getAdminEmail());
            Session receiverSession = userSessions.get(m.getClientEmail());
            if (receiverSession != null) {
                receiverSession.getBasicRemote().sendText(o.writeValueAsString(m));
                System.out.println("Poruka poslata sa " + m.getAdminEmail() + " i ide ka " + m.getClientEmail());
            }

            // send to admins
            for (String adminEmail: adminSessions.keySet()) {
                if (!m.getAdminEmail().equals(adminEmail)){
                    Session adminSession = adminSessions.get(adminEmail);
                    adminSession.getBasicRemote().sendText(o.writeValueAsString(m));
                    System.out.println("Poruka poslata sa " + m.getAdminEmail() + " i ide ka " + adminEmail);
                }
            }

        } else {
            for (Session adminSession: adminSessions.values()) {
                adminSession.getBasicRemote().sendText(o.writeValueAsString(m));
                System.out.println("Poruka poslata sa " + m.getClientEmail() + " i ide ka " + m.getAdminEmail());
            }
        }

//        messageService.save(m);
    }

    @OnClose
    public void onClose(@PathParam("username") String username, Session session, CloseReason closeReason) {
        adminSessions.remove(username);
        userSessions.remove(username);
        System.out.println(username + " disconected from" + session.getId());
    }
}
