import React, { useEffect, useState } from 'react';
import '../../App.css'

export const DefaultCardImage = () => {
  const imageUrl = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbTb7h0%2FbtsKsm4U5Wu%2FhEUkN4Lgu4jcGPDIb3pMkk%2Fimg.jpg";

  return (
    <div>
      <img className="default-image" src={imageUrl} alt="Default Card Image" />
    </div>
  );
};




