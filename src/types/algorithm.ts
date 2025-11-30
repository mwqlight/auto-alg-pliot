// 算法分类
export interface AlgorithmCategory {
  id: string
  name: string
  icon: string
  count: number
}

// 算法接口
export interface Algorithm {
  id: string
  name: string
  description: string
  category: string
  difficulty: 'easy' | 'medium' | 'hard'
  timeComplexity: string
  spaceComplexity: string
  tags: string[]
  language: 'java' | 'python' | 'javascript'
  code: string
  testCases: TestCase[]
}

// 测试用例
export interface TestCase {
  input: string
  expected: string
}

// 算法执行结果
export interface ExecutionResult {
  id: string
  algorithmId: string
  algorithmName: string
  input: string
  output: string
  expected: string
  success: boolean
  executionTime: number
  memoryUsage: number
  timestamp: number
  errorMessage?: string
  steps?: VisualizationStep[]
}

// 算法执行配置
export interface ExecutionConfig {
  timeout: number
  memoryLimit: number
  enableVisualization: boolean
  showSteps: boolean
  autoPlay: boolean
}

// 算法性能指标
export interface PerformanceMetrics {
  executionTime: number
  memoryUsage: number
  comparisons: number
  swaps: number
  steps: number
}

// 算法搜索和过滤选项
export interface AlgorithmFilter {
  category?: string
  difficulty?: string[]
  tags?: string[]
  language?: string[]
  searchQuery?: string
}

// 算法执行历史
export interface ExecutionHistory {
  algorithmId: string
  executions: ExecutionResult[]
  totalExecutions: number
  successRate: number
  avgExecutionTime: number
}

// 算法收藏
export interface AlgorithmFavorite {
  algorithmId: string
  addedAt: number
  note?: string
}

// 算法比较结果
export interface AlgorithmComparison {
  algorithm1: Algorithm
  algorithm2: Algorithm
  metrics1: PerformanceMetrics
  metrics2: PerformanceMetrics
  winner: 'algorithm1' | 'algorithm2' | 'tie'
  differences: {
    time: number
    memory: number
    steps: number
  }
}

// 算法学习进度
export interface LearningProgress {
  algorithmId: string
  completed: boolean
  attempts: number
  bestTime: number
  understanding: number // 0-100
  lastPracticed: number
}

// 算法挑战
export interface AlgorithmChallenge {
  id: string
  title: string
  description: string
  difficulty: 'easy' | 'medium' | 'hard'
  algorithms: string[]
  testCases: TestCase[]
  constraints: string[]
  timeLimit: number
  memoryLimit: number
}