package com.han_batang.back.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "chat_generated_id")
    JoinChatEntity joinChatEntity;

    @Column(name = "messages_text")
    private String messagesText;

    @Column(name = "messages_sent_time")
    private LocalDateTime messageSentTime;

    public MessageEntity(JoinChatEntity joinChatEntity, String messageText, LocalDateTime messageSentTime) {
        this.joinChatEntity = joinChatEntity;
        this.messagesText = messageText;
        this.messageSentTime = messageSentTime;
    }

    public Integer getChatGeneretedId(){
        return this.joinChatEntity !=null ? this.joinChatEntity.getChatGeneratedId() : null;
    }

}
