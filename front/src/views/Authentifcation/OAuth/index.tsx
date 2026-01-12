import React, { useEffect } from 'react'
import { useCookies } from 'react-cookie';
import { useNavigate, useParams } from 'react-router-dom'

export default function OAuth() {
  
    const { token, expirationTime } = useParams();
    const [cookie, setCookie] = useCookies();
  
    useEffect(()=> {

        if(!token || !expirationTime) return;

        console.log('토큰 발급!')
        const now = Date.now();
        const expires = new Date(now + Number(expirationTime));
        setCookie('accessToken', token, {expires, path: '/' });
        
        window.location.replace('/');

    }, [token, expirationTime]);
    return (
      <div></div>
  )
}

