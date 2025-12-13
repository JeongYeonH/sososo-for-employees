package com.han_batang.back.filter;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;



import com.han_batang.back.provider.JwtProvider;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import org.springframework.lang.NonNull;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    
    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
        throws ServletException, IOException{         
            try{

                String token = parseBearerToken(request);
    
                if (token != null) {
                    System.out.println("JWT 토큰이 발견되었습니다: " + token);
                    String userId = jwtProvider.validate(token);
        
                    if (userId != null) {
                        System.out.println("유효한 사용자 ID: " + userId);
                        AbstractAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    } else {
                        System.out.println("유효하지 않은 토큰");
                    }
                } else {
                    System.out.println("토큰이 발견되지 않았습니다");
                }

                filterChain.doFilter(request, response);
                
            }catch(Exception exception){
                exception.printStackTrace();
            }
           
        }

    private String parseBearerToken(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        boolean hasAuthorization = StringUtils.hasText(authorization);
        if(!hasAuthorization) return null;

        boolean isBearer = authorization.startsWith("Bearer ");
        if(!isBearer) return null;

        String token = authorization.substring(7);
        return token;
    }
}
