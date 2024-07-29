package org.example.oristationbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer, WebSocketMessageBrokerConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new QueueWebSocketHandler(), "/queue-status")
                .addInterceptors(new CustomHandshakeInterceptor())
                .setAllowedOrigins("*");
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}

