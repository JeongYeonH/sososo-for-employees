package com.han_batang.back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.han_batang.back.entity.ChatRoomEntity;
import com.han_batang.back.entity.JoinChatEntity;
import com.han_batang.back.entity.UserEntity;

@Repository
public interface JoinChatRepository extends JpaRepository<JoinChatEntity, Integer>{
    List<JoinChatEntity> findByUserEntityUserId(String userId);
    
    
    Optional<JoinChatEntity> findByChatRoomEntityAndUserEntity(Optional<ChatRoomEntity> chatRoomEntity, UserEntity userEntity);
    Optional<JoinChatEntity> findByChatRoomEntityInAndUserEntity(List<ChatRoomEntity> chatRoomEntity, UserEntity userEntity);
    JoinChatEntity findByChatGeneratedId(Integer generatedId);
    List<JoinChatEntity> findByChatRoomEntityChatRoomId(Integer chatRoomId);
    JoinChatEntity findByChatRoomEntityAndUserEntity(ChatRoomEntity chatRoomEntity, UserEntity userEntity);
}
