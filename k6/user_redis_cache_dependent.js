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
    const page = 0;
    const type = 'Popular';
    const category = '맛집';

    const urlDefault = `http://localhost:4040/api/v1/response/show-club-list?size=${size}&page=${page}&type=${type}`;
    const urlCategory = `http://localhost:4040/api/v1/response/show-club-list?size=${size}&page=${page}&type=${type}&category=${category}`;

    const res = http.get(
        urlDefault  ,{
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

//redis 실행 시
    //HTTP
    //http_req_duration..............: avg=6.7ms min=4ms med=5.99ms max=56.39ms p(90)=7.63ms p(95)=9.39ms
    //  { expected_response:true }...: avg=6.7ms min=4ms med=5.99ms max=56.39ms p(90)=7.63ms p(95)=9.39ms
    //http_req_failed................: 0.00%  0 out of 293
    //http_reqs......................: 293    14.223044/s

//redis 없을 시
    //HTTP
    //http_req_duration..............: avg=24.69ms min=19ms  med=22.17ms max=116.24ms p(90)=27.55ms p(95)=34.19ms
    //  { expected_response:true }...: avg=24.69ms min=19ms  med=22.17ms max=116.24ms p(90)=27.55ms p(95)=34.19ms
    //http_req_failed................: 0.00%  0 out of 292
    //http_reqs......................: 292    14.162479/s