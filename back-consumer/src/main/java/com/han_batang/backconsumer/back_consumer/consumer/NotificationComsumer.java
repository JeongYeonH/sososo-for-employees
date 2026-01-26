package com.han_batang.backconsumer.back_consumer.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.han_batang.backconsumer.back_consumer.service.ChatService;
import com.han_batang.backconsumer.dto.event.NotificationEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationComsumer {
    private final ChatService chatService;
    private final ObjectMapper objectMapper;
       
    @KafkaListener(
        topics = "notification",
        groupId = "new-test-group-tst",
        properties = {
            "key.deserializer=org.apache.kafka.common.serialization.StringDeserializer",
            "value.deserializer=org.apache.kafka.common.serialization.StringDeserializer"
        }
    )
    public void onMessage(String data){
        try{
            NotificationEvent event = objectMapper.readValue(data, NotificationEvent.class);
            System.out.println("데이터를 받아옴: " + data);
            chatService.saveNotification(event);
            System.out.println("데이터를 DB에 저장: " + event);
        }catch(Exception e){
            System.err.println("데이터 변환 에러 발생: " + e.getMessage());
        }
    }
}
