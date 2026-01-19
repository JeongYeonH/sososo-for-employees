package com.han_batang.back.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.han_batang.back.dto.request.chat.ChatMessageRequestDto;
import com.han_batang.back.dto.response.chat.ShowChatRoomListResponseDto;
import com.han_batang.back.dto.response.chat.ShowMessageListResponseDto;
import com.han_batang.back.entity.ChatRoomEntity;
import com.han_batang.back.entity.JoinChatEntity;
import com.han_batang.back.entity.MessageEntity;
import com.han_batang.back.entity.NotificationEntity;
import com.han_batang.back.entity.UserEntity;
import com.han_batang.back.service.ChatService;
import com.han_batang.back.websocket.RoomUserTracker;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final RoomUserTracker roomUserTracker;

    @MessageMapping("/chat.sendMessage/{roomId}")
    public void sendMessage(ChatMessageRequestDto chatMessage){
        String destination = "/room/" + chatMessage.getRoomId();
        int roomId = Integer.parseInt(chatMessage.getRoomId());
        // JoinChatEntity joinChatEntity 
        // = chatService.getJoinChatByGeneratedId(chatMessage.getChatGeneratedId());
        
        // MessageEntity messageEntity 
        // = new MessageEntity(
        //         joinChatEntity,
        //         chatMessage.getContent(),
        //         chatMessage.getSentTime()
        //     );
        //chatService.saveMessage(messageEntity);
        chatService.sendToKafka(chatMessage);
        
        ChatRoomEntity currentChatRoom = chatService.getChatRoomByRoomId(roomId);
        List<UserEntity> roomMembers = chatService.findAllUsersByRoomId(roomId);
        Set<String> activeUsersInRoom = roomUserTracker.getActiveUsersInRoom(String.valueOf(roomId));

        for(UserEntity roomMember : roomMembers){
            if(roomMember.getUserInfoEntity().getNickName() == chatMessage.getSender()){
                System.out.println("발신자이므로 알람 안함" + roomMember.getUserId());
                continue;
            }
            if(activeUsersInRoom.contains(roomMember.getUserId())){
                System.out.println("다음 접속자에게는 알람 안함" + roomMember.getUserId());
                continue;
            }
            String notice = "메시지가 발송되었습니다.";
            String route = "/room/" + roomId;
            String userDestination = "/room/notifications/" + roomMember.getUserId();
            messagingTemplate.convertAndSend(userDestination, notice);
            
            JoinChatEntity sendTo 
            = chatService.getRecieverJoinChatEntity(roomMember, currentChatRoom);
            Optional<NotificationEntity> isAlreadyStacked = chatService.getAlreadyStackedNotification(sendTo);
            System.out.println("기존 스택된 알람엔터티: " + isAlreadyStacked);
            if(isAlreadyStacked.isPresent()){
                System.out.println("기존 알람에 추가");
                NotificationEntity notificationEntity = isAlreadyStacked.get();
                notificationEntity.setNotificationContent(chatMessage.getContent());
                notificationEntity.setNotificationSentTime(chatMessage.getSentTime());
                notificationEntity.setStacks(notificationEntity.getStacks() + 1);
                chatService.saveNotification(notificationEntity);
            }else{
                System.out.println("새로운 알람");
                NotificationEntity notificationEntity
                = new NotificationEntity(
                    null,
                    sendTo,
                    chatMessage.getContent(),
                    chatMessage.getSentTime(),
                    false,
                    1,
                    route
                );
                chatService.saveNotification(notificationEntity);
            }
        }      
        messagingTemplate.convertAndSend(destination, chatMessage);
    }

    @GetMapping("/api/v1/user/show-chat-room")
    public ResponseEntity<? super ShowChatRoomListResponseDto> showChatRoomByUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        ResponseEntity<? super ShowChatRoomListResponseDto> response = chatService.showChatRoomByUser(userId);
        return response;
    }

    @GetMapping("/api/v1/user/get-join-chat/{roomId}")
    public ResponseEntity<?> getJoinChatRoomByUser(
        @PathVariable Integer roomId
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        ResponseEntity<?> response = chatService.getJoinChatRoomByUser(userId, roomId);
        return response;
    }

    @GetMapping("/api/v1/user/show-message-list/{roomId}")
    public ResponseEntity<? super ShowMessageListResponseDto> showMessageListByRoom(
        @PathVariable Integer roomId
    ){
        ResponseEntity<? super ShowMessageListResponseDto> response = chatService.showMessageListByRoom(roomId);
        return response;
    }

    @GetMapping("/api/v1/user/chat/join-chat-invitation/{roomName}")
    public ResponseEntity<?> joinChatInvitation(
        @PathVariable String roomName
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        ResponseEntity<?> response = chatService.joinChatInvitation(userId, roomName);
        return response;
    }

    @PostMapping("/api/v1/response/message-read/{notificationId}")
    public ResponseEntity<?> readMessage(
        @PathVariable Integer notificationId
    ){
        ResponseEntity<?> response = chatService.readMessage(notificationId);
        return response;
    }
}
