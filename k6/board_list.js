import http from 'k6/http';
import { sleep, check } from 'k6';
import { SharedArray } from 'k6/data';

export const options = {
    scenarios: {
        open_model: {
            executor: 'ramping-arrival-rate',
            startRate: 5,
            timeUnit: '1s',
            preAllocatedVUs: 10,
            maxVUs: 50,

            stages: [
                { target: 10, duration: '5s'},
                { target: 30, duration: '10s'},
                { target: 0, duration: '5s'}
            ],
        },
    },
};

export default function(){
    
    const size = 8;
    const page = [0, 1, 2];
    const type = ['Popular', 'Latest'];
    const category = ['스터디', '문화', '스포츠', '레저', '여행', '맛집', '종교', '친목', '기타'];

    const randomPage = page[Math.floor(Math.random() * page.length)];
    const randomType = type[Math.floor(Math.random() * type.length)];
    const randomCategory = encodeURIComponent(category[Math.floor(Math.random() * category.length)]);

    const useCategory = Math.random() < 0.5;
    
    const base = 'http://localhost:4040';
    const command1 = 'show-club-list';
    const command2 = 'show-club-list-category';

    function customUrl(base, command, size, randomPage, randomType){
        const custom = base
                + `/api/v1/response/${command}?`
                + `size=${size}`
                + `&page=${randomPage}`
                + `&type=${randomType}`;
        
        return custom;
    }
    
    let url = null;
    
    if(!useCategory){
        url = customUrl(base, command1, size, randomPage, randomType);
    }else{
        url = customUrl(base, command2, size, randomPage, randomType) 
                + `&category=${randomCategory}`;
    }

    const res = http.get(
        url ,{
                headers: {
                    Origin: 'http://localhost:3000'
                },
            }
        );
    
    check(res, {
        'status is 200': (r) => r.status == 200,
    });

    sleep(1)
}

//redis 있을 시 .. max가 가끔 튐
    //HTTP
    //http_req_duration..............: avg=4.64ms min=1.37ms med=3.08ms max=32.42ms p(90)=7.92ms p(95)=8.44ms
    //  { expected_response:true }...: avg=4.64ms min=1.37ms med=3.08ms max=32.42ms p(90)=7.92ms p(95)=8.44ms
    //http_req_failed................: 0.00%  0 out of 293
    //http_reqs......................: 293    14.224/s

//redis 없을 시
    //HTTP
    //http_req_duration..............: avg=8.5ms min=4.13ms med=6.62ms max=32.74ms p(90)=19.4ms p(95)=20.61ms
    //  { expected_response:true }...: avg=8.5ms min=4.13ms med=6.62ms max=32.74ms p(90)=19.4ms p(95)=20.61ms
    //http_req_failed................: 0.00%  0 out of 293
    //http_reqs......................: 293    14.223049/s