package com.han_batang.back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.han_batang.back.entity.ChatRoomEntity;
import com.han_batang.back.entity.JoinChatEntity;
import com.han_batang.back.entity.UserEntity;

@Repository
public interface JoinChatRepository extends JpaRepository<JoinChatEntity, Integer>{
    List<JoinChatEntity> findByUserEntityUserId(String userId);
    
    @Query(value = """
            select jc
            from join_chat jc
            JOIN jc.chatRoomEntity cr 
            JOIN jc.userEntity u
            WHERE cr = :chatRoomEntity
              AND u = :userEntity
            """)
    Optional<JoinChatEntity> findByChatRoomNUser(
        @Param("chatRoomEntity") Optional<ChatRoomEntity> chatRoomEntity, 
        @Param("userEntity") UserEntity userEntity
    );
    
    Optional<JoinChatEntity> findByChatRoomEntityInAndUserEntity(List<ChatRoomEntity> chatRoomEntity, UserEntity userEntity);
    JoinChatEntity findByChatGeneratedId(Integer generatedId);
    List<JoinChatEntity> findByChatRoomEntityChatRoomId(Integer chatRoomId);
    JoinChatEntity findByChatRoomEntityAndUserEntity(ChatRoomEntity chatRoomEntity, UserEntity userEntity);
}
