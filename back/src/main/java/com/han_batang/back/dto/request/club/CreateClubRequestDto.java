package com.han_batang.back.dto.request.club;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateClubRequestDto {
    

    @NotBlank
    private String club_title;
    
    private MultipartFile club_thumbnail;

    @NotBlank
    private String club_short_intro;

    @NotBlank
    private String club_member_requirements;

    @NotBlank
    private String club_activity_description;

    @NotNull
    private Integer club_current_member_num;

    @NotNull
    private Integer club_page_visited_num;

    
    private String selectedType;

    private String selectedLocation;

    private Integer selectedMaxNumber;
}
