import React, { ChangeEvent, useRef, useState } from 'react'
import { CreateClubRequestDto } from 'apis/request/club';
import { createClubRequest } from 'apis';
import './style.css'
import { useNavigate } from 'react-router-dom';
import { getCookie } from 'utils';

export function CreateClubPage() {

  const clubTitleRef = useRef<HTMLInputElement | null>(null);
  const clubIntroduceRef= useRef<HTMLTextAreaElement | null>(null);
  const clubRequirementRef = useRef<HTMLTextAreaElement | null>(null);
  const clubActivityRef = useRef<HTMLTextAreaElement | null>(null);
  const clubThumbnailRef = useRef<HTMLInputElement | null>(null);

  const [clubTitle, setClubTitle] = useState<string>('');
  const [clubIntroduce, setClubIntroduce] = useState<string>('');
  const [clubRequirement, setClubRequirement] = useState<string>('');
  const [clubActivity, setClubActivity] = useState<string>('');
  const [clubThumbnail, setClubThumbnail] = useState<File | null>(null);

  const [isTypeOpen, setIsTypeOpen] = useState(false);
  const [selectedType, setSelectedType] = useState(String);
  const [isLocationOpen, setIsLocationOpen] = useState(false);
  const [selectedLocation, setSelectedLocation] = useState(String);
  const [isMaxNumberOpen, setIsMaxNumberOpen] = useState(false);
  const [selectedMaxNumber, setSelectedMaxNumber] = useState(String);

  const navigate = useNavigate();

  const onclubTitleChangeHandler = (event: ChangeEvent<HTMLInputElement>) =>{
    const { value } = event.target;
    setClubTitle(value);   
  }

  const onClubIntroduceChangeHandler = (event: ChangeEvent<HTMLTextAreaElement>) =>{
    const { value } = event.target;
    setClubIntroduce(value);   
  }
  const onClubRequirementChangeHandler = (event: ChangeEvent<HTMLTextAreaElement>) =>{
    const { value } = event.target;
    setClubRequirement(value);   
  }
  const onClubActivityChangeHandler = (event: ChangeEvent<HTMLTextAreaElement>) =>{
    const { value } = event.target;
    setClubActivity(value);   
  }
  const onClubThumbnailChangeHandler = (event: ChangeEvent<HTMLInputElement>) =>{
    const file = event.target.files?.[0];
    if(file){
      setClubThumbnail(file);   
    }
  }

  const onCreateClubButtonClickHandler = () => {
    if(!clubTitle || !clubIntroduce || !clubRequirement || !clubActivity 
      || !clubThumbnail || !selectedType || !selectedLocation || !selectedMaxNumber 
    ){
      alert('요구되는 사항을 모두 입력해주세요.');
    }
    
    const formData = new FormData();
    formData.append("club_title", clubTitle);
    formData.append("club_short_intro", clubIntroduce);
    formData.append("club_member_requirements", clubRequirement);
    formData.append("club_activity_description", clubActivity);
    
    formData.append("club_type", selectedType);
    formData.append("club_location", selectedLocation);
    formData.append("club_total_num", selectedMaxNumber);

    formData.append("club_current_member_num", "1");
    formData.append("club_page_visited_num", "1");
    
    if(clubThumbnail){
      formData.append("club_thumbnail", clubThumbnail); 
    }
    const token = getCookie('accessToken');

    if(!token){
      alert('로그인이 필요합니다.');
    }

    createClubRequest(formData, token).then();
    navigate("/", { replace: true });

  };


  const types = ["문화",  "스포츠", "레저", "여행","맛집", "종교", "친목", "기타"];
  const locations = ["서울/경기", "충청권", "호남권" , "대구경북", "부산경남", "강원권", "제주도"]
  const maxNumbers = ["5", "10", "15", "20", "30", "40", "50", "75", "100"]

  const handleTypeClick = (item: string) => {
    setSelectedType(item);
    setIsTypeOpen(false);
  }

  const handleLocationClick = (item: string) => {
    setSelectedLocation(item);
    setIsLocationOpen(false);
  }

  const handleMaxNumberClick = (item: string) => {
    setSelectedMaxNumber(item);
    setIsMaxNumberOpen(false);
  }


  return (
    <div className='create-club-wrapper'>
      <div className='create-club-wrpper-box'>
        <div className='default-wrapper'>
          <li>소모임 공고 등록</li>
          <div className='create-club-box'>
            <div className='create-club-title-wrapper'>
              <li>소모임 이름 <span className='highlight'>*</span></li>
              <input ref={clubTitleRef} onChange={onclubTitleChangeHandler} type='text' className="create-club-title-input" placeholder="소모임 이름을 입력해 주세요 (5~15자)"/>
            </div>
            <div className='create-club-text-input-wrapper'>
              <li>소모임 소개글 <span className='highlight'>*</span></li>
              <textarea ref={clubIntroduceRef} onChange={onClubIntroduceChangeHandler} className="create-club-text-input" placeholder="소모임 소개글을 작성해 주세요"/>
              <div className='create-club-text-input-short-brief'>· 최소 20자에서 최대 200자까지 입력이 가능합니다.</div>
            </div>
            <div className='create-club-text-input-wrapper'>
              <li>자격요건 <span className='highlight'>*</span></li>
              <textarea ref={clubRequirementRef} onChange={onClubRequirementChangeHandler} className="create-club-text-input" placeholder="소모임 가입 요건을 작성해 주세요"/>
              <div className='create-club-text-input-short-brief'>· 최소 20자에서 최대 200자까지 입력이 가능합니다.</div>
            </div>
            <div className='create-club-text-input-wrapper'>
              <li>소모임 활동 <span className='highlight'>*</span></li>
              <textarea ref={clubActivityRef} onChange={onClubActivityChangeHandler} className="create-club-text-input" placeholder="소모임에서 어떤 활동을 하는지 작성해 주세요"/>
              <div className='create-club-text-input-short-brief'>· 최소 20자에서 최대 200자까지 입력이 가능합니다.</div>
            </div>
            <div className='create-club-text-input-wrapper'>
              <li>이미지 파일</li>
              <input ref={clubThumbnailRef} onChange={onClubThumbnailChangeHandler} type="file" accept="image/*" className="create-club-image-input" />
            </div>
          </div>
        </div>
        <div className='create-club-status-box'>
            <ul className='create-club-status-list'>
              <div className='create-club-status-list-title'>소모임 항목</div>
              <div className='flex justify-between items-center'>
                <li className='create-club-status-text'>소모임 타입</li>
                <button 
                onClick={()=> setIsTypeOpen((prev) => !prev)}
                className='create-club-status-choice-btn'>
                  {selectedType ? selectedType : "타입 선택"}
                </button>
              {isTypeOpen &&(
                <div className='create-club-choice-toogle-box'>
                  {types.map((itm, index) =>(
                    <div className='create-club-choice-toogle-card'
                      key={index} onClick={()=>handleTypeClick(itm)}>
                      {itm}
                    </div>
                  ))}
                </div>
              )}
              </div>
              <div className='flex justify-between items-center'>
                <li className='create-club-status-text'>소모임 활동위치</li>
                <button 
                onClick={()=> setIsLocationOpen((prev) => !prev)}
                className='create-club-status-choice-btn'>
                  {selectedLocation ? selectedLocation : "위치 선택"}
                </button>
                {isLocationOpen &&(
                <div className='create-club-choice-toogle-box-loc'>
                  {locations.map((itm, index) =>(
                    <div className='create-club-choice-toogle-card'
                      key={index} onClick={()=>handleLocationClick(itm)}>
                      {itm}
                    </div>
                  ))}
                </div>
              )}
              </div>
              <div className='flex justify-between items-center'>
                <li className='create-club-status-text'>소모임 인원제한</li>
                <button 
                onClick={()=> setIsMaxNumberOpen((prev) => !prev)}
                className='create-club-status-choice-btn'>
                  {selectedMaxNumber ? selectedMaxNumber : "인원 선택"}
                </button>
                {isMaxNumberOpen &&(
                <div className='create-club-choice-toogle-box-num'>
                  {maxNumbers.map((itm, index) =>(
                    <div className='create-club-choice-toogle-card'
                      key={index} onClick={()=>handleMaxNumberClick(itm)}>
                      {itm}
                    </div>
                  ))}
                </div>
              )}
              </div>
            </ul>
            <div className='create-club-status-description'>본 공고 등록자는 소모임 호스트로 자동 등록되며, 추가 회원 가입 시 소모임 삭제가 불가합니다.</div>
            <button className='create-club-status-button' onClick={onCreateClubButtonClickHandler}>등록하기</button>
        </div>
      </div>
    </div>
  )
}
