import http from 'k6/http';
import { sleep, check } from 'k6';
import { SharedArray } from 'k6/data';

export const options = {
    scenarios: {
        open_model: {
            executor: 'ramping-arrival-rate',
            startRate: 100,
            timeUnit: '20s',
            preAllocatedVUs: 10,
            maxV
        }
    }
}

export default function(){
    http.get('https://k6.io')
    sleep(1)
}