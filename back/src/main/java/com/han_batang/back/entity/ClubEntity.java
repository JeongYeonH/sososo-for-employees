package com.han_batang.back.entity;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.han_batang.back.dto.request.club.CreateClubRequestDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="club")
@Table(name="club")
public class ClubEntity {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "club_id")
    private Integer clubId;
    
    @Column(name = "club_title")
    private String clubTitle;
    private String club_thumbnail_url;
    private String club_short_intro;
    private String club_member_requirements;
    private String club_activity_description;

    private Integer club_current_member_num;

    @Column(name = "club_page_visited_num")
    private Integer clubPageVisitedNum;
    
    @Column(nullable = false, updatable = false, name = "created_date")
    private LocalDateTime createdDate;

    @OneToOne(mappedBy = "clubEntity", cascade = CascadeType.REMOVE)
    private ClubInfoEntity clubInfo;

    public ClubEntity(CreateClubRequestDto dto, String clubThumbnailUrl){
        this.clubTitle = dto.getClub_title();
        this.club_thumbnail_url = clubThumbnailUrl;
        this.club_short_intro = dto.getClub_short_intro();
        this.club_member_requirements = dto.getClub_member_requirements();
        this.club_activity_description = dto.getClub_activity_description();
        this.club_current_member_num = dto.getClub_current_member_num();
        this.clubPageVisitedNum = dto.getClub_page_visited_num();
    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }


    public void increaseVisitedNum() {
        if (this.clubPageVisitedNum == null) {
            this.clubPageVisitedNum = 0;
        }
        this.clubPageVisitedNum +=1;
    }

    public Integer getClubPageVisitedNum() {
        return clubPageVisitedNum;
    }

    public void setClubPageVisitedNum(Integer clubPageVisitedNum) {
        this.clubPageVisitedNum = clubPageVisitedNum;
    }

    public String getClubIntro() {
        return this.club_short_intro;
    }

    public String getClubTitle(){
        return this.clubTitle;
    }

}
