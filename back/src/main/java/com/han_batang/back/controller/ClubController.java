package com.han_batang.back.controller;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.han_batang.back.dto.request.club.CreateClubRequestDto;
import com.han_batang.back.dto.request.club.JoinClubRequestDto;
import com.han_batang.back.dto.response.club.CreateClubResponseDto;
import com.han_batang.back.dto.response.club.JoinClubResponseDto;
import com.han_batang.back.dto.response.club.ShowClubListResponseDto;
import com.han_batang.back.dto.response.club.ShowClubDetailResponseDto;
import com.han_batang.back.dto.response.user.ShowUserInfoResponseDto;
import com.han_batang.back.dto.response.user.ShowUserInfoListResponseDto;
import com.han_batang.back.service.ClubService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;

    @PostMapping("/api/v1/user/post-club")
    public ResponseEntity<? super CreateClubResponseDto> createClub(
        @RequestParam("club_title") String clubTitle,
        @RequestParam("club_thumbnail") MultipartFile clubThumbnail,
        @RequestParam("club_short_intro") String clubShortIntro,
        @RequestParam("club_member_requirements") String clubMemberRequirements,
        @RequestParam("club_activity_description") String clubActivityDescription,
        @RequestParam("club_current_member_num") Integer clubCurrentMemberNum,
        @RequestParam("club_page_visited_num") Integer clubPageVisitedNum,

        @RequestParam("club_type") String selectedType,
        @RequestParam("club_location") String selectedLocation,
        @RequestParam("club_total_num") Integer selectedMaxNumber
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        System.out.println("Authenticated user ID: " + userId);

        CreateClubRequestDto requestBody 
        = new CreateClubRequestDto(
            clubTitle, 
            clubThumbnail, 
            clubShortIntro, 
            clubMemberRequirements, 
            clubActivityDescription, 
            clubCurrentMemberNum, 
            clubPageVisitedNum,

            selectedType,
            selectedLocation,
            selectedMaxNumber
        );
        ResponseEntity<? super CreateClubResponseDto> response = clubService.createClub(requestBody, userId);
        return response;
    }
    
    @PostMapping("/api/v1/user/join-club")
    public ResponseEntity<? super JoinClubResponseDto> joinClub(
        @RequestBody @Valid JoinClubRequestDto requestBody
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        ResponseEntity<? super JoinClubResponseDto> response = clubService.joinClub(requestBody, userId);
        return response;
    }

    @GetMapping("/api/v1/response/show-club-list")
    public ShowClubListResponseDto showClubList(
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam(required = false) String type
    ){
        ShowClubListResponseDto response = clubService.showClubList(page, size, type);
        return response;
    }

    @GetMapping("/api/v1/response/show-club-list-category")
    public ShowClubListResponseDto showClubListByCategory(
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String category
    ){
        System.out.println("타입은 무엇일까? :" + type);
        ShowClubListResponseDto response = clubService.showClubListByCategory(page, size, type, category);
        return response;
    }
    
    @GetMapping("/api/v1/response/search-club")
    public ResponseEntity<? super ShowClubListResponseDto> searchClub(
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam String keyword
    ){
        ResponseEntity<? super ShowClubListResponseDto> response = clubService.searchClub(page, size, keyword);
        return response;
    }

    @GetMapping("/api/v1/response/show-club/{clubId}")
    public ShowClubDetailResponseDto showClubByClubId(
        @PathVariable Integer clubId
    ){
        ShowClubDetailResponseDto response = clubService.showClubByClubId(clubId);
        return response;
    }
    
    @GetMapping("/api/v1/response/show-host-user/{clubId}")
    public ResponseEntity<? super ShowUserInfoResponseDto> showHostUserInfoByClubId(
        @PathVariable Integer clubId
    ){
        ResponseEntity<? super ShowUserInfoResponseDto> response = clubService.showHostUserInfoByClubId(clubId);
        return response;
    }

    @GetMapping("/api/v1/response/show-all-user/{clubId}")
    public ResponseEntity<? super ShowUserInfoListResponseDto> showAllUserInfoByClubId(
        @PathVariable Integer clubId
    ){
        ResponseEntity<? super ShowUserInfoListResponseDto> response = clubService.showAllUserInfoByClubId(clubId);
        return response;
    }


}
