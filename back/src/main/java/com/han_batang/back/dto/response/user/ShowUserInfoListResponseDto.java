package com.han_batang.back.dto.response.user;

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
public class ShowUserInfoListResponseDto extends ResponseDto{
    
    private List<ShowUserInfoResponseDto> userInfoList;

    public ShowUserInfoListResponseDto(List<ShowUserInfoResponseDto> list){
        this.userInfoList = list;
    }

    public static ResponseEntity<? super ShowUserInfoListResponseDto> noData() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.NO_DATA, ResponseMessage.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
