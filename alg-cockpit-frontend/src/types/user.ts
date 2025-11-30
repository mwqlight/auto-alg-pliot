export interface UserInfo {
  id: number
  username: string
  email: string
  role: string
  avatar?: string
  createdAt: string
  updatedAt: string
  lastLoginTime?: string
}

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  userInfo: UserInfo
}

export interface RegisterRequest {
  username: string
  email: string
  password: string
  confirmPassword: string
}

export interface UpdateUserRequest {
  username?: string
  email?: string
  avatar?: string
}