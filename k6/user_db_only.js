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