package org.example.oristationbackend.config;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

public class QueueWebSocketHandler extends TextWebSocketHandler {
    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
    }

    public static void sendMessageToUser(String userId, String message) throws Exception {
        for (WebSocketSession session : sessions.values()) {
            if (session.getAttributes().get("userId").equals(userId)) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }
}
