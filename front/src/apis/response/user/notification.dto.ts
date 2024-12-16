import ResponseDto from "../response.dto";

export default interface NotificationDto extends ResponseDto{
    notificationId: number;
    roomName: String;
    notificationContent: String;
    notificationSentTime: Date
    stacks: number;
    route: String;
}