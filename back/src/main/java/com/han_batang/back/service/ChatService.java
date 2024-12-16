package com.han_batang.back.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.han_batang.back.dto.response.chat.ChatRoomCreationResponseDto;
import com.han_batang.back.dto.response.chat.ShowChatRoomListResponseDto;
import com.han_batang.back.dto.response.chat.ShowMessageListResponseDto;
import com.han_batang.back.entity.ChatRoomEntity;
import com.han_batang.back.entity.JoinChatEntity;
import com.han_batang.back.entity.MessageEntity;
import com.han_batang.back.entity.NotificationEntity;
import com.han_batang.back.entity.UserEntity;

public interface ChatService {   
    ChatRoomCreationResponseDto createChatRoom(UserEntity userEntity, String clubTitle, String userId, Boolean isForInvitation);
    ResponseEntity<? super ShowChatRoomListResponseDto> showChatRoomByUser(String userId);
    ResponseEntity<?> getJoinChatRoomByUser(String userId, Integer roomId);
    JoinChatEntity getJoinChatByGeneratedId(Integer chatGeneratedId);
    JoinChatEntity getRecieverJoinChatEntity(UserEntity roomMember, ChatRoomEntity room);
    void saveMessage(MessageEntity messageEntity);
    void saveNotification(NotificationEntity notificationEntity);
    ResponseEntity<? super ShowMessageListResponseDto> showMessageListByRoom(Integer roomId);
    ResponseEntity<?> joinChatInvitation(String userId, String roomName);
    ResponseEntity<?> joinChatRoom(String joinerUserId, Integer clubId);
    List<UserEntity> findAllUsersByRoomId(Integer roomId);
    ChatRoomEntity getChatRoomByRoomId(Integer roomId);
    Optional<NotificationEntity> getAlreadyStackedNotification(JoinChatEntity joinChatEntity);
    ResponseEntity<?> readMessage(Integer notificationId);
}
