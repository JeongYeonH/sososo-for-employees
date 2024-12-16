import React, { ChangeEvent, useRef, useState } from 'react'
import './style.css'
import CreateUserInfoRequestDto from 'apis/request/user/create-user-info.request.dto';
import { createUserInfoRequest } from 'apis';
import { useNavigate } from 'react-router-dom';

export default function CreateUserPage(){

    const userThumbnailRef = useRef<HTMLInputElement | null>(null);
    const userNicknameRef = useRef<HTMLInputElement | null>(null);
    const userAgeRef = useRef<HTMLInputElement | null>(null);
    const userIntroduceRef = useRef<HTMLTextAreaElement | null>(null);
  
    const [userThumbnail, setUserThumbnail] = useState<File | null>(null);
    const [userNickname, setUserNickname] = useState<string>('');
    const [userAge, setUserAge] = useState<Number>(0);
    const [userIntroduce, setUserIntroduce] = useState<string>('');
  
    const navigate = useNavigate();

    const onUserThumbnailChangeHandler = (event: ChangeEvent<HTMLInputElement>) =>{
      const file = event.target.files?.[0];
      if(file){
        setUserThumbnail(file);   
      }
    }

    const onUserNicknameChangeHandler = (event: ChangeEvent<HTMLInputElement>) =>{
      const { value } = event.target;
      setUserNickname(value);   
    }
  
    const onUserAgeChangeHandler = (event: ChangeEvent<HTMLInputElement>) =>{
      const { value } = event.target;
      setUserAge(+value);   
    }
    const onUserIntroduceChangeHandler = (event: ChangeEvent<HTMLTextAreaElement>) =>{
      const { value } = event.target;
      setUserIntroduce(value);   
    }

  
    const onCreateClubButtonClickHandler = () => {
      if(!userNickname || !userAge || !userIntroduce ){
        alert('요구되는 내용을 모두 입력해주세요.');
        return;
      }
  
      const formData = new FormData();
      formData.append('nickName', userNickname);
      formData.append('age', userAge.toString());  
      formData.append('shortIntro', userIntroduce);

      if (userThumbnail) {
        formData.append('userThumbnail', userThumbnail);
      }
     
      const token = getCookie('accessToken');
  
      if(!token){
        alert('로그인이 필요합니다.');
      }
  
      createUserInfoRequest(formData, token).then();

      navigate('/user/personal-profile', { replace: true });
    };
  
  
    function getCookie(name: any){
      const value = `; ${document.cookie}`;
      const parts = value.split(`; ${name}=`);
      if(parts.length === 2) return parts.pop()?.split(';').shift();
      return null;
    }

  return (
    <div id='create-user-page-wrapper'>
        <div className='create-user-page-sections'>
            <div className='create-user-page-container'>
                <div className='box-one'>
                    <div className='create-user-page-box'>
                        <div className='create-user-box-sub-box-profile-image'>
                            <div className='create-user-box-data-name'>
                                프로필 이미지
                            </div>
                            <input ref={userThumbnailRef} onChange={onUserThumbnailChangeHandler} type="file" accept="image/*" className="create-user-box-data-image" />
                        </div>
                        <div className='create-user-box-sub-box'>
                            <div className='create-user-box-data-name'>
                                닉네임
                            </div>
                            <input ref={userNicknameRef} onChange={onUserNicknameChangeHandler} type='text' className="create-user-box-data-input" placeholder="닉네임을 입력해주세요"/>
                        </div>
                        <div className='create-user-box-sub-box'>
                            <div className='create-user-box-data-name'>
                                나이
                            </div>
                            <input ref={userAgeRef} onChange={onUserAgeChangeHandler} className="create-user-box-data-input" placeholder="나이를 입력해주세요"/>
                        </div>
                        <div className='create-user-box-sub-box-short-intro'>
                            <div className='create-user-box-data-name'>
                                자기 소개
                            </div>
                            <textarea ref={userIntroduceRef} onChange={onUserIntroduceChangeHandler} className="create-user-box-data-text" placeholder="자기소개를 짧게 입력해주세요(30~100자)"/>
                        </div>
                    </div>
                </div>
                <div className='box-two'>
                    <button className='create-user-submit-btn' onClick={onCreateClubButtonClickHandler}>제출하기</button>
                </div>
            </div>
        </div>
    </div>
  )
}
