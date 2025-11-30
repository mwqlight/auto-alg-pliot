<template>
  <TechLayout>
    <div class="visualization-page">
      <!-- 页面头部 -->
      <div class="page-header">
        <h1 class="page-title">算法可视化</h1>
        <p class="page-subtitle">实时观察算法执行过程，理解每一步操作</p>
      </div>

      <!-- 主要内容区域 -->
      <div class="visualization-content">
        <!-- 左侧控制面板 -->
        <div class="control-panel">
          <TechCard class="control-card">
            <template #title>算法选择</template>
            <template #content>
              <div class="control-section">
                <TechSelect
                  v-model="selectedAlgorithmId"
                  :options="algorithmOptions"
                  placeholder="选择算法"
                  @change="handleAlgorithmChange"
                />
                
                <div v-if="selectedAlgorithm" class="algorithm-info">
                  <h4>{{ selectedAlgorithm.name }}</h4>
                  <p class="algorithm-description">{{ selectedAlgorithm.description }}</p>
                  <div class="complexity-info">
                    <span>时间复杂度: {{ selectedAlgorithm.timeComplexity }}</span>
                    <span>空间复杂度: {{ selectedAlgorithm.spaceComplexity }}</span>
                  </div>
                </div>
              </div>
            </template>
          </TechCard>

          <TechCard class="control-card">
            <template #title>输入数据</template>
            <template #content>
              <div class="control-section">
                <TechInput
                  v-model="inputData"
                  placeholder="输入数据 (如: 5,3,8,4,2)"
                  @input="handleInputChange"
                />
                
                <div class="input-options">
                  <TechButton 
                    type="secondary" 
                    size="small"
                    @click="generateRandomData"
                  >
                    随机生成
                  </TechButton>
                  <TechButton 
                    type="secondary" 
                    size="small"
                    @click="useExampleData"
                  >
                    使用示例
                  </TechButton>
                </div>
              </div>
            </template>
          </TechCard>

          <TechCard class="control-card">
            <template #title>可视化控制</template>
            <template #content>
              <div class="control-section">
                <div class="control-group">
                  <label>播放速度</label>
                  <div class="speed-control">
                    <button 
                      :class="['speed-btn', { active: speed === 2000 }]"
                      @click="setSpeed(2000)"
                    >
                      慢
                    </button>
                    <button 
                      :class="['speed-btn', { active: speed === 1000 }]"
                      @click="setSpeed(1000)"
                    >
                      中
                    </button>
                    <button 
                      :class="['speed-btn', { active: speed === 500 }]"
                      @click="setSpeed(500)"
                    >
                      快
                    </button>
                  </div>
                </div>

                <div class="control-group">
                  <label>显示选项</label>
                  <div class="toggle-options">
                    <label class="toggle-label">
                      <input type="checkbox" v-model="showCode" />
                      <span>显示代码</span>
                    </label>
                    <label class="toggle-label">
                      <input type="checkbox" v-model="showPerformance" />
                      <span>性能指标</span>
                    </label>
                    <label class="toggle-label">
                      <input type="checkbox" v-model="autoPlay" />
                      <span>自动播放</span>
                    </label>
                  </div>
                </div>
              </div>
            </template>
          </TechCard>

          <TechCard class="control-card">
            <template #title>执行控制</template>
            <template #content>
              <div class="control-section">
                <div class="execution-controls">
                  <TechButton 
                    type="primary" 
                    :disabled="!canRun"
                    @click="runAlgorithm"
                  >
                    🚀 开始执行
                  </TechButton>
                  
                  <div class="playback-controls">
                    <button 
                      :disabled="!canGoBack"
                      @click="prevStep"
                      class="control-btn"
                    >
                      ⏮️
                    </button>
                    <button 
                      v-if="!isPlaying"
                      @click="play"
                      :disabled="!canPlay"
                      class="control-btn play-btn"
                    >
                      ▶️
                    </button>
                    <button 
                      v-else
                      @click="pause"
                      class="control-btn pause-btn"
                    >
                      ⏸️
                    </button>
                    <button 
                      :disabled="!canGoForward"
                      @click="nextStep"
                      class="control-btn"
                    >
                      ⏭️
                    </button>
                  </div>
                  
                  <TechButton 
                    type="secondary" 
                    @click="reset"
                  >
                    🔄 重置
                  </TechButton>
                </div>

                <div class="progress-section">
                  <div class="progress-info">
                    <span>步骤: {{ currentStep + 1 }} / {{ totalSteps }}</span>
                    <span>进度: {{ progress.toFixed(1) }}%</span>
                  </div>
                  <div class="progress-bar">
                    <div 
                      class="progress-fill" 
                      :style="{ width: progress + '%' }"
                    ></div>
                  </div>
                </div>
              </div>
            </template>
          </TechCard>
        </div>

        <!-- 右侧可视化区域 -->
        <div class="visualization-area">
          <!-- 可视化画布 -->
          <TechCard class="visualization-canvas">
            <template #title>
              <div class="canvas-header">
                <span>算法执行可视化</span>
                <span v-if="currentStepData" class="step-description">
                  {{ stepDescription }}
                </span>
              </div>
            </template>
            <template #content>
              <div class="canvas-content">
                <!-- 排序算法可视化 -->
                <div v-if="visualizationType === 'sorting'" class="sorting-visualization">
                  <div class="array-container">
                    <div 
                      v-for="(value, index) in currentArray" 
                      :key="index"
                      :class="[
                        'array-element',
                        { 
                          'highlighted': isHighlighted(index),
                          'compared': isCompared(index),
                          'swapped': isSwapped(index),
                          'sorted': isSorted(index)
                        }
                      ]"
                      :style="{ height: getElementHeight(value) }"
                    >
                      <span class="element-value">{{ value }}</span>
                      <span class="element-index">{{ index }}</span>
                    </div>
                  </div>
                </div>

                <!-- 搜索算法可视化 -->
                <div v-if="visualizationType === 'searching'" class="searching-visualization">
                  <div class="search-array">
                    <div 
                      v-for="(value, index) in currentArray" 
                      :key="index"
                      :class="[
                        'search-element',
                        { 
                          'current': index === currentIndex,
                          'visited': isVisited(index),
                          'found': isFound(index),
                          'range': isInRange(index)
                        }
                      ]"
                    >
                      <span class="element-value">{{ value }}</span>
                      <span class="element-index">{{ index }}</span>
                    </div>
                  </div>
                  <div class="search-info">
                    <div v-if="targetValue !== null" class="target-info">
                      查找目标: <strong>{{ targetValue }}</strong>
                    </div>
                    <div class="search-status">
                      {{ getSearchStatus() }}
                    </div>
                  </div>
                </div>

                <!-- 图算法可视化 -->
                <div v-if="visualizationType === 'graph'" class="graph-visualization">
                  <div class="graph-canvas">
                    <svg :width="graphWidth" :height="graphHeight" class="graph-svg">
                      <!-- 绘制边 -->
                      <line 
                        v-for="edge in graphEdges" 
                        :key="edge.id"
                        :x1="getNodeX(edge.source)"
                        :y1="getNodeY(edge.source)"
                        :x2="getNodeX(edge.target)"
                        :y2="getNodeY(edge.target)"
                        :stroke="edge.color"
                        :stroke-width="edge.width"
                        :class="{ visited: edge.visited }"
                      />
                      
                      <!-- 绘制节点 -->
                      <circle 
                        v-for="node in graphNodes" 
                        :key="node.id"
                        :cx="node.x"
                        :cy="node.y"
                        :r="node.size"
                        :fill="node.color"
                        :class="{ visited: node.visited }"
                        @click="selectNode(node.id)"
                      />
                      
                      <!-- 节点标签 -->
                      <text 
                        v-for="node in graphNodes" 
                        :key="`label-${node.id}`"
                        :x="node.x"
                        :y="node.y"
                        text-anchor="middle"
                        dominant-baseline="middle"
                        class="node-label"
                      >
                        {{ node.label }}
                      </text>
                    </svg>
                  </div>
                </div>

                <!-- 暂无可视化 -->
                <div v-if="!currentStepData" class="no-visualization">
                  <div class="placeholder">
                    <div class="placeholder-icon">🎯</div>
                    <h3>选择算法并输入数据开始可视化</h3>
                    <p>算法执行过程将在这里实时展示</p>
                  </div>
                </div>
              </div>
            </template>
          </TechCard>

          <!-- 代码显示区域 -->
          <TechCard v-if="showCode && selectedAlgorithm" class="code-panel">
            <template #title>算法代码</template>
            <template #content>
              <div class="code-content">
                <pre class="algorithm-code"><code>{{ selectedAlgorithm.code }}</code></pre>
              </div>
            </template>
          </TechCard>

          <!-- 性能指标区域 -->
          <TechCard v-if="showPerformance && performanceMetrics" class="performance-panel">
            <template #title>性能指标</template>
            <template #content>
              <div class="performance-metrics">
                <div class="metric-item">
                  <span class="metric-label">执行时间:</span>
                  <span class="metric-value">{{ performanceMetrics.executionTime.toFixed(2) }}ms</span>
                </div>
                <div class="metric-item">
                  <span class="metric-label">内存使用:</span>
                  <span class="metric-value">{{ performanceMetrics.memoryUsage.toFixed(2) }}MB</span>
                </div>
                <div class="metric-item">
                  <span class="metric-label">比较次数:</span>
                  <span class="metric-value">{{ performanceMetrics.comparisons }}</span>
                </div>
                <div class="metric-item">
                  <span class="metric-label">交换次数:</span>
                  <span class="metric-value">{{ performanceMetrics.swaps }}</span>
                </div>
                <div class="metric-item">
                  <span class="metric-label">步骤总数:</span>
                  <span class="metric-value">{{ performanceMetrics.steps }}</span>
                </div>
              </div>
            </template>
          </TechCard>
        </div>
      </div>
    </div>
  </TechLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import TechLayout from '@/components/layout/TechLayout.vue'
import TechCard from '@/components/base/TechCard.vue'
import TechButton from '@/components/base/TechButton.vue'
import TechInput from '@/components/base/TechInput.vue'
import TechSelect from '@/components/base/TechSelect.vue'
import { useAlgorithmStore } from '@/store/algorithms'
import { useVisualizationStore } from '@/store/visualization'
import type { Algorithm } from '@/types/algorithm'

const route = useRoute()
const router = useRouter()
const algorithmStore = useAlgorithmStore()
const visualizationStore = useVisualizationStore()

// 状态
const selectedAlgorithmId = ref('')
const inputData = ref('')
const speed = ref(1000)
const showCode = ref(true)
const showPerformance = ref(true)
const autoPlay = ref(true)

// 计算属性
const algorithms = computed(() => {
  return algorithmStore.algorithms
})

const algorithmOptions = computed(() => {
  return algorithms.value.map(alg => ({
    label: alg.name,
    value: alg.id
  }))
})

const selectedAlgorithm = computed(() => {
  return algorithmStore.getAlgorithmById(selectedAlgorithmId.value)
})

const canRun = computed(() => {
  return selectedAlgorithmId.value && inputData.value.trim()
})

// 从store获取可视化状态
const currentStep = computed(() => visualizationStore.currentStep)
const totalSteps = computed(() => visualizationStore.totalSteps)
const isPlaying = computed(() => visualizationStore.isPlaying)
const currentStepData = computed(() => visualizationStore.currentStepData)
const stepDescription = computed(() => visualizationStore.stepDescription)
const progress = computed(() => visualizationStore.progress)
const canGoBack = computed(() => visualizationStore.canGoBack)
const canGoForward = computed(() => visualizationStore.canGoForward)
const canPlay = computed(() => visualizationStore.canGoForward)
const performanceMetrics = computed(() => visualizationStore.performanceMetrics)
const visualizationType = computed(() => visualizationStore.visualizationType)
const graphNodes = computed(() => visualizationStore.graphNodes)
const graphEdges = computed(() => visualizationStore.graphEdges)

// 可视化数据（简化版）
const currentArray = computed(() => {
  if (!inputData.value) return []
  return inputData.value.split(',').map(Number).filter(n => !isNaN(n))
})

const currentIndex = computed(() => {
  return currentStepData.value?.indices[0] || -1
})

const targetValue = computed(() => {
  return currentStepData.value?.target || null
})

const graphWidth = 600
const graphHeight = 400

// 方法
const handleAlgorithmChange = () => {
  if (selectedAlgorithm.value) {
    // 根据算法类型设置可视化类型
    const type = selectedAlgorithm.value.category === 'searching' ? 'searching' : 'sorting'
    visualizationStore.setVisualizationType(type)
  }
}

const handleInputChange = () => {
  // 输入数据变化时重置可视化
  visualizationStore.reset()
}

const generateRandomData = () => {
  const size = Math.floor(Math.random() * 10) + 5
  const data = Array.from({ length: size }, () => Math.floor(Math.random() * 100) + 1)
  inputData.value = data.join(',')
}

const useExampleData = () => {
  if (selectedAlgorithm.value?.category === 'searching') {
    inputData.value = '1,3,5,7,9,11,13,15'
  } else {
    inputData.value = '5,3,8,4,2,9,1,7,6'
  }
}

const setSpeed = (newSpeed: number) => {
  speed.value = newSpeed
  visualizationStore.setSpeed(newSpeed)
}

const runAlgorithm = () => {
  if (!selectedAlgorithm.value || !inputData.value) return
  
  // 使用store的演示数据生成
  visualizationStore.generateDemoSteps(visualizationStore.visualizationType)
  
  if (autoPlay.value) {
    visualizationStore.play()
  }
}

const play = () => {
  visualizationStore.play()
}

const pause = () => {
  visualizationStore.pause()
}

const nextStep = () => {
  visualizationStore.nextStep()
}

const prevStep = () => {
  visualizationStore.prevStep()
}

const reset = () => {
  visualizationStore.reset()
}

// 可视化辅助方法
const isHighlighted = (index: number) => {
  return currentStepData.value?.highlight?.includes(index) || false
}

const isCompared = (index: number) => {
  return currentStepData.value?.operation === 'compare' && 
         currentStepData.value.indices.includes(index)
}

const isSwapped = (index: number) => {
  return currentStepData.value?.operation === 'swap' && 
         currentStepData.value.indices.includes(index)
}

const isSorted = (index: number) => {
  return currentStepData.value?.marker === 'sorted' && 
         currentStepData.value.indices.includes(index)
}

const isVisited = (index: number) => {
  return currentStepData.value?.operation === 'visit' && 
         currentStepData.value.indices.includes(index)
}

const isFound = (index: number) => {
  return currentStepData.value?.operation === 'found' && 
         currentStepData.value.indices.includes(index)
}

const isInRange = (index: number) => {
  const step = currentStepData.value
  if (!step || step.indices.length < 2) return false
  return index >= step.indices[0] && index <= step.indices[1]
}

const getElementHeight = (value: number) => {
  const maxValue = Math.max(...currentArray.value)
  return `${(value / maxValue) * 100}%`
}

const getSearchStatus = () => {
  const step = currentStepData.value
  if (!step) return '等待执行...'
  
  switch (step.operation) {
    case 'found': return '找到目标值!'
    case 'not_found': return '未找到目标值'
    case 'compare': return '比较中...'
    case 'move': return '调整搜索范围'
    default: return '搜索中...'
  }
}

const getNodeX = (nodeId: string) => {
  const node = graphNodes.value.find(n => n.id === nodeId)
  return node?.x || 0
}

const getNodeY = (nodeId: string) => {
  const node = graphNodes.value.find(n => n.id === nodeId)
  return node?.y || 0
}

const selectNode = (nodeId: string) => {
  console.log('Selected node:', nodeId)
}

// 生命周期
onMounted(() => {
  algorithmStore.initialize()
  
  // 从URL参数中获取算法
  const algorithmFromUrl = route.query.algorithm as string
  if (algorithmFromUrl && algorithmStore.getAlgorithmById(algorithmFromUrl)) {
    selectedAlgorithmId.value = algorithmFromUrl
    handleAlgorithmChange()
  }
})

// 监听播放状态
watch(isPlaying, (newVal) => {
  if (newVal && canGoForward.value) {
    const interval = setInterval(() => {
      if (isPlaying.value && canGoForward.value) {
        nextStep()
      } else {
        clearInterval(interval)
      }
    }, speed.value)
  }
})
</script>

<style lang="scss" scoped>
@import '@/styles/tech-theme.scss';

.visualization-page {
  padding: 2rem;
  min-height: 100vh;
  background: linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 100%);
}

.page-header {
  margin-bottom: 2rem;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, var(--primary) 0%, var(--accent) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 0.5rem;
}

.page-subtitle {
  font-size: 1.1rem;
  color: var(--text-secondary);
  opacity: 0.8;
}

.visualization-content {
  display: grid;
  grid-template-columns: 350px 1fr;
  gap: 2rem;
  height: calc(100vh - 200px);
}

.control-panel {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  height: 100%;
  overflow-y: auto;
}

.control-card {
  flex-shrink: 0;
}

.control-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.algorithm-info {
  padding: 1rem;
  background: var(--bg-tertiary);
  border-radius: 8px;
  
  h4 {
    margin: 0 0 0.5rem 0;
    color: var(--text-primary);
  }
  
  .algorithm-description {
    font-size: 0.9rem;
    color: var(--text-secondary);
    margin-bottom: 0.5rem;
  }
  
  .complexity-info {
    display: flex;
    flex-direction: column;
    gap: 0.25rem;
    font-size: 0.8rem;
    color: var(--text-secondary);
  }
}

.input-options {
  display: flex;
  gap: 0.5rem;
}

.control-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  
  label {
    font-weight: 600;
    color: var(--text-primary);
    font-size: 0.9rem;
  }
}

.speed-control {
  display: flex;
  gap: 0.5rem;
}

.speed-btn {
  flex: 1;
  padding: 0.5rem;
  border: 1px solid var(--border-color);
  background: var(--bg-card);
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    border-color: var(--primary);
  }
  
  &.active {
    background: var(--primary);
    color: white;
    border-color: var(--primary);
  }
}

.toggle-options {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.toggle-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  
  input {
    margin: 0;
  }
  
  span {
    font-size: 0.9rem;
    color: var(--text-secondary);
  }
}

.execution-controls {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.playback-controls {
  display: flex;
  justify-content: center;
  gap: 0.5rem;
}

.control-btn {
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  background: var(--bg-card);
  border-radius: 8px;
  cursor: pointer;
  font-size: 1.2rem;
  transition: all 0.3s ease;
  
  &:hover:not(:disabled) {
    border-color: var(--primary);
    transform: scale(1.05);
  }
  
  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
  
  &.play-btn, &.pause-btn {
    background: var(--primary);
    color: white;
    border-color: var(--primary);
  }
}

.progress-section {
  margin-top: 1rem;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  font-size: 0.9rem;
  color: var(--text-secondary);
  margin-bottom: 0.5rem;
}

.progress-bar {
  height: 6px;
  background: var(--bg-tertiary);
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--primary) 0%, var(--accent) 100%);
  border-radius: 3px;
  transition: width 0.3s ease;
}

.visualization-area {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  height: 100%;
  overflow-y: auto;
}

.visualization-canvas {
  flex: 1;
  min-height: 400px;
}

.canvas-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.step-description {
  font-size: 0.9rem;
  color: var(--text-secondary);
  font-style: italic;
}

.canvas-content {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.sorting-visualization {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: end;
  justify-content: center;
}

.array-container {
  display: flex;
  align-items: end;
  gap: 4px;
  height: 200px;
  padding: 1rem;
}

.array-element {
  width: 40px;
  background: var(--primary);
  border-radius: 4px 4px 0 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-end;
  transition: all 0.3s ease;
  position: relative;
  
  &.highlighted {
    background: var(--accent);
  }
  
  &.compared {
    background: var(--warning);
  }
  
  &.swapped {
    background: var(--error);
  }
  
  &.sorted {
    background: var(--success);
  }
}

.element-value {
  font-weight: 600;
  color: white;
  margin-bottom: 0.25rem;
}

.element-index {
  font-size: 0.7rem;
  color: rgba(255, 255, 255, 0.8);
}

.searching-visualization {
  width: 100%;
  text-align: center;
}

.search-array {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.search-element {
  width: 60px;
  height: 60px;
  border: 2px solid var(--border-color);
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  
  &.current {
    border-color: var(--accent);
    background: var(--accent-light);
  }
  
  &.visited {
    border-color: var(--primary);
    background: var(--primary-light);
  }
  
  &.found {
    border-color: var(--success);
    background: var(--success-light);
  }
  
  &.range {
    border-color: var(--warning);
    background: var(--warning-light);
  }
}

.search-info {
  margin-top: 1rem;
}

.target-info {
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
}

.search-status {
  font-size: 1rem;
  font-weight: 600;
  color: var(--text-primary);
}

.graph-visualization {
  width: 100%;
  height: 100%;
}

.graph-canvas {
  width: 100%;
  height: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.graph-svg {
  border: 1px solid var(--border-color);
  border-radius: 8px;
}

.node-label {
  font-size: 12px;
  font-weight: 600;
  fill: white;
  pointer-events: none;
}

.no-visualization {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.placeholder {
  text-align: center;
  color: var(--text-secondary);
}

.placeholder-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.code-panel, .performance-panel {
  flex-shrink: 0;
}

.code-content {
  max-height: 300px;
  overflow-y: auto;
}

.algorithm-code {
  margin: 0;
  font-family: 'Courier New', monospace;
  font-size: 0.9rem;
  line-height: 1.4;
  color: var(--text-primary);
  background: var(--bg-tertiary);
  padding: 1rem;
  border-radius: 6px;
  overflow-x: auto;
}

.performance-metrics {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.metric-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0;
  border-bottom: 1px solid var(--border-color);
  
  &:last-child {
    border-bottom: none;
  }
}

.metric-label {
  font-weight: 600;
  color: var(--text-primary);
}

.metric-value {
  color: var(--accent);
  font-family: 'Courier New', monospace;
}

// 响应式设计
@media (max-width: 1024px) {
  .visualization-content {
    grid-template-columns: 1fr;
    height: auto;
  }
  
  .control-panel {
    order: 2;
  }
  
  .visualization-area {
    order: 1;
  }
}

@media (max-width: 768px) {
  .visualization-page {
    padding: 1rem;
  }
  
  .array-container {
    gap: 2px;
  }
  
  .array-element {
    width: 30px;
  }
  
  .search-element {
    width: 50px;
    height: 50px;
  }
  
  .graph-canvas {
    height: 250px;
  }
}
</style>