import { SearchIconImage } from 'assets/images/home-page-resource'
import { SlArrowDown } from "react-icons/sl";
import { CgSortAz } from "react-icons/cg";
import { ChangeEvent, useEffect, useRef, useState } from 'react';
import CardBox from 'components/Card/card';
import { SearchClubRequestDto, ShowClubListRequestDto } from 'apis/request/club';
import { ShowClubListResponseDto } from 'apis/response/club';
import { searchClubRequest, showClubListByCategoryRequest, showClubListRequest } from 'apis';
import ClubDto from 'apis/response/club/club.dto';
import { useNavigate } from 'react-router-dom';
import './style.css'
import ShowClubListCategoryRequestDto from 'apis/request/club/show-club-list-category.request.dto';


export default function HomePage() {

    const navigate = useNavigate();

    const [isOpen, setIsOpen] = useState(false);
    const [clubList, setClubList] = useState<ClubDto[]>([]);
    const [showingType, setShowingType] = useState(String);
    const [category, setCategory] = useState<string | null>(null);
    
    const [page, setPage] = useState(0);
    const [isLast, setIsLast] = useState(false);
    const [loading, setLoading] = useState(false);
    const [noData, setNoData] = useState(false);

    const keywordRef = useRef<HTMLInputElement | null>(null);
    const [keyword, setKeyword] = useState<string>('');

    const filterOps = ["Latest", "Popular"];
    const categoryOps = ["스터티", "문화", "스포츠", "레저", "여행", "맛집", "종교", "친목", "기타"];
    const quote = "아직 소모임이 존재하지 않습니다 :("

    useEffect(() => {
        setShowingType(filterOps[0]);
        fetchClubs(filterOps[0]);
    }, []);

    useEffect(() => {
        if (showingType && !category) {
            fetchClubs(showingType);
        }else{
            console.log('패치클럽 조건 불만족')
        }
    }, [page, showingType]);

    useEffect(() => {
        if(category){
            fetchClubsByCategories(showingType, category);
        }else{
            console.log("패치 바이 카테고리 조건 불만족")
        }
    }, [category]);

    useEffect(() => {
        window.addEventListener("scroll", handleScroll); 
        return () => window.removeEventListener("scroll", handleScroll); 
    }, [loading, isLast]);


    const fetchClubs = async (type: string) => {
        if(isLast || loading){
            console.log("조건이 성립되어 실행되지 않습니다/옵션")
            console.log(isLast)
            console.log(loading)
            return;
        }         
        setLoading(true); 

        const requestBody: ShowClubListRequestDto = { type, page, size: 8 };
        const response = await showClubListRequest(requestBody);
        const { clubs, last: lastPage } = response;

        if (!Array.isArray(clubs)) {
            console.error("clubs is not array", response);
            setLoading(false);
            return;
        }

        setClubList(prevClubs => [...prevClubs, ...clubs]);
        setIsLast(lastPage);
        setLoading(false);       
    }

    const fetchClubsByCategories = async (type: string, category: string) => {
        if(isLast || loading){
            console.log("조건이 성립되어 실행되지 않습니다/카테고리")
            return;
        } 

        console.log("패치클럽바이카테고리 실행")
        setLoading(true);
        const requestBody: ShowClubListCategoryRequestDto = { category, type, page, size: 8 };
        const response = await showClubListByCategoryRequest(requestBody);
        if(response.code === "ND"){
            setNoData(true);
        }else{
            setNoData(false);
            const { clubs: newClubs, last: lastPage } = response;
            setClubList((prevClubs) => [...prevClubs, ...newClubs]);
            setIsLast(lastPage);
            setLoading(false);  
        }       
    }

    const searchClubByKeyword = async (keyword: string) => {
        if(isLast || loading){
            console.log("조건이 성립되어 실행되지 않습니다/검색")
            console.log(isLast)
            console.log(loading)
            return;
        } 

        setLoading(true);
        const requestBody: SearchClubRequestDto = {keyword, page, size: 8 };
        const response = await searchClubRequest(requestBody);
        const { clubs: newClubs, last: lastPage } = response;
        
        if (!Array.isArray(newClubs)) {
            console.error("clubs is not array", response);
            setLoading(false);
            return;
        }
       
        setClubList(prevClubs => [...prevClubs, ...newClubs]);
        setIsLast(!!lastPage);
        setLoading(false);             
    }



    const handleScroll = () => {
        if (
            window.innerHeight + document.documentElement.scrollTop >=
                document.documentElement.offsetHeight - 50 &&
            !loading &&
            !isLast
        ) {
            setPage((prevPage) => prevPage + 1);
        }
    };




    const toogleAccordion = () => {
        setIsOpen(!isOpen);
    }

    const handleCardClick = (clubId: any) => {
        navigate(`/response/show-club/${clubId}`);
    }

    const handleFilterOps = (ops: any, index: number) => {
        setNoData(false);
        setShowingType(filterOps[index])
        setCategory(null);
        refreshHandler();
        setIsOpen(false);
    }

    const handleCategoryOps = (category: any) => {
        setCategory(category);
        refreshHandler();
    }

    const handleSearchingClubs = (keyword: string) => {
        setCategory(null);
        refreshHandler();
        searchClubByKeyword(keyword);
    }

    const onSearchClubKeydownHandler = (event:any) =>{
        console.log('카다운 실행')
        setIsLast(false)
        if(event.key ==='Enter'){
            console.log('엔터 인식')
            setCategory(null);
            refreshHandler();
            searchClubByKeyword(keyword);
        }
    }

    const onSearchingClubsKeywordHandler = (event: ChangeEvent<HTMLInputElement>) =>{
        const { value } = event.target;
        setKeyword(value);   
    }


    const refreshHandler = () => {
        setClubList([]);
        setPage(0);
        setIsLast(false);
        setLoading(false);
    }

    return (
        <div id='home-page-wrapper'>
            <div className='sections'>
                <div className='main-text'>
                    Fill your life with joy
                </div>
                <div className='sub-text'>
                    <p className='discription'>바쁜 직장인들에게 소확행을 전달하기 위한 커뮤니티,</p>
                    <p className='discription'>어디에서나 그 무엇이든 함께하면 즐거움이 두배!</p>               
                </div>

                <div className='sososo-search-box'>
                    <input ref={keywordRef} onChange={onSearchingClubsKeywordHandler} onKeyDown={(event) =>onSearchClubKeydownHandler(event)} type="sososo-text" placeholder='무엇이든 물어보살?'/>
                        <div className='search-icon' onClick={() => handleSearchingClubs(keyword)} >
                            <SearchIconImage />
                        </div>
                </div>

                <div className='category-box'>
                    <button className='category-filter' onClick={toogleAccordion}>
                        <div className='arrow-down'><SlArrowDown/></div>
                        <div className='category-btn'>{showingType}</div>
                    </button>
                        {isOpen && (
                            <ul className='category-list-chocies'>
                                {filterOps.map((ops, index)=>(
                                    <div className='category-list-chocies-card'
                                    key={index}
                                    onClick={()=> handleFilterOps(ops, index)}>
                                        {ops}
                                    </div>
                                ))}
                            </ul>
                        )}

                    <ul className='category-list'>
                        {categoryOps.map((category, index)=>(
                            <div className='category-list-card'
                            key={index}
                            onClick={() => handleCategoryOps(category)}>
                                {category}
                            </div>
                        ))}
                    </ul>

                    <button className='filter'>
                        <div className='kind-of-wifi-icon'><CgSortAz /></div>
                        <div className='filter-text'>Filters</div>
                    </button>
                </div>
            </div>
            { noData ? (
                <>
                <div className='h-[200px] flex items-center justify-center text-gray-400 text-3xl'>
                    {quote}
                </div>
                </>
            ):(
                <>
                <div className='card-sections'>
                    {clubList.map((club: ClubDto) =>(
                        <div className='card-box' key={club.clubId} onClick={() => {handleCardClick(club.clubId)}}>
                            <CardBox 
                            clubTitle={club.clubTitle} 
                            clubCardImage={club.clubThumbnailUrl} 
                            clubCurrentMemberNum={club.clubCurrentMemberNum} 
                            clubPageVisitedNum={club.clubPageVisitedNum}
                            clubCardLocation={club.clubLocation}/>
                        </div>
                    ))}
                </div>
                </>
            )}

        </div>
    )
}