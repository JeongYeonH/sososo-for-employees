import React, { useEffect, useState, useRef } from 'react'
import { useNavigate } from 'react-router-dom';
import { DefaultPersonProfileImage } from 'assets/images/club-page-resource'
import { notificationIsRead, showClubListByJoined, showClubListByOwnerShip, showNotificationListByOwner, showUserInfoRequest } from 'apis';
import UserInfoDto from 'apis/response/user/user-info.dto';
import { LocalImages } from 'assets/links/image-links';
import { IoIosArrowDropleft, IoIosArrowDropright } from "react-icons/io";
import { FaLocationDot } from "react-icons/fa6";
import './style.css'
import { DefaultCardImage } from 'assets/images/card-image-resource';
import TextWithEllipsis from 'components/CheckLengthOfText/checkingFuction';
import { ShowClubListRequestDto } from 'apis/request/club';
import ClubDto from 'apis/response/club/club.dto';
import { getCookie } from 'utils';
import NotificationDto from 'apis/response/user/notification.dto';
import { useAuthStore } from 'store/state-store';

export default function ViewUserPage() {

  const [clubListJoined, setClubListJoined] = useState([]);
  const [clubListOwned, setClubListOwned] = useState([]);
  const [messageListOwned, setMessageListOwned] = useState<NotificationDto[] | null>(null);


  const [noData, setNoData] = useState(false);
  const [userInfoData, setUserInfoData] = useState<UserInfoDto | null>(null);
  const [isLoggedIn, setIsLoggedIn] = useState(true);

  const token = getCookie('accessToken');

  const navigate = useNavigate();
  const logout = useAuthStore((state) => state.logout);


  const handleLogout = () => {
    deleteCookie('accessToken');
    setIsLoggedIn(false);
    logout();
    navigate('/')
    window.location.reload();
  }

  const gotoCreateUserPage = () => {
    navigate('/user/create-profile')
  }

  const gotoDedicatedChatRoom = async (messageId: any, messageRoute: any) => {
    console.log(messageId);
    console.log(token);
    await notificationIsRead(messageId, token);
    navigate(`/user/chat?club=${messageRoute}`)
  }

  useEffect(()=>{
      const fetchClubData = async () =>{
        //console.log('시작')
        const response = await showUserInfoRequest(token)
        //console.log('리스폰스')
        //console.log(response)       
        if(response.code == "ND"){
          setNoData(true);
        }
        setUserInfoData(response);
      }
      fetchClubData();
  },[])

  useEffect(()=>{
    const fetchClubData = async () =>{

      const token = getCookie('accessToken');
      const responseJoinClub = await showClubListByJoined(token)
      const responseOwnClub = await showClubListByOwnerShip(token)
      const responseNotification  = await showNotificationListByOwner(token);        
      setClubListJoined(responseJoinClub.clubs);
      setClubListOwned(responseOwnClub.clubs);
      setMessageListOwned(responseNotification.notificationList);  
    }
    fetchClubData();
  },[])

  const deleteCookie = (name: any) => {
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
  };


  const GoToCreateClubPageClickHandler = () => {
    navigate('/user/create-club')
  }

  const GoToShowChatRoomsClickHandler = () => {
    navigate('/user/chat')
  }

  const handleCardClick = (clubId: any) => {
    //console.log('네비게이트 작동')
    navigate(`/response/show-club/${clubId}`);
  }

  const handleMessageClick = (messageId: any, messageRoute: any) => {
    gotoDedicatedChatRoom(messageId, messageRoute);
  }


  const scrollRef1 = useRef<HTMLDivElement>(null);
  const scrollRef2 = useRef<HTMLDivElement>(null);

  const scrollLeft = (Ref: any) => {
    if (Ref.current) {
      Ref.current.scrollBy({ left: -200, behavior: 'smooth' });
    }
  };

  const scrollRight = (Ref: any) => {
    if (Ref.current) {
      Ref.current.scrollBy({ left: 200, behavior: 'smooth' });
    }
  };


  const renderDateSting = (dateString: any) => {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); 
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const formattedDate = `${year}.${month}.${day} ${hours}:${minutes}`;
    return formattedDate;
  }


  return (
    <div id="user-page-viewer-wrapper">
      <div className='user-page-sections'>
        <div className='user-page-container'>
          <div className='container-profile-sub-list'>
            { noData ?  (
              <>
                <div className='container-profile-sub-upper'>
                  <div className='container-profile-container-title'>
                      <button className='container-profile-container-create-btn' onClick={gotoCreateUserPage}>
                        <p>프로필이 없습니다. <br />프로필을 작성해주세요.</p>
                        </button>
                  </div>
                </div>
              </>
            ):(
              <>
              <div className='container-profile-sub-upper'>
                <div  className='container-profile-container'>
                  <div className='container-profile-container-title'>
                    <li>프로필</li>
                    <button className='container-profile-container-edit-btn'>프로필 수정</button>
                  </div>
                  <div className='container-profile-container-image'>
                    <img className='default-image' src = {userInfoData?.userThumbnailUrl.toString()} alt="그냥 막아 놈" /> 
                  </div>
                  <div className='container-profile-container-nickname'>{userInfoData?.nickName}</div>                  
                  <div className='container-profile-container-id'>안녕하세요 회원님</div>
                </div>
              </div>
              <div className='container-profile-sub-middle'>
                <div className='container-profile-intro-list'>
                  <div className='container-profile-intro-title'>
                    <p>소개</p> 
                    <span className="intro-title-highlight-text">age {userInfoData?.age.toString()}</span>
                  </div>
                  <li className='container-profile-intro-content'>{userInfoData?.shortIntro}</li>
                </div>
              </div>
              </>
            )}
            <div className='container-profile-sub-lower'> 
              <button className='container-profile-logout-btn' onClick={handleLogout}>로그아웃</button>
            </div>
          </div>
          <div className='user-page-show-target-sections'>


            <div className='user-page-show-box-container'>
              <div className='show-target-clubs-titles'>
                <li>알림 관리</li>
                <button className='show-target-clubs-btn' onClick={GoToShowChatRoomsClickHandler}>채팅방 입장</button>               
              </div>
              <div className='user-page-message-descriptions'>
                <li>소모임 이름</li>
                <li>최신 메시지</li>
                <li>작성 시간</li>
              </div>
              { messageListOwned?.length == 0 ? (
                <>
                  <div className='user-page-message-box-frame-empty'>
                    <li>신규 알림이 없습니다.</li>
                  </div>
                </>
              ):(
                <>
                <div className='user-page-message-box-frame'>
                  {messageListOwned?.map((message, index) =>(
                    <div 
                    className='user-page-message-card' 
                    key={index} 
                    onClick={()=> handleMessageClick(message.notificationId, message.route)}>
                      <div className='user-page-message-card-title'><TextWithEllipsis text={message.roomName.toString()} maxLength={10} /></div>
                      <div className='user-page-message-card-inner'>
                        <div className='user-page-message-card-text'><TextWithEllipsis text={message.notificationContent.toString()} maxLength={10} /></div>
                        <div className='user-page-message-card-stack'>{message.stacks}</div>
                      </div>
                      <div className='user-page-message-card-time'><TextWithEllipsis text={renderDateSting(message.notificationSentTime.toString())} maxLength={21} /></div>
                    </div>
                  ))}
                </div>
                </>
              )}            
            </div>

            <div className='user-page-show-box-container'>
              <div className='show-target-clubs-titles'>
                <li>내가 회원인 소모임</li>               
              </div>
              
              <div className='user-page-show-target-clubs'>
                <button className='show-club-card-scroll-btn' onClick={() => scrollLeft(scrollRef1)}><IoIosArrowDropleft/></button>
                <div className='show-target-clubs-card-frame' ref={scrollRef1}>
                  {clubListJoined.length > 0 ?(
                    <>
                      {clubListJoined.map((club: ClubDto, index) =>(
                          <div className='show-target-clubs-card' 
                          key={index}
                          onClick={() => handleCardClick(club.clubId)} >
                            <div className='clubs-card-in-thumbnail' >
                              <img className='default-image' src={club.clubThumbnailUrl} alt="이미지 없음" />
                            </div>
                            <div className='clubs-card-in-title'>
                              <TextWithEllipsis text={club.clubTitle} maxLength={10} />
                            </div>
                            <div className='clubs-card-in-small-information'>
                              <div className='clubs-card-in-small-information-location'>
                                <FaLocationDot />
                                {club.clubLocation}
                              </div>
                              <div>{club.clubCurrentMemberNum}/{club.clubTotalNum}</div>
                            </div>
                            <button className='clubs-card-in-chat-btn'>채팅 참여하기</button>
                          </div>
                      ))}
                    </>
                  ):(
                    <>
                       <div className='show-club-empty-box-container'>
                          <li>아직 참여한 소모임이 없어요.</li>
                        </div>
                    </>
                  )}
                </div>                          
                <button className='show-club-card-scroll-btn' onClick={() => scrollRight(scrollRef1)}><IoIosArrowDropright /></button>          
              </div>             
            </div>
            

            <div className='user-page-show-box-container'>
              <div className='show-target-clubs-titles'>
                <li>내가 호스트한 소모임</li>
                <button className='show-target-clubs-btn' onClick={GoToCreateClubPageClickHandler}>소모임 만들기</button>
              </div>
              
              <div className='user-page-show-target-clubs'>
                <button className='show-club-card-scroll-btn' onClick={() => scrollLeft(scrollRef2)}><IoIosArrowDropleft/></button>
                <div className='show-target-clubs-card-frame' ref={scrollRef2}>
                    {clubListOwned.length > 0 ?(
                      <>
                        {clubListOwned.map((club: ClubDto, index) =>(
                            <div className='show-target-clubs-card' 
                            key={index}
                            onClick={() => handleCardClick(club.clubId)} >
                              <div className='clubs-card-in-thumbnail'>
                                <img className='default-image' src={club.clubThumbnailUrl} alt="이미지 없음" />
                              </div>
                              <div className='clubs-card-in-title'>
                                <TextWithEllipsis text={club.clubTitle} maxLength={10} />
                              </div>
                              <div className='clubs-card-in-small-information'>
                                <div className='clubs-card-in-small-information-location'>
                                  <FaLocationDot />
                                  {club.clubLocation}
                                </div>
                                <div>{club.clubCurrentMemberNum}/{club.clubTotalNum}</div>
                              </div>
                              <button className='clubs-card-in-chat-btn'>채팅 참여하기</button>
                            </div>
                        ))}                    
                      </>
                    ) : (
                      <>
                        <div className='show-club-empty-box-container'>
                          <li>아직 호스트한 소모임이 없어요.</li>
                        </div>
                      </>
                    )}
                    
                </div>                          
                <button className='show-club-card-scroll-btn' onClick={() => scrollRight(scrollRef2)}><IoIosArrowDropright /></button>          
              </div>
            </div>
          

          </div>
        </div>
      </div>
    </div>
  )
}
