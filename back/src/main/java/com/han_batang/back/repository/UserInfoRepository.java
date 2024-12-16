package com.han_batang.back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.han_batang.back.entity.UserInfoEntity;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Integer>{
    Optional<UserInfoEntity> findByUserEntity_UserId(String userId);
    Optional<UserInfoEntity> findByNickName(String nickName);
}
