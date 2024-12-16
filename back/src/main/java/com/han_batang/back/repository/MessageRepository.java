package com.han_batang.back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.han_batang.back.entity.JoinChatEntity;
import com.han_batang.back.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer>{
    List<MessageEntity> findByJoinChatEntityChatGeneratedIdIn(List<Integer> chatGeneratedIds);
    Optional<MessageEntity> findFirstByJoinChatEntityInOrderByMessageSentTimeDesc(List<JoinChatEntity> joinChatEntities);
}
