package com.example.ubernet.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatWebSocketHandler  {//extends TextWebSocketHandler {

//    private List<WebSocketSession> sessions = new ArrayList<>();
//
////    private Map<String, WebSocketSession> sessions = new HashMap<>();
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
////        sessions.put(session.)
//        sessions.add(session);
////        String username = @PathParam("username");
//    }
//
//    @Override
//    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
//        for (WebSocketSession webSocketSession : sessions){
//            webSocketSession.sendMessage(message);
//        }
//    }

//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        sessions.remove(session);
//    }
}
