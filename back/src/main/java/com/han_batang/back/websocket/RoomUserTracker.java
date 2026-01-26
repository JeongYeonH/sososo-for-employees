package com.han_batang.back.websocket;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.han_batang.back.entity.UserEntity;
import com.han_batang.back.provider.JwtProvider;
import com.han_batang.back.service.ChatService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoomUserTracker {

    private final JwtProvider jwtProvider;
    private final ChatService chatService;
    //private final ConcurrentHashMap<String, Set<String>> roomUserMap = new ConcurrentHashMap<>();

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String[] infos = trackConnectorInfo(headerAccessor);
        String userId = infos[0];
        String roomId = infos[1];
        
        if (roomId != null && userId != null) {
            //roomUserMap.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(userId);
            //System.out.println("연결된 룸 아이디: " + roomId + " 연결된 유저 아이디: " + userId);
            //System.out.println(roomUserMap);
            redisTemplate.opsForSet().add("room:" + roomId + ":active", userId);
        } else {
            System.out.println("사용자 인증 실패");
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());        
        String[] infos = trackConnectorInfo(headerAccessor);
        String userId = infos[0];
        String roomId = infos[1];    
        //System.out.println("디스커넥트 리스너 실행.");
        //System.out.println("해제될 룸 아이디: " + roomId + " 해제될 유저 아이디: " + userId);
        if (roomId != null ) {
            //System.out.println("디스커넥트 실행됩니다.");
            //Set<String> roomUsers = roomUserMap.get(roomId);
            //roomUsers.remove(userId);
            //System.out.println("다음 방에서 제거: " + roomId);
            //System.out.println("User " + userId + " removed from room " + roomId); 
            redisTemplate.opsForSet().remove("room:" + roomId + ":active", userId); 
            
            String memberKey = "room:" + roomId + ":members";
            if(Boolean.FALSE.equals(redisTemplate.hasKey(memberKey))){
                List<UserEntity> allMembers = chatService.findAllUsersByRoomId(Integer.parseInt(roomId));
                List<String> allMemberIds = allMembers.stream()
                    .map(UserEntity::getUserId)
                    .collect(Collectors.toList());

                redisTemplate.opsForSet().add(memberKey, allMemberIds.toArray(new String[0]));
                redisTemplate.expire(memberKey, 1, TimeUnit.DAYS);
                System.out.println("redis에 모든 회원 값으로 들어감");
            }
        } else {
            System.out.println("연결 해제 실패");
        }
    }


    // public Set<String> getActiveUsersInRoom(String roomId) {
    //     return roomUserMap.getOrDefault(roomId, ConcurrentHashMap.newKeySet());
    // }


    private String[] trackConnectorInfo(SimpMessageHeaderAccessor headerAccessor){
        String authHeader = Optional.ofNullable(headerAccessor.getSessionAttributes())
            .map(attributes -> attributes.get("Authorization")) 
            .filter(String.class::isInstance) 
            .map(String.class::cast) 
            .orElse(null);
        String roomId = Optional.ofNullable(headerAccessor.getSessionAttributes())
            .map(attributes -> attributes.get("roomId"))
            .filter(String.class::isInstance)
            .map(String.class::cast)
            .orElse(null);

        authHeader = authHeader.substring(7);
        String userId = jwtProvider.validate(authHeader);

        return new String[]{userId, roomId};
    }
}
