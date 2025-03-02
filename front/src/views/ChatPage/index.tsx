import { getGeneratedId, joinClubRequest, showAllUserInfoByClub, showChatRoomByUser, showMessagesByRoomId, showUserInfoRequest } from "apis";
import React, { useState, useEffect, useRef } from "react";
import SockJS from "sockjs-client";
import { Client, Stomp } from "@stomp/stompjs";
import { getCookie } from "utils";
import { IoPerson } from "react-icons/io5";
import { HiBars3 } from "react-icons/hi2";
import { BsArrowUpCircleFill } from "react-icons/bs";
import { DefaultPersonProfileImage } from "assets/images/club-page-resource";
import ChatRoomDto from "apis/response/chat/chat-room.dto";
import TextWithEllipsis from "components/CheckLengthOfText/checkingFuction";
import UserInfoDto from "apis/response/user/user-info.dto";
import { JoinClubRequestDto } from "apis/request/club";
import { JoinClubResponseDto } from "apis/response/club";
import { ResponseBody } from "types";
import { ResponseCode } from "types/enums";
import './style.css'
import { useLocation } from "react-router-dom";
import dotenv from 'dotenv';


export function JoinChatPage() {

    interface ChatMessage {
        sender: string;
        content: string;
        sentTime: Date;
        profileImgUrl: string;
        chatGeneratedId: Number;
    }

    const [messages, setMessages] = useState<ChatMessage[]>([]);
    const [messageInput, setMessageInput] = useState('');
    const [legacyMessages, setLegacyMessages] = useState([]);
    const [stompClient, setStompClient] = useState< Client | null>(null);
    const [userInfoData, setUserInfoData] = useState<UserInfoDto | null>(null);
    const [allUserInfoData, setAllUserInfoData] = useState<UserInfoDto[] | null>(null);
    const [userChatRooms, setUserChatRooms] = useState<ChatRoomDto[] | null>(null);
    const [currentChatRoom, setCurrentChatRoom] = useState<ChatRoomDto | null>(null);
    const [generatedId, setGeneratedId] = useState(Number);
    const [joinerUserID, setJoinerUserID] = useState<string | null>(null);

    const [isOpen, setIsOpen] = useState(false);

    const token = getCookie('accessToken');
    const location = useLocation();
    
    const userId = userInfoData?.userId;

    useEffect(()=>{
        const fetchClubData = async () =>{ 
            const userInfoResponse = await showUserInfoRequest(token)    
            const chatRoomResponse = await showChatRoomByUser(token) as { chatRoomList: ChatRoomDto[] };;    
            setUserInfoData(userInfoResponse);
            setUserChatRooms(chatRoomResponse.chatRoomList);

            console.log(chatRoomResponse.chatRoomList)
            console.log(userChatRooms)
            const queryParams = new URLSearchParams(location.search);
            const clubToClick = queryParams.get('club');
            if(clubToClick !== null){
                console.log('자동 클릭이 실행됩니다');
                const roomId = clubToClick.replace('/room/', '');
                const targetChatRoom 
                = chatRoomResponse.chatRoomList.find(chatRoom => chatRoom.chatRoomId.toString() === roomId);
                console.log(userChatRooms);
                if(targetChatRoom){
                    console.log('타겟 채팅방을 찾았습니다');
                    handleRoomClick(targetChatRoom);
                }else{
                    console.warn('대상 채팅방을 찾을 수 없습니다.');
                }
            }
        }
        fetchClubData();
    },[location])

    // 채팅 방 접속 시, 메시지 추가 시 메시지 바가 아래로 스크롤 됩니다
    const chatFrameRef = useRef<HTMLDivElement | null>(null);
    useEffect(()=>{
        if (chatFrameRef.current) {
            chatFrameRef.current.scrollTop = chatFrameRef.current.scrollHeight;
        }
    }, [messages])

    useEffect(() => {
        const notificationClient = new Client({
            webSocketFactory: () => new SockJS("http://localhost:4040/api/v1/user/chat"),
            onConnect: () => {
                notificationClient.subscribe(`/room/notifications/${userId}`, (message) => {
                    const notification = JSON.parse(message.body);
                });
            },
            onStompError: (frame) => {
                console.error("Notification STOMP error:", frame);
            },
        });
    
        notificationClient.activate();
    
        return () => {
            if (notificationClient.connected) {
                notificationClient.deactivate();
            }
        };
    }, [currentChatRoom?.chatRoomId]);
    
    useEffect(() => {
        if (messages.length > 0) {
            const firstMessage = messages[0].content;
            const extractedID = extractIDFromMessage(firstMessage);
            if (extractedID) {
                setJoinerUserID(extractedID);
            }
        }
    }, [messages]); 

    useEffect(() => {
        const fetchLegacyMessages = async () =>{
            try{
                if(currentChatRoom?.chatRoomId !=null){
                    const legacyList = await showMessagesByRoomId(currentChatRoom.chatRoomId, token);
                    setMessages(legacyList.messageList);
                }
            }catch(error){
                console.error("Error fetching legacy messages:", error);
            }
        }
        setMessages([]);
        fetchLegacyMessages();
    }, [currentChatRoom?.chatRoomId])

    const joinRoom = async (chatRoom: ChatRoomDto) =>{            
        if (stompClient && stompClient.connected) {
            stompClient.deactivate();
        }

        const client = new Client({
            
            webSocketFactory: () => 
                new SockJS(`http://localhost:4040/api/v1/user/chat?Authorization=Bearer ${token}&roomId=${chatRoom.chatRoomId.toString()}`),
            connectHeaders:{},
            onConnect: () => {
                client.subscribe(`/room/${chatRoom.chatRoomId}`, (message)=>{
                    setMessages(
                        (prevMessages) => [
                                ...prevMessages, 
                                JSON.parse(message.body)
                        ]
                )});
                setStompClient(client); 
                setCurrentChatRoom(chatRoom);     

            },
            onStompError: (frame) => {
                console.error("STOMP error:", frame);
            },
        });

        client.activate();

        const genID = await getGeneratedId(chatRoom.chatRoomId, token);
        const allUserInfos = await showAllUserInfoByClub(chatRoom.clubId);
        setGeneratedId(genID.chatGeneratedId);
        setAllUserInfoData(allUserInfos.userInfoList);
    }

    const sendMessage = () =>{
        // console.log('메시지 발송 시작')
        if (!currentChatRoom?.chatRoomId) {
            alert("현재 방이 선택되지 않았습니다.");
            return;
        }
        const sentTime = new Date();
        const formatToKST = (date: any) => {         
            const offset = 9 * 60; 
            const localDate = new Date(date.getTime() + offset * 60000); 
            return localDate.toISOString(); 
        };
        const isoSentTime = formatToKST(sentTime);
        const message = { 
            sender: userInfoData?.nickName, 
            content: messageInput,
            roomId: currentChatRoom.chatRoomId,
            sentTime: isoSentTime,
            profileImgUrl: userInfoData?.userThumbnailUrl,
            chatGeneratedId: generatedId
        };
        

        if(stompClient && stompClient.connected){
            stompClient.publish({
                destination : `/room/chat.sendMessage/${currentChatRoom.chatRoomId}`,
                body: JSON.stringify(message),
            });
            setMessageInput("");
        }else{
            console.error("STOMP 클라이언트가 연결되어 있지 않습니다.");
        }
    }

    const handleKeyDown = (event: any) =>{
        if(event.key === 'Enter'){
            sendMessage();
        }
    }


    const joinClubResponse = (responseBody: ResponseBody<JoinClubResponseDto>) => {
        if(!responseBody) return;
        const { code } = responseBody;
        if(code===ResponseCode.UN_AUTHORIZED) alert('권한이 없습니다.');

        return alert('가입이 승인되었습니다.')
    }

    const joinClubButtonClickHandler = () => {
        // console.log('실행?')
        const joinerUserId = joinerUserID ? joinerUserID : "없음";
        const clubId = currentChatRoom?.clubId ? currentChatRoom.clubId : 9999;
        const requestBody: JoinClubRequestDto = {clubId, joinerUserId};
        joinClubRequest(requestBody, token).then(joinClubResponse);
    }

    const handleRoomClick = (chatRoom: ChatRoomDto) => {
        joinRoom(chatRoom);
    }

    const extractIDFromMessage = (messageText: string) => {
        const combinedRegex = /(<ID>.*?<\/ID>)/g;
        const matches = messageText.match(combinedRegex);
        if (matches && matches.length > 0) {
            return matches[0].replace(/<\/?ID>/g, "");
        }
        return null;
    };

    const renderMessageBlock = (messageText: string) => {
        const btnTxt = messageText.split("${승인하기}");
        const combinedRegex = /(<name>.*?<\/name>|<ID>.*?<\/ID>|<club>.*?<\/club>)/g;

        const parseTags = (text: string) => {
            const parts = text.split(combinedRegex);
                return text.split(combinedRegex).map((part, index) => {
                    if (part.startsWith("<name>")) {
                        return (
                        <span className="strong-stress-chat-message">
                            {part.replace(/<\/?name>/g, "")}
                        </span>
                        );
                    } else if (part.startsWith("<ID>")) {
                    }else if (part.startsWith("<club>")) {
                        return (
                        <span className="strong-stress-chat-message">
                            {part.replace(/<\/?club>/g, "")}
                        </span>
                        );
                    }else {
                        return part; 
                    }
                });
            };
        
        if(btnTxt.length > 1){
            return(
                <span>
                    {parseTags(btnTxt[0])}
                    <button 
                    className="chat-room-invite-request-btn" 
                    onClick={ () => joinClubButtonClickHandler()}>
                        승인하기
                    </button>
                </span>
            )
        }
        return <span>{messageText}</span>
    }

    const renderProps = (messageText: string) =>{
        if (!messageText) {
            return null; 
        }
        const combinedRegex = /<\/?(name|ID|club)>|\$\{승인하기\}/g;
        const cleanedText = messageText.replace(combinedRegex, "");
        return cleanedText;
    }

    const renderDateSting = (dateString: any) => {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); 
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const formattedDate = `${year}년 ${month}월 ${day}일 ${hours}시 ${minutes}분`;
        return formattedDate;
    }

    const toogleAccordion = () => {
        setIsOpen(!isOpen);
    }

    return (
        <div className="chat-room-wrapper">
            <div className="chat-room-container">
                <div className="chat-room-list">
                    <div className="chat-room-title-title">
                        채팅방
                    </div>
                    <div className="chat-room-list-frame">
                        {userChatRooms?.map((chatRoom, index) => (
                            <div className="chat-room-list-card" 
                            key={chatRoom.chatRoomId.toString()} 
                            onClick={() => handleRoomClick(chatRoom) }>
                                <div className="chat-room-list-card-thumbnail"><img className="default-image" src={chatRoom.clubThumbnailUrl} alt="" /></div>
                                <div className="chat-room-list-card-text-container">
                                    <div className="chat-room-list-card-text-titles">
                                        <div className="chat-room-list-card-text-title"><TextWithEllipsis text={chatRoom.chatRoomName} maxLength={12} /></div>
                                        <div className="chat-room-list-card-text-members">{chatRoom.clubCurrentMemberNum.toString()}<IoPerson className="icon-adjust"/></div>  
                                    </div>
                                    <div className="chat-room-list-card-text-recent-message"><TextWithEllipsis text={renderProps(chatRoom.newestMessage)} maxLength={20} /></div>
                                </div>                        
                            </div>
                        ))}
                    </div>
                </div>
                <div className="chat-room-chats">
                    <div className="flex justify-between items-center w-full border-b-[1px] border-b-[rgb(235, 235, 235)]">
                        <div className="chat-room-title">
                            <div className="chat-room-list-card-thumbnail">
                                <img className="default-image" src={currentChatRoom?.clubThumbnailUrl} alt="" />
                            </div>
                            {currentChatRoom?.chatRoomName || "방을 선택하세요"}
                            <div className="chat-room-title-joined-memebers">
                                {currentChatRoom?.clubCurrentMemberNum.toString() || "0"}
                                <IoPerson className="icon-adjust"/>
                            </div>
                        </div>
                        <div className="mr-5 text-xl text-gray-500 cursor-pointer hover:text-gray-800" onClick={toogleAccordion}>
                            <HiBars3 />
                        </div>
                    </div>
                    <div className="chat-room-chat-frame" ref={chatFrameRef}>
                        {messages.map((msg, index)=>(
                            <div className="chat-room-chat-comment-card" key={index}>
                                <div className="chat-room-chat-comment-img">
                                    <img src={msg.profileImgUrl} alt="" />
                                </div>
                                <div className="chat-room-chat-comment-infos">
                                    <div className="chat-room-chat-comment-info-titles">
                                        <div className="chat-room-chat-comment-info-sender">{msg.sender}</div>
                                        <div className="chat-room-chat-comment-info-time">{renderDateSting(msg.sentTime)}</div>
                                    </div>
                                    <div className="chat-room-chat-comment-info-message">{renderMessageBlock(msg.content)}</div>
                                </div>
                                
                            </div>
                        ))}
                    </div>
                    <div className="chat-room-chat-input-box">
                        <textarea onKeyDown={handleKeyDown} 
                            value={messageInput}
                            className="chat-room-chat-input"
                            placeholder="서로를 존중하는 매너를 보여주세요 :)"
                            onChange={(e) => setMessageInput(e.target.value)}
                        />
                        <button className="chat-room-chat-input-btn" onClick={sendMessage} ><BsArrowUpCircleFill/></button>
                    </div>
                </div>
                {isOpen && (
                            <div className='chat-room-member-list-box'>
                                <div className="h-[51px] flex items-center font-medium w-full border-b-[1px] border-b-[rgb(235, 235, 235)]">
                                    <div className="chat-room-title">
                                    대화상대
                                    </div>     
                                </div>
                                <div className="chat-room-member-list">
                                {allUserInfoData?.map((member) => (
                                    <div className="flex h-[66px] items-center justify-between cursor-pointer hover:bg-blue-100" 
                                    key={member.userId.toString()} 
                                    onClick={() => console.log('클릭')}>
                                        <div className="flex items-center">
                                            <div className="chat-room-list-card-thumbnail"><img className="default-image" src={member.userThumbnailUrl.toString()} alt="" /></div>
                                            <div className="font-medium text-sm"><TextWithEllipsis text={member.nickName.toString()} maxLength={6} /></div>  
                                        </div>                        
                                        <div className="text-xs mr-3">회원</div>
                                    </div>
                                ))}
                                </div>
                            </div>
                        )}               
            </div>
        </div>
    )
}