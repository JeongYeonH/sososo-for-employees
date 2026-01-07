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

export function setup(){
    const url = 
        'http://43.202.199.147:4040/api/v1/response/show-club-list?size=8&page=0&type=Popular';
    const res = http.get(url);

    check(res, { 'list 200': r => r.status === 200 });

    const body = res.json();
    const clubs = body.clubs;

    if (!clubs || clubs.length === 0) {
        return;
    }

    const clubIds = clubs.map(v => v.clubId);
    const weights = getWeigths(clubIds.length);

    return {clubIds, weights};
}

function getWeigths(length, base = 0.8){
    const weights = [];
    for(let i = 0; i < length; i++){
        weights.push(Math.pow(base, i));
    }
    const total = weights.reduce((a, b) => a + b, 0);
    return weights.map(w => w/total);
}

function chooseRandomByWeights(clubIds, weights){
    let sum = 0;
    const r = Math.random();
    for(let i = 0; i < clubIds.length; i++){
        sum += weights[i];
        if(r <= sum){
            return clubIds[i];
        }
    }
}


export default function(data){ 

    const clubId = chooseRandomByWeights(data.clubIds, data.weights);

    const url = `http://43.202.199.147:4040/api/v1/response/show-club/${clubId}`;

    const res = http.get(
        url  ,{
                headers: {
                    Origin: 'http://43.202.199.147'
                },
            }
        );
    
    check(res, {
        'status is 200': (r) => r.status == 200,
    });

    sleep(1)
}


    // redis 있을 시
    //HTTP
    //http_req_duration..............: avg=7.06ms min=4.34ms med=6.88ms max=18.92ms p(90)=7.89ms p(95)=8.18ms
    //  { expected_response:true }...: avg=7.06ms min=4.34ms med=6.88ms max=18.92ms p(90)=7.89ms p(95)=8.18ms
    //http_req_failed................: 0.00%  0 out of 294
    //http_reqs......................: 294    14.261181/s

    // redis 없을 시
    //HTTP
    //http_req_duration..............: avg=7.79ms min=5.64ms med=7.32ms max=85.44ms p(90)=8.6ms p(95)=9.26ms
    //  { expected_response:true }...: avg=7.79ms min=5.64ms med=7.32ms max=85.44ms p(90)=8.6ms p(95)=9.26ms
    //http_req_failed................: 0.00%  0 out of 294
    //http_reqs......................: 294    14.204972/



// 배포 테스트 환경
//redis 있을 시
    //HTTP
    //http_req_duration..............: avg=13.94ms min=11.12ms med=13.5ms max=49.54ms p(90)=15.69ms p(95)=16.66ms
    //  { expected_response:true }...: avg=13.94ms min=11.12ms med=13.5ms max=49.54ms p(90)=15.69ms p(95)=16.66ms
    //http_req_failed................: 0.00%  0 out of 294
    //http_reqs......................: 294    14.253106/s

//redis 없을 시 
    //HTTP
    //http_req_duration..............: avg=21.67ms min=16.71ms med=20.55ms max=93.06ms p(90)=23.65ms p(95)=26.09ms
    //  { expected_response:true }...: avg=21.67ms min=16.71ms med=20.55ms max=93.06ms p(90)=23.65ms p(95)=26.09ms
    //http_req_failed................: 0.00%  0 out of 294
    //http_reqs......................: 294    14.19439/s

