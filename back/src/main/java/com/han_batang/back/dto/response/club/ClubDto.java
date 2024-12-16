package com.han_batang.back.dto.response.club;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.han_batang.back.entity.ClubEntity;
import com.han_batang.back.entity.ClubInfoEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubDto {
    
    @JsonProperty("clubId")
    private Integer clubId;
    private String clubTitle;
    private String clubThumbnailUrl;
    private String clubShortIntro;
    private String clubMemberRequirements;
    private String clubActivityDescription;
    private Integer clubCurrentMemberNum;
    private Integer clubPageVisitedNum;

    private String clubType;
    private String clubLocation;
    private Integer clubTotalNum;

    public ClubDto(ClubEntity clubEntity, Optional<ClubInfoEntity> clubInfoEntity){
        this.clubId = clubEntity.getClubId();
        this.clubTitle = clubEntity.getClubTitle();
        this.clubThumbnailUrl = clubEntity.getClub_thumbnail_url();
        this.clubShortIntro = clubEntity.getClub_short_intro();
        this.clubMemberRequirements = clubEntity.getClub_member_requirements();
        this.clubActivityDescription = clubEntity.getClub_activity_description();
        this.clubCurrentMemberNum = clubEntity.getClub_current_member_num();
        this.clubPageVisitedNum = clubEntity.getClubPageVisitedNum();
    
        clubInfoEntity.ifPresent(info -> {
            this.clubType = info.getClubType();
            this.clubLocation = info.getClubLocation();
            this.clubTotalNum = info.getClubTotalNum();
        });
        
    }
}
