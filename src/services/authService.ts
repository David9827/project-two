import { api } from './api';
import { LoginRequest, LoginResponse } from '../types';

export const authService = {
  // Đăng nhập
  login: async (username: string, password: string): Promise<LoginResponse> => {
    // Tạm thời mock response vì backend chưa có endpoint login
    // Trong thực tế, sẽ gọi API đến backend
    if (username === 'admin' && password === 'admin') {
      return Promise.resolve({
        token: 'mock-admin-token',
        user: {
          id: 1,
          username: 'admin',
          email: 'admin@example.com',
          role: 'ADMIN',
          firstName: 'Admin',
          lastName: 'User'
        }
      });
    } else if (username === 'user' && password === 'user') {
      return Promise.resolve({
        token: 'mock-user-token',
        user: {
          id: 2,
          username: 'user',
          email: 'user@example.com',
          role: 'USER',
          firstName: 'Regular',
          lastName: 'User'
        }
      });
    } else {
      return Promise.reject(new Error('Tên đăng nhập hoặc mật khẩu không đúng'));
    }
    
    // Uncomment khi backend có endpoint login
    // const response = await api.post<LoginResponse>('/api/auth/login', {
    //   username,
    //   password
    // });
    // return response.data;
  },

  // Đăng ký (sẽ implement sau)
  register: async (userData: any) => {
    const response = await api.post('/api/auth/register', userData);
    return response.data;
  },

  // Xác thực token
  validateToken: async (token: string) => {
    const response = await api.get('/api/auth/validate', {
      headers: { Authorization: `Bearer ${token}` }
    });
    return response.data;
  }
};
