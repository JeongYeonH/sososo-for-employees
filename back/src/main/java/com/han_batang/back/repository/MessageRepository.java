package com.han_batang.back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.han_batang.back.entity.JoinChatEntity;
import com.han_batang.back.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer>{
    
    @Query(value = """
            select m 
            from message m
            join join_chat jc ON jc.chatGeneratedId = m.joinChatEntity.chatGeneratedId
            WHERE jc.chatGeneratedId IN :chatGeneratedIds
            ORDER BY m.messageSentTime ASC
            """)
    List<MessageEntity> findMessageList(@Param("chatGeneratedIds")List<Integer> chatGeneratedIds);
    
    
    @Query(value = """
            select m
            from message m
            join join_chat jc ON jc.chatGeneratedId = m.joinChatEntity.chatGeneratedId
            WHERE jc IN :joinChatEntities 
            ORDER BY m.messageSentTime DESC
            FETCH FIRST 1 ROWS ONLY
            """)
    Optional<MessageEntity> findFirstMessage(@Param("joinChatEntities") List<JoinChatEntity> joinChatEntities);
}
