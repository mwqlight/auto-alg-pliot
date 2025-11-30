// API响应格式
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
  timestamp: number
}

// 分页请求参数
export interface PageRequest {
  page: number
  size: number
  sort?: string
  order?: 'asc' | 'desc'
}

// 分页响应数据
export interface PageResponse<T = any> {
  items: T[]
  total: number
  page: number
  size: number
  pages: number
}

// 通用列表响应
export interface ListResponse<T = any> {
  items: T[]
  total: number
}

// 通用ID参数
export interface IdParams {
  id: string | number
}

// 通用搜索参数
export interface SearchParams {
  keyword?: string
  [key: string]: any
}