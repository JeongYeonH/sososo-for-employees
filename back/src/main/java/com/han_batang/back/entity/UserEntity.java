package com.han_batang.back.entity;

import com.han_batang.back.dto.request.auth.SignUpRequestDto;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="user")
@Table(name="user")
public class UserEntity {
    
    @Id
    private String userId;
    private String password;
    private String email;

    @OneToOne(mappedBy = "userEntity", fetch = FetchType.LAZY)
    private UserInfoEntity userInfoEntity;

    public UserEntity(SignUpRequestDto dto){
        this.userId = dto.getId();
        this.password = dto.getPassword();
        this.email = dto.getEmail();
    }

    public UserEntity(String userId, String email){
        this.userId = userId;
        this.password = "Passw0rd";
        this.email = email;
    }

    public UserInfoEntity getUserInfoEntity() {
        return userInfoEntity;
    }
}
