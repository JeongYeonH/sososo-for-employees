package com.han_batang.back.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.han_batang.back.dto.event.ChatMessageEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatMessageProducer {
    private final KafkaTemplate<String, ChatMessageEvent> kafkaTemplate;
    
    private static final String TOPIC = "chat-message";

    public void sendMessage(ChatMessageEvent event){
        kafkaTemplate.send(TOPIC, event.getRoomId(), event);
    }
}
