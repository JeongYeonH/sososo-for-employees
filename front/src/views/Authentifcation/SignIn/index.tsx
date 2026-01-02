import InputBox from 'components/InputBox';
import { KaKaoTalkImage, GoogleImage } from 'assets/images/social-login-mark';
import React, { ChangeEvent, useRef, useState, KeyboardEvent } from 'react'
import { useNavigate } from 'react-router-dom';
import { CheckCertificationRequestDto, EmailCertificationRequestDto, IdCheckRequestDto, SignInRequestDto, SignUpRequestDto } from 'apis/request/auth';
import { CheckCertificationRequest, emailCertificationRequest, idCheckRequest, signInRequest, signUpRequest, SNS_SIGN_IN_URL } from 'apis';
import { CheckCertificationResponseDto, EmailCertificationResponseDto, IdCheckResponseDto, SignInResponseDto, SignUpResponseDto } from 'apis/response/auth';
import { ResponseCode } from 'types/enums';
import { ResponseBody } from 'types';
import { useCookies } from 'react-cookie';
import './style.css'

export default function SignIn() {
    const idRef = useRef<HTMLInputElement | null>(null);
    const passwordRef= useRef<HTMLInputElement | null>(null);

    const [cookie, setCookie] = useCookies();
  
    const [id, setId] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [message, setMessage] = useState<string>('');  
    
    const navigate = useNavigate();

    const signInResponse = (responseBody: ResponseBody<SignInResponseDto>) =>{
        if(!responseBody) return;
        const { code } = responseBody;
        if(code===ResponseCode.VALIDATION_FAIL) alert('아이디와 비밀번호를 입력하세요.');
        if(code===ResponseCode.SIGH_IN_FAIL) setMessage('로그인 정보가 일치하지 않습니다.');
        if(code===ResponseCode.DATABASE_ERROR) alert('데이터베이스 오류입니다.');
        //if(code !==ResponseCode.SUCCESS) return;

        const {token, expirationTime} = responseBody as SignInResponseDto;
        const now = new Date().getTime();
        const expires = new Date(now + expirationTime*1000);
        setCookie('accessToken', token, {expires, path: '/' });
        navigate('/');
    }

    const onIdChangeHandler = (event: ChangeEvent<HTMLInputElement>) =>{
        const { value } = event.target;
        setId(value); 
        setMessage('');
    }
    const onPasswordChangeHandler = (event: ChangeEvent<HTMLInputElement>) =>{
        const { value } = event.target;
        setPassword(value);
        setMessage('');
    }
    
    const onSignUpButtonClickHandler = () => {
        navigate('/auth/sign-up');
    };

    const onSignInButtonClickHandler = () => {
        if(!id || !password) {
            alert('아이디와 비밀번호 모두 입력하세요.');
            return;
        }
        
        const requestBody: SignInRequestDto = {id, password};
        signInRequest(requestBody).then(signInResponse);
    };

    const onSnSSignInButtonClickHandler = (type: 'kakao' | 'google') =>{
        console.log('실행 확인')
        window.location.href = SNS_SIGN_IN_URL(type);
    }

    const onIdKeyDownHandler = (event: KeyboardEvent<HTMLInputElement>) =>{
        if(event.key !== 'Enter') return;
        if(!passwordRef.current) return;
        passwordRef.current.focus();
        
        
    }
    const onPasswordKeyDownHandler = (event: KeyboardEvent<HTMLInputElement>) =>{
        if(event.key !== 'Enter') return;
        onSignInButtonClickHandler();
        
    }

   
    return (
    <div id='sign-in-wrapper'>
        <div className='sign-in-image'></div>
        <div className='sign-in-container'>
            <div className='sign-in-box'>
                <div className='sign-in-title'>{'Sign in to SoSoSo'}</div>
                <div className='sign-in-content-sns-sign-in-box'>
                        <div className='sign-in-content-sns-sign-in-button-box'>
                            <div className='social-sign-in-button' onClick={()=> onSnSSignInButtonClickHandler('kakao')}>
                                <div className='icon-renderer'>
                                    <KaKaoTalkImage/>
                                </div>
                                카카오로 로그인 하기
                            </div>
                            <div className='social-sign-in-button' onClick={()=> onSnSSignInButtonClickHandler('google')}>
                                <div className='icon-renderer'>
                                    <GoogleImage/>
                                </div>
                                구글로 로그인 하기
                            </div>
                        </div>
                        <div className='sign-in-content-sns-sign-in-title'>혹은 아이디로 로그인 하기</div>
                    </div>
                <div className='sign-in-content-box'>                            
                    <div className='sign-in-content-input-box'>
                        <InputBox ref={idRef} title='아이디' placeholder='아이디를 입력해주세요' type='text' value={id} onChange={onIdChangeHandler} />
                        <InputBox ref={passwordRef} title='비밀번호' placeholder='비밀번호를 입력해주세요' type='password' value={password} onChange={onPasswordChangeHandler} isErrorMessage message={message} onKeyDown={onPasswordKeyDownHandler}/>
                    </div>
                    <div className='sign-in-content-button-box'>
                        <div className='sign-in-button' onClick={onSignInButtonClickHandler}>{'로그인'}</div>
                    </div>
                    <div className='sign-in-content-divider'></div>
                    
                </div>
            </div>
        </div>
    </div>
  )

}
