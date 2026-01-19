package com.han_batang.backconsumer.back_consumer.service;

import com.han_batang.backconsumer.dto.event.ChatMessageEvent;

import lombok.NonNull;

public interface ChatService {
    void saveMessage(@NonNull ChatMessageEvent event);    
}
