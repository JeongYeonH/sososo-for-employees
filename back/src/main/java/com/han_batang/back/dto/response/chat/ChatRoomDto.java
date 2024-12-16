package com.han_batang.back.dto.response.chat;

import java.time.LocalDateTime;

import com.han_batang.back.entity.ChatRoomEntity;
import com.han_batang.back.entity.ClubEntity;
import com.han_batang.back.entity.MessageEntity;
import com.han_batang.back.entity.UserInfoEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomDto {
    private Integer clubId;
    private Integer chatRoomId;
    private String chatRoomName;
    private LocalDateTime chatRoomGeneratedDate;
    private Boolean chatRoomStatus;
    private String clubThumbnailUrl;
    private Integer clubCurrentMemberNum;
    private String newestMessage;

    public ChatRoomDto(
        ChatRoomEntity chatRoomEntity, 
        ClubEntity clubEntity,
        UserInfoEntity userInfoEntity,
        MessageEntity messageEntity) {
        
        this.clubId = clubEntity.getClubId();
        this.chatRoomId = chatRoomEntity.getChatRoomId();       
        this.chatRoomGeneratedDate = chatRoomEntity.getChatRoomGeneratedDate();
        this.chatRoomStatus = chatRoomEntity.getChatRoomStatus();
        this.chatRoomName = chatRoomEntity.getChatRoomName();
        this.clubThumbnailUrl = clubEntity.getClub_thumbnail_url();
        if (messageEntity != null) {
            this.newestMessage = messageEntity.getMessagesText();
        } 

        if (chatRoomEntity.getIsForInvitation() == false) {
            this.clubCurrentMemberNum = clubEntity.getClub_current_member_num();
        } else{
            this.clubCurrentMemberNum = 2;
        }
    }
}
