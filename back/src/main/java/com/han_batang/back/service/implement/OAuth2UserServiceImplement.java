package com.han_batang.back.service.implement;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.han_batang.back.entity.GoogleOAuth2User;
import com.han_batang.back.entity.KakaoOAuth2User;
import com.han_batang.back.entity.UserEntity;
import com.han_batang.back.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImplement extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        try {
            OAuth2User oAuth2User = super.loadUser(request);
            String oauthClientName = request.getClientRegistration().getClientName();

            UserEntity userEntity = null;
            String userId = null;
            String email = "email@email.com";
            System.out.println(oAuth2User);

            if (oauthClientName.equals("kakao")) {
                userId = "kakao_" + oAuth2User.getAttributes().get("id");
                userEntity = new UserEntity(userId, email);
                userRepository.save(userEntity);
                return new KakaoOAuth2User(userId);
            } else if (oauthClientName.equals("Google")) {
                userId = "google_" + oAuth2User.getAttributes().get("sub");
                userEntity = new UserEntity(userId, email);
                userRepository.save(userEntity);
                return new GoogleOAuth2User(userId, oAuth2User.getAttributes(), oAuth2User.getAuthorities());                
            }
            return oAuth2User;

        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
