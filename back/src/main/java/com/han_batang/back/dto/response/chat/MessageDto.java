package com.han_batang.back.dto.response.chat;

import java.time.LocalDateTime;

import com.han_batang.back.entity.JoinChatEntity;
import com.han_batang.back.entity.MessageEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {

    private String sender;
    private String profileImgUrl;
    private LocalDateTime sentTime;
    private String content;

    public MessageDto(MessageEntity messageEntity, JoinChatEntity joinChatEntity ) {
        this.sender = joinChatEntity.getSender();
        this.profileImgUrl =  joinChatEntity.getSenderThumbnailUrl();
        this.sentTime = messageEntity.getMessageSentTime();
        this.content = messageEntity.getMessagesText();   
    }
}
