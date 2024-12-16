import ResponseDto from "../response.dto";
import ChatRoomDto from "./chat-room.dto";


export default interface ShowChatRoomListDto extends ResponseDto{
    chatRooms: ChatRoomDto[];
}