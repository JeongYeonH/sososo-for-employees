package com.han_batang.back.service;

import org.springframework.http.ResponseEntity;

import com.han_batang.back.dto.response.user.ShowUserInfoListResponseDto;
import com.han_batang.back.dto.response.user.ShowUserInfoResponseDto;
import com.han_batang.back.dto.request.club.CreateClubRequestDto;
import com.han_batang.back.dto.request.club.JoinClubRequestDto;
import com.han_batang.back.dto.response.club.CreateClubResponseDto;
import com.han_batang.back.dto.response.club.JoinClubResponseDto;
import com.han_batang.back.dto.response.club.ShowClubDetailResponseDto;
import com.han_batang.back.dto.response.club.ShowClubListResponseDto;

public interface ClubService {
    ResponseEntity<? super CreateClubResponseDto> createClub(CreateClubRequestDto dto, String userId);
    ResponseEntity<? super JoinClubResponseDto> joinClub(JoinClubRequestDto dto, String userId);
    ShowClubListResponseDto showClubList(int page, int size, String type);
    ShowClubListResponseDto showClubListByCategory(int page, int size, String type, String category);
    ResponseEntity<? super ShowClubDetailResponseDto> showClubByClubId(Integer clubId);
    ResponseEntity<? super ShowUserInfoResponseDto> showHostUserInfoByClubId(Integer clubId);
    ResponseEntity<? super ShowUserInfoListResponseDto> showAllUserInfoByClubId(Integer clubId);
    ResponseEntity<? super ShowClubListResponseDto> searchClub(int page, int size, String keyword);
} 