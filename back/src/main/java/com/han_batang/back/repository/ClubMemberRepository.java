package com.han_batang.back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.han_batang.back.entity.ClubEntity;
import com.han_batang.back.entity.ClubMemberEntity;
import java.util.List;


public interface ClubMemberRepository extends JpaRepository<ClubMemberEntity, Integer>{
    
    Optional<ClubMemberEntity> findByClubEntityClubIdAndUserEntityUserId(Integer club_id, String userId);
    
    
    
    List<ClubMemberEntity> findByUserEntityUserId(String userId);
    
    ClubMemberEntity findByClubEntityClubTitleAndIsAdmin(String clubTitle, boolean isAdmin);
    Optional<ClubMemberEntity> findByClubEntityAndIsAdmin(Optional<ClubEntity> clubEntity, boolean isAdmin);
    List<ClubMemberEntity> findByClubEntityClubId(Integer club_id);
} 
