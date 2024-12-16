package com.han_batang.back.dto.request.chat;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageRequestDto {
    private String roomId;
    private String sender;
    private String content;
    private LocalDateTime sentTime;
    private String profileImgUrl;
    private Integer chatGeneratedId;
}
