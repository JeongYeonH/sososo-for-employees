package com.han_batang.back.service.implement;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import com.han_batang.back.dto.request.club.CreateClubRequestDto;
import com.han_batang.back.dto.request.club.JoinClubRequestDto;

import com.han_batang.back.dto.response.club.ClubDto;
import com.han_batang.back.dto.response.club.CreateClubResponseDto;
import com.han_batang.back.dto.response.club.JoinClubResponseDto;
import com.han_batang.back.dto.response.club.ShowClubDetailResponseDto;
import com.han_batang.back.dto.response.club.ShowClubListResponseDto;
import com.han_batang.back.dto.response.user.ShowUserInfoListResponseDto;
import com.han_batang.back.dto.response.user.ShowUserInfoResponseDto;
import com.han_batang.back.entity.ClubEntity;
import com.han_batang.back.entity.ClubInfoEntity;
import com.han_batang.back.entity.ClubMemberEntity;
import com.han_batang.back.entity.UserEntity;
import com.han_batang.back.entity.UserInfoEntity;
import com.han_batang.back.repository.ClubInfoRepository;
import com.han_batang.back.repository.ClubMemberRepository;
import com.han_batang.back.repository.ClubRepository;
import com.han_batang.back.repository.UserRepository;
import com.han_batang.back.service.ClubService;
import com.han_batang.back.service.S3Service;
import com.han_batang.back.service.ChatService;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClubServiceImplement implements ClubService{

    private final S3Service s3Service;
    private final ChatService chatService;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubInfoRepository clubInfoRepository;

    private static final int TOP_N = 4;
    private static final Duration CACHE_TTL = Duration.ofMinutes(5);

    private final RedisTemplate<String, Object> redisTemplate;
    
    @Override
    @Transactional
    public ResponseEntity<? super CreateClubResponseDto> createClub(CreateClubRequestDto dto, String userId) {       
        
        try{
            MultipartFile thumbnail = dto.getClub_thumbnail();
            String fileUrl = "임의 지정";
            String clubTitle = dto.getClub_title();
            if(thumbnail !=null && !thumbnail.isEmpty()){
                fileUrl = s3Service.uploadFile(thumbnail);
            }
            ClubEntity clubEntity = new ClubEntity(dto, fileUrl);
            
            ClubEntity savedClub = clubRepository.save(clubEntity);
            
            UserEntity userEntity = userRepository.findByUserId(userId);
            
            ClubInfoEntity clubInfoEntity 
            = new ClubInfoEntity(
                null,
                dto.getSelectedType(),
                dto.getSelectedLocation(),
                dto.getSelectedMaxNumber(),
                savedClub
            );

            ClubMemberEntity clubMemberEntity 
            = new ClubMemberEntity(
                null, 
                clubEntity, 
                userEntity, 
                true, 
                true
            );
            clubMemberEntity.setClubEntity(savedClub);
            clubMemberRepository.save(clubMemberEntity);
            clubInfoRepository.save(clubInfoEntity);
            
            chatService.createChatRoom(userEntity, clubTitle, userId, false);

        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }


        return CreateClubResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<? super JoinClubResponseDto> joinClub(JoinClubRequestDto dto, String userId) {
        
        if(userId.isEmpty()) return JoinClubResponseDto.notLoggedIn();

        Integer clubId = dto.getClubId();

        if(clubId == null){
            return JoinClubResponseDto.notAClubHost();
        }

        Optional<ClubMemberEntity> clubMemberEntity 
        = clubMemberRepository.findByClubIdNUserId(clubId, userId);

        boolean isAdmin = clubMemberEntity.map(ClubMemberEntity::isAdmin).orElse(false);
        System.out.println(isAdmin);
        if(!isAdmin){
            return JoinClubResponseDto.notAClubHost();
        } 
                    
        String joinerUserId = dto.getJoinerUserId();
        Optional<ClubMemberEntity> existingClubMemberEntity 
        = clubMemberRepository.findByClubIdNUserId(clubId, joinerUserId);
        if(existingClubMemberEntity.isPresent()) return JoinClubResponseDto.alreayExist();

        UserEntity userEntity = userRepository.findByUserId(joinerUserId);
        Optional<ClubEntity> clubEntityOptional = clubRepository.findById(clubId);
        ClubEntity clubEntity = clubEntityOptional.orElseThrow();
        
        Integer clubMemeberNum = clubEntity.getClub_current_member_num();
        clubEntity.setClub_current_member_num(clubMemeberNum + 1);
        clubRepository.save(clubEntity);

        ClubMemberEntity savedClubMemberEntity 
        = new ClubMemberEntity(dto, clubEntity, userEntity);
        clubMemberRepository.save(savedClubMemberEntity);

        chatService.joinChatRoom(joinerUserId, clubId);



        return JoinClubResponseDto.success();
    }

    @Override
    @Cacheable(
    value = "club:list", 
    key = "#page + ':' + #size + ':' + #type",
    unless = "#result == null")
    public ShowClubListResponseDto showClubList(int page, int size, String type) {        
        Pageable pageable = null;            
        if("Latest".equals(type)){
            pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        }else if("Popular".equals(type)){
            pageable = PageRequest.of(page, size, Sort.by("clubPageVisitedNum").descending());
        }else{
            return null; 
        }       
        Page<ClubEntity> clubPages = clubRepository.findAll(pageable);          
        List<ClubDto> clubDtos = getClubDtos(clubPages);
        
        ShowClubListResponseDto responseDto = new ShowClubListResponseDto(clubDtos, clubPages.isLast());
        return responseDto;
  
    }

    @Override
    @Transactional
    @Cacheable(
    value = "club:list",
    key = "#page + ':' + #size + ':' + #type + ':' + #category",
    unless = "#result == null"
    )
    public ShowClubListResponseDto showClubListByCategory(int page, int size, String type, String category) {      
        Pageable pageable = null;          
        if("Latest".equals(type)){
            pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        }else if("Popular".equals(type)){
            pageable = PageRequest.of(page, size, Sort.by("clubPageVisitedNum").descending());
        }else{      
            return null;    
        }

        Page<ClubEntity> clubPages = clubRepository.findByClubInfoClubType(category, pageable);
        if(clubPages.isEmpty()){
            return null;
        }

        List<ClubDto> clubDtos = getClubDtos(clubPages);
        ShowClubListResponseDto responseDto = new ShowClubListResponseDto(clubDtos, clubPages.isLast());
        return responseDto;
    }


    @Override
    @Transactional
    public ResponseEntity<? super ShowClubDetailResponseDto> showClubByClubId(@NonNull Integer clubId) {
        Optional<ClubEntity> clubEntity = clubRepository.findById(clubId);
        
        ClubEntity club = clubEntity.get();
        club.increaseVisitedNum();
        clubRepository.save(club);

        Optional<ClubInfoEntity> clubInfoEntity = clubInfoRepository.getClubInfo(clubId);
        ClubDto clubDto = new ClubDto(club, clubInfoEntity);
        ShowClubDetailResponseDto responseDto = new ShowClubDetailResponseDto(clubDto);
        return ResponseEntity.ok(responseDto);       
    }


    @Override
    @Transactional
    public ResponseEntity<? super ShowClubListResponseDto> searchClub(int page, int size, String keyword) {
       try{
            Pageable pageable = PageRequest.of(page, size, Sort.by("created_date").descending()); 
            Page<ClubEntity> clubPages = clubRepository.findClubs(keyword, pageable);
            
            if(clubPages.isEmpty()){
                return ShowClubListResponseDto.noData();
            }

            List<ClubDto> clubDtos = getClubDtos(clubPages);
            ShowClubListResponseDto responseDto = new ShowClubListResponseDto(clubDtos, clubPages.isLast());
            return ResponseEntity.ok(responseDto);

       }catch(Exception exception){
            exception.printStackTrace();
            return null;
       }
    }


    @Override
    public ResponseEntity<? super ShowUserInfoResponseDto> showHostUserInfoByClubId(@NonNull Integer clubId) {
        try{
            Optional<ClubEntity> clubEntity = clubRepository.findById(clubId);
            if(!clubEntity.isPresent()) return ShowUserInfoResponseDto.noData();

            Optional<ClubMemberEntity> hostClubMemberEntity 
            = clubMemberRepository.findByClubEntityAndIsAdmin(clubEntity, true);
            if(!hostClubMemberEntity.isPresent()) return ShowUserInfoResponseDto.noData();

            UserEntity hostUserEntity = hostClubMemberEntity.get().getUserEntity();
            UserInfoEntity hostUserInfoEntity = hostUserEntity.getUserInfoEntity();

            ShowUserInfoResponseDto responseDto = new ShowUserInfoResponseDto(hostUserEntity, hostUserInfoEntity);
            return ResponseEntity.ok(responseDto);
            
        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
   
    }


    @Override
    public ResponseEntity<? super ShowUserInfoListResponseDto> showAllUserInfoByClubId(Integer clubId) {
        try{
            List<ClubMemberEntity> allClubMemberEntites
            = clubMemberRepository.findByClubEntityClubId(clubId);
            if(allClubMemberEntites.isEmpty()) return ShowUserInfoListResponseDto.noData();

            List<ShowUserInfoResponseDto> userInfoDtos = new ArrayList<>();       
            allClubMemberEntites.stream()
                .forEach(clubMemeber -> {
                    UserInfoEntity userInfoEntity = clubMemeber.getUserEntity().getUserInfoEntity();
                    UserEntity userEntity = clubMemeber.getUserEntity();
                    ShowUserInfoResponseDto userInfoDto = new ShowUserInfoResponseDto(userEntity, userInfoEntity);
                    userInfoDtos.add(userInfoDto);
                });
            
            ShowUserInfoListResponseDto userInfoListDtos = new ShowUserInfoListResponseDto(userInfoDtos);
            return ResponseEntity.ok(userInfoListDtos);

        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    public List<ClubDto> getClubDtos(Page<ClubEntity> clubPages) {
        List<ClubDto> clubDtos = new ArrayList<>();
        
        for(ClubEntity clubEntity : clubPages){
            Integer clubId = clubEntity.getClubId();
            Optional<ClubInfoEntity> clubInfoEntity 
            = clubInfoRepository.getClubInfo(clubId);
            ClubDto clubDto = new ClubDto(clubEntity, clubInfoEntity);
            clubDtos.add(clubDto);
        }
        return clubDtos;
    } 
}
