package com.han_batang.back.dto.response.user;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificationDto {
    private Integer notificationId;
    private String roomName;
    private String notificationContent;
    private LocalDateTime notificationSentTime;
    private Integer stacks;
    private String route;
}
