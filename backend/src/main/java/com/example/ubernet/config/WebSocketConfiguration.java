package com.example.ubernet.config;

import com.example.ubernet.handler.ChatWebSocket;
import com.example.ubernet.service.MessageService;
import com.example.ubernet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

//    private @Autowired
//    AutowireCapableBeanFactory beanFactory;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //registry.addHandler(unitWebSocketHandler, "/unit").setAllowedOrigins("*");
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        //registry.addHandler(unitWebSocketHandler, "/unit").setAllowedOrigins("*");
//    }
//
//    @Bean
//    public ChatWebSocket chatWebSocket() {
//        ChatWebSocket wcchat = new ChatWebSocket();
//        beanFactory.autowireBean(wcchat);
//        return wcchat;
//    }
//
//    @Bean
//    public ServerEndpointExporter serverEndpointExporter() {
//        return new ServerEndpointExporter();
//    }
}
