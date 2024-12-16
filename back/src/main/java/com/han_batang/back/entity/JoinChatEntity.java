package com.han_batang.back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "join_chat")
@Table(name = "join_chat")
public class JoinChatEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_generated_id")
    private Integer chatGeneratedId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "chat_room_id")
    @JsonIgnore
    ChatRoomEntity chatRoomEntity;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    UserEntity userEntity;

    @Column(name = "is_permitted")
    private Boolean isPermitted;

    @Column(name = "sender")
    private String sender;

    @Column(name = "sender_thumbnail_url")
    private String senderThumbnailUrl;

}
