// 算法可视化工具函数

// 可视化步骤接口
export interface VisualizationStep {
  step: number
  data: number[]
  highlightedIndices: number[]
  description: string
  comparisons?: number
  swaps?: number
}

// 图节点接口
export interface GraphNode {
  id: number
  label: string
  x: number
  y: number
  visited?: boolean
  distance?: number
}

// 图边接口
export interface GraphEdge {
  source: number
  target: number
  weight?: number
  visited?: boolean
}

// 图数据结构
export interface GraphData {
  nodes: GraphNode[]
  edges: GraphEdge[]
}

// 排序算法可视化生成器
export class SortingVisualizer {
  static generateBubbleSortSteps(arr: number[]): VisualizationStep[] {
    const steps: VisualizationStep[] = []
    const data = [...arr]
    let comparisons = 0
    let swaps = 0
    
    steps.push({
      step: 0,
      data: [...data],
      highlightedIndices: [],
      description: '初始数组',
      comparisons,
      swaps
    })
    
    const n = data.length
    let stepCount = 1
    
    for (let i = 0; i < n - 1; i++) {
      let swapped = false
      
      for (let j = 0; j < n - i - 1; j++) {
        comparisons++
        
        // 记录比较步骤
        steps.push({
          step: stepCount++,
          data: [...data],
          highlightedIndices: [j, j + 1],
          description: `比较 ${data[j]} 和 ${data[j + 1]}`,
          comparisons,
          swaps
        })
        
        if (data[j] > data[j + 1]) {
          // 交换元素
          [data[j], data[j + 1]] = [data[j + 1], data[j]]
          swaps++
          swapped = true
          
          // 记录交换步骤
          steps.push({
            step: stepCount++,
            data: [...data],
            highlightedIndices: [j, j + 1],
            description: `交换 ${data[j + 1]} 和 ${data[j]}`,
            comparisons,
            swaps
          })
        }
      }
      
      // 如果没有发生交换，说明数组已经有序
      if (!swapped) break
    }
    
    // 记录最终结果
    steps.push({
      step: stepCount,
      data: [...data],
      highlightedIndices: [],
      description: '排序完成',
      comparisons,
      swaps
    })
    
    return steps
  }
  
  static generateQuickSortSteps(arr: number[]): VisualizationStep[] {
    const steps: VisualizationStep[] = []
    const data = [...arr]
    let comparisons = 0
    let swaps = 0
    let stepCount = 0
    
    const quickSort = (low: number, high: number) => {
      if (low < high) {
        // 记录分区开始
        steps.push({
          step: stepCount++,
          data: [...data],
          highlightedIndices: Array.from({length: high - low + 1}, (_, i) => low + i),
          description: `分区 [${low}, ${high}]`,
          comparisons,
          swaps
        })
        
        const pivotIndex = partition(low, high)
        
        // 记录分区结果
        steps.push({
          step: stepCount++,
          data: [...data],
          highlightedIndices: [pivotIndex],
          description: `基准元素 ${data[pivotIndex]} 就位`,
          comparisons,
          swaps
        })
        
        quickSort(low, pivotIndex - 1)
        quickSort(pivotIndex + 1, high)
      }
    }
    
    const partition = (low: number, high: number): number => {
      const pivot = data[high]
      let i = low - 1
      
      for (let j = low; j < high; j++) {
        comparisons++
        
        // 记录比较步骤
        steps.push({
          step: stepCount++,
          data: [...data],
          highlightedIndices: [j, high],
          description: `比较 ${data[j]} 和基准 ${pivot}`,
          comparisons,
          swaps
        })
        
        if (data[j] <= pivot) {
          i++
          
          if (i !== j) {
            // 交换元素
            [data[i], data[j]] = [data[j], data[i]]
            swaps++
            
            // 记录交换步骤
            steps.push({
              step: stepCount++,
              data: [...data],
              highlightedIndices: [i, j],
              description: `交换 ${data[j]} 和 ${data[i]}`,
              comparisons,
              swaps
            })
          }
        }
      }
      
      // 将基准元素放到正确位置
      [data[i + 1], data[high]] = [data[high], data[i + 1]]
      swaps++
      
      // 记录基准元素放置
      steps.push({
        step: stepCount++,
        data: [...data],
        highlightedIndices: [i + 1, high],
        description: `基准元素 ${pivot} 放到位置 ${i + 1}`,
        comparisons,
        swaps
      })
      
      return i + 1
    }
    
    // 初始步骤
    steps.push({
      step: stepCount++,
      data: [...data],
      highlightedIndices: [],
      description: '初始数组',
      comparisons,
      swaps
    })
    
    quickSort(0, data.length - 1)
    
    // 最终步骤
    steps.push({
      step: stepCount,
      data: [...data],
      highlightedIndices: [],
      description: '排序完成',
      comparisons,
      swaps
    })
    
    return steps
  }
  
  static generateMergeSortSteps(arr: number[]): VisualizationStep[] {
    const steps: VisualizationStep[] = []
    const data = [...arr]
    let comparisons = 0
    let stepCount = 0
    
    const mergeSort = (left: number, right: number) => {
      if (left < right) {
        const mid = Math.floor((left + right) / 2)
        
        // 记录分割步骤
        steps.push({
          step: stepCount++,
          data: [...data],
          highlightedIndices: Array.from({length: right - left + 1}, (_, i) => left + i),
          description: `分割 [${left}, ${mid}] 和 [${mid + 1}, ${right}]`,
          comparisons,
          swaps: 0
        })
        
        mergeSort(left, mid)
        mergeSort(mid + 1, right)
        merge(left, mid, right)
      }
    }
    
    const merge = (left: number, mid: number, right: number) => {
      const leftArr = data.slice(left, mid + 1)
      const rightArr = data.slice(mid + 1, right + 1)
      
      let i = 0, j = 0, k = left
      
      // 记录合并开始
      steps.push({
        step: stepCount++,
        data: [...data],
        highlightedIndices: Array.from({length: right - left + 1}, (_, idx) => left + idx),
        description: `合并 [${left}, ${mid}] 和 [${mid + 1}, ${right}]`,
        comparisons,
        swaps: 0
      })
      
      while (i < leftArr.length && j < rightArr.length) {
        comparisons++
        
        if (leftArr[i] <= rightArr[j]) {
          data[k] = leftArr[i]
          i++
        } else {
          data[k] = rightArr[j]
          j++
        }
        k++
        
        // 记录合并步骤
        steps.push({
          step: stepCount++,
          data: [...data],
          highlightedIndices: [k - 1],
          description: `放置元素 ${data[k - 1]}`,
          comparisons,
          swaps: 0
        })
      }
      
      // 复制剩余元素
      while (i < leftArr.length) {
        data[k] = leftArr[i]
        i++
        k++
        
        steps.push({
          step: stepCount++,
          data: [...data],
          highlightedIndices: [k - 1],
          description: `放置剩余元素 ${data[k - 1]}`,
          comparisons,
          swaps: 0
        })
      }
      
      while (j < rightArr.length) {
        data[k] = rightArr[j]
        j++
        k++
        
        steps.push({
          step: stepCount++,
          data: [...data],
          highlightedIndices: [k - 1],
          description: `放置剩余元素 ${data[k - 1]}`,
          comparisons,
          swaps: 0
        })
      }
    }
    
    // 初始步骤
    steps.push({
      step: stepCount++,
      data: [...data],
      highlightedIndices: [],
      description: '初始数组',
      comparisons,
      swaps: 0
    })
    
    mergeSort(0, data.length - 1)
    
    // 最终步骤
    steps.push({
      step: stepCount,
      data: [...data],
      highlightedIndices: [],
      description: '排序完成',
      comparisons,
      swaps: 0
    })
    
    return steps
  }
}

// 图算法可视化生成器
export class GraphVisualizer {
  static generateBFSSteps(graph: number[][], start: number): VisualizationStep[] {
    const steps: VisualizationStep[] = []
    const visited: boolean[] = new Array(graph.length).fill(false)
    const queue: number[] = [start]
    const result: number[] = []
    
    visited[start] = true
    
    steps.push({
      step: 0,
      data: [],
      highlightedIndices: [start],
      description: `从顶点 ${start} 开始`
    })
    
    let stepCount = 1
    
    while (queue.length > 0) {
      const current = queue.shift()!
      result.push(current)
      
      // 记录当前访问的顶点
      steps.push({
        step: stepCount++,
        data: [...result],
        highlightedIndices: [current],
        description: `访问顶点 ${current}`
      })
      
      for (const neighbor of graph[current]) {
        if (!visited[neighbor]) {
          visited[neighbor] = true
          queue.push(neighbor)
          
          // 记录发现新顶点
          steps.push({
            step: stepCount++,
            data: [...result],
            highlightedIndices: [current, neighbor],
            description: `从顶点 ${current} 发现顶点 ${neighbor}`
          })
        }
      }
    }
    
    // 最终结果
    steps.push({
      step: stepCount,
      data: [...result],
      highlightedIndices: [],
      description: 'BFS遍历完成'
    })
    
    return steps
  }
  
  static generateDijkstraSteps(graph: number[][], start: number): VisualizationStep[] {
    const steps: VisualizationStep[] = []
    const n = graph.length
    const dist: number[] = new Array(n).fill(Infinity)
    const visited: boolean[] = new Array(n).fill(false)
    
    dist[start] = 0
    
    steps.push({
      step: 0,
      data: [...dist],
      highlightedIndices: [start],
      description: `初始化，从顶点 ${start} 开始`
    })
    
    let stepCount = 1
    
    for (let count = 0; count < n - 1; count++) {
      // 找到未访问顶点中距离最小的顶点
      let minDist = Infinity
      let minIndex = -1
      
      for (let v = 0; v < n; v++) {
        if (!visited[v] && dist[v] <= minDist) {
          minDist = dist[v]
          minIndex = v
        }
      }
      
      if (minIndex === -1) break
      
      visited[minIndex] = true
      
      steps.push({
        step: stepCount++,
        data: [...dist],
        highlightedIndices: [minIndex],
        description: `选择顶点 ${minIndex}，距离: ${dist[minIndex]}`
      })
      
      // 更新相邻顶点的距离
      for (let v = 0; v < n; v++) {
        if (!visited[v] && graph[minIndex][v] !== 0 && 
            dist[minIndex] !== Infinity && 
            dist[minIndex] + graph[minIndex][v] < dist[v]) {
          
          const oldDist = dist[v]
          dist[v] = dist[minIndex] + graph[minIndex][v]
          
          steps.push({
            step: stepCount++,
            data: [...dist],
            highlightedIndices: [minIndex, v],
            description: `更新顶点 ${v} 的距离: ${oldDist} -> ${dist[v]}`
          })
        }
      }
    }
    
    // 最终结果
    steps.push({
      step: stepCount,
      data: [...dist],
      highlightedIndices: [],
      description: 'Dijkstra算法完成'
    })
    
    return steps
  }
}

// 搜索算法可视化生成器
export class SearchVisualizer {
  static generateBinarySearchSteps(arr: number[], target: number): VisualizationStep[] {
    const steps: VisualizationStep[] = []
    let left = 0
    let right = arr.length - 1
    
    steps.push({
      step: 0,
      data: [...arr],
      highlightedIndices: [],
      description: `在有序数组中查找 ${target}`
    })
    
    let stepCount = 1
    
    while (left <= right) {
      const mid = Math.floor((left + right) / 2)
      
      // 记录中间元素
      steps.push({
        step: stepCount++,
        data: [...arr],
        highlightedIndices: [mid],
        description: `检查中间元素 arr[${mid}] = ${arr[mid]}`
      })
      
      if (arr[mid] === target) {
        steps.push({
          step: stepCount++,
          data: [...arr],
          highlightedIndices: [mid],
          description: `找到目标元素，索引: ${mid}`
        })
        break
      } else if (arr[mid] < target) {
        left = mid + 1
        steps.push({
          step: stepCount++,
          data: [...arr],
          highlightedIndices: Array.from({length: right - mid}, (_, i) => mid + 1 + i),
          description: `目标在右半部分，搜索范围: [${mid + 1}, ${right}]`
        })
      } else {
        right = mid - 1
        steps.push({
          step: stepCount++,
          data: [...arr],
          highlightedIndices: Array.from({length: mid - left}, (_, i) => left + i),
          description: `目标在左半部分，搜索范围: [${left}, ${mid - 1}]`
        })
      }
    }
    
    if (left > right) {
      steps.push({
        step: stepCount,
        data: [...arr],
        highlightedIndices: [],
        description: `目标元素 ${target} 不存在于数组中`
      })
    }
    
    return steps
  }
}

// 可视化工具函数
export function generateVisualizationSteps(
  algorithm: string, 
  data: any, 
  target?: any
): VisualizationStep[] {
  switch (algorithm) {
    case 'bubble-sort':
      return SortingVisualizer.generateBubbleSortSteps(data)
    case 'quick-sort':
      return SortingVisualizer.generateQuickSortSteps(data)
    case 'merge-sort':
      return SortingVisualizer.generateMergeSortSteps(data)
    case 'binary-search':
      return SearchVisualizer.generateBinarySearchSteps(data, target)
    case 'bfs':
      return GraphVisualizer.generateBFSSteps(data, target || 0)
    case 'dijkstra':
      return GraphVisualizer.generateDijkstraSteps(data, target || 0)
    default:
      return []
  }
}

// 颜色工具函数
export function getColorForValue(value: number, min: number, max: number): string {
  const normalized = (value - min) / (max - min)
  
  // 从蓝色到红色的渐变
  const r = Math.floor(normalized * 255)
  const g = 0
  const b = Math.floor((1 - normalized) * 255)
  
  return `rgb(${r}, ${g}, ${b})`
}

// 动画工具函数
export function animateStep(
  currentStep: VisualizationStep, 
  nextStep: VisualizationStep, 
  duration: number = 500
): Promise<void> {
  return new Promise(resolve => {
    setTimeout(resolve, duration)
  })
}

// 性能分析工具
export function analyzePerformance(steps: VisualizationStep[]): {
  totalSteps: number
  totalComparisons: number
  totalSwaps: number
  efficiency: number
} {
  const totalSteps = steps.length
  const totalComparisons = steps.reduce((sum, step) => sum + (step.comparisons || 0), 0)
  const totalSwaps = steps.reduce((sum, step) => sum + (step.swaps || 0), 0)
  const efficiency = totalSteps > 0 ? (totalComparisons + totalSwaps) / totalSteps : 0
  
  return {
    totalSteps,
    totalComparisons,
    totalSwaps,
    efficiency
  }
}