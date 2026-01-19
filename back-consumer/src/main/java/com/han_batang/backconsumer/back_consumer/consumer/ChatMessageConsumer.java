package com.han_batang.backconsumer.back_consumer.consumer;

import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.han_batang.backconsumer.back_consumer.service.ChatService;
import com.han_batang.backconsumer.dto.event.ChatMessageEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatMessageConsumer {
    private final ChatService chatService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "chat-message",
        groupId = "new-test-group-tst",
        properties = {
            "key.deserializer=org.apache.kafka.common.serialization.StringDeserializer",
            "value.deserializer=org.apache.kafka.common.serialization.StringDeserializer"
        }
    )
    public void onMessage(String data){
        try{
            ChatMessageEvent event = objectMapper.readValue(data, ChatMessageEvent.class);
            chatService.saveMessage(event);
        }catch(Exception e){
            System.err.println("데이터 변환 에러 발생: " + e.getMessage());
        }
    }
}
