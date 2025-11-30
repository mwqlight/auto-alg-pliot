import request from '../index'
import type { ApiResponse, PageRequest, PageResponse } from '@/types/api'
import type { ExecuteRequest, ExecuteResponse } from '@/types/execution'

// 执行记录搜索参数
export interface ExecutionSearchParams extends PageRequest {
  algorithmId?: number
  userId?: number
  status?: string
  startTime?: string
  endTime?: string
}

export const executionApi = {
  // 执行算法代码
  execute: (data: ExecuteRequest): Promise<ApiResponse<ExecuteResponse>> => {
    return request.post('/api/v1/algorithms/execute', data)
  },

  // 获取执行记录列表
  getExecutionRecords: (params: ExecutionSearchParams): Promise<ApiResponse<PageResponse<any>>> => {
    return request.get('/api/v1/algorithms/executions', { params })
  },

  // 获取执行详情
  getExecutionDetail: (executionId: string): Promise<ApiResponse<any>> => {
    return request.get(`/api/v1/algorithms/executions/${executionId}`)
  },

  // 停止算法执行
  stopAlgorithmExecution: (executionId: string): Promise<ApiResponse<void>> => {
    return request.post(`/api/v1/algorithms/executions/${executionId}/stop`)
  },

  // 验证代码安全性
  validateSecurity: (code: string, language: string): Promise<ApiResponse<boolean>> => {
    return request.post('/api/v1/algorithms/validate-security', { code, language })
  },

  // 获取执行统计
  getStatistics: (algorithmId: number): Promise<ApiResponse<any>> => {
    return request.get(`/api/v1/algorithms/${algorithmId}/statistics`)
  },

  // 健康检查
  healthCheck: (): Promise<ApiResponse<string>> => {
    return request.get('/api/v1/algorithms/health')
  }
}