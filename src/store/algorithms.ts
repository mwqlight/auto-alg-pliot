import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { Algorithm, AlgorithmCategory, ExecutionResult } from '@/types/algorithm'

export const useAlgorithmStore = defineStore('algorithms', () => {
  // 状态
  const algorithms = ref<Algorithm[]>([])
  const categories = ref<AlgorithmCategory[]>([])
  const selectedAlgorithm = ref<Algorithm | null>(null)
  const executionHistory = ref<ExecutionResult[]>([])
  const favorites = ref<string[]>([])
  const searchQuery = ref('')
  const selectedCategory = ref<string>('all')
  const loading = ref(false)
  const error = ref<string | null>(null)

  // 计算属性
  const filteredAlgorithms = computed(() => {
    let filtered = algorithms.value
    
    // 按分类过滤
    if (selectedCategory.value !== 'all') {
      filtered = filtered.filter(alg => alg.category === selectedCategory.value)
    }
    
    // 按搜索查询过滤
    if (searchQuery.value.trim()) {
      const query = searchQuery.value.toLowerCase()
      filtered = filtered.filter(alg => 
        alg.name.toLowerCase().includes(query) ||
        alg.description.toLowerCase().includes(query) ||
        alg.tags.some(tag => tag.toLowerCase().includes(query))
      )
    }
    
    return filtered
  })

  const favoriteAlgorithms = computed(() => {
    return algorithms.value.filter(alg => favorites.value.includes(alg.id))
  })

  const recentExecutions = computed(() => {
    return executionHistory.value
      .sort((a, b) => b.timestamp - a.timestamp)
      .slice(0, 10)
  })

  const executionStats = computed(() => {
    const total = executionHistory.value.length
    const successful = executionHistory.value.filter(e => e.success).length
    const failed = total - successful
    const avgExecutionTime = executionHistory.value.reduce((sum, e) => sum + e.executionTime, 0) / total || 0
    
    return {
      total,
      successful,
      failed,
      successRate: total > 0 ? (successful / total) * 100 : 0,
      avgExecutionTime
    }
  })

  // 方法
  const loadAlgorithms = async () => {
    loading.value = true
    error.value = null
    
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      // 模拟算法数据
      algorithms.value = [
        {
          id: 'bubble-sort',
          name: '冒泡排序',
          description: '一种简单的排序算法，重复遍历要排序的数列，一次比较两个元素',
          category: 'sorting',
          difficulty: 'easy',
          timeComplexity: 'O(n²)',
          spaceComplexity: 'O(1)',
          tags: ['排序', '基础', '稳定'],
          language: 'java',
          code: `public class BubbleSort {
  public static void bubbleSort(int[] arr) {
    int n = arr.length;
    for (int i = 0; i < n - 1; i++) {
      for (int j = 0; j < n - i - 1; j++) {
        if (arr[j] > arr[j + 1]) {
          // 交换元素
          int temp = arr[j];
          arr[j] = arr[j + 1];
          arr[j + 1] = temp;
        }
      }
    }
  }
}`,
          testCases: [
            { input: '[5, 3, 8, 4, 2]', expected: '[2, 3, 4, 5, 8]' },
            { input: '[1, 2, 3, 4, 5]', expected: '[1, 2, 3, 4, 5]' },
            { input: '[5, 4, 3, 2, 1]', expected: '[1, 2, 3, 4, 5]' }
          ]
        },
        {
          id: 'quick-sort',
          name: '快速排序',
          description: '使用分治策略的排序算法，通过选择一个基准元素将数组分成两部分',
          category: 'sorting',
          difficulty: 'medium',
          timeComplexity: 'O(n log n)',
          spaceComplexity: 'O(log n)',
          tags: ['排序', '高效', '分治'],
          language: 'java',
          code: `public class QuickSort {
  public static void quickSort(int[] arr, int low, int high) {
    if (low < high) {
      int pi = partition(arr, low, high);
      quickSort(arr, low, pi - 1);
      quickSort(arr, pi + 1, high);
    }
  }
  
  private static int partition(int[] arr, int low, int high) {
    int pivot = arr[high];
    int i = low - 1;
    for (int j = low; j < high; j++) {
      if (arr[j] < pivot) {
        i++;
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
      }
    }
    int temp = arr[i + 1];
    arr[i + 1] = arr[high];
    arr[high] = temp;
    return i + 1;
  }
}`,
          testCases: [
            { input: '[5, 3, 8, 4, 2]', expected: '[2, 3, 4, 5, 8]' },
            { input: '[1, 2, 3, 4, 5]', expected: '[1, 2, 3, 4, 5]' },
            { input: '[5, 4, 3, 2, 1]', expected: '[1, 2, 3, 4, 5]' }
          ]
        },
        {
          id: 'binary-search',
          name: '二分查找',
          description: '在有序数组中查找特定元素的搜索算法',
          category: 'searching',
          difficulty: 'easy',
          timeComplexity: 'O(log n)',
          spaceComplexity: 'O(1)',
          tags: ['搜索', '二分', '有序'],
          language: 'java',
          code: `public class BinarySearch {
  public static int binarySearch(int[] arr, int target) {
    int left = 0;
    int right = arr.length - 1;
    
    while (left <= right) {
      int mid = left + (right - left) / 2;
      
      if (arr[mid] == target) {
        return mid;
      } else if (arr[mid] < target) {
        left = mid + 1;
      } else {
        right = mid - 1;
      }
    }
    
    return -1;
  }
}`,
          testCases: [
            { input: '[1, 3, 5, 7, 9], 5', expected: '2' },
            { input: '[1, 3, 5, 7, 9], 3', expected: '1' },
            { input: '[1, 3, 5, 7, 9], 10', expected: '-1' }
          ]
        }
      ]
      
      // 加载分类
      categories.value = [
        { id: 'all', name: '全部算法', icon: '📚', count: algorithms.value.length },
        { id: 'sorting', name: '排序算法', icon: '🔢', count: algorithms.value.filter(a => a.category === 'sorting').length },
        { id: 'searching', name: '搜索算法', icon: '🔍', count: algorithms.value.filter(a => a.category === 'searching').length },
        { id: 'graph', name: '图算法', icon: '🕸️', count: 0 },
        { id: 'dynamic', name: '动态规划', icon: '🧩', count: 0 },
        { id: 'greedy', name: '贪心算法', icon: '💰', count: 0 }
      ]
      
      // 加载收藏
      const savedFavorites = localStorage.getItem('algorithm-favorites')
      if (savedFavorites) {
        favorites.value = JSON.parse(savedFavorites)
      }
      
      // 加载执行历史
      const savedHistory = localStorage.getItem('algorithm-history')
      if (savedHistory) {
        executionHistory.value = JSON.parse(savedHistory)
      }
      
    } catch (err) {
      error.value = '加载算法数据失败'
      console.error('Failed to load algorithms:', err)
    } finally {
      loading.value = false
    }
  }

  const selectAlgorithm = (algorithmId: string) => {
    const algorithm = algorithms.value.find(alg => alg.id === algorithmId)
    if (algorithm) {
      selectedAlgorithm.value = algorithm
    }
  }

  const clearSelectedAlgorithm = () => {
    selectedAlgorithm.value = null
  }

  const toggleFavorite = (algorithmId: string) => {
    const index = favorites.value.indexOf(algorithmId)
    if (index > -1) {
      favorites.value.splice(index, 1)
    } else {
      favorites.value.push(algorithmId)
    }
    
    // 保存到本地存储
    localStorage.setItem('algorithm-favorites', JSON.stringify(favorites.value))
  }

  const isFavorite = (algorithmId: string) => {
    return favorites.value.includes(algorithmId)
  }

  const addExecutionResult = (result: Omit<ExecutionResult, 'id' | 'timestamp'>) => {
    const execution: ExecutionResult = {
      id: Date.now().toString(),
      timestamp: Date.now(),
      ...result
    }
    
    executionHistory.value.unshift(execution)
    
    // 限制历史记录数量
    if (executionHistory.value.length > 100) {
      executionHistory.value = executionHistory.value.slice(0, 100)
    }
    
    // 保存到本地存储
    localStorage.setItem('algorithm-history', JSON.stringify(executionHistory.value))
  }

  const clearExecutionHistory = () => {
    executionHistory.value = []
    localStorage.removeItem('algorithm-history')
  }

  const setSearchQuery = (query: string) => {
    searchQuery.value = query
  }

  const setSelectedCategory = (categoryId: string) => {
    selectedCategory.value = categoryId
  }

  const getAlgorithmById = (id: string) => {
    return algorithms.value.find(alg => alg.id === id)
  }

  const getAlgorithmsByCategory = (category: string) => {
    if (category === 'all') {
      return algorithms.value
    }
    return algorithms.value.filter(alg => alg.category === category)
  }

  const getRelatedAlgorithms = (algorithm: Algorithm, limit = 5) => {
    return algorithms.value
      .filter(alg => alg.id !== algorithm.id && alg.category === algorithm.category)
      .slice(0, limit)
  }

  // 初始化
  const initialize = () => {
    loadAlgorithms()
  }

  return {
    // 状态
    algorithms,
    categories,
    selectedAlgorithm,
    executionHistory,
    favorites,
    searchQuery,
    selectedCategory,
    loading,
    error,
    
    // 计算属性
    filteredAlgorithms,
    favoriteAlgorithms,
    recentExecutions,
    executionStats,
    
    // 方法
    loadAlgorithms,
    selectAlgorithm,
    clearSelectedAlgorithm,
    toggleFavorite,
    isFavorite,
    addExecutionResult,
    clearExecutionHistory,
    setSearchQuery,
    setSelectedCategory,
    getAlgorithmById,
    getAlgorithmsByCategory,
    getRelatedAlgorithms,
    initialize
  }
})