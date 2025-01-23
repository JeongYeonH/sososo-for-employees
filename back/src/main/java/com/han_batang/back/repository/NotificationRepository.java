
package com.han_batang.back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.han_batang.back.entity.JoinChatEntity;
import com.han_batang.back.entity.NotificationEntity;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    List<NotificationEntity> findByJoinChatEntityIn(List<JoinChatEntity> joinChatEntity);
    
    @Query(value = """
            SELECT n
            FROM notification n
            JOIN n.joinChatEntity jc
            WHERE jc = :joinChatEntity
              AND n.isRead = false
            """)
    Optional<NotificationEntity> findNotification(@Param("joinChatEntity")JoinChatEntity joinChatEntity);
    NotificationEntity findByNotificationId(Integer notificationId);
}