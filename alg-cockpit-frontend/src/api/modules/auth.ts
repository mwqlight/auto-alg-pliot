import request from '../index'
import type { ApiResponse } from '@/types/api'
import type { 
  LoginRequest, 
  LoginResponse, 
  RegisterRequest, 
  UserInfo, 
  UpdateUserRequest 
} from '@/types/user'

/**
 * 用户登录
 */
export const login = (data: LoginRequest): Promise<ApiResponse<LoginResponse>> => {
  return request.post('/auth/login', data)
}

/**
 * 用户注册
 */
export const register = (data: RegisterRequest): Promise<ApiResponse<void>> => {
  return request.post('/auth/register', data)
}

/**
 * 用户登出
 */
export const logout = (): Promise<ApiResponse<void>> => {
  return request.post('/auth/logout')
}

/**
 * 获取当前用户信息
 */
export const getCurrentUser = (): Promise<ApiResponse<UserInfo>> => {
  return request.get('/users/profile')
}

/**
 * 更新用户信息
 */
export const updateUser = (data: UpdateUserRequest): Promise<ApiResponse<UserInfo>> => {
  return request.put('/users/profile', data)
}

/**
 * 修改密码
 */
export const changePassword = (data: {
  oldPassword: string
  newPassword: string
}): Promise<ApiResponse<void>> => {
  return request.put('/users/password', data)
}