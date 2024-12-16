package com.han_batang.back.controller;

import com.han_batang.back.dto.response.user.CreateUserInfoResponseDto;
import com.han_batang.back.dto.response.user.ShowUserInfoResponseDto;
import com.han_batang.back.dto.response.user.ShowNotificationListResponseDto;
import com.han_batang.back.dto.response.club.ShowClubListResponseDto;
import com.han_batang.back.service.UserService;

import lombok.RequiredArgsConstructor;

import com.han_batang.back.dto.request.user.CreateUserInfoRequestDto;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create-user-info")
    public ResponseEntity<? super CreateUserInfoResponseDto> createUserInfo(
        @RequestParam("nickName") String nickName,
        @RequestParam("age") Integer age,
        @RequestParam("shortIntro") String shortIntro,
        @RequestParam("userThumbnail") MultipartFile userThumbnail
    ){
        

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        
        CreateUserInfoRequestDto requestBody = new CreateUserInfoRequestDto(nickName, age, shortIntro, userThumbnail);        
        ResponseEntity<? super CreateUserInfoResponseDto> response = userService.createUserInfo(requestBody, userId);

        return response;
    }
    
    @GetMapping("/show-user-info")
    public ResponseEntity<? super ShowUserInfoResponseDto> showUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        ResponseEntity<? super ShowUserInfoResponseDto> response = userService.showUserInfo(userId);

        return response;
    }

    @GetMapping("/show-club-list-owned")
    public ResponseEntity<? super ShowClubListResponseDto> showClubByOwner(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        ResponseEntity<? super ShowClubListResponseDto> response = userService.showClubByOwner(userId);

        return response;
    }

    @GetMapping("/show-club-list-joined")
    public ResponseEntity<? super ShowClubListResponseDto> showClubByJoined(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        ResponseEntity<? super ShowClubListResponseDto> response = userService.showClubByJoined(userId);
        
        return response;
    }

    @GetMapping("/show-notification-list")
    public ResponseEntity<? super ShowNotificationListResponseDto> showNotificationsByOwner(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        ResponseEntity<? super ShowNotificationListResponseDto> response = userService.showNotificationsByOwner(userId);
        
        return response;
    }
}
