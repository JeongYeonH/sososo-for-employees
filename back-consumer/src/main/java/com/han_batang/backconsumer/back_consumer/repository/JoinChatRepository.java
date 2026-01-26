package com.han_batang.backconsumer.back_consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.han_batang.backconsumer.back_consumer.entity.JoinChatEntity;

public interface JoinChatRepository extends JpaRepository<JoinChatEntity, Integer>{    
    @Query(value = """
    SELECT * FROM join_chat
    WHERE chat_room_id = :chatRoomId
    AND user_id = :userId
    """, nativeQuery = true)
    JoinChatEntity findByChatRoomIdAndUserId(@Param("chatRoomId") Integer chatRoomId, @Param("userId") String userId);
}
