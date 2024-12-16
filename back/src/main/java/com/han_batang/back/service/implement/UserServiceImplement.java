package com.han_batang.back.service.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.han_batang.back.dto.request.user.CreateUserInfoRequestDto;
import com.han_batang.back.dto.response.club.ClubDto;
import com.han_batang.back.dto.response.club.ShowClubListResponseDto;
import com.han_batang.back.dto.response.user.CreateUserInfoResponseDto;
import com.han_batang.back.dto.response.user.NotificationDto;
import com.han_batang.back.dto.response.user.ShowNotificationListResponseDto;
import com.han_batang.back.dto.response.user.ShowUserInfoResponseDto;
import com.han_batang.back.entity.UserEntity;
import com.han_batang.back.entity.UserInfoEntity;
import com.han_batang.back.entity.ChatRoomEntity;
import com.han_batang.back.entity.ClubEntity;
import com.han_batang.back.entity.ClubInfoEntity;
import com.han_batang.back.entity.ClubMemberEntity;
import com.han_batang.back.entity.JoinChatEntity;
import com.han_batang.back.entity.NotificationEntity;
import com.han_batang.back.repository.ClubInfoRepository;
import com.han_batang.back.repository.ClubMemberRepository;
import com.han_batang.back.repository.JoinChatRepository;
import com.han_batang.back.repository.NotificationRepository;
import com.han_batang.back.repository.UserInfoRepository;
import com.han_batang.back.repository.UserRepository;
import com.han_batang.back.service.S3Service;
import com.han_batang.back.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService{

    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubInfoRepository clubInfoRepository;
    private final JoinChatRepository joinChatRepository;
    private final NotificationRepository notificationRepository;

    

    @Override
    @Transactional
    public ResponseEntity<? super CreateUserInfoResponseDto> createUserInfo(CreateUserInfoRequestDto dto, String userId) {
        
        try{
            MultipartFile thumbnail = dto.getUserThumbnail();
            String fileUrl = "임의 지정";
            if(thumbnail !=null && !thumbnail.isEmpty()){
                fileUrl = s3Service.uploadFile(thumbnail);
            }

            UserEntity userEntity = userRepository.findByUserId(userId);
            UserInfoEntity userInfoEntity 
            = new UserInfoEntity(
                dto,
                userEntity,
                fileUrl
            );

            userInfoRepository.save(userInfoEntity);

        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }

        return CreateUserInfoResponseDto.success();
    }

    @Override
    public ResponseEntity<? super ShowUserInfoResponseDto> showUserInfo(String userId) {
        
        try{
            Optional<UserInfoEntity> userInfoEntity = userInfoRepository.findByUserEntity_UserId(userId);
            UserEntity userEntity = userRepository.findByUserId(userId);
            if(userInfoEntity.isPresent()){
                ShowUserInfoResponseDto responseDto = new ShowUserInfoResponseDto(userEntity, userInfoEntity.get());
                return ResponseEntity.ok(responseDto);
            }else{
                return ShowUserInfoResponseDto.noData();
            }
            

        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public ResponseEntity<? super ShowClubListResponseDto> showClubByOwner(String userId) {
        try{
            List<ClubMemberEntity> clubMemberEntity = clubMemberRepository.findByUserEntityUserId(userId);
            List<ClubEntity> clubEntities = clubMemberEntity.stream()
                .filter(clubMember -> clubMember.isAdmin()== true)
                .map(ClubMemberEntity::getClubEntity)
                .collect(Collectors.toList());
            
            List<ClubDto> clubDtos = new ArrayList<>();
            
            for(ClubEntity clubEntity : clubEntities){
                Integer clubId = clubEntity.getClubId();
                Optional<ClubInfoEntity> clubInfoEntity 
                = clubInfoRepository.findByClubEntityClubId(clubId);
                ClubDto clubDto = new ClubDto(clubEntity, clubInfoEntity);
                clubDtos.add(clubDto);
            }
            
            ShowClubListResponseDto responseDto = new ShowClubListResponseDto(clubDtos, false);
            return ResponseEntity.ok(responseDto);

        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public ResponseEntity<? super ShowClubListResponseDto> showClubByJoined(String userId) {
        try{
            List<ClubMemberEntity> clubMemberEntity = clubMemberRepository.findByUserEntityUserId(userId);
            
            
            List<ClubEntity> clubEntities = clubMemberEntity.stream()
                .filter(clubMember -> clubMember.isAdmin()== false)
                .map(ClubMemberEntity::getClubEntity)
                .collect(Collectors.toList());

            List<ClubDto> clubDtos = new ArrayList<>();

            for(ClubEntity clubEntity : clubEntities){
                Integer clubId = clubEntity.getClubId();
                Optional<ClubInfoEntity> clubInfoEntity 
                = clubInfoRepository.findByClubEntityClubId(clubId);
                ClubDto clubDto = new ClubDto(clubEntity, clubInfoEntity);
                clubDtos.add(clubDto);
            }
            
            ShowClubListResponseDto responseDto = new ShowClubListResponseDto(clubDtos, false);
            return ResponseEntity.ok(responseDto);

        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public ResponseEntity<? super ShowNotificationListResponseDto> showNotificationsByOwner(String userId) {
        try{
            List<JoinChatEntity> ownerJoinChatEntities = joinChatRepository.findByUserEntityUserId(userId);
            List<NotificationEntity> ownerNotificationEntities 
            = notificationRepository.findByJoinChatEntityIn(ownerJoinChatEntities);
            List<NotificationDto> notificationDtos = new ArrayList<>();
            for(NotificationEntity notificationEntity : ownerNotificationEntities){
                if(notificationEntity.getIsRead() == false){
                    JoinChatEntity targetJoinChatEntity = notificationEntity.getJoinChatEntity();
                    ChatRoomEntity tagetRoomEntity = targetJoinChatEntity.getChatRoomEntity();
                     
                    NotificationDto notificationDto = new NotificationDto(
                        notificationEntity.getNotificationId(),
                        tagetRoomEntity.getChatRoomName(),
                        notificationEntity.getNotificationContent(),
                        notificationEntity.getNotificationSentTime(),
                        notificationEntity.getStacks(),
                        notificationEntity.getRoute()
                    );
                    notificationDtos.add(notificationDto);
                }
            }
            ShowNotificationListResponseDto responseDto = new ShowNotificationListResponseDto(notificationDtos);
            return ResponseEntity.ok(responseDto);       
        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
    
}
