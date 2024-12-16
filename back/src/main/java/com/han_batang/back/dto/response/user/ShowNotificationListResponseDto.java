package com.han_batang.back.dto.response.user;

import java.util.List;

import com.han_batang.back.dto.response.ResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShowNotificationListResponseDto extends ResponseDto{
    
    private List<NotificationDto> notificationList;

    public ShowNotificationListResponseDto(List<NotificationDto> dtoList){
        this.notificationList = dtoList;
    }
}
