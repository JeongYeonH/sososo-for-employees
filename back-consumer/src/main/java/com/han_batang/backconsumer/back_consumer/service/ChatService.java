package com.han_batang.backconsumer.back_consumer.service;

import com.han_batang.backconsumer.dto.event.ChatMessageEvent;
import com.han_batang.backconsumer.dto.event.NotificationEvent;

import lombok.NonNull;

public interface ChatService {
    void saveMessage(@NonNull ChatMessageEvent event);
    void saveNotification(@NonNull NotificationEvent event);    
}
