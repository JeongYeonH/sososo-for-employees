package com.han_batang.backconsumer.dto.event;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageEvent {
    private String roomId;
    private Integer chatGeneratedId;
    private String sender;
    private String content;
    private LocalDateTime sentTime;  
}
