import React, { useEffect, useState } from 'react'
import { SearchIconImage, SoSoSoLogo } from 'assets/images/home-page-resource'
import { useNavigate } from 'react-router-dom'
import { ClubMemberProfile } from 'components/Profile/profile'
import { GoBell } from "react-icons/go";
import { BsChatText } from "react-icons/bs";
import UserInfoDto from 'apis/response/user/user-info.dto';
import { showUserInfoRequest } from 'apis';
import { getCookie } from 'utils';
import './navbar.css'



const Navbar = () => {
  const navigate = useNavigate();
  const [loggedIn, setLoggedIn] = useState(false);
  const [userInfoData, setUserInfoData] = useState<UserInfoDto | null>(null);
  

  const LoginButtonClickHandler = () => {
    navigate('/auth/sign-in')
  }

  const SignInButtonClickHandler = () => {
    navigate('/auth/sign-up')
  }

  const GoToPersonalPageClickHandler = () => {
    navigate('/user/personal-profile')
  }

  const GoToHomePageClickHandler = () => {
    navigate('/')
  }
  

  useEffect(() => {
    const token = getCookie('accessToken');
    setLoggedIn(!!token); 
  }, []);

  useEffect(()=>{
    const fetchClubData = async () =>{

      const token = getCookie('accessToken');
      const response = await showUserInfoRequest(token)        
      setUserInfoData(response);
    }
    fetchClubData();
  },[])
  



  const url = userInfoData?.userThumbnailUrl;

  return (
    <div className='navbar'>
      <div className='logos'>
        <div className='sososo-logo'>
          <SoSoSoLogo />
        </div>
        <div className='title' onClick={GoToHomePageClickHandler}>
          SoSoSo
        </div>
      </div>
        <div className='user-info'>
          { loggedIn ? (
            <>
              <div className='user-logged-info'>
                <BsChatText />
                <GoBell />
                <button className='user-go-to-profile' onClick={GoToPersonalPageClickHandler}>
                  <img className='default-image' src={url?.toString()} alt="" />
                </button>
              </div>
            </>
          ): (
            <>
              <button className='login-btn' onClick={LoginButtonClickHandler }>로그인</button>
              <button className='signin-btn' onClick={SignInButtonClickHandler }>회원가입</button>
            </>
          )}
        </div>
    </div>
  )
}

export default Navbar