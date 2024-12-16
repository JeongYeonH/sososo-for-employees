package com.han_batang.back.entity;

import com.han_batang.back.dto.request.club.JoinClubRequestDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity(name="club_member")
@Table(name="club_member")
public class ClubMemberEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_member_id")
    private Integer clubMemberId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="club_id", referencedColumnName = "club_id")
    ClubEntity clubEntity;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    UserEntity userEntity;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @Column(name = "is_joined")
    private boolean isJoined;

    public ClubMemberEntity(){
        
    }

    public ClubMemberEntity(JoinClubRequestDto dto, ClubEntity clubEntity, UserEntity userEntity){
        this.clubEntity = clubEntity;
        this.userEntity = userEntity;
        this.isAdmin = false;
        this.isJoined = true;
    }
}
