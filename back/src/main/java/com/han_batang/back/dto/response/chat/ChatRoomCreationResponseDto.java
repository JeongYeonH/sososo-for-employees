package com.han_batang.back.dto.response.chat;

import com.han_batang.back.entity.ChatRoomEntity;
import com.han_batang.back.entity.JoinChatEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomCreationResponseDto {

    private ChatRoomEntity chatRoomEntity;
    private JoinChatEntity joinChatEntity;

    public ChatRoomCreationResponseDto(ChatRoomEntity chatRoomEntity, JoinChatEntity joinChatEntity) {
        this.chatRoomEntity = chatRoomEntity;
        this.joinChatEntity = joinChatEntity;
    }
}
