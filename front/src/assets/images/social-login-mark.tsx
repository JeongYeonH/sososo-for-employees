import React, { useEffect, useState } from 'react';
import '../../App.css'

export const KaKaoTalkImage = () => {
  const imageUrl = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcHugeb%2FbtsJ2Z4yzW5%2FBSneumWXA0IkYw2vHMrWyK%2Fimg.png";

  return (
    <div>
      <img className='social-sign-in-button' src={imageUrl} alt="Google Drive Image" />
    </div>
  );
};

export const GoogleImage = () => {
    const imageUrl = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FWXEUk%2FbtsJ4vHCGxA%2FKMcdXsaZRBORmLKqIKSTZ0%2Fimg.png";
  
    return (
      <div>
        <img className='social-sign-in-button' src={imageUrl} alt="Google Drive Image" />
      </div>
    );
  };

