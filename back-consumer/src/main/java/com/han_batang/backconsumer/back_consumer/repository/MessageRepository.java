package com.han_batang.backconsumer.back_consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.han_batang.backconsumer.back_consumer.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer>{
    
}
