package com.han_batang.back.dto.response.chat;


import java.util.List;

import com.han_batang.back.dto.response.ResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShowChatRoomListResponseDto extends ResponseDto{

    private List<ChatRoomDto> chatRoomList;

    public ShowChatRoomListResponseDto(List<ChatRoomDto> dtoList) {
        super();
        this.chatRoomList = dtoList;
    }
}

