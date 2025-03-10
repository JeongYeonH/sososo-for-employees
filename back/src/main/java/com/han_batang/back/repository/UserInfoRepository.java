package com.han_batang.back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.han_batang.back.entity.UserInfoEntity;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Integer>{
    
    @Query(value = """
            SELECT ui
            FROM user_info ui
            JOIN ui.userEntity u
            WHERE u.userId = :userId
            """)
    Optional<UserInfoEntity> getUserInfo(@Param("userId")String userId);
    Optional<UserInfoEntity> findByNickName(String nickName);
}
