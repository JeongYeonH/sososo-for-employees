package com.han_batang.back.entity;

import java.time.LocalDateTime;

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
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "notification")
@Table(name = "notification")
public class NotificationEntity {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer notificationId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "chat_generated_id")
    JoinChatEntity joinChatEntity;

    @Column(name = "notification_content")
    private String notificationContent;

    @Column(name = "notification_sent_time")
    private LocalDateTime notificationSentTime;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "stacks")
    private Integer stacks;

    @Column(name = "route")
    private String route;
}
