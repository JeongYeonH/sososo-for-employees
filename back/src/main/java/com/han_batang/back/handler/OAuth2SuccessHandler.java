package com.han_batang.back.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import com.han_batang.back.provider.JwtProvider;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
   
    private final JwtProvider jwtProvider;
    
    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request, 
        HttpServletResponse response,
        Authentication authentication) 
        throws IOException, ServletException{
            System.out.println("핸들러 진입");
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String userId = oAuth2User.getName();
            System.out.println("사용자 이름은: " + userId);
            String token = jwtProvider.create(userId);
            System.out.println("OAuth2 authentication success: " + authentication.getName());
            System.out.println("핸들러 거치고 나옴");
            response.sendRedirect("http://localhost:3000/auth/oauth-response/" + token + "/3600");
        }
}
