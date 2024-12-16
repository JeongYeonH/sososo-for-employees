export default interface ClubDto {
    clubId: number;
    clubTitle: string;
    clubThumbnailUrl: string;
    clubShortIntro: string;
    clubMemberRequirements: string;
    clubActivityDescription: string;
    clubCurrentMemberNum: number;
    clubPageVisitedNum: number;

    clubLocation: string;
    clubType: string;
    clubTotalNum: number;
}