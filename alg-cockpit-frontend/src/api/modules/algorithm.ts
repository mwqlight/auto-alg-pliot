import request from '../index'
import type { ApiResponse, PageRequest, PageResponse } from '@/types/api'
import type { Algorithm, AlgorithmCategory, AlgorithmCode } from '@/types/algorithm'

// 算法搜索参数
export interface AlgorithmSearchParams extends PageRequest {
  categoryId?: number
  keyword?: string
  type?: string
}

export const algorithmApi = {
  // 获取算法分类列表
  getCategories: (): Promise<ApiResponse<AlgorithmCategory[]>> => {
    return request.get('/api/v1/categories')
  },

  // 获取算法列表（分页）
  getAlgorithms: (params: AlgorithmSearchParams): Promise<ApiResponse<PageResponse<Algorithm>>> => {
    return request.get('/api/v1/algorithms', { params })
  },

  // 根据分类获取算法列表
  getAlgorithmsByCategory: (categoryId: number): Promise<ApiResponse<Algorithm[]>> => {
    return request.get(`/api/v1/algorithms/category/${categoryId}`)
  },

  // 获取算法详情
  getAlgorithm: (id: number): Promise<ApiResponse<Algorithm>> => {
    return request.get(`/api/v1/algorithms/${id}`)
  },

  // 获取算法代码
  getAlgorithmCode: (algorithmId: number, language: string): Promise<ApiResponse<AlgorithmCode>> => {
    return request.get(`/api/v1/algorithms/${algorithmId}/code/${language}`)
  },

  // 保存算法代码
  saveAlgorithmCode: (data: {
    algorithmId: number
    language: string
    code: string
  }): Promise<ApiResponse<void>> => {
    return request.post('/api/v1/algorithms/code', data)
  },

  // 搜索算法
  searchAlgorithms: (keyword: string): Promise<ApiResponse<Algorithm[]>> => {
    return request.get(`/api/v1/algorithms/search?keyword=${keyword}`)
  },

  // 获取热门算法
  getPopularAlgorithms: (): Promise<ApiResponse<Algorithm[]>> => {
    return request.get('/api/v1/algorithms/popular')
  },

  // 按类型获取算法
  getAlgorithmsByType: (type: string): Promise<ApiResponse<Algorithm[]>> => {
    return request.get(`/api/v1/algorithms/type/${type}`)
  }
}