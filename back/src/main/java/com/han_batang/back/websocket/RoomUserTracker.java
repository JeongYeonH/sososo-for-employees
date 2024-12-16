package com.han_batang.back.websocket;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.han_batang.back.provider.JwtProvider;

@Component
public class RoomUserTracker {

    @Autowired
    private JwtProvider jwtProvider;

    private final ConcurrentHashMap<String, Set<String>> roomUserMap = new ConcurrentHashMap<>();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String authHeader = (String) headerAccessor.getSessionAttributes().get("Authorization");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        if (authHeader == null) {
            System.out.println("Authorization header is null.");
        }
        authHeader = authHeader.substring(7);
        String userId = jwtProvider.validate(authHeader);
        if (roomId != null && userId != null) {
            roomUserMap.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(userId);
            System.out.println("연결된 룸 아이디: " + roomId + " 연결된 유저 아이디: " + userId);
            System.out.println(roomUserMap);
        } else {
            System.out.println("사용자 인증 실패");
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        String authHeader = (String) headerAccessor.getSessionAttributes().get("Authorization");
        authHeader = authHeader.substring(7);
        String userId = jwtProvider.validate(authHeader);      
        System.out.println("디스커넥트 리스너 실행.");
        System.out.println("해제될 룸 아이디: " + roomId + " 해제될 유저 아이디: " + userId);
        if (roomId != null ) {
            System.out.println("디스커넥트 실행됩니다.");
            Set<String> roomUsers = roomUserMap.get(roomId);
            roomUsers.remove(userId);
            System.out.println("다음 방에서 제거: " + roomId);
            System.out.println("User " + userId + " removed from room " + roomId);          
        } else {
            System.out.println("roomId or userId is null during disconnect.");
        }
    }

    public Set<String> getActiveUsersInRoom(String roomId) {
        return roomUserMap.getOrDefault(roomId, ConcurrentHashMap.newKeySet());
    }

}
