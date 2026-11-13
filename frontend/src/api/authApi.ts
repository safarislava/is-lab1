import { request } from './httpClient';
import type { LoginRequest, RegisterRequest, AuthResponse, UserResponse } from '@/types/auth';

export const authApi = {
  login(data: LoginRequest): Promise<AuthResponse> {
    return request<AuthResponse>('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  register(data: RegisterRequest): Promise<UserResponse> {
    return request<UserResponse>('/api/auth/register', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  logout(): Promise<void> {
    return request<void>('/api/auth/logout', {
      method: 'POST',
    });
  },

  getCurrentUser(): Promise<UserResponse> {
    return request<UserResponse>('/api/auth/me', {
      method: 'GET',
    });
  },

  refresh(): Promise<AuthResponse> {
    return request<AuthResponse>('/api/auth/refresh', {
      method: 'POST',
    });
  },
};
