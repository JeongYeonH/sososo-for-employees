package com.han_batang.back.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.han_batang.back.entity.ChatRoomEntity;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Integer>{
    List<ChatRoomEntity> findByChatRoomName(String chatRoomName);
    ChatRoomEntity findByChatRoomId(Integer chatRoomId);
} 
