export default interface CreateClubRequestDto {
    club_title: String,
    club_thumbnail: File,
    club_short_intro: String,
    club_member_requirements: String,
    club_activity_description: String,
    club_current_member_num: Number,
    club_page_visited_num: Number
}