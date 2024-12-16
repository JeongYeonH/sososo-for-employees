import React, { useEffect, useState } from 'react'
import { DefaultPersonProfileImage } from 'assets/images/club-page-resource'
import { ClubMemberProfile } from 'components/Profile/profile'
import { useParams } from 'react-router-dom'
import { clubInviteRequest, showAllUserInfoByClub, showClubDetailRequest, showUserInfoByClub } from 'apis'
import ShowClubDetailResponseDto from 'apis/response/club/show-club-detail.response.dto'
import './style.css'
import { getCookie } from 'utils'
import { ResponseBody } from 'types'
import { JoinClubResponseDto } from 'apis/response/club'
import { ResponseCode } from 'types/enums'
import UserInfoDto from 'apis/response/user/user-info.dto'

export function ClubPage() {

    const clubMemberList = [1, 2, 3];
    const {clubId} = useParams();
    const [clubData, setClubData] = useState<ShowClubDetailResponseDto | null >(null);
    const [hostUserData, setHostUserData] = useState<UserInfoDto | null >(null);
    const [allUserData, setAllUserData] = useState<UserInfoDto[] | null >(null);

    const invitationResponse = (responseBody: ResponseBody<JoinClubResponseDto>) => {
        if(!responseBody) return;
        const { code } = responseBody;
        console.log(code)
        if(code == ResponseCode.VALIDATION_FAILED) return alert("권한이 없습니다.");
        if(code == ResponseCode.DUPLICATED_DATA) return alert("중복 요청입니다.");
        if(code == ResponseCode.SUCCESS) return alert("초대 요청이 발송되었습니다.")

    }


    useEffect(()=>{
        const fetchClubData = async () =>{
            const requestParam = clubId;
            const clubData = await showClubDetailRequest(requestParam);
            const hostUserData = await showUserInfoByClub(requestParam);
            const allUserData = await showAllUserInfoByClub(requestParam);
            setClubData(clubData);
            setHostUserData(hostUserData);
            setAllUserData(allUserData.userInfoList);
        }
        fetchClubData();
    },[clubId])


    const RequestInvitationFromHost = async () => {
        const token = getCookie('accessToken');
        const roomName = clubData?.club.clubTitle ?? "오류가 있습니다";
        await clubInviteRequest(roomName, token).then(invitationResponse)
    }

    console.log(clubData);

  return (
    <div id='club-page-wrapper'>
        <div className='club-detail-section'>
            <div className='club-detail-main-image'>
            <img className="default-image" src={clubData?.club.clubThumbnailUrl} alt="DefaultPersonProfileImage" />
            </div>
        </div>
        <div className='club-content-container'>
            <div className='club-content-box'>
                <div className='club-content-box-title-first'>
                    <h1 className='content-title'>{clubData?.club.clubTitle}</h1>
                    <button className='join-button' onClick={RequestInvitationFromHost}>가입하기</button>
                </div>
                <div className='club-content-box-title-second'>
                    <h3 className='content-title-description-list'>
                        <div className='content-title-tags'>{clubData?.club.clubType}</div>
                        <div className='content-title-tags'>{clubData?.club.clubLocation}</div>
                        <div className='content-title-tags'>최대 {clubData?.club.clubTotalNum}명</div>
                    </h3>
                    <h3 className='join-button-description'>클릭 시 '호스트'에게 자동으로 요청을 보냅니다.</h3>
                </div>
                <div className='club-content-box-descriptions'>
                    <ul className='club-content-box-descriptions-with-list'>
                        <li className='sub-titles'>저희 소모임을 소개해요</li>
                        <li className='sub-discriptions'>{clubData?.club.clubShortIntro}</li>
                        <li className='sub-titles'>회원 자격요건</li>
                        <li className='sub-discriptions'>{clubData?.club.clubMemberRequirements}</li>
                        <li className='sub-titles'>소모임 활동</li>
                        <li className='sub-discriptions'>{clubData?.club.clubActivityDescription}</li>
                    </ul>
                    <div className='club-content-box-member-list'>
                        <div className='club-member-info'>
                            소모임 회원 소개
                        </div>
                        <div className='club-host-profile'>
                            <div className='profile-image'>
                                <img className="default-image" src={hostUserData?.userThumbnailUrl.toString()} alt="DB Error" />
                            </div>
                            <li>{hostUserData?.nickName}</li>
                            <div className='club-host-mark'>호스트</div>
                            <button className='club-host-profile-contact-button'>연락하기</button>                           
                        </div>
                        <div className='club-member-info-others'>
                            회원 목록
                        </div>
                        <div className='club-member-info-others-sections'>
                            {allUserData?.map((userData) =>(
                                <div className='club-member-info-box' >
                                    <img className='default-image' src={userData.userThumbnailUrl.toString()} alt="" />
                                </div>
                            ))}
                        </div>
                        <div className='club-member-total-number'>
                            회원 수: {allUserData?.length} 명
                        </div>
                    </div>
                </div>
                <div className='extra-space'>

                </div>
            </div>
        </div>
    </div>
  )
}
