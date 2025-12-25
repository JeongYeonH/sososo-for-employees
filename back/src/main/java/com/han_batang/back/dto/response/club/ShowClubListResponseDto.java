package com.han_batang.back.dto.response.club;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.han_batang.back.common.ResponseCode;
import com.han_batang.back.common.ResponseMessage;
import com.han_batang.back.dto.response.ResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShowClubListResponseDto extends ResponseDto{
    
    private List<ClubDto> clubs;
    private boolean isLast;

    public ShowClubListResponseDto(List<ClubDto> dtoList, boolean isLast){
        super();
        this.clubs = dtoList;
        this.isLast = isLast;
    }

    public static ResponseEntity<ResponseDto> databaseError(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> noData(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.NO_DATA, ResponseMessage.NO_DATA);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}

