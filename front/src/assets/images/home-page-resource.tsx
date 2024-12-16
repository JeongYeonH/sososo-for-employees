import React, { useEffect, useState } from 'react';
import '../../App.css'

export const SearchIconImage = () => {
  const imageUrl = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fcm0DX5%2FbtsKwB96EkT%2FfGMcfNEURgK0n1DIOLmxyK%2Fimg.png";

  return (
    <div>
      <img className='search-icon' src={imageUrl} alt="Search Icon" />
    </div>
  );
};

export const SoSoSoLogo = () => {
  const imageUrl = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbakMcQ%2FbtsKkfMMvw6%2F5vt6gkeQt8sqUsCtOYUC4K%2Fimg.png";

  return (
    <div>
      <img className='sososo-logo' src={imageUrl} alt="SoSoSo Logo" />
    </div>
  );
};

