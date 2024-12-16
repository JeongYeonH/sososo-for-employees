export default interface ChatRoomDto {
    clubId: Number,
    chatRoomId: Number;
    chatRoomName: string;
    chatRoomGeneratedDate: Date;
    chatRoomStatus: Boolean;
    clubThumbnailUrl: string;
    clubCurrentMemberNum: Number;
    newestMessage: string;
}