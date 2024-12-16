package com.han_batang.back.dto.response.club;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.han_batang.back.common.ResponseCode;
import com.han_batang.back.common.ResponseMessage;
import com.han_batang.back.dto.response.ResponseDto;

public class JoinClubResponseDto extends ResponseDto{
    private JoinClubResponseDto(){
        super();
    }

    public static ResponseEntity<JoinClubResponseDto> success (){
        JoinClubResponseDto responseBody = new JoinClubResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notAClubHost(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

    public static ResponseEntity<? super JoinClubResponseDto> alreayExist() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.DUPICATED_DATA, ResponseMessage.DUPICATED_DATA);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<? super JoinClubResponseDto> notLoggedIn() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.UNAUTHORIZED, ResponseMessage.DUPICATED_DATA);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
