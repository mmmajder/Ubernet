package com.example.ubernet.config;

import com.example.ubernet.handler.ChatWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

public class WebSocketConfig { //implements WebSocketConfigurer {

//    private final static String CHAT_ENDPOINT = "/wschat";
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(getWebSocketHandler(), CHAT_ENDPOINT).setAllowedOrigins("*");
//    }
//
//    public WebSocketHandler getWebSocketHandler(){
//        return new ChatWebSocketHandler();
//    }


}
