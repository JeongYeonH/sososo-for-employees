package com.han_batang.back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.han_batang.back.entity.ClubInfoEntity;

@Repository
public interface ClubInfoRepository extends JpaRepository<ClubInfoEntity, Integer>{
    Optional<ClubInfoEntity> findByClubEntityClubId(Integer clubId);
}
