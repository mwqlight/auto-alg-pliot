import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { VisualizationStep, GraphNode, GraphEdge, PerformanceMetrics } from '@/types/visualization'

export const useVisualizationStore = defineStore('visualization', () => {
  // 状态
  const currentStep = ref<number>(0)
  const totalSteps = ref<number>(0)
  const steps = ref<VisualizationStep[]>([])
  const isPlaying = ref<boolean>(false)
  const speed = ref<number>(1000) // 毫秒
  const graphNodes = ref<GraphNode[]>([])
  const graphEdges = ref<GraphEdge[]>([])
  const performanceMetrics = ref<PerformanceMetrics | null>(null)
  const selectedAlgorithm = ref<string>('')
  const inputData = ref<string>('')
  const visualizationType = ref<'sorting' | 'searching' | 'graph' | 'dynamic'>('sorting')
  const theme = ref<'light' | 'dark'>('dark')
  const showCode = ref<boolean>(true)
  const showPerformance = ref<boolean>(true)
  const autoPlay = ref<boolean>(true)

  // 计算属性
  const currentStepData = computed(() => {
    return steps.value[currentStep.value] || null
  })

  const progress = computed(() => {
    return totalSteps.value > 0 ? (currentStep.value / totalSteps.value) * 100 : 0
  })

  const canGoBack = computed(() => {
    return currentStep.value > 0
  })

  const canGoForward = computed(() => {
    return currentStep.value < totalSteps.value - 1
  })

  const isAtStart = computed(() => {
    return currentStep.value === 0
  })

  const isAtEnd = computed(() => {
    return currentStep.value === totalSteps.value - 1
  })

  const stepDescription = computed(() => {
    const step = currentStepData.value
    if (!step) return ''
    
    switch (visualizationType.value) {
      case 'sorting':
        return getSortingStepDescription(step)
      case 'searching':
        return getSearchingStepDescription(step)
      case 'graph':
        return getGraphStepDescription(step)
      default:
        return step.description || ''
    }
  })

  // 方法
  const setSteps = (newSteps: VisualizationStep[]) => {
    steps.value = newSteps
    totalSteps.value = newSteps.length
    currentStep.value = 0
  }

  const setGraphData = (nodes: GraphNode[], edges: GraphEdge[]) => {
    graphNodes.value = nodes
    graphEdges.value = edges
  }

  const setPerformanceMetrics = (metrics: PerformanceMetrics) => {
    performanceMetrics.value = metrics
  }

  const nextStep = () => {
    if (canGoForward.value) {
      currentStep.value++
    }
  }

  const prevStep = () => {
    if (canGoBack.value) {
      currentStep.value--
    }
  }

  const goToStep = (stepIndex: number) => {
    if (stepIndex >= 0 && stepIndex < totalSteps.value) {
      currentStep.value = stepIndex
    }
  }

  const reset = () => {
    currentStep.value = 0
    isPlaying.value = false
  }

  const play = () => {
    isPlaying.value = true
  }

  const pause = () => {
    isPlaying.value = false
  }

  const togglePlay = () => {
    isPlaying.value = !isPlaying.value
  }

  const setSpeed = (newSpeed: number) => {
    speed.value = Math.max(100, Math.min(5000, newSpeed))
  }

  const setVisualizationType = (type: 'sorting' | 'searching' | 'graph' | 'dynamic') => {
    visualizationType.value = type
    reset()
  }

  const setTheme = (newTheme: 'light' | 'dark') => {
    theme.value = newTheme
  }

  const toggleTheme = () => {
    theme.value = theme.value === 'light' ? 'dark' : 'light'
  }

  const toggleCode = () => {
    showCode.value = !showCode.value
  }

  const togglePerformance = () => {
    showPerformance.value = !showPerformance.value
  }

  const toggleAutoPlay = () => {
    autoPlay.value = !autoPlay.value
  }

  const setSelectedAlgorithm = (algorithmId: string) => {
    selectedAlgorithm.value = algorithmId
  }

  const setInputData = (data: string) => {
    inputData.value = data
  }

  // 辅助方法
  const getSortingStepDescription = (step: VisualizationStep): string => {
    const { operation, indices, values } = step
    
    switch (operation) {
      case 'compare':
        return `比较索引 ${indices[0]} 和 ${indices[1]} 的值: ${values[0]} 和 ${values[1]}`
      case 'swap':
        return `交换索引 ${indices[0]} 和 ${indices[1]} 的值: ${values[0]} ↔ ${values[1]}`
      case 'mark':
        return `标记索引 ${indices[0]} 为 ${step.marker || '已处理'}`
      case 'partition':
        return `分区操作: 基准值 ${step.pivot || values[0]}, 分区范围 [${indices[0]}, ${indices[1]}]`
      default:
        return step.description || '排序步骤'
    }
  }

  const getSearchingStepDescription = (step: VisualizationStep): string => {
    const { operation, indices, values } = step
    
    switch (operation) {
      case 'compare':
        return `比较目标值 ${step.target || values[0]} 与索引 ${indices[0]} 的值: ${values[0]}`
      case 'move':
        return `移动搜索范围到 [${indices[0]}, ${indices[1]}]`
      case 'found':
        return `找到目标值 ${step.target || values[0]} 在索引 ${indices[0]}`
      case 'not_found':
        return `未找到目标值 ${step.target || values[0]}`
      default:
        return step.description || '搜索步骤'
    }
  }

  const getGraphStepDescription = (step: VisualizationStep): string => {
    const { operation, indices, values } = step
    
    switch (operation) {
      case 'visit':
        return `访问节点 ${step.nodeId || values[0]}, 距离: ${step.distance || '未知'}`
      case 'explore':
        return `探索从节点 ${step.fromNode || values[0]} 到节点 ${step.toNode || values[1]} 的边`
      case 'update':
        return `更新节点 ${step.nodeId || values[0]} 的最短距离为 ${step.distance || values[0]}`
      case 'complete':
        return `完成节点 ${step.nodeId || values[0]} 的处理`
      default:
        return step.description || '图算法步骤'
    }
  }

  // 初始化方法
  const initializeVisualization = (
    algorithmId: string,
    input: string,
    type: 'sorting' | 'searching' | 'graph' | 'dynamic'
  ) => {
    selectedAlgorithm.value = algorithmId
    inputData.value = input
    visualizationType.value = type
    reset()
  }

  // 模拟数据生成（实际项目中会调用算法执行器）
  const generateDemoSteps = (type: 'sorting' | 'searching' | 'graph') => {
    const demoSteps: VisualizationStep[] = []
    
    switch (type) {
      case 'sorting':
        // 冒泡排序演示步骤
        demoSteps.push(
          { step: 0, operation: 'start', description: '开始冒泡排序', indices: [], values: [] },
          { step: 1, operation: 'compare', description: '比较前两个元素', indices: [0, 1], values: [5, 3] },
          { step: 2, operation: 'swap', description: '交换元素', indices: [0, 1], values: [3, 5] },
          { step: 3, operation: 'compare', description: '继续比较', indices: [1, 2], values: [5, 8] },
          { step: 4, operation: 'compare', description: '继续比较', indices: [2, 3], values: [8, 4] },
          { step: 5, operation: 'swap', description: '交换元素', indices: [2, 3], values: [4, 8] },
          { step: 6, operation: 'mark', description: '第一轮完成', indices: [3], values: [8], marker: 'sorted' },
          { step: 7, operation: 'complete', description: '排序完成', indices: [], values: [] }
        )
        break
        
      case 'searching':
        // 二分查找演示步骤
        demoSteps.push(
          { step: 0, operation: 'start', description: '开始二分查找', indices: [], values: [] },
          { step: 1, operation: 'move', description: '设置搜索范围', indices: [0, 4], values: [1, 9] },
          { step: 2, operation: 'compare', description: '比较中间元素', indices: [2], values: [5], target: 5 },
          { step: 3, operation: 'found', description: '找到目标值', indices: [2], values: [5], target: 5 }
        )
        break
        
      case 'graph':
        // BFS演示步骤
        demoSteps.push(
          { step: 0, operation: 'start', description: '开始广度优先搜索', indices: [], values: [] },
          { step: 1, operation: 'visit', description: '访问起始节点', indices: [0], values: ['A'], distance: 0 },
          { step: 2, operation: 'explore', description: '探索相邻节点', indices: [0, 1], values: ['A', 'B'] },
          { step: 3, operation: 'visit', description: '访问节点B', indices: [1], values: ['B'], distance: 1 },
          { step: 4, operation: 'complete', description: '搜索完成', indices: [], values: [] }
        )
        break
    }
    
    setSteps(demoSteps)
    
    // 设置性能指标
    setPerformanceMetrics({
      executionTime: Math.random() * 100 + 50,
      memoryUsage: Math.random() * 10 + 5,
      comparisons: demoSteps.filter(s => s.operation === 'compare').length,
      swaps: demoSteps.filter(s => s.operation === 'swap').length,
      steps: demoSteps.length
    })
  }

  return {
    // 状态
    currentStep,
    totalSteps,
    steps,
    isPlaying,
    speed,
    graphNodes,
    graphEdges,
    performanceMetrics,
    selectedAlgorithm,
    inputData,
    visualizationType,
    theme,
    showCode,
    showPerformance,
    autoPlay,
    
    // 计算属性
    currentStepData,
    progress,
    canGoBack,
    canGoForward,
    isAtStart,
    isAtEnd,
    stepDescription,
    
    // 方法
    setSteps,
    setGraphData,
    setPerformanceMetrics,
    nextStep,
    prevStep,
    goToStep,
    reset,
    play,
    pause,
    togglePlay,
    setSpeed,
    setVisualizationType,
    setTheme,
    toggleTheme,
    toggleCode,
    togglePerformance,
    toggleAutoPlay,
    setSelectedAlgorithm,
    setInputData,
    initializeVisualization,
    generateDemoSteps
  }
})