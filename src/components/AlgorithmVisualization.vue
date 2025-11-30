<template>
  <div class="algorithm-visualization">
    <!-- 可视化控制面板 -->
    <div class="visualization-controls">
      <div class="control-group">
        <el-button-group>
          <el-button 
            :type="isPlaying ? 'primary' : 'default'"
            @click="togglePlay"
            :disabled="!hasData"
          >
            <el-icon><VideoPlay v-if="!isPlaying" /><VideoPause v-else /></el-icon>
            {{ isPlaying ? '暂停' : '播放' }}
          </el-button>
          <el-button @click="reset" :disabled="!hasData">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
          <el-button @click="stepForward" :disabled="!hasData || isPlaying">
            <el-icon><Right /></el-icon>
            下一步
          </el-button>
          <el-button @click="stepBackward" :disabled="!hasData || isPlaying || currentStep === 0">
            <el-icon><Left /></el-icon>
            上一步
          </el-button>
        </el-button-group>
      </div>
      
      <div class="control-group">
        <span class="control-label">速度:</span>
        <el-slider
          v-model="playSpeed"
          :min="100"
          :max="2000"
          :step="100"
          :format-tooltip="(val) => `${val}ms`"
          style="width: 120px; margin: 0 10px;"
        />
        <span class="speed-display">{{ playSpeed }}ms</span>
      </div>
      
      <div class="control-group">
        <span class="control-label">数据大小:</span>
        <el-slider
          v-model="dataSize"
          :min="5"
          :max="50"
          :step="5"
          @change="generateData"
          style="width: 120px; margin: 0 10px;"
        />
        <span class="size-display">{{ dataSize }} 个</span>
      </div>
    </div>

    <!-- 可视化展示区域 -->
    <div class="visualization-area">
      <!-- 柱状图可视化（用于排序算法） -->
      <div v-if="visualizationType === 'bar'" class="bar-visualization">
        <div 
          v-for="(item, index) in currentData" 
          :key="index"
          class="bar-item"
          :style="{
            height: `${(item.value / maxValue) * 100}%`,
            backgroundColor: getBarColor(index),
            borderColor: getBarBorderColor(index)
          }"
          :class="{
            'active': isActive(index),
            'compared': isCompared(index),
            'sorted': isSorted(index)
          }"
        >
          <span class="bar-value">{{ item.value }}</span>
          <span class="bar-index">{{ index }}</span>
        </div>
      </div>

      <!-- 节点图可视化（用于图算法） -->
      <div v-if="visualizationType === 'graph'" class="graph-visualization">
        <svg :width="svgWidth" :height="svgHeight" class="graph-svg">
          <!-- 绘制边 -->
          <line
            v-for="edge in currentEdges"
            :key="`edge-${edge.from}-${edge.to}`"
            :x1="getNodeX(edge.from)"
            :y1="getNodeY(edge.from)"
            :x2="getNodeX(edge.to)"
            :y2="getNodeY(edge.to)"
            :stroke="getEdgeColor(edge)"
            :stroke-width="getEdgeWidth(edge)"
            stroke-linecap="round"
          />
          
          <!-- 绘制节点 -->
          <circle
            v-for="(node, index) in currentNodes"
            :key="`node-${index}`"
            :cx="getNodeX(index)"
            :cy="getNodeY(index)"
            :r="nodeRadius"
            :fill="getNodeColor(index)"
            :stroke="getNodeBorderColor(index)"
            :stroke-width="getNodeBorderWidth(index)"
            class="graph-node"
            @click="onNodeClick(index)"
          />
          
          <!-- 节点标签 -->
          <text
            v-for="(node, index) in currentNodes"
            :key="`label-${index}`"
            :x="getNodeX(index)"
            :y="getNodeY(index) + 5"
            text-anchor="middle"
            fill="white"
            font-size="12"
            font-weight="bold"
          >
            {{ node.label || index }}
          </text>
        </svg>
      </div>

      <!-- 路径可视化（用于搜索算法） -->
      <div v-if="visualizationType === 'path'" class="path-visualization">
        <div class="path-grid">
          <div 
            v-for="(row, rowIndex) in pathGrid" 
            :key="rowIndex"
            class="path-row"
          >
            <div
              v-for="(cell, colIndex) in row"
              :key="colIndex"
              class="path-cell"
              :class="getPathCellClass(cell, rowIndex, colIndex)"
              @click="onPathCellClick(rowIndex, colIndex)"
            >
              <span v-if="cell.type === 'start'" class="cell-icon">🚀</span>
              <span v-else-if="cell.type === 'end'" class="cell-icon">🎯</span>
              <span v-else-if="cell.type === 'wall'" class="cell-icon">🧱</span>
              <span v-else-if="cell.type === 'path'" class="cell-icon">🟢</span>
              <span v-else-if="cell.type === 'visited'" class="cell-icon">🔵</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 执行信息面板 -->
    <div class="execution-info">
      <el-card class="info-card">
        <template #header>
          <div class="card-header">
            <span>执行信息</span>
          </div>
        </template>
        
        <div class="info-content">
          <div class="info-item">
            <span class="info-label">当前步骤:</span>
            <span class="info-value">{{ currentStep }}/{{ totalSteps }}</span>
          </div>
          
          <div class="info-item">
            <span class="info-label">比较次数:</span>
            <span class="info-value">{{ executionStats.comparisons }}</span>
          </div>
          
          <div class="info-item">
            <span class="info-label">交换次数:</span>
            <span class="info-value">{{ executionStats.swaps }}</span>
          </div>
          
          <div class="info-item">
            <span class="info-label">访问节点:</span>
            <span class="info-value">{{ executionStats.visitedNodes }}</span>
          </div>
          
          <div class="info-item">
            <span class="info-label">执行时间:</span>
            <span class="info-value">{{ executionStats.executionTime }}ms</span>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import {
  VideoPlay,
  VideoPause,
  Refresh,
  Right,
  Left
} from '@element-plus/icons-vue'

// 类型定义
interface BarData {
  value: number
  state: 'normal' | 'active' | 'compared' | 'sorted'
}

interface GraphNode {
  label?: string
  x?: number
  y?: number
  state: 'normal' | 'visited' | 'current' | 'path'
}

interface GraphEdge {
  from: number
  to: number
  weight?: number
  state: 'normal' | 'visited' | 'path'
}

interface PathCell {
  type: 'empty' | 'wall' | 'start' | 'end' | 'path' | 'visited'
  distance?: number
}

interface ExecutionStats {
  comparisons: number
  swaps: number
  visitedNodes: number
  executionTime: number
}

// Props
interface Props {
  algorithmType?: 'sorting' | 'searching' | 'graph'
  initialData?: number[]
  visualizationType?: 'bar' | 'graph' | 'path'
}

const props = withDefaults(defineProps<Props>(), {
  algorithmType: 'sorting',
  visualizationType: 'bar'
})

// 响应式数据
const isPlaying = ref(false)
const playSpeed = ref(500)
const dataSize = ref(15)
const currentStep = ref(0)
const totalSteps = ref(0)
const currentData = ref<BarData[]>([])
const currentNodes = ref<GraphNode[]>([])
const currentEdges = ref<GraphEdge[]>([])
const pathGrid = ref<PathCell[][]>([])
const executionStats = ref<ExecutionStats>({
  comparisons: 0,
  swaps: 0,
  visitedNodes: 0,
  executionTime: 0
})

// SVG尺寸
const svgWidth = ref(600)
const svgHeight = ref(400)
const nodeRadius = ref(20)

// 定时器
let playTimer: NodeJS.Timeout | null = null

// 计算属性
const hasData = computed(() => {
  if (props.visualizationType === 'bar') {
    return currentData.value.length > 0
  } else if (props.visualizationType === 'graph') {
    return currentNodes.value.length > 0
  } else {
    return pathGrid.value.length > 0
  }
})

const maxValue = computed(() => {
  return Math.max(...currentData.value.map(item => item.value), 1)
})

// 方法
const generateData = () => {
  if (props.visualizationType === 'bar') {
    // 生成随机数据用于排序算法
    currentData.value = Array.from({ length: dataSize.value }, (_, i) => ({
      value: Math.floor(Math.random() * 100) + 1,
      state: 'normal'
    }))
    totalSteps.value = 0
    currentStep.value = 0
  } else if (props.visualizationType === 'graph') {
    // 生成图数据
    generateGraphData()
  } else {
    // 生成路径搜索数据
    generatePathData()
  }
  resetStats()
}

const generateGraphData = () => {
  const nodeCount = Math.min(dataSize.value, 10)
  currentNodes.value = Array.from({ length: nodeCount }, (_, i) => ({
    label: String.fromCharCode(65 + i),
    state: 'normal'
  }))
  
  // 生成随机边
  currentEdges.value = []
  for (let i = 0; i < nodeCount; i++) {
    for (let j = i + 1; j < nodeCount; j++) {
      if (Math.random() > 0.6) {
        currentEdges.value.push({
          from: i,
          to: j,
          weight: Math.floor(Math.random() * 10) + 1,
          state: 'normal'
        })
      }
    }
  }
}

const generatePathData = () => {
  const gridSize = Math.min(Math.ceil(Math.sqrt(dataSize.value)), 8)
  pathGrid.value = Array.from({ length: gridSize }, (_, row) => 
    Array.from({ length: gridSize }, (_, col) => ({
      type: Math.random() > 0.2 ? 'empty' : 'wall'
    }))
  )
  
  // 设置起点和终点
  pathGrid.value[0][0].type = 'start'
  pathGrid.value[gridSize - 1][gridSize - 1].type = 'end'
}

const togglePlay = () => {
  if (!hasData.value) return
  
  isPlaying.value = !isPlaying.value
  
  if (isPlaying.value) {
    startPlayback()
  } else {
    stopPlayback()
  }
}

const startPlayback = () => {
  if (playTimer) clearInterval(playTimer)
  
  playTimer = setInterval(() => {
    if (currentStep.value < totalSteps.value) {
      stepForward()
    } else {
      stopPlayback()
      isPlaying.value = false
    }
  }, playSpeed.value)
}

const stopPlayback = () => {
  if (playTimer) {
    clearInterval(playTimer)
    playTimer = null
  }
}

const stepForward = () => {
  if (currentStep.value < totalSteps.value) {
    currentStep.value++
    updateVisualization()
  }
}

const stepBackward = () => {
  if (currentStep.value > 0) {
    currentStep.value--
    updateVisualization()
  }
}

const reset = () => {
  currentStep.value = 0
  resetStats()
  updateVisualization()
}

const resetStats = () => {
  executionStats.value = {
    comparisons: 0,
    swaps: 0,
    visitedNodes: 0,
    executionTime: 0
  }
}

const updateVisualization = () => {
  // 根据当前步骤更新可视化状态
  // 这里应该根据具体的算法执行步骤来更新
  if (props.visualizationType === 'bar') {
    updateBarVisualization()
  } else if (props.visualizationType === 'graph') {
    updateGraphVisualization()
  } else {
    updatePathVisualization()
  }
}

const updateBarVisualization = () => {
  // 模拟排序算法的步骤更新
  // 实际项目中这里应该接收真实的算法执行步骤数据
  const stepData = simulateSortingStep(currentData.value, currentStep.value)
  currentData.value = stepData.data
  executionStats.value = stepData.stats
}

const updateGraphVisualization = () => {
  // 模拟图算法的步骤更新
  const stepData = simulateGraphStep(currentNodes.value, currentEdges.value, currentStep.value)
  currentNodes.value = stepData.nodes
  currentEdges.value = stepData.edges
  executionStats.value = stepData.stats
}

const updatePathVisualization = () => {
  // 模拟路径搜索算法的步骤更新
  const stepData = simulatePathStep(pathGrid.value, currentStep.value)
  pathGrid.value = stepData.grid
  executionStats.value = stepData.stats
}

// 模拟算法步骤的函数（简化版）
const simulateSortingStep = (data: BarData[], step: number) => {
  // 这里应该根据具体的排序算法来模拟步骤
  // 简化实现：冒泡排序模拟
  const newData = [...data]
  const stats = { ...executionStats.value }
  
  if (step > 0 && step <= data.length) {
    for (let i = 0; i < data.length - step; i++) {
      newData[i].state = 'normal'
      newData[i + 1].state = 'compared'
      
      if (newData[i].value > newData[i + 1].value) {
        // 交换
        [newData[i], newData[i + 1]] = [newData[i + 1], newData[i]]
        stats.swaps++
      }
      stats.comparisons++
    }
    
    // 标记已排序的元素
    newData[data.length - step].state = 'sorted'
  }
  
  return { data: newData, stats }
}

const simulateGraphStep = (nodes: GraphNode[], edges: GraphEdge[], step: number) => {
  // 简化实现：广度优先搜索模拟
  const newNodes = [...nodes]
  const newEdges = [...edges]
  const stats = { ...executionStats.value }
  
  if (step > 0 && step <= nodes.length) {
    newNodes[step - 1].state = 'visited'
    stats.visitedNodes = step
  }
  
  return { nodes: newNodes, edges: newEdges, stats }
}

const simulatePathStep = (grid: PathCell[][], step: number) => {
  // 简化实现：路径搜索模拟
  const newGrid = grid.map(row => [...row])
  const stats = { ...executionStats.value }
  
  if (step > 0) {
    // 模拟搜索过程
    const visitedCells = Math.min(step, grid.length * grid[0].length)
    stats.visitedNodes = visitedCells
  }
  
  return { grid: newGrid, stats }
}

// 样式相关方法
const getBarColor = (index: number) => {
  const state = currentData.value[index]?.state || 'normal'
  switch (state) {
    case 'active': return '#409EFF'
    case 'compared': return '#F56C6C'
    case 'sorted': return '#67C23A'
    default: return '#909399'
  }
}

const getBarBorderColor = (index: number) => {
  const state = currentData.value[index]?.state || 'normal'
  return state === 'active' ? '#303133' : 'transparent'
}

const isActive = (index: number) => {
  return currentData.value[index]?.state === 'active'
}

const isCompared = (index: number) => {
  return currentData.value[index]?.state === 'compared'
}

const isSorted = (index: number) => {
  return currentData.value[index]?.state === 'sorted'
}

// 图算法相关方法
const getNodeX = (index: number) => {
  const nodeCount = currentNodes.value.length
  const angle = (2 * Math.PI * index) / nodeCount
  return svgWidth.value / 2 + (svgWidth.value / 3) * Math.cos(angle)
}

const getNodeY = (index: number) => {
  const nodeCount = currentNodes.value.length
  const angle = (2 * Math.PI * index) / nodeCount
  return svgHeight.value / 2 + (svgHeight.value / 3) * Math.sin(angle)
}

const getNodeColor = (index: number) => {
  const state = currentNodes.value[index]?.state || 'normal'
  switch (state) {
    case 'visited': return '#67C23A'
    case 'current': return '#409EFF'
    case 'path': return '#E6A23C'
    default: return '#909399'
  }
}

const getNodeBorderColor = (index: number) => {
  return currentNodes.value[index]?.state === 'current' ? '#303133' : 'transparent'
}

const getNodeBorderWidth = (index: number) => {
  return currentNodes.value[index]?.state === 'current' ? 3 : 1
}

const getEdgeColor = (edge: GraphEdge) => {
  switch (edge.state) {
    case 'visited': return '#67C23A'
    case 'path': return '#E6A23C'
    default: return '#DCDFE6'
  }
}

const getEdgeWidth = (edge: GraphEdge) => {
  return edge.state === 'path' ? 3 : 1
}

// 路径搜索相关方法
const getPathCellClass = (cell: PathCell, row: number, col: number) => {
  const classes = ['path-cell']
  
  switch (cell.type) {
    case 'start': classes.push('cell-start'); break
    case 'end': classes.push('cell-end'); break
    case 'wall': classes.push('cell-wall'); break
    case 'path': classes.push('cell-path'); break
    case 'visited': classes.push('cell-visited'); break
    default: classes.push('cell-empty')
  }
  
  return classes
}

// 事件处理
const onNodeClick = (index: number) => {
  console.log('Node clicked:', index)
  // 可以在这里添加节点点击的逻辑
}

const onPathCellClick = (row: number, col: number) => {
  console.log('Path cell clicked:', row, col)
  // 可以在这里添加路径单元格点击的逻辑
}

// 生命周期
onMounted(() => {
  generateData()
  totalSteps.value = dataSize.value * 2 // 简化设置总步骤数
})

onUnmounted(() => {
  stopPlayback()
})

// 监听数据大小变化
watch(dataSize, () => {
  generateData()
  totalSteps.value = dataSize.value * 2
})

// 监听算法类型变化
watch(() => props.algorithmType, () => {
  generateData()
})
</script>

<style scoped lang="scss">
.algorithm-visualization {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: var(--bg-color-secondary);
  border-radius: 8px;
  overflow: hidden;
}

.visualization-controls {
  display: flex;
  align-items: center;
  padding: 16px;
  background: var(--bg-color-primary);
  border-bottom: 1px solid var(--border-color-light);
  gap: 20px;
  flex-wrap: wrap;
}

.control-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.control-label {
  font-size: 14px;
  color: var(--text-color-secondary);
  font-weight: 500;
}

.speed-display,
.size-display {
  font-size: 14px;
  color: var(--text-color-primary);
  min-width: 60px;
}

.visualization-area {
  flex: 1;
  padding: 20px;
  overflow: auto;
  display: flex;
  align-items: center;
  justify-content: center;
}

// 柱状图样式
.bar-visualization {
  display: flex;
  align-items: flex-end;
  justify-content: center;
  height: 300px;
  gap: 2px;
  width: 100%;
  max-width: 800px;
}

.bar-item {
  flex: 1;
  min-width: 20px;
  max-width: 40px;
  border-radius: 4px 4px 0 0;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-end;
  position: relative;
  
  &.active {
    transform: scaleY(1.1);
    box-shadow: 0 0 10px rgba(64, 158, 255, 0.5);
  }
  
  &.compared {
    transform: scaleY(1.05);
    box-shadow: 0 0 8px rgba(245, 108, 108, 0.4);
  }
  
  &.sorted {
    opacity: 0.8;
  }
}

.bar-value {
  font-size: 12px;
  color: white;
  font-weight: bold;
  margin-bottom: 4px;
}

.bar-index {
  font-size: 10px;
  color: rgba(255, 255, 255, 0.7);
  position: absolute;
  bottom: -20px;
}

// 图算法样式
.graph-visualization {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.graph-svg {
  border: 1px solid var(--border-color-light);
  border-radius: 8px;
  background: var(--bg-color-primary);
}

.graph-node {
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    transform: scale(1.1);
  }
}

// 路径搜索样式
.path-visualization {
  display: flex;
  align-items: center;
  justify-content: center;
}

.path-grid {
  display: inline-block;
  border: 2px solid var(--border-color-light);
  border-radius: 8px;
  overflow: hidden;
  background: var(--bg-color-primary);
}

.path-row {
  display: flex;
}

.path-cell {
  width: 40px;
  height: 40px;
  border: 1px solid var(--border-color-lighter);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    background-color: var(--bg-color-hover);
  }
  
  &.cell-start { background-color: #67C23A; }
  &.cell-end { background-color: #F56C6C; }
  &.cell-wall { background-color: #606266; }
  &.cell-path { background-color: #E6A23C; }
  &.cell-visited { background-color: #409EFF; }
  &.cell-empty { background-color: var(--bg-color-primary); }
}

.cell-icon {
  font-size: 16px;
}

.execution-info {
  padding: 16px;
  border-top: 1px solid var(--border-color-light);
}

.info-card {
  background: var(--bg-color-primary);
}

.card-header {
  font-weight: 600;
  color: var(--text-color-primary);
}

.info-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 12px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
}

.info-label {
  font-size: 14px;
  color: var(--text-color-secondary);
}

.info-value {
  font-size: 14px;
  color: var(--text-color-primary);
  font-weight: 600;
}

// 响应式设计
@media (max-width: 768px) {
  .visualization-controls {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }
  
  .control-group {
    justify-content: space-between;
  }
  
  .bar-item {
    min-width: 15px;
    max-width: 25px;
  }
  
  .path-cell {
    width: 30px;
    height: 30px;
  }
  
  .info-content {
    grid-template-columns: 1fr;
  }
}
</style>