import React, { useEffect } from 'react'
import { useCookies } from 'react-cookie';
import { useNavigate, useParams } from 'react-router-dom'

export default function OAuth() {
  
    const { token, expirationTime } = useParams();
    const [cookie, setCookie] = useCookies();
    const navigate = useNavigate();
  
    useEffect(()=> {

        if(!token || !expirationTime) return;

        console.log('토큰 발급!')
        const now = new Date().getTime()*1000;
        const expires = new Date(now + Number(expirationTime));
        setCookie('accessToken', token, {expires, path: '/' });
        
        navigate('/');
        window.location.reload();

    }, [token]);
    return (
      <div></div>
  )
}

