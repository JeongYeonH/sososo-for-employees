package com.han_batang.back.dto.response.user;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.han_batang.back.common.ResponseCode;
import com.han_batang.back.common.ResponseMessage;
import com.han_batang.back.dto.response.ResponseDto;
import com.han_batang.back.entity.UserEntity;
import com.han_batang.back.entity.UserInfoEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShowUserInfoResponseDto extends ResponseDto{
    private String userId;
    private Integer userInfoId; 
    private UserEntity userEntity;
    private String nickName;   
    private Integer age;
    private String shortIntro;
    private String userThumbnailUrl;

    public ShowUserInfoResponseDto(UserEntity userEntity, UserInfoEntity userInfoEntity){
        this.userId = userEntity.getUserId();
        this.userInfoId = userInfoEntity.getUserInfoId();
        this.nickName = userInfoEntity.getNickName();
        this.age = userInfoEntity.getAge();
        this.shortIntro = userInfoEntity.getShortIntro();
        this.userThumbnailUrl = userInfoEntity.getUserThumbnailUrl();
    }

    public static ResponseEntity<? super ShowUserInfoResponseDto> noData() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.NO_DATA, ResponseMessage.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

}
