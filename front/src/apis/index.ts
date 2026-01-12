import axios, { Axios, AxiosResponse } from "axios";
import { CheckCertificationRequestDto, EmailCertificationRequestDto, IdCheckRequestDto, SignInRequestDto, SignUpRequestDto } from "./request/auth";
import { CheckCertificationResponseDto, EmailCertificationResponseDto, IdCheckResponseDto, SignInResponseDto, SignUpResponseDto } from "./response/auth";
import { CreateClubRequestDto, JoinClubRequestDto, SearchClubRequestDto, ShowClubListRequestDto } from "./request/club";
import { CreateClubResponseDto, JoinClubResponseDto, ShowClubListResponseDto } from "./response/club";
import CreateUserInfoRequestDto from "./request/user/create-user-info.request.dto";
import CreateUserInfoResponseDto from "./response/user/create-user-info.response.dto";
import { ResponseDto } from "./response";
import ShowClubListCategoryRequestDto from "./request/club/show-club-list-category.request.dto";
import dotenv from 'dotenv';

const responseHandler = <T>(response: AxiosResponse<any, any>) => {
    const responseBody: T = response.data;
    return responseBody;
}

const errorHandler = (error: any) => {
    if(!error.response || !error.response.data) return null;
    const responseBody: ResponseDto = error.response.data;
    return responseBody;
}

const hostname = "43.202.199.147";
const API_BASE_URL = `http://${hostname}:4040`;
const API_DOMAIN = `${API_BASE_URL}/api/v1`;

export const SNS_SIGN_IN_URL = (type: 'kakao' | 'google') => `${API_DOMAIN}/auth/oauth2/${type}`;
const SIGN_IN_URL = () => `${API_DOMAIN}/auth/sign-in`;
const SIGN_UP_URL = () => `${API_DOMAIN}/auth/sign-up`;
const ID_CHECK_URL = () => `${API_DOMAIN}/auth/id-check`;
const EMAIL_CERTIFICATION_URL = () => `${API_DOMAIN}/auth/email-certification`;
const CHECK_CERTIFICATION_URL = () => `${API_DOMAIN}/auth/check-certification`;

const SHOW_CLUB_LIST_URL = () => `${API_DOMAIN}/response/show-club-list`;
const SHOW_CLUB_LIST_BY_CATEGORY_URL = () => `${API_DOMAIN}/response/show-club-list-category`;
const SHOW_CLUB_DETAIL_URL = (clubId: any) => `${API_DOMAIN}/response/show-club/${clubId}`;
const SEARCH_CLUB_URL = () => `${API_DOMAIN}/response/search-club`
const SHOW_HOST_USER = (clubId: any) => `${API_DOMAIN}/response/show-host-user/${clubId}`;
const SHOW_ALL_USER = (clubId: any) => `${API_DOMAIN}/response/show-all-user/${clubId}`
const CREATE_CLUB_URL = () => `${API_DOMAIN}/user/post-club`;
const JOIN_CLUB = () => `${API_DOMAIN}/user/join-club`;

const SHOW_USER_INFO_URL = () => `${API_DOMAIN}/user/show-user-info`;
const CREATE_USER_INFO_URL = () => `${API_DOMAIN}/user/create-user-info`;
const SHOW_CLUB_LIST_BY_OWNERSHIP = () => `${API_DOMAIN}/user/show-club-list-owned`;
const SHOW_CLUB_LIST_BY_JOINED = () => `${API_DOMAIN}/user/show-club-list-joined`;
const SHOW_NOTIFICATION_LIST_BY_OWNER = () => `${API_DOMAIN}/user/show-notification-list`;
const NOTIFICATION_IS_READ = (notificationId: any) => `${API_DOMAIN}/response/message-read/${notificationId}`

const SHOW_CHAT_ROOM_LIST_BY_USER = () => `${API_DOMAIN}/user/show-chat-room`;
const GET_GENERATED_ID = (roomId: any) => `${API_DOMAIN}/user/get-join-chat/${roomId}`;
const SHOW_MESSAGE_LIST_BY_ROOM_ID = (roomId: any) => `${API_DOMAIN}/user/show-message-list/${roomId}`;
const CLUB_INVITEMENT_REQUEST = (roomName: string) => `${API_DOMAIN}/user/chat/join-chat-invitation/${roomName}`;


export const signInRequest = async (requestBody: SignInRequestDto) => {
    const result = await axios.post (SIGN_IN_URL(), requestBody)
            .then(responseHandler<SignInResponseDto>)
            .catch(errorHandler)
    return result;
}

export const signUpRequest = async (requestBody: SignUpRequestDto) =>{
    const result = await axios.post(SIGN_UP_URL(), requestBody)
            .then(responseHandler<SignUpResponseDto>)
            .catch(errorHandler)
    return result;
}

export const idCheckRequest = async (requestBody: IdCheckRequestDto) =>{
    const result = await axios.post(ID_CHECK_URL(), requestBody)
            .then(responseHandler<IdCheckResponseDto>)
            .catch(errorHandler)
    return result;
}

export const emailCertificationRequest = async (requestBody: EmailCertificationRequestDto) => {
    const result = await axios.post(EMAIL_CERTIFICATION_URL(), requestBody)
            .then(responseHandler<EmailCertificationResponseDto>)
            .catch(errorHandler);
    return result;
}

export const CheckCertificationRequest = async (requestBody: CheckCertificationRequestDto) => {
    const result = await axios.post(CHECK_CERTIFICATION_URL(), requestBody)
            .then(responseHandler<CheckCertificationResponseDto>)
            .catch(errorHandler)
    return result;
}


export const showClubListRequest = async (requestBody: ShowClubListRequestDto) => {
    const result = await axios.get(SHOW_CLUB_LIST_URL(), {params: requestBody});
    return result.data;
}

export const showClubListByCategoryRequest = async (requestBody: ShowClubListCategoryRequestDto) => {
    const result = await axios.get(SHOW_CLUB_LIST_BY_CATEGORY_URL(), {params: requestBody});
        
    return result.data;
}

export const searchClubRequest = async (requestBody: SearchClubRequestDto) => {
    const result = await axios.get(SEARCH_CLUB_URL(), {params: requestBody});
        
    return result.data;
}

export const showClubDetailRequest = async (clubId: any) => {
    const result = await axios.get(SHOW_CLUB_DETAIL_URL(clubId));
    return result.data;
}

export const createClubRequest = async (formData: FormData, token: any) => {
    const result = await axios.post(
        CREATE_CLUB_URL(), 
        formData,{
        headers: {
            'Authorization': `Bearer ${token}`,
        }})
                .then(responseHandler<CreateClubResponseDto>)
                .catch(errorHandler)
    return result;
}

export const joinClubRequest = async (requestBody: JoinClubRequestDto, token: any) => {
    const result = await axios.post(
        JOIN_CLUB(), 
        requestBody, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }})
        .then(responseHandler<JoinClubResponseDto>)
        .catch(errorHandler)
    return result;
}


export const showUserInfoRequest = async (token: any) => {
    try{
        const result = await axios.get(
            SHOW_USER_INFO_URL(),{
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }})        
        return result.data;
    }catch(error: any){
        return error.response.data;
    }
}

export const createUserInfoRequest = async (formData: FormData, token: any) => {
    const result = await axios.post(
        CREATE_USER_INFO_URL(), 
        formData,{
        headers: {
            'Authorization': `Bearer ${token}`
        }}
    )
            .then(responseHandler<CreateUserInfoResponseDto>)
            .catch(errorHandler)
    return result;
}

export const showClubListByOwnerShip = async (token: any) => {
    const result = await axios.get(SHOW_CLUB_LIST_BY_OWNERSHIP(), {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
        }
    })
    return result.data;
}

export const showUserInfoByClub = async (clubId: any) => {
    const result = await axios.get(SHOW_HOST_USER(clubId))
    return result.data;
}

export const showAllUserInfoByClub = async (clubId: any) => {
    const result = await axios.get(SHOW_ALL_USER(clubId))
    return result.data;
}

export const showClubListByJoined = async (token: any) => {
    const result = await axios.get(SHOW_CLUB_LIST_BY_JOINED(), {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
        }
    })
    return result.data;
}

export const showNotificationListByOwner = async (token: any) => {
    const result = await axios.get(SHOW_NOTIFICATION_LIST_BY_OWNER(), {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
        }
    })
    return result.data;
}

export const notificationIsRead = async (notificationId: any, token: any)=>{
    const result = await axios.post(NOTIFICATION_IS_READ(notificationId));
    return result;
}


export const showChatRoomByUser = async (token: any) => {
    try{
        const result = await axios.get(
            SHOW_CHAT_ROOM_LIST_BY_USER(),{
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }})
        
        return result.data;
    }catch(error: any){
        return error.response.data;
    }
}

export const getGeneratedId = async (roomId: any, token: any) => {
    try{
        const result = await axios.get(
            GET_GENERATED_ID(roomId),{
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }})
        
        return result.data;
    }catch(error: any){
        return error.response.data;
    }
}

export const showMessagesByRoomId = async (roomId: any, token: any) => {
    try{
        const result = await axios.get(
            SHOW_MESSAGE_LIST_BY_ROOM_ID(roomId),{
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }})
        
        return result.data;
    }catch(error: any){
        return error.response.data;
    }
}

export const clubInviteRequest = async (roomName: string, token: any) => {
    try{
        const result = await axios.get(
            CLUB_INVITEMENT_REQUEST(roomName),{
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }})
            .then(responseHandler<JoinClubResponseDto>)
            .catch(errorHandler)
        
        return result;
    }catch(error: any){
        return error.response.data;
    }
}