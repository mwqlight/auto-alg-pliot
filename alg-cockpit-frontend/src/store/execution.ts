import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { ExecuteRequest, ExecuteResponse } from '@/types/execution'
import { executionApi } from '@/api/modules/execution'

export const useExecutionStore = defineStore('execution', () => {
  // 执行结果
  const executionResult = ref<ExecuteResponse | null>(null)
  
  // 执行记录列表
  const executionRecords = ref<any[]>([])
  
  // 当前执行详情
  const currentExecution = ref<any | null>(null)
  
  // 执行状态
  const isExecuting = ref<boolean>(false)
  
  // 加载状态
  const loading = ref<boolean>(false)
  
  // 错误信息
  const error = ref<string | null>(null)

  // 执行算法代码
  const executeAlgorithm = async (request: ExecuteRequest) => {
    try {
      isExecuting.value = true
      error.value = null
      const response = await executionApi.execute(request)
      if (response.code === 200) {
        executionResult.value = response.data
      }
    } catch (err: any) {
      error.value = err.message || '执行算法失败'
    } finally {
      isExecuting.value = false
    }
  }

  // 获取执行记录列表
  const fetchExecutionRecords = async (params?: any) => {
    try {
      loading.value = true
      error.value = null
      const response = await executionApi.getExecutionRecords(params)
      if (response.code === 200) {
        executionRecords.value = response.data.items
      }
    } catch (err: any) {
      error.value = err.message || '获取执行记录失败'
    } finally {
      loading.value = false
    }
  }

  // 获取执行详情
  const fetchExecutionDetail = async (executionId: string) => {
    try {
      loading.value = true
      error.value = null
      const response = await executionApi.getExecutionDetail(executionId)
      if (response.code === 200) {
        currentExecution.value = response.data
      }
    } catch (err: any) {
      error.value = err.message || '获取执行详情失败'
    } finally {
      loading.value = false
    }
  }

  // 停止算法执行
  const stopExecution = async (executionId: string) => {
    try {
      error.value = null
      const response = await executionApi.stopAlgorithmExecution(executionId)
      if (response.code === 200) {
        // 停止成功
        return true
      }
    } catch (err: any) {
      error.value = err.message || '停止执行失败'
    }
    return false
  }

  // 验证代码安全性
  const validateCodeSecurity = async (code: string, language: string) => {
    try {
      error.value = null
      const response = await executionApi.validateSecurity(code, language)
      if (response.code === 200) {
        return response.data
      }
    } catch (err: any) {
      error.value = err.message || '代码安全验证失败'
    }
    return false
  }

  // 清除执行结果
  const clearExecutionResult = () => {
    executionResult.value = null
  }

  // 清除当前执行
  const clearCurrentExecution = () => {
    currentExecution.value = null
  }

  // 清除错误信息
  const clearError = () => {
    error.value = null
  }

  return {
    executionResult,
    executionRecords,
    currentExecution,
    isExecuting,
    loading,
    error,
    executeAlgorithm,
    fetchExecutionRecords,
    fetchExecutionDetail,
    stopExecution,
    validateCodeSecurity,
    clearExecutionResult,
    clearCurrentExecution,
    clearError
  }
})