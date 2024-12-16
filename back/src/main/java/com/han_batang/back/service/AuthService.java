package com.han_batang.back.service;

import org.springframework.http.ResponseEntity;

import com.han_batang.back.dto.request.auth.CheckCertificationRequestDto;
import com.han_batang.back.dto.request.auth.EmailCertificationRequestDto;
import com.han_batang.back.dto.request.auth.IdCheckRequestDto;
import com.han_batang.back.dto.request.auth.SignInRequestDto;
import com.han_batang.back.dto.request.auth.SignUpRequestDto;
import com.han_batang.back.dto.response.auth.CheckCertificationResponseDto;
import com.han_batang.back.dto.response.auth.EmailCertificationResponseDto;
import com.han_batang.back.dto.response.auth.IdCheckResponseDto;
import com.han_batang.back.dto.response.auth.SignUpResponseDto;
import com.han_batang.back.dto.response.auth.SignInResponseDto;

public interface AuthService {
    
    ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto);
    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto);
    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto);
    ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto);
    ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto);
}
