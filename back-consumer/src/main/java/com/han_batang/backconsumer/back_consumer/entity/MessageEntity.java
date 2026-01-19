package com.han_batang.backconsumer.back_consumer.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "message")
@Table(name = "message")
public class MessageEntity {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "chat_generated_id")
    private Integer chatGeneratedId;

    @Column(name = "messages_text")
    private String messagesText;

    @Column(name = "messages_sent_time")
    private LocalDateTime messageSentTime;

    public MessageEntity(Integer chatGeneratedId, String messageText, LocalDateTime messageSentTime) {
        this.chatGeneratedId = chatGeneratedId;
        this.messagesText = messageText;
        this.messageSentTime = messageSentTime;
    }
}
