package com.han_batang.back.dto.request.user;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserInfoRequestDto {
    
    private String nickName;

    private Integer age;

    private String shortIntro;

    private MultipartFile userThumbnail;
}
