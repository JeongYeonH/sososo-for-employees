package com.han_batang.backconsumer.back_consumer.service.implement;

import org.springframework.stereotype.Service;

import com.han_batang.backconsumer.back_consumer.entity.MessageEntity;
import com.han_batang.backconsumer.back_consumer.repository.MessageRepository;
import com.han_batang.backconsumer.back_consumer.service.ChatService;
import com.han_batang.backconsumer.dto.event.ChatMessageEvent;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImplement implements ChatService{
    
    private final MessageRepository messageRepository;

    @Override
    public void saveMessage(@NonNull ChatMessageEvent event) {      
        MessageEntity messageEntity = new MessageEntity(
            event.getChatGeneratedId(),
            event.getContent(),
            event.getSentTime()
        );
        
        messageRepository.save(messageEntity);
    }
}
