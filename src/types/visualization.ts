// 可视化步骤
export interface VisualizationStep {
  step: number
  operation: string
  description: string
  indices: number[]
  values: any[]
  highlight?: number[]
  marker?: string
  pivot?: any
  target?: any
  fromNode?: string
  toNode?: string
  distance?: number
  color?: string
}

// 图节点
export interface GraphNode {
  id: string
  label: string
  x: number
  y: number
  color: string
  size: number
  visited: boolean
  distance: number
  parent?: string
}

// 图边
export interface GraphEdge {
  id: string
  source: string
  target: string
  weight: number
  color: string
  width: number
  visited: boolean
  directed: boolean
}

// 可视化配置
export interface VisualizationConfig {
  speed: number
  theme: 'light' | 'dark'
  showCode: boolean
  showPerformance: boolean
  autoPlay: boolean
  highlightColor: string
  visitedColor: string
  currentColor: string
  comparisonColor: string
  swapColor: string
}

// 性能指标
export interface PerformanceMetrics {
  executionTime: number
  memoryUsage: number
  comparisons: number
  swaps: number
  steps: number
  complexity: {
    time: string
    space: string
  }
}

// 排序可视化数据
export interface SortingVisualizationData {
  array: number[]
  currentStep: number
  comparisons: number
  swaps: number
  highlightedIndices: number[]
  sortedIndices: number[]
}

// 搜索可视化数据
export interface SearchingVisualizationData {
  array: number[]
  target: number
  currentIndex: number
  left: number
  right: number
  comparisons: number
  found: boolean
}

// 图算法可视化数据
export interface GraphVisualizationData {
  nodes: GraphNode[]
  edges: GraphEdge[]
  currentStep: number
  visitedNodes: string[]
  visitedEdges: string[]
  queue: string[]
  stack: string[]
}

// 动画配置
export interface AnimationConfig {
  duration: number
  easing: string
  delay: number
  iterations: number
}

// 颜色主题
export interface ColorTheme {
  primary: string
  secondary: string
  accent: string
  background: string
  text: string
  highlight: string
  success: string
  warning: string
  error: string
}

// 可视化组件属性
export interface VisualizationProps {
  algorithmId: string
  inputData: string
  config: VisualizationConfig
  onStepChange?: (step: number) => void
  onComplete?: (metrics: PerformanceMetrics) => void
  onError?: (error: string) => void
}

// 可视化事件
export interface VisualizationEvent {
  type: 'step_change' | 'play' | 'pause' | 'reset' | 'complete' | 'error'
  data?: any
  timestamp: number
}

// 图表数据点
export interface ChartDataPoint {
  x: number | string
  y: number
  label?: string
  color?: string
}

// 图表配置
export interface ChartConfig {
  type: 'line' | 'bar' | 'scatter' | 'pie'
  title: string
  xAxis: {
    label: string
    type: 'number' | 'category'
  }
  yAxis: {
    label: string
    type: 'number'
  }
  colors: string[]
  animation: AnimationConfig
}

// 复杂度分析数据
export interface ComplexityAnalysis {
  algorithmId: string
  inputSizes: number[]
  executionTimes: number[]
  memoryUsages: number[]
  theoreticalTime: string
  theoreticalSpace: string
  actualTimeComplexity: string
  actualSpaceComplexity: string
}

// 可视化状态
export interface VisualizationState {
  isPlaying: boolean
  currentStep: number
  totalSteps: number
  speed: number
  theme: 'light' | 'dark'
  showCode: boolean
  showPerformance: boolean
  autoPlay: boolean
}