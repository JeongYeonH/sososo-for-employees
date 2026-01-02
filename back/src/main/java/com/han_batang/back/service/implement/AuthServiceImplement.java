package com.han_batang.back.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.han_batang.back.common.CertificationNumber;
import com.han_batang.back.dto.request.auth.CheckCertificationRequestDto;
import com.han_batang.back.dto.request.auth.EmailCertificationRequestDto;
import com.han_batang.back.dto.request.auth.IdCheckRequestDto;
import com.han_batang.back.dto.request.auth.SignInRequestDto;
import com.han_batang.back.dto.request.auth.SignUpRequestDto;
import com.han_batang.back.dto.response.ResponseDto;
import com.han_batang.back.dto.response.auth.CheckCertificationResponseDto;
import com.han_batang.back.dto.response.auth.EmailCertificationResponseDto;
import com.han_batang.back.dto.response.auth.IdCheckResponseDto;
import com.han_batang.back.dto.response.auth.SignInResponseDto;
import com.han_batang.back.dto.response.auth.SignUpResponseDto;
import com.han_batang.back.entity.CertificationEntity;
import com.han_batang.back.entity.UserEntity;
import com.han_batang.back.provider.EmailProvider;
import com.han_batang.back.provider.JwtProvider;
import com.han_batang.back.repository.CertificationRepository;
import com.han_batang.back.repository.UserRepository;
import com.han_batang.back.service.AuthService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService{
    
     private final UserRepository userRepository;
     private final CertificationRepository certificationRepository;
     
     private final JwtProvider jwtProvider;
     private final EmailProvider emailProvider;
     private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

     @Override  
     public ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto) {
          try{

               String userId = dto.getId();

               if(userId == null){
                    return null;
               }

               boolean isExistId = userRepository.existsById((userId));
               if(isExistId) return IdCheckResponseDto.duplicateId();

          }catch(Exception exception){
               exception.printStackTrace();
               return null;
          }

          return IdCheckResponseDto.success();
     }

     @Override
     public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {
          try{

               String userId = dto.getId();
               String email = dto.getEmail();
               System.out.println("확인 실행 1");
               boolean isExistId = userRepository.existsByUserId(userId);
               if(isExistId) return EmailCertificationResponseDto.duplicatedId();
               System.out.println("확인 실행 2");
               String certificationNumber = CertificationNumber.getCertificationNumber();
               if(certificationNumber == null) return EmailCertificationResponseDto.mailSendFail();
               if(email == null) return EmailCertificationResponseDto.mailSendFail();
               System.out.println("확인 실행 3");
               boolean isSuccessed = emailProvider.sendCertificationMail(email, certificationNumber);
               if(!isSuccessed) return EmailCertificationResponseDto.mailSendFail();
               System.out.println("확인 실행 4");
               CertificationEntity certificationEntity = new CertificationEntity(userId, email, certificationNumber);
               certificationRepository.save(certificationEntity);
               System.out.println("확인 실행 5");
          }catch(Exception exception){
               exception.printStackTrace();
               return null;
          }

          return EmailCertificationResponseDto.success();
     }

     @Override
     public ResponseEntity<? super CheckCertificationResponseDto> checkCertification (CheckCertificationRequestDto dto) {
          try{

               String userId = dto.getId();
               String email = dto.getEmail();
               String certificationNumber = dto.getCertificationNumber();

               CertificationEntity certificationEntity = certificationRepository.findByUserId(userId);
               if(certificationEntity == null) return CheckCertificationResponseDto.certificationFail();

               boolean isMatch = certificationEntity.getEmail().equals(email) && certificationEntity.getCertificationNumber().equals(certificationNumber);
               if(!isMatch) return CheckCertificationResponseDto.certificationFail();

          }catch(Exception exception){
               exception.printStackTrace();
               return null;
          }

          return  CheckCertificationResponseDto.success();
     }

     @Override
     public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
          try{

               String userId = dto.getId();
               boolean isExistId = userRepository.existsByUserId(userId);
               if(isExistId) return SignUpResponseDto.duplicateId();

               String email = dto.getEmail();
               String certificationNumber = dto.getCertificationNumber();
               CertificationEntity certificationEntity = certificationRepository.findByUserId(userId);
               boolean isMatched = 
               certificationEntity.getEmail().equals(email) && 
               certificationEntity.getCertificationNumber().equals(certificationNumber);
               if(!isMatched) return SignUpResponseDto.certificationFail();
               
               String password = dto.getPassword();
               String encodedPassword = passwordEncoder.encode(password);
               dto.setPassword(encodedPassword);

               UserEntity userEntity = new UserEntity(dto);
               userRepository.save(userEntity);

               //certificationRepository.deleteByUserId(userId);

          }catch(Exception exception){
               exception.printStackTrace();
               return ResponseDto.databaseError();
          }

          return SignUpResponseDto.success();
     }

     @Override
     public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {
          String token = null;

          try {
               
               String userid = dto.getId();
               UserEntity userEntity = userRepository.findByUserId(userid);
               if(userEntity==null) return SignInResponseDto.signInFail();

               String password = dto.getPassword();
               String encodedPassword = userEntity.getPassword();
               boolean isMatched = passwordEncoder.matches(password, encodedPassword);
               if(!isMatched) return SignInResponseDto.signInFail();

               System.out.println("여기 실행?");
               token = jwtProvider.create(userid);
          }catch(Exception exception){
               exception.printStackTrace();
               return null;
          }

          return SignInResponseDto.success(token);
     }  
     
}
