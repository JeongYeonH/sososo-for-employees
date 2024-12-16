package com.han_batang.back.websocket;

import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(
        ServerHttpRequest request, 
        ServerHttpResponse response, 
        WebSocketHandler wsHandler, 
        Map<String, Object> attributes
    ) throws Exception {
        String authHeader = null;
        String roomId = null;
        System.out.println("Handshake started");
        String query = request.getURI().getQuery();
        if (query != null && query.contains("Authorization")) {
            String[] queryParams = query.split("&");
            for (String param : queryParams) {
                if (param.startsWith("Authorization=")) {
                    authHeader = param.substring("Authorization=".length());
                }
                if (param.startsWith("roomId=")) {
                    roomId = param.substring("roomId=".length());
                }
            }
        }
    
        System.out.println("authHeader is: " + authHeader);
        if (authHeader != null) {
            System.out.println("헤더가 저장됩니다");
            attributes.put("Authorization", authHeader); 
        }

        if (roomId != null) {
            System.out.println("룸 아이디가 저장됩니다");
            attributes.put("roomId", roomId);
        }
        return true;
    }

    @Override
    public void afterHandshake(
        ServerHttpRequest request,
        ServerHttpResponse response,
        WebSocketHandler wsHandler,
        Exception exception
    ) {
        System.out.println("Handshake completed");
    }
}