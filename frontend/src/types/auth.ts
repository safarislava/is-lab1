export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  message: string;
  username: string;
  roles: string[];
}

export interface UserResponse {
  id: number;
  username: string;
  roles: string[];
}
