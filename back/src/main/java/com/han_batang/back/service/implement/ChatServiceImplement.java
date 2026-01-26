package com.han_batang.back.service.implement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.han_batang.back.dto.event.ChatMessageEvent;
import com.han_batang.back.dto.event.NotificationEvent;
import com.han_batang.back.dto.request.chat.ChatMessageRequestDto;
import com.han_batang.back.dto.response.chat.ChatRoomCreationResponseDto;
import com.han_batang.back.dto.response.chat.ChatRoomDto;
import com.han_batang.back.dto.response.chat.MessageDto;
import com.han_batang.back.dto.response.chat.ShowChatRoomListResponseDto;
import com.han_batang.back.dto.response.chat.ShowMessageListResponseDto;
import com.han_batang.back.dto.response.club.JoinClubResponseDto;
import com.han_batang.back.entity.ChatRoomEntity;
import com.han_batang.back.entity.ClubEntity;
import com.han_batang.back.entity.ClubMemberEntity;
import com.han_batang.back.entity.JoinChatEntity;
import com.han_batang.back.entity.MessageEntity;
import com.han_batang.back.entity.NotificationEntity;
import com.han_batang.back.entity.UserEntity;
import com.han_batang.back.entity.UserInfoEntity;
import com.han_batang.back.producer.ChatMessageProducer;
import com.han_batang.back.producer.NotificationProducer;
import com.han_batang.back.repository.ChatRoomRepository;
import com.han_batang.back.repository.ClubMemberRepository;
import com.han_batang.back.repository.ClubRepository;
import com.han_batang.back.repository.JoinChatRepository;
import com.han_batang.back.repository.MessageRepository;
import com.han_batang.back.repository.NotificationRepository;
import com.han_batang.back.repository.UserInfoRepository;
import com.han_batang.back.repository.UserRepository;
import com.han_batang.back.service.ChatService;


import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImplement implements ChatService{
    
    private final ChatRoomRepository chatRoomRepository;
    private final JoinChatRepository joinChatRepository;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final UserInfoRepository userInfoRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final NotificationRepository notificationRepository;
    
    private final ChatMessageProducer chatMessageProducer;
    private final NotificationProducer notificationProducer;

    @Override
    public ChatRoomCreationResponseDto createChatRoom(UserEntity userEntity, String clubTitle, String userId, Boolean isForInvitation){
        ChatRoomEntity chatRoomEntity
        = new ChatRoomEntity(
            null, 
            clubTitle,
            LocalDateTime.now(),
            true,
            isForInvitation
        );
        chatRoomRepository.save(chatRoomEntity);

        Optional<UserInfoEntity> userInfoEntity = userInfoRepository.getUserInfo(userId);
        JoinChatEntity joinChatEntity
        = new JoinChatEntity(
            null, 
            chatRoomEntity, 
            userEntity, 
            true,
            userInfoEntity.get().getNickName(),
            userInfoEntity.get().getUserThumbnailUrl()
        );

        joinChatRepository.save(joinChatEntity);

        return new ChatRoomCreationResponseDto(chatRoomEntity, joinChatEntity);

    }

    @Override
    public ChatRoomEntity getChatRoomByRoomId(Integer roomId){        
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findByChatRoomId(roomId);
        return chatRoomEntity;     
    }

    @Override
    public JoinChatEntity getRecieverJoinChatEntity(UserEntity roomMember, ChatRoomEntity room){
        JoinChatEntity recieverJoinChat 
        = joinChatRepository.findByChatRoomEntityAndUserEntity(room, roomMember);

        return recieverJoinChat;
    }

    @Override
    public void saveNotification(@NonNull NotificationEntity notificationEntity) {       
        notificationRepository.save(notificationEntity);
    }

 

    @Override
    public ResponseEntity<? super ShowChatRoomListResponseDto> showChatRoomByUser(String userId) {
        try{
            List<JoinChatEntity> joinChatEntity = joinChatRepository.findByUserEntityUserId(userId);
            List<ChatRoomEntity> chatRoomEntity = joinChatEntity.stream()
                .filter(joinChat -> joinChat.getIsPermitted() == true)
                .map(JoinChatEntity::getChatRoomEntity)
                .collect(Collectors.toList());

            List<ChatRoomDto> dtoList = chatRoomEntity.stream()
                .map(chatRoom -> {
                    String chatRoomName = chatRoom.getChatRoomName();
                    Optional<ClubEntity> optionalClub = clubRepository.findByClubTitle(chatRoomName); 
                    
                    Integer chatRoomId = chatRoom.getChatRoomId();
                    List<JoinChatEntity> joinChatEntities = joinChatRepository.getJoinChats(chatRoomId);
                    Optional<MessageEntity> newestMessageOp
                    = messageRepository.findFirstMessage(joinChatEntities);                               
                    MessageEntity messageEntity = newestMessageOp.orElse(null);
                    return new ChatRoomDto(chatRoom, optionalClub.get(), null, messageEntity);                   
                })
                .collect(Collectors.toList());

            ShowChatRoomListResponseDto responseDto = new ShowChatRoomListResponseDto(dtoList);

            return ResponseEntity.ok(responseDto);

        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public JoinChatEntity getJoinChatRoomByUser(String userId, @NonNull Integer roomId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        Optional<ChatRoomEntity> chatRoomEntity = chatRoomRepository.findById(roomId);
        
        Optional<JoinChatEntity> joinChatEntity 
        = joinChatRepository
        .findByChatRoomNUser(chatRoomEntity, userEntity);

        return joinChatEntity.orElse(null);  
    }

    @Override
    public JoinChatEntity getJoinChatByGeneratedId(Integer chatGeneratedId) {
        try{
            JoinChatEntity joinChatEntity = joinChatRepository.findByChatGeneratedId(chatGeneratedId);
            return joinChatEntity;
        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public void saveMessage(@NonNull MessageEntity messageEntity) {      
        messageRepository.save(messageEntity);
    }

    @Override
    public void sendMessageToKafka(ChatMessageRequestDto dto) {       
        ChatMessageEvent event = new ChatMessageEvent(
            dto.getRoomId(),
            dto.getChatGeneratedId(),
            dto.getSender(),
            dto.getContent(),
            dto.getSentTime()
        );
        //System.out.println("message를 카프카로 보냅니다, dto: " + dto);
        chatMessageProducer.sendMessage(event);
    }

    @Override
    public void sendNotificationToKafka(ChatMessageRequestDto dto) {
        NotificationEvent event = new NotificationEvent(
            dto.getRoomId(),
            dto.getChatGeneratedId(),
            dto.getSender(),
            dto.getContent(),
            dto.getSentTime()
        );
        System.out.println("notification을 카프카로 보냅니다, dto: " + dto);
         notificationProducer.sendNotification(event);
        
    }


    @Override
    public Optional<NotificationEntity> getAlreadyStackedNotification(JoinChatEntity joinChatEntity) {
        Optional<NotificationEntity> alreadyStacked 
        = notificationRepository.findNotification(joinChatEntity);
        return alreadyStacked;
    
    }


    @Override
    public ResponseEntity<? super ShowMessageListResponseDto> showMessageListByRoom(Integer roomId) {
        try{
            List<JoinChatEntity> joinChatEntities = joinChatRepository.getJoinChats(roomId);
            
            List<Integer> chatGeneratedIds = joinChatEntities.stream()
                .map(JoinChatEntity::getChatGeneratedId)
                .collect(Collectors.toList());
            
            List<MessageEntity> messageEntities 
            = messageRepository.findMessageList(chatGeneratedIds);
                     
            List<MessageDto> messageDtos = new ArrayList<>();
            
            for(MessageEntity messageEntity : messageEntities){
                JoinChatEntity joinChatEntity = joinChatRepository
                    .findByChatGeneratedId(messageEntity.getChatGeneretedId());
                if(joinChatEntity != null){
                    MessageDto messageDto = new MessageDto( messageEntity, joinChatEntity);
                    messageDtos.add(messageDto);
                }
            }
            
            ShowMessageListResponseDto responseDto = new ShowMessageListResponseDto(messageDtos);
           
            return ResponseEntity.ok(responseDto);

        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<? super JoinClubResponseDto> joinChatInvitation(String userId, String roomName) {
        try{
            UserEntity userEntity = userRepository.findByUserId(userId);
            Optional<UserInfoEntity> userInfoEntity = userInfoRepository.getUserInfo(userId);
            if(!userInfoEntity.isPresent()) return ResponseEntity.notFound().build();

            List<ChatRoomEntity> alreadyInvitedChatRoom = 
            chatRoomRepository.getChatRoom(roomName)
            .stream()
            .filter(chatRoom -> chatRoom.getIsForInvitation() == true)
            .collect(Collectors.toList());

            Optional<JoinChatEntity> alreadyInvitedJoinChat
            = joinChatRepository.findExistChatRoom(alreadyInvitedChatRoom, userEntity);
            if(alreadyInvitedJoinChat.isPresent()) {
                System.out.println("중복 요청");
                return JoinClubResponseDto.alreayExist();
            }

            String userName = userInfoEntity.get().getNickName();         
            ChatRoomCreationResponseDto chatRoomCreationResponseDto = createChatRoom(userEntity, roomName, userId, true);
            JoinChatEntity savedJoinChatEntity = chatRoomCreationResponseDto.getJoinChatEntity();
            ChatRoomEntity savedChatRoomEntity = chatRoomCreationResponseDto.getChatRoomEntity();

            ClubMemberEntity clubHostMember 
            = clubMemberRepository.findHost(roomName);
            UserEntity hostUserEntity = clubHostMember .getUserEntity();
            UserInfoEntity hostUserInfoEntity = hostUserEntity.getUserInfoEntity();

            JoinChatEntity joinChatEntity
            = new JoinChatEntity(
                null, 
                savedChatRoomEntity, 
                hostUserEntity, 
                true,
                hostUserInfoEntity.getNickName(),
                hostUserInfoEntity.getUserThumbnailUrl()
            );
            joinChatRepository.save(joinChatEntity);

            LocalDateTime now = LocalDateTime.now();
            MessageEntity messageEntity 
            = new MessageEntity(
                savedJoinChatEntity,
                "<name>" + userName +"</name><ID>" + userId +"</ID>(이)가 <club>" + roomName + "</club>에 참여하고 싶어합니다. ${승인하기}",
                now
            );
            saveMessage(messageEntity);

        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }   

        return JoinClubResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<?> joinChatRoom(String joinerUserId, @NonNull Integer clubId){
        UserEntity JoinerUserEntity = userRepository.findByUserId(joinerUserId);
        Optional<UserInfoEntity> joinerUserInfoEntity = userInfoRepository.getUserInfo(joinerUserId);
        Optional<ClubEntity> hasToJoinClubEntity = clubRepository.findById(clubId);

        if(!hasToJoinClubEntity.isPresent() ){
            return ResponseEntity.notFound().build();
        } 

        String clubName = hasToJoinClubEntity.get().getClubTitle();
        Optional<ChatRoomEntity> hasToJoinChatRoomEntity 
        = chatRoomRepository.getChatRoom(clubName)
            .stream()
            .filter(chatRoom -> !chatRoom.getIsForInvitation())
            .findFirst();

        if(!hasToJoinChatRoomEntity.isPresent()) return ResponseEntity.notFound().build();
        

        JoinChatEntity joinChatEntity
        = new JoinChatEntity(
            null, 
            hasToJoinChatRoomEntity.orElse(null), 
            JoinerUserEntity, 
            true,
            joinerUserInfoEntity.get().getNickName(),
            joinerUserInfoEntity.get().getUserThumbnailUrl()
        );

        joinChatRepository.save(joinChatEntity);

        return ResponseEntity.ok().build();
    }


    @Override
    public List<UserEntity> findAllUsersByRoomId(Integer roomId) {
        List<JoinChatEntity> joinChatEntities = joinChatRepository.getJoinChats(roomId);
        List<UserEntity> users = joinChatEntities.stream()
            .map(JoinChatEntity::getUserEntity)
            .collect(Collectors.toList());
        return users;    
    }

    @Override
    public ResponseEntity<?> readMessage(Integer notificationId) {
        NotificationEntity targetNotificationEntity = notificationRepository.findByNotificationId(notificationId);
        targetNotificationEntity.setIsRead(true);
        notificationRepository.save(targetNotificationEntity);
        return ResponseEntity.ok().build(); 
    }
}
