package com.han_batang.back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.han_batang.back.entity.ClubInfoEntity;

@Repository
public interface ClubInfoRepository extends JpaRepository<ClubInfoEntity, Integer>{
    
    @Query(value = """
            SELECT ci
            FROM club_info ci
            JOIN ci.clubEntity c
            WHERE c.clubId = :clubId
            """)
    Optional<ClubInfoEntity> getClubInfo(@Param("clubId")Integer clubId);
}
