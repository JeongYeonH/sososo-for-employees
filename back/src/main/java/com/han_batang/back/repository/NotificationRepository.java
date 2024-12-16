
package com.han_batang.back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.han_batang.back.entity.JoinChatEntity;
import com.han_batang.back.entity.NotificationEntity;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    List<NotificationEntity> findByJoinChatEntityIn(List<JoinChatEntity> joinChatEntity);
    Optional<NotificationEntity> findByJoinChatEntityAndIsReadFalse(JoinChatEntity joinChatEntity);
    NotificationEntity findByNotificationId(Integer notificationId);
}