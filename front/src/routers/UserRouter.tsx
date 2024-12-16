import React from 'react'
import { Route, Routes } from 'react-router-dom'
import { JoinChatPage } from 'views/ChatPage';
import { CreateClubPage } from 'views/Club/CreateClubPage';
import CreateUserPage from 'views/User/CreateUserPage';
import ViewUserPage from 'views/User/ViewUserPage'

function UserRouter (){
  return (
    <Routes>
      <Route path= 'personal-profile' element={<ViewUserPage/>} />
      <Route path= 'create-club' element={<CreateClubPage/>}/>
      <Route path= 'create-profile' element={<CreateUserPage/>} />
      <Route path= 'chat' element={<JoinChatPage/>} />
    </Routes>
  )
}

export default UserRouter;