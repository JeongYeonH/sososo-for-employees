import React, { useEffect } from 'react'
import { useCookies } from 'react-cookie';
import { useNavigate, useParams } from 'react-router-dom';
import { useAuthStore } from 'store/state-store';

export default function OAuth() {
  
    const { token, expirationTime } = useParams();
    const [cookie, setCookie] = useCookies();
    const isLoggedIn = useAuthStore((state) => state.isLoggedIn);
  
    useEffect(()=> {

        if(!token || !expirationTime) return;
        

        console.log('토큰 발급!')
        const now = new Date().getTime()*1000;
        console.log('토큰 발급! - 2')
        const expires = new Date(now + Number(expirationTime));
        console.log('토큰 발급! - 3')
        setCookie('accessToken', token, {expires, path: '/' });
        useAuthStore.getState().login();
        console.log('토큰 발급! - 4')
        window.location.href = "/";

    }, [token]);
    return (
      <div></div>
  )
}

