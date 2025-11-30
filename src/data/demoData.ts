// 演示数据和可视化数据

// 可视化步骤数据结构
export interface VisualizationStep {
  step: number
  data: number[]
  highlightedIndices: number[]
  description: string
  comparisonCount?: number
  swapCount?: number
}

// 算法执行统计
export interface AlgorithmStats {
  comparisons: number
  swaps: number
  timeTaken: number
  memoryUsed: number
}

// 排序算法演示数据
export const sortingDemoData = {
  bubbleSort: {
    name: '冒泡排序',
    steps: [
      { step: 0, data: [64, 34, 25, 12, 22, 11, 90], highlightedIndices: [], description: '初始数组' },
      { step: 1, data: [34, 64, 25, 12, 22, 11, 90], highlightedIndices: [0, 1], description: '比较64和34，交换位置' },
      { step: 2, data: [34, 25, 64, 12, 22, 11, 90], highlightedIndices: [1, 2], description: '比较64和25，交换位置' },
      { step: 3, data: [34, 25, 12, 64, 22, 11, 90], highlightedIndices: [2, 3], description: '比较64和12，交换位置' },
      { step: 4, data: [34, 25, 12, 22, 64, 11, 90], highlightedIndices: [3, 4], description: '比较64和22，交换位置' },
      { step: 5, data: [34, 25, 12, 22, 11, 64, 90], highlightedIndices: [4, 5], description: '比较64和11，交换位置' },
      { step: 6, data: [34, 25, 12, 22, 11, 64, 90], highlightedIndices: [5, 6], description: '比较64和90，不交换' },
      { step: 7, data: [25, 34, 12, 22, 11, 64, 90], highlightedIndices: [0, 1], description: '第二轮：比较34和25，交换位置' },
      { step: 8, data: [25, 12, 34, 22, 11, 64, 90], highlightedIndices: [1, 2], description: '比较34和12，交换位置' },
      { step: 9, data: [25, 12, 22, 34, 11, 64, 90], highlightedIndices: [2, 3], description: '比较34和22，交换位置' },
      { step: 10, data: [25, 12, 22, 11, 34, 64, 90], highlightedIndices: [3, 4], description: '比较34和11，交换位置' },
      { step: 11, data: [12, 25, 22, 11, 34, 64, 90], highlightedIndices: [0, 1], description: '第三轮：比较25和12，交换位置' },
      { step: 12, data: [12, 22, 25, 11, 34, 64, 90], highlightedIndices: [1, 2], description: '比较25和22，交换位置' },
      { step: 13, data: [12, 22, 11, 25, 34, 64, 90], highlightedIndices: [2, 3], description: '比较25和11，交换位置' },
      { step: 14, data: [12, 11, 22, 25, 34, 64, 90], highlightedIndices: [0, 1], description: '第四轮：比较12和11，交换位置' },
      { step: 15, data: [11, 12, 22, 25, 34, 64, 90], highlightedIndices: [0, 1], description: '第五轮：比较12和11，交换位置' },
      { step: 16, data: [11, 12, 22, 25, 34, 64, 90], highlightedIndices: [], description: '排序完成' }
    ]
  },
  
  quickSort: {
    name: '快速排序',
    steps: [
      { step: 0, data: [10, 7, 8, 9, 1, 5], highlightedIndices: [], description: '初始数组' },
      { step: 1, data: [10, 7, 8, 9, 1, 5], highlightedIndices: [5], description: '选择基准元素：5' },
      { step: 2, data: [1, 7, 8, 9, 10, 5], highlightedIndices: [0, 4], description: '分区：小于5的元素放在左边' },
      { step: 3, data: [1, 5, 8, 9, 10, 7], highlightedIndices: [1], description: '基准元素5放在正确位置' },
      { step: 4, data: [1, 5, 8, 9, 10, 7], highlightedIndices: [0], description: '递归排序左半部分[1]' },
      { step: 5, data: [1, 5, 8, 9, 10, 7], highlightedIndices: [2, 3, 4, 5], description: '递归排序右半部分[8,9,10,7]' },
      { step: 6, data: [1, 5, 7, 9, 10, 8], highlightedIndices: [2, 5], description: '选择基准元素：8' },
      { step: 7, data: [1, 5, 7, 8, 10, 9], highlightedIndices: [3], description: '基准元素8放在正确位置' },
      { step: 8, data: [1, 5, 7, 8, 10, 9], highlightedIndices: [2], description: '递归排序左半部分[7]' },
      { step: 9, data: [1, 5, 7, 8, 10, 9], highlightedIndices: [4, 5], description: '递归排序右半部分[10,9]' },
      { step: 10, data: [1, 5, 7, 8, 9, 10], highlightedIndices: [4, 5], description: '选择基准元素：9' },
      { step: 11, data: [1, 5, 7, 8, 9, 10], highlightedIndices: [], description: '排序完成' }
    ]
  },
  
  mergeSort: {
    name: '归并排序',
    steps: [
      { step: 0, data: [12, 11, 13, 5, 6, 7], highlightedIndices: [], description: '初始数组' },
      { step: 1, data: [12, 11, 13, 5, 6, 7], highlightedIndices: [0, 1, 2], description: '分割：[12,11,13] 和 [5,6,7]' },
      { step: 2, data: [12, 11, 13, 5, 6, 7], highlightedIndices: [0, 1], description: '分割：[12,11] 和 [13]' },
      { step: 3, data: [12, 11, 13, 5, 6, 7], highlightedIndices: [0], description: '分割：[12] 和 [11]' },
      { step: 4, data: [11, 12, 13, 5, 6, 7], highlightedIndices: [0, 1], description: '合并：[11,12]' },
      { step: 5, data: [11, 12, 13, 5, 6, 7], highlightedIndices: [0, 1, 2], description: '合并：[11,12,13]' },
      { step: 6, data: [11, 12, 13, 5, 6, 7], highlightedIndices: [3, 4], description: '分割：[5,6] 和 [7]' },
      { step: 7, data: [11, 12, 13, 5, 6, 7], highlightedIndices: [3], description: '分割：[5] 和 [6]' },
      { step: 8, data: [11, 12, 13, 5, 6, 7], highlightedIndices: [3, 4], description: '合并：[5,6]' },
      { step: 9, data: [11, 12, 13, 5, 6, 7], highlightedIndices: [3, 4, 5], description: '合并：[5,6,7]' },
      { step: 10, data: [5, 6, 7, 11, 12, 13], highlightedIndices: [], description: '最终合并：[5,6,7,11,12,13]' }
    ]
  }
}

// 搜索算法演示数据
export const searchingDemoData = {
  binarySearch: {
    name: '二分查找',
    steps: [
      { step: 0, data: [1, 3, 5, 7, 9, 11, 13, 15], highlightedIndices: [], description: '有序数组' },
      { step: 1, data: [1, 3, 5, 7, 9, 11, 13, 15], highlightedIndices: [3], description: '中间元素：7，目标：11' },
      { step: 2, data: [1, 3, 5, 7, 9, 11, 13, 15], highlightedIndices: [5], description: '右半部分中间元素：11' },
      { step: 3, data: [1, 3, 5, 7, 9, 11, 13, 15], highlightedIndices: [5], description: '找到目标元素，索引：5' }
    ]
  }
}

// 图算法演示数据
export const graphDemoData = {
  bfs: {
    name: '广度优先搜索',
    graph: {
      nodes: [
        { id: 0, label: 'A', x: 100, y: 100 },
        { id: 1, label: 'B', x: 200, y: 50 },
        { id: 2, label: 'C', x: 200, y: 150 },
        { id: 3, label: 'D', x: 300, y: 100 }
      ],
      edges: [
        { source: 0, target: 1 },
        { source: 0, target: 2 },
        { source: 1, target: 2 },
        { source: 2, target: 0 },
        { source: 2, target: 3 },
        { source: 3, target: 3 }
      ]
    },
    steps: [
      { step: 0, visitedNodes: [0], queue: [0], description: '从顶点A开始' },
      { step: 1, visitedNodes: [0, 1, 2], queue: [1, 2], description: '访问A的邻居B和C' },
      { step: 2, visitedNodes: [0, 1, 2, 3], queue: [2, 3], description: '访问B的邻居C（已访问）' },
      { step: 3, visitedNodes: [0, 1, 2, 3], queue: [3], description: '访问C的邻居A（已访问）和D' },
      { step: 4, visitedNodes: [0, 1, 2, 3], queue: [], description: '访问D的邻居D（已访问）' }
    ]
  },
  
  dijkstra: {
    name: '迪杰斯特拉算法',
    graph: {
      nodes: [
        { id: 0, label: 'A', x: 100, y: 100 },
        { id: 1, label: 'B', x: 200, y: 50 },
        { id: 2, label: 'C', x: 200, y: 150 },
        { id: 3, label: 'D', x: 300, y: 100 }
      ],
      edges: [
        { source: 0, target: 1, weight: 4 },
        { source: 0, target: 2, weight: 2 },
        { source: 1, target: 2, weight: 1 },
        { source: 1, target: 3, weight: 5 },
        { source: 2, target: 3, weight: 8 }
      ]
    },
    steps: [
      { step: 0, distances: [0, Infinity, Infinity, Infinity], visited: [0], description: '初始化，从A开始' },
      { step: 1, distances: [0, 4, 2, Infinity], visited: [0, 2], description: '更新B和C的距离' },
      { step: 2, distances: [0, 3, 2, 10], visited: [0, 2, 1], description: '通过C更新B的距离' },
      { step: 3, distances: [0, 3, 2, 8], visited: [0, 2, 1, 3], description: '通过B更新D的距离' }
    ]
  }
}

// 复杂度分析数据
export const complexityData = {
  timeComplexity: {
    'O(1)': { name: '常数时间', values: Array.from({length: 100}, (_, i) => ({n: i+1, time: 1})) },
    'O(log n)': { name: '对数时间', values: Array.from({length: 100}, (_, i) => ({n: i+1, time: Math.log2(i+1)})) },
    'O(n)': { name: '线性时间', values: Array.from({length: 100}, (_, i) => ({n: i+1, time: i+1})) },
    'O(n log n)': { name: '线性对数时间', values: Array.from({length: 100}, (_, i) => ({n: i+1, time: (i+1) * Math.log2(i+1)})) },
    'O(n²)': { name: '平方时间', values: Array.from({length: 100}, (_, i) => ({n: i+1, time: Math.pow(i+1, 2)})) },
    'O(2ⁿ)': { name: '指数时间', values: Array.from({length: 20}, (_, i) => ({n: i+1, time: Math.pow(2, i+1)})) }
  },
  
  spaceComplexity: {
    'O(1)': { name: '常数空间', values: Array.from({length: 100}, (_, i) => ({n: i+1, space: 1})) },
    'O(log n)': { name: '对数空间', values: Array.from({length: 100}, (_, i) => ({n: i+1, space: Math.log2(i+1)})) },
    'O(n)': { name: '线性空间', values: Array.from({length: 100}, (_, i) => ({n: i+1, space: i+1})) },
    'O(n²)': { name: '平方空间', values: Array.from({length: 100}, (_, i) => ({n: i+1, space: Math.pow(i+1, 2)})) }
  }
}

// 性能测试数据
export const performanceData = {
  bubbleSort: {
    sizes: [10, 50, 100, 500, 1000],
    times: [0.1, 2.5, 10.2, 255.8, 1020.5],
    comparisons: [45, 1225, 4950, 124750, 499500],
    swaps: [22, 612, 2475, 62375, 249750]
  },
  
  quickSort: {
    sizes: [10, 50, 100, 500, 1000],
    times: [0.05, 0.8, 1.8, 12.5, 28.3],
    comparisons: [25, 200, 450, 3500, 7000],
    swaps: [8, 75, 150, 800, 1800]
  },
  
  mergeSort: {
    sizes: [10, 50, 100, 500, 1000],
    times: [0.08, 1.2, 2.5, 15.8, 35.2],
    comparisons: [34, 283, 672, 4483, 9967],
    swaps: [0, 0, 0, 0, 0]
  }
}

// 随机数据生成器
export function generateRandomArray(size: number, min: number = 1, max: number = 100): number[] {
  return Array.from({length: size}, () => Math.floor(Math.random() * (max - min + 1)) + min)
}

// 生成排序数据
export function generateSortingData(size: number = 10): number[] {
  return generateRandomArray(size)
}

// 生成搜索数据
export function generateSearchingData(size: number = 20): number[] {
  const data = generateRandomArray(size).sort((a, b) => a - b)
  return data
}

// 生成图数据
export function generateGraphData(nodeCount: number = 6, edgeProbability: number = 0.3) {
  const nodes = Array.from({length: nodeCount}, (_, i) => ({
    id: i,
    label: String.fromCharCode(65 + i),
    x: 100 + (i % 3) * 150,
    y: 100 + Math.floor(i / 3) * 150
  }))
  
  const edges = []
  for (let i = 0; i < nodeCount; i++) {
    for (let j = i + 1; j < nodeCount; j++) {
      if (Math.random() < edgeProbability) {
        edges.push({
          source: i,
          target: j,
          weight: Math.floor(Math.random() * 10) + 1
        })
      }
    }
  }
  
  return { nodes, edges }
}

// 获取算法演示数据
export function getDemoData(algorithmId: string) {
  switch (algorithmId) {
    case 'bubble-sort':
      return sortingDemoData.bubbleSort
    case 'quick-sort':
      return sortingDemoData.quickSort
    case 'merge-sort':
      return sortingDemoData.mergeSort
    case 'binary-search':
      return searchingDemoData.binarySearch
    case 'bfs':
      return graphDemoData.bfs
    case 'dijkstra':
      return graphDemoData.dijkstra
    default:
      return null
  }
}

// 获取复杂度数据
export function getComplexityData(type: 'time' | 'space') {
  return type === 'time' ? complexityData.timeComplexity : complexityData.spaceComplexity
}

// 获取性能数据
export function getPerformanceData(algorithmId: string) {
  switch (algorithmId) {
    case 'bubble-sort':
      return performanceData.bubbleSort
    case 'quick-sort':
      return performanceData.quickSort
    case 'merge-sort':
      return performanceData.mergeSort
    default:
      return null
  }
}