package com.han_batang.back.dto.response.chat;

import java.util.List;

import com.han_batang.back.dto.response.ResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShowMessageListResponseDto extends ResponseDto{
    
    private List<MessageDto> messageList;

    public ShowMessageListResponseDto(List<MessageDto> dtoList){
        this.messageList = dtoList;
    }
}
