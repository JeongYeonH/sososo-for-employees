package com.han_batang.back.service;

import org.springframework.http.ResponseEntity;

import com.han_batang.back.dto.request.user.CreateUserInfoRequestDto;
import com.han_batang.back.dto.response.club.ShowClubListResponseDto;
import com.han_batang.back.dto.response.user.CreateUserInfoResponseDto;
import com.han_batang.back.dto.response.user.ShowNotificationListResponseDto;
import com.han_batang.back.dto.response.user.ShowUserInfoResponseDto;


public interface UserService {

    ResponseEntity<? super CreateUserInfoResponseDto> createUserInfo(CreateUserInfoRequestDto requestBody, String userId);
    ResponseEntity<? super ShowUserInfoResponseDto> showUserInfo(String userId);
    ResponseEntity<? super ShowClubListResponseDto> showClubByOwner(String userId);
    ResponseEntity<? super ShowClubListResponseDto> showClubByJoined(String userId);
    ResponseEntity<? super ShowNotificationListResponseDto> showNotificationsByOwner(String userId);
    
}
