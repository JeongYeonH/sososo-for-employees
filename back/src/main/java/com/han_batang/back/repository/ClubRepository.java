package com.han_batang.back.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.han_batang.back.entity.ClubEntity;


@Repository
public interface ClubRepository extends JpaRepository<ClubEntity, Integer>{
    Page<ClubEntity> findAllByOrderByCreatedDateDesc(Pageable pageable);
    Page<ClubEntity> findByClubInfoClubType(String clubType, Pageable pageable);
    Optional<ClubEntity> findByClubTitle(@Param("clubTitle") String clubTitle);
    
    @Query(value = """
        SELECT c.* 
        FROM club c 
        JOIN club_info ci ON c.club_id = ci.club_id 
        WHERE c.club_title LIKE CONCAT('%', ?1, '%') 
           OR ci.club_type LIKE CONCAT('%', ?1, '%') 
           OR ci.club_location LIKE CONCAT('%', ?1, '%')
        """, 
        countQuery = """
        SELECT COUNT(*) 
        FROM club c 
        JOIN club_info ci ON c.club_id = ci.club_id 
        WHERE c.club_title LIKE CONCAT('%', ?1, '%') 
           OR ci.club_type LIKE CONCAT('%', ?1, '%') 
           OR ci.club_location LIKE CONCAT('%', ?1, '%')
        """, 
        nativeQuery = true)
    Page<ClubEntity> findClubs( String keyword, Pageable pageable);
} 
