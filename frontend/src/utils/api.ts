import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1', // 백엔드 API의 전체 URL로 변경
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터: 모든 요청에 JWT 토큰을 추가합니다.
api.interceptors.request.use(
  (config) => {
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터: 토큰 만료 등 오류를 처리합니다.
// TODO: 향후 Refresh Token 로직을 여기에 추가할 수 있습니다.
api.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;

    // 401 Unauthorized 에러 (토큰 만료 등) 발생 시
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      // 여기에 Refresh Token을 사용하여 Access Token을 갱신하는 로직을 추가할 수 있습니다.
      // 현재는 간단하게 로그인 페이지로 리디렉션합니다.
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('userType');
      alert('세션이 만료되었습니다. 다시 로그인해주세요.');
      window.location.href = '/login';
      return Promise.reject(error);
    }
    return Promise.reject(error);
  }
);

export default api; 