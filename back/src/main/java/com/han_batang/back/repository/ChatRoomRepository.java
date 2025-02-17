package com.han_batang.back.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.han_batang.back.entity.ChatRoomEntity;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Integer>{
    
    @Query(value = """
            SELECT ch
            FROM chat_room ch
            WHERE ch.chatRoomName IN :chatRoomName
            """)
    List<ChatRoomEntity> getChatRoom(@Param("chatRoomName")String chatRoomName);
    ChatRoomEntity findByChatRoomId(Integer chatRoomId);
} 
