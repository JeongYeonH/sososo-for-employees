package com.han_batang.back.dto.event;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    private String roomId;
    private Integer chatGeneratedId;
    private String userId;
    private String notificationContent;
    private LocalDateTime notificationSentTime; 
}
