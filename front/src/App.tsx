import React, {ChangeEvent, useState} from 'react';
import { Route, Routes, useLocation } from 'react-router-dom';
import SignUp from 'views/Authentifcation/SignUp';
import SignIn from 'views/Authentifcation/SignIn';
import OAuth from 'views/Authentifcation/OAuth';
import HomePage from 'views/HomePage';
import { HeaderComponent } from 'views/Common/Header/header';
import { FooterComponent } from 'views/Common/Footer/footer';
import { ClubPage } from 'views/Club/ViewClubPage';
import { CreateClubPage } from 'views/Club/CreateClubPage';
import './App.css';
import UserRouter from 'routers/UserRouter';



export const isLoggedIn = () => {
  return document.cookie.includes('accessToken');

};



function App() {

  const location = useLocation();
  const hideHeaderPaths = ['/auth/sign-up', '/auth/sign-in'];
  const hideHeader = hideHeaderPaths.includes(location.pathname)

  return (
    <>
      {!hideHeader && <HeaderComponent />}
      <Routes>
        <Route path='/auth'>
          <Route path='sign-up' element={<SignUp/>}/>
          <Route path='sign-in' element={<SignIn/>}/>
          <Route path='oauth-response/:token/:expirationTime' element={ <OAuth/>}/>
        </Route>
        <Route path='/user/*' element={<UserRouter/>}/>
        <Route path='/' element={<HomePage/>}/>
        <Route path='/response'>
          <Route path='show-club/:clubId' element={<ClubPage/>}/>
        </Route>
      </Routes>
      <FooterComponent />
    </>
  );
}

export default App;
