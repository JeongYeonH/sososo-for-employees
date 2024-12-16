package com.han_batang.back.dto.response.club;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.han_batang.back.dto.response.ResponseDto;




public class CreateClubResponseDto extends ResponseDto{
    private CreateClubResponseDto(){
        super();
    }

    public static ResponseEntity<CreateClubResponseDto> success (){
        CreateClubResponseDto responseBody = new CreateClubResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
