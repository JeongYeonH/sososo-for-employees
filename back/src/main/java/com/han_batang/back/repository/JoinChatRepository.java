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
    
    @Query(value = """
            SELECT jc
            FROM join_chat jc
            JOIN jc.chatRoomEntity cr 
            JOIN jc.userEntity u
            WHERE cr IN :chatRoomEntities
              AND u = :userEntity
        """)
    Optional<JoinChatEntity> findExistChatRoom(
      @Param("chatRoomEntities") List<ChatRoomEntity> chatRoomEntity, 
      @Param("userEntity") UserEntity userEntity
    );
    
    JoinChatEntity findByChatGeneratedId(Integer generatedId);

    @Query(value = """
        SELECT jc
        FROM join_chat jc
        JOIN jc.chatRoomEntity cr
        WHERE cr.chatRoomId IN :chatRoomId
        """)
    List<JoinChatEntity> getJoinChats(@Param("chatRoomId")Integer chatRoomId);
    JoinChatEntity findByChatRoomEntityAndUserEntity(ChatRoomEntity chatRoomEntity, UserEntity userEntity);
}
