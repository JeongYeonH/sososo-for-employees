package com.han_batang.back.dto.response.user;

import com.han_batang.back.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDto {
    
    private String userId;
    private Integer userInfoId; 
    private UserEntity userEntity;
    private String nickName;   
    private Integer age;
    private String shortIntro;
    private String userThumbnailUrl;
}
