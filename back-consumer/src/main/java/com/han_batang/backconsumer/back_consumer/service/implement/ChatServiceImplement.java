package com.han_batang.backconsumer.back_consumer.service.implement;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.han_batang.backconsumer.back_consumer.entity.JoinChatEntity;
import com.han_batang.backconsumer.back_consumer.entity.MessageEntity;
import com.han_batang.backconsumer.back_consumer.entity.NotificationEntity;
import com.han_batang.backconsumer.back_consumer.repository.JoinChatRepository;
import com.han_batang.backconsumer.back_consumer.repository.MessageRepository;
import com.han_batang.backconsumer.back_consumer.repository.NotificationRepository;
import com.han_batang.backconsumer.back_consumer.service.ChatService;
import com.han_batang.backconsumer.dto.event.ChatMessageEvent;
import com.han_batang.backconsumer.dto.event.NotificationEvent;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImplement implements ChatService{
    
    private final MessageRepository messageRepository;
    private final NotificationRepository notificationRepository;
    private final JoinChatRepository joinChatRepository;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveMessage(@NonNull ChatMessageEvent event) {      
        MessageEntity messageEntity = new MessageEntity(
            event.getChatGeneratedId(),
            event.getContent(),
            event.getSentTime()
        );        
        messageRepository.save(messageEntity);
    }

    @Override
    public void saveNotification(@NonNull NotificationEvent event){        
        Integer roomId = Integer.parseInt(event.getRoomId());
        Integer chatGeneratedId = event.getChatGeneratedId();

        Set<Object> offlines = redisTemplate.opsForSet().difference(
            "room:" + roomId + ":members",
            "room:" + roomId + ":active"
        );
        Set<String> offLineUsers = offlines.stream().map(String::valueOf).collect(Collectors.toSet());
        
        System.out.println("모든 오프라인 유저들: " + offLineUsers.toString());
        for(String offLineUser : offLineUsers){
            if(offLineUser.equals(event.getUserId())){
                System.out.println("발신자이므로 알람 안함" + offLineUser);
                continue;
            }

            // String notice = "메시지가 발송되었습니다.";
            String route = "/room/" + roomId;
            // String userDestination = "/room/notifications/" + offLineUser;
            //messagingTemplate.convertAndSend(userDestination, notice);
        
            //     JoinChatEntity recieverJoinChat 
            // = joinChatRepository.findByChatRoomEntityAndUserEntity(room, roomMember);
            JoinChatEntity sendTo 
                = joinChatRepository.findByChatRoomIdAndUserId(roomId, offLineUser);
            Optional<NotificationEntity> isAlreadyStacked = notificationRepository.findNotification(sendTo);
            System.out.println("알람 발송이 진행됨: " + offLineUser);
            
            if(isAlreadyStacked.isPresent()){
                System.out.println("기존 알람에 추가");
                NotificationEntity notificationEntity = isAlreadyStacked.get();
                notificationEntity.setNotificationContent(event.getNotificationContent());
                notificationEntity.setNotificationSentTime(event.getNotificationSentTime());
                notificationEntity.setStacks(notificationEntity.getStacks() + 1);
                notificationRepository.save(notificationEntity);
            }else{
                System.out.println("새로운 알람");
                NotificationEntity notificationEntity
                = new NotificationEntity(
                    null,
                    sendTo,
                    event.getNotificationContent(),
                    event.getNotificationSentTime(),
                    false,
                    1,
                    route
                );
                notificationRepository.save(notificationEntity);
            }   
        }
    }
}
