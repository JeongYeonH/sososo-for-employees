package com.han_batang.back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.han_batang.back.entity.ClubEntity;
import com.han_batang.back.entity.ClubMemberEntity;
import java.util.List;


public interface ClubMemberRepository extends JpaRepository<ClubMemberEntity, Integer>{
    
    @Query(value = """
            SELECT cm
            FROM club_member cm
            JOIN cm.clubEntity c
            JOIN cm.userEntity u
            WHERE c.clubId = :clubId
              AND u.userId = :userId
            """)
    Optional<ClubMemberEntity> findByClubIdNUserId(@Param("clubId")Integer club_id, @Param("userId") String userId);
    
    
    
    List<ClubMemberEntity> findByUserEntityUserId(String userId);
    
    ClubMemberEntity findByClubEntityClubTitleAndIsAdmin(String clubTitle, boolean isAdmin);
    Optional<ClubMemberEntity> findByClubEntityAndIsAdmin(Optional<ClubEntity> clubEntity, boolean isAdmin);
    List<ClubMemberEntity> findByClubEntityClubId(Integer club_id);
} 
