package com.han_batang.back.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "chat_room")
@Table(name = "chat_room")
public class ChatRoomEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Integer chatRoomId;

    @Column(name = "chat_room_name")
    private String chatRoomName;

    @Column(name = "chat_room_generated_date")
    private LocalDateTime chatRoomGeneratedDate;

    @Column(name = "chat_room_status")
    private Boolean chatRoomStatus;

    @Column(name = "is_for_invitation")
    private Boolean isForInvitation;

    public String getChatRoomName() {
        return this.chatRoomName;
    }
}
