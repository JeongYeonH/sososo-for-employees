import ResponseDto from "../response.dto";
import ClubDto from "./club.dto";


export default interface ShowClubDetailResponseDto extends ResponseDto{
    club: ClubDto
}