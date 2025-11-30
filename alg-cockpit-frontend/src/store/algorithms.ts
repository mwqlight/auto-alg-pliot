import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Algorithm, AlgorithmCategory } from '@/types/algorithm'
import { algorithmApi } from '@/api/modules/algorithm'

export const useAlgorithmsStore = defineStore('algorithms', () => {
  // 算法分类列表
  const categories = ref<AlgorithmCategory[]>([])
  
  // 算法列表
  const algorithms = ref<Algorithm[]>([])
  
  // 当前选中的算法
  const currentAlgorithm = ref<Algorithm | null>(null)
  
  // 热门算法
  const popularAlgorithms = ref<Algorithm[]>([])
  
  // 加载状态
  const loading = ref<boolean>(false)
  
  // 错误信息
  const error = ref<string | null>(null)

  // 获取算法分类列表
  const fetchCategories = async () => {
    try {
      loading.value = true
      error.value = null
      const response = await algorithmApi.getCategories()
      if (response.code === 200) {
        categories.value = response.data
      }
    } catch (err: any) {
      error.value = err.message || '获取分类列表失败'
    } finally {
      loading.value = false
    }
  }

  // 获取算法列表
  const fetchAlgorithms = async (params?: any) => {
    try {
      loading.value = true
      error.value = null
      const response = await algorithmApi.getAlgorithms(params)
      if (response.code === 200) {
        algorithms.value = response.data.items
      }
    } catch (err: any) {
      error.value = err.message || '获取算法列表失败'
    } finally {
      loading.value = false
    }
  }

  // 获取算法详情
  const fetchAlgorithmDetail = async (id: number) => {
    try {
      loading.value = true
      error.value = null
      const response = await algorithmApi.getAlgorithm(id)
      if (response.code === 200) {
        currentAlgorithm.value = response.data
      }
    } catch (err: any) {
      error.value = err.message || '获取算法详情失败'
    } finally {
      loading.value = false
    }
  }

  // 获取热门算法
  const fetchPopularAlgorithms = async () => {
    try {
      loading.value = true
      error.value = null
      const response = await algorithmApi.getPopularAlgorithms()
      if (response.code === 200) {
        popularAlgorithms.value = response.data
      }
    } catch (err: any) {
      error.value = err.message || '获取热门算法失败'
    } finally {
      loading.value = false
    }
  }

  // 搜索算法
  const searchAlgorithms = async (keyword: string) => {
    try {
      loading.value = true
      error.value = null
      const response = await algorithmApi.searchAlgorithms(keyword)
      if (response.code === 200) {
        algorithms.value = response.data
      }
    } catch (err: any) {
      error.value = err.message || '搜索算法失败'
    } finally {
      loading.value = false
    }
  }

  // 清除当前算法
  const clearCurrentAlgorithm = () => {
    currentAlgorithm.value = null
  }

  // 清除错误信息
  const clearError = () => {
    error.value = null
  }

  return {
    categories,
    algorithms,
    currentAlgorithm,
    popularAlgorithms,
    loading,
    error,
    fetchCategories,
    fetchAlgorithms,
    fetchAlgorithmDetail,
    fetchPopularAlgorithms,
    searchAlgorithms,
    clearCurrentAlgorithm,
    clearError
  }
})