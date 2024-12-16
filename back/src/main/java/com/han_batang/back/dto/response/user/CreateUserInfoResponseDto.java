package com.han_batang.back.dto.response.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.han_batang.back.dto.response.ResponseDto;

public class CreateUserInfoResponseDto extends ResponseDto{
    private CreateUserInfoResponseDto(){
        super();
    }

    public static ResponseEntity<CreateUserInfoResponseDto> success(){
        CreateUserInfoResponseDto responseBody = new CreateUserInfoResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }


}
