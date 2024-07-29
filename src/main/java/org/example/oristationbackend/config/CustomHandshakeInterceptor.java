package org.example.oristationbackend.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 요청에서 userId를 추출하여 WebSocket 세션 속성에 추가
        String userId = getUserIdFromRequest(request);
        attributes.put("userId", userId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
        // 핸드셰이크 후 추가 작업이 필요할 경우 여기에 작성
    }

    private String getUserIdFromRequest(ServerHttpRequest request) {
        // 요청에서 userId를 추출하는 로직 (예: 헤더, 파라미터, 쿠키 등)
        // 예시로 쿼리 파라미터에서 userId를 추출
        return request.getURI().getQuery().split("=")[1];
    }
}