package com.han_batang.back.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.han_batang.back.dto.event.NotificationEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationProducer {
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    private static final String TOPIC = "notification";

    public void sendNotification(NotificationEvent event){
        System.out.println("notification 프로듀서 실행, event: " + event);
        kafkaTemplate.send(TOPIC, event.getRoomId(), event);
    }
}
