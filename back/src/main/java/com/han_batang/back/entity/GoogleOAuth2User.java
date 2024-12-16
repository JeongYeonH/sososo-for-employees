package com.han_batang.back.entity;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GoogleOAuth2User implements OAuth2User {

    private String userId;
    private Map<String, Object> attributes;
    private Collection<? extends GrantedAuthority> authorities;

    public GoogleOAuth2User(String userId, Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.attributes = attributes;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;  
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; 
    }

    @Override
    public String getName() {
        return this.userId;  
    }
    
}
