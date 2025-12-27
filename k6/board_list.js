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
    const randomCategory = category[Math.floor(Math.random() * category.length)];

    const useCategory = Math.random() < 0.5;

    const url = `http://localhost:4040/api/v1/response/show-club-list?`
                        + `size=${size}`
                        + `&page=${randomPage}`
                        + `&type=${randomType}`;
    
    if(useCategory){
        //url += `&category=${randomCategory}`
    }

    const res = http.get(
        url  ,{
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

//redis 있을 시
    //HTTP
    //http_req_duration..............: avg=2.85ms min=1.38ms med=2.74ms max=14.04ms p(90)=3.49ms p(95)=3.86ms
    //  { expected_response:true }...: avg=2.85ms min=1.38ms med=2.74ms max=14.04ms p(90)=3.49ms p(95)=3.86ms
    //http_req_failed................: 0.00%  0 out of 293
    //http_reqs......................: 293    14.226679/s

//redis 없을 시
    //HTTP
    //http_req_duration..............: avg=13.39ms min=6.34ms med=9.1ms max=32.25ms p(90)=24.75ms p(95)=26.65ms
    //  { expected_response:true }...: avg=13.39ms min=6.34ms med=9.1ms max=32.25ms p(90)=24.75ms p(95)=26.65ms
    //http_req_failed................: 0.00%  0 out of 293
    //http_reqs......................: 293    14.21106/s