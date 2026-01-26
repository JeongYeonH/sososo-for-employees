package com.han_batang.backconsumer.back_consumer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.han_batang.backconsumer.back_consumer.entity.JoinChatEntity;
import com.han_batang.backconsumer.back_consumer.entity.NotificationEntity;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    
    @Query(value = """
            SELECT n
            FROM notification n
            JOIN n.joinChatEntity jc
            WHERE jc = :joinChatEntity
              AND n.isRead = false
            """)
    Optional<NotificationEntity> findNotification(@Param("joinChatEntity")JoinChatEntity joinChatEntity);
    
} 