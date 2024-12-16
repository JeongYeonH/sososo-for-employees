import ResponseDto from "../response.dto";
import UserInfoDto from "./user-info.dto";


export default interface ShowUserInfoResponseDto extends ResponseDto{
    userInfo: UserInfoDto
}