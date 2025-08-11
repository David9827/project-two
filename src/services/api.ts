// src/services/api.ts
import axios from 'axios';

const api = axios.create({
    baseURL: process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080',
    headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        // đảm bảo luôn có headers
        config.headers = config.headers || {};
        const h = config.headers as any;

        // axios v1 có .set(); axios v0.x thì không
        if (typeof h.set === 'function') {
            h.set('Authorization', `Bearer ${token}`);
        } else {
            h['Authorization'] = `Bearer ${token}`;
        }
    }
    return config;
});

export default api;
// (tùy chọn) thêm named export để ai lỡ dùng `import { api }` vẫn chạy
export { api };
