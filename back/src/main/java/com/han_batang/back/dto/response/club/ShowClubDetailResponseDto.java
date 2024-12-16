package com.han_batang.back.dto.response.club;



import com.han_batang.back.dto.response.ResponseDto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowClubDetailResponseDto extends ResponseDto{
    
    private ClubDto club;

    public ShowClubDetailResponseDto(ClubDto clubDto){
        super();
        this.club = clubDto;
    }
}
