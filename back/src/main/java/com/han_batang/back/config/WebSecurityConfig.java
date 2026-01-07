package com.han_batang.back.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.han_batang.back.filter.JwtAuthenticationFilter;
import com.han_batang.back.handler.OAuth2SuccessHandler;
import com.han_batang.back.websocket.WebSocketHandshakeInterceptor;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@Configurable
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSecurityConfig implements WebSocketMessageBrokerConfigurer{
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final DefaultOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Value("${front-end-url}")
    private String frontendServer;
    
    @Value("${front-end-test-url}")
    private String frontendTestServer;
 
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {       
        httpSecurity
            .cors(cors -> cors
            .configurationSource(corsConfigurationSource())
            )
            .csrf(CsrfConfigurer::disable)
            .httpBasic(HttpBasicConfigurer::disable)
            .sessionManagement(sessionManagement -> sessionManagement
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(request -> request
                .anyRequest().permitAll()
                // .requestMatchers("/", "/api/v1/auth/**", "/oauth2/**").permitAll()
                // .requestMatchers("/api/v1/response/**").permitAll()
                // .requestMatchers("/api/v1/s3/**").permitAll()
                // .requestMatchers("/api/v1/user/chat/**"
                //                             , "/api/v1/user/notifications"
                //                 ).permitAll()
                // .requestMatchers("/api/v1/user/**").authenticated()
                // .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2 
                .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/v1/auth/oauth2"))
                .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
                .successHandler(oAuth2SuccessHandler)
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(new FaildAuthenticationEntryPoint())
            )
             .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            
        return httpSecurity.build();
    }

    

    @Bean
    protected CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin(frontendServer);
        corsConfiguration.addAllowedOrigin(frontendTestServer);
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
    
    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry registry){
        registry.setApplicationDestinationPrefixes("/room");
        registry.enableSimpleBroker("/room");
    }
    
    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry){
        registry.addEndpoint("/api/v1/user/chat", "/api/v1/user/notifications")
        .setAllowedOrigins(frontendTestServer)
        .addInterceptors(new WebSocketHandshakeInterceptor())
        .withSockJS();
    }

}

class FaildAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        
                
                System.out.println("Authentication failed: " + authException.getMessage());
                authException.printStackTrace();
        
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        
                
                response.getWriter().write("{\"code\": \"NP\", \"message\": \"Authentication failed: " 
                    + authException.getMessage() + "\"}");
    }

}

