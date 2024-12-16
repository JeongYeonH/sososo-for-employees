package com.han_batang.back.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.han_batang.back.dto.request.user.CreateUserInfoRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="user_info")
@Table(name="user_info")
public class UserInfoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "user_info_id")
    private Integer userInfoId;

    @OneToOne
    @JoinColumn(name = "user_id") 
    private UserEntity userEntity;
    
    @Column(name="nick_name")
    private String nickName;
    
    private Integer age;
    
    @Column(name="short_intro")
    private String shortIntro;

    @Column(name = "user_thumbnail_url")
    private String userThumbnailUrl;

    public UserInfoEntity(CreateUserInfoRequestDto dto, UserEntity userEntity, String userThumbnailUrl){
        this.userEntity = userEntity;
        this.nickName = dto.getNickName();
        this.age = dto.getAge();
        this.shortIntro = dto.getShortIntro();
        this.userThumbnailUrl = userThumbnailUrl;
    }


}
