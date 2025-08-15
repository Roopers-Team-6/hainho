import http from 'k6/http';
import {check, sleep} from 'k6';
// 🔧 URL 폴리필
import {URL} from 'https://jslib.k6.io/url/1.0.0/index.js';

export const options = {
    vus: Number(__ENV.VUS || 10),
    duration: __ENV.DURATION || '30s',
};

// 다른 프로젝트라 컨테이너에서 호출 시 호스트 포트로 우회
// (Mac은 host.docker.internal 지원)
const BASE = __ENV.BASE_URL || 'http://host.docker.internal:8080/api/v1/products';

export default function () {
    const u = new URL(BASE);
    // 필요한 쿼리 파라미터 채우기
    if (!u.searchParams.get('brandId')) u.searchParams.set('brandId', '1');
    if (!u.searchParams.get('page')) u.searchParams.set('page', '0');
    if (!u.searchParams.get('size')) u.searchParams.set('size', '20');
    if (!u.searchParams.get('sort')) u.searchParams.set('sort', 'lastest');
    u.searchParams.set('_t', String(Date.now())); // 캐시 우회

    const res = http.get(u.toString(), {
        headers: {'X-USER-ID': '1', 'Cache-Control': 'no-cache'},
    });

    check(res, {'status 200': r => r.status === 200});

    if (__VU === 1 && __ITER === 0) {
        console.log(`[DEBUG] GET ${u} -> ${res.status}`);
    }

    sleep(1);
}
