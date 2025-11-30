<template>
  <div class="complexity-chart">
    <!-- 图表控制面板 -->
    <div class="chart-controls">
      <div class="control-group">
        <span class="control-label">图表类型:</span>
        <el-radio-group v-model="chartType" size="small">
          <el-radio-button label="time">时间复杂度</el-radio-button>
          <el-radio-button label="space">空间复杂度</el-radio-button>
          <el-radio-button label="comparison">对比分析</el-radio-button>
        </el-radio-group>
      </div>
      
      <div class="control-group">
        <span class="control-label">数据规模:</span>
        <el-slider
          v-model="dataScale"
          :min="10"
          :max="1000"
          :step="10"
          :format-tooltip="(val) => `${val}`"
          style="width: 150px; margin: 0 10px;"
          @change="updateChartData"
        />
        <span class="scale-display">n = {{ dataScale }}</span>
      </div>
      
      <div class="control-group">
        <span class="control-label">显示选项:</span>
        <el-checkbox-group v-model="displayOptions" size="small">
          <el-checkbox label="showGrid">显示网格</el-checkbox>
          <el-checkbox label="showLabels">显示标签</el-checkbox>
          <el-checkbox label="animate">动画效果</el-checkbox>
        </el-checkbox-group>
      </div>
    </div>

    <!-- 图表展示区域 -->
    <div class="chart-container">
      <!-- 时间复杂度图表 -->
      <div v-if="chartType === 'time'" class="time-complexity-chart">
        <div class="chart-title">时间复杂度分析</div>
        <div class="chart-content">
          <svg :width="chartWidth" :height="chartHeight" class="chart-svg">
            <!-- 坐标轴 -->
            <g class="axes">
              <!-- X轴 -->
              <line
                :x1="margin.left"
                :y1="chartHeight - margin.bottom"
                :x2="chartWidth - margin.right"
                :y2="chartHeight - margin.bottom"
                stroke="var(--border-color)"
                stroke-width="2"
              />
              
              <!-- Y轴 -->
              <line
                :x1="margin.left"
                :y1="margin.top"
                :x2="margin.left"
                :y2="chartHeight - margin.bottom"
                stroke="var(--border-color)"
                stroke-width="2"
              />
              
              <!-- 网格线 -->
              <g v-if="displayOptions.includes('showGrid')" class="grid-lines">
                <line
                  v-for="(tick, index) in yTicks"
                  :key="`y-grid-${index}`"
                  :x1="margin.left"
                  :y1="tick.y"
                  :x2="chartWidth - margin.right"
                  :y2="tick.y"
                  stroke="var(--border-color-light)"
                  stroke-width="1"
                  stroke-dasharray="2,2"
                />
                
                <line
                  v-for="(tick, index) in xTicks"
                  :key="`x-grid-${index}`"
                  :x1="tick.x"
                  :y1="margin.top"
                  :x2="tick.x"
                  :y2="chartHeight - margin.bottom"
                  stroke="var(--border-color-light)"
                  stroke-width="1"
                  stroke-dasharray="2,2"
                />
              </g>
              
              <!-- X轴刻度 -->
              <g class="x-ticks">
                <line
                  v-for="(tick, index) in xTicks"
                  :key="`x-tick-${index}`"
                  :x1="tick.x"
                  :y1="chartHeight - margin.bottom"
                  :x2="tick.x"
                  :y2="chartHeight - margin.bottom + 5"
                  stroke="var(--border-color)"
                  stroke-width="1"
                />
                <text
                  v-for="(tick, index) in xTicks"
                  :key="`x-label-${index}`"
                  :x="tick.x"
                  :y="chartHeight - margin.bottom + 20"
                  text-anchor="middle"
                  fill="var(--text-color-secondary)"
                  font-size="12"
                >
                  {{ tick.label }}
                </text>
              </g>
              
              <!-- Y轴刻度 -->
              <g class="y-ticks">
                <line
                  v-for="(tick, index) in yTicks"
                  :key="`y-tick-${index}`"
                  :x1="margin.left"
                  :y1="tick.y"
                  :x2="margin.left - 5"
                  :y2="tick.y"
                  stroke="var(--border-color)"
                  stroke-width="1"
                />
                <text
                  v-for="(tick, index) in yTicks"
                  :key="`y-label-${index}`"
                  :x="margin.left - 10"
                  :y="tick.y + 4"
                  text-anchor="end"
                  fill="var(--text-color-secondary)"
                  font-size="12"
                >
                  {{ tick.label }}
                </text>
              </g>
              
              <!-- 轴标签 -->
              <text
                :x="chartWidth / 2"
                :y="chartHeight - 10"
                text-anchor="middle"
                fill="var(--text-color-primary)"
                font-size="14"
                font-weight="600"
              >
                数据规模 (n)
              </text>
              
              <text
                :x="10"
                :y="chartHeight / 2"
                text-anchor="middle"
                fill="var(--text-color-primary)"
                font-size="14"
                font-weight="600"
                transform="rotate(-90, 10, ${chartHeight / 2}) translate(0, 50)"
              >
                操作次数 (T(n))
              </text>
            </g>
            
            <!-- 复杂度曲线 -->
            <g class="complexity-curves">
              <!-- O(1) 常数复杂度 -->
              <path
                :d="generateCurvePath(constantData)"
                fill="none"
                stroke="#67C23A"
                stroke-width="3"
                stroke-dasharray="5,5"
              />
              
              <!-- O(log n) 对数复杂度 -->
              <path
                :d="generateCurvePath(logData)"
                fill="none"
                stroke="#409EFF"
                stroke-width="3"
              />
              
              <!-- O(n) 线性复杂度 -->
              <path
                :d="generateCurvePath(linearData)"
                fill="none"
                stroke="#E6A23C"
                stroke-width="3"
              />
              
              <!-- O(n log n) 线性对数复杂度 -->
              <path
                :d="generateCurvePath(nLogNData)"
                fill="none"
                stroke="#F56C6C"
                stroke-width="3"
              />
              
              <!-- O(n²) 平方复杂度 -->
              <path
                :d="generateCurvePath(quadraticData)"
                fill="none"
                stroke="#909399"
                stroke-width="3"
              />
              
              <!-- O(2ⁿ) 指数复杂度 -->
              <path
                :d="generateCurvePath(exponentialData)"
                fill="none"
                stroke="#F56C6C"
                stroke-width="3"
                stroke-dasharray="2,2"
              />
            </g>
            
            <!-- 数据点 -->
            <g class="data-points" v-if="displayOptions.includes('showLabels')">
              <circle
                v-for="(point, index) in currentDataPoints"
                :key="`point-${index}`"
                :cx="point.x"
                :cy="point.y"
                r="4"
                :fill="point.color"
                stroke="white"
                stroke-width="2"
              />
              
              <text
                v-for="(point, index) in currentDataPoints"
                :key="`label-${index}`"
                :x="point.x + 10"
                :y="point.y - 10"
                fill="var(--text-color-primary)"
                font-size="12"
                font-weight="600"
              >
                {{ point.label }}
              </text>
            </g>
            
            <!-- 当前数据点标记 -->
            <circle
              :cx="currentPoint.x"
              :cy="currentPoint.y"
              r="6"
              fill="#F56C6C"
              stroke="white"
              stroke-width="2"
              class="current-point"
            />
            
            <text
              :x="currentPoint.x + 15"
              :y="currentPoint.y - 15"
              fill="#F56C6C"
              font-size="14"
              font-weight="bold"
              class="current-label"
            >
              n = {{ dataScale }}
            </text>
          </svg>
        </div>
        
        <!-- 图例 -->
        <div class="chart-legend">
          <div 
            v-for="item in legendItems" 
            :key="item.label"
            class="legend-item"
          >
            <div 
              class="legend-color" 
              :style="{ backgroundColor: item.color }"
            ></div>
            <span class="legend-label">{{ item.label }}</span>
            <span class="legend-complexity">{{ item.complexity }}</span>
          </div>
        </div>
      </div>

      <!-- 空间复杂度图表 -->
      <div v-if="chartType === 'space'" class="space-complexity-chart">
        <div class="chart-title">空间复杂度分析</div>
        <div class="chart-content">
          <!-- 实现空间复杂度图表 -->
          <div class="placeholder">空间复杂度图表实现中...</div>
        </div>
      </div>

      <!-- 对比分析图表 -->
      <div v-if="chartType === 'comparison'" class="comparison-chart">
        <div class="chart-title">算法复杂度对比</div>
        <div class="chart-content">
          <!-- 实现对比分析图表 -->
          <div class="placeholder">对比分析图表实现中...</div>
        </div>
      </div>
    </div>

    <!-- 复杂度分析说明 -->
    <div class="complexity-analysis">
      <el-card class="analysis-card">
        <template #header>
          <div class="card-header">
            <span>复杂度分析说明</span>
          </div>
        </template>
        
        <div class="analysis-content">
          <div class="analysis-item" v-for="item in analysisItems" :key="item.type">
            <h4>{{ item.type }}</h4>
            <p>{{ item.description }}</p>
            <div class="examples">
              <strong>典型算法:</strong>
              <el-tag 
                v-for="algo in item.examples" 
                :key="algo"
                size="small"
                :type="getTagType(item.type)"
                class="example-tag"
              >
                {{ algo }}
              </el-tag>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'

// 类型定义
interface ChartPoint {
  x: number
  y: number
  label?: string
  color?: string
}

interface ComplexityData {
  type: string
  data: ChartPoint[]
  color: string
}

interface AnalysisItem {
  type: string
  description: string
  examples: string[]
}

// 响应式数据
const chartType = ref('time')
const dataScale = ref(100)
const displayOptions = ref(['showGrid', 'showLabels', 'animate'])

// 图表尺寸配置
const chartWidth = ref(800)
const chartHeight = ref(500)
const margin = {
  top: 40,
  right: 40,
  bottom: 60,
  left: 60
}

// 复杂度数据
const constantData = ref<ChartPoint[]>([])
const logData = ref<ChartPoint[]>([])
const linearData = ref<ChartPoint[]>([])
const nLogNData = ref<ChartPoint[]>([])
const quadraticData = ref<ChartPoint[]>([])
const exponentialData = ref<ChartPoint[]>([])

// 计算属性
const xTicks = computed(() => {
  const ticks = []
  const step = chartWidth.value / 10
  
  for (let i = 0; i <= 10; i++) {
    const x = margin.left + (i * step)
    const value = Math.round((i / 10) * 1000)
    ticks.push({
      x,
      label: value === 0 ? '0' : value.toString()
    })
  }
  
  return ticks
})

const yTicks = computed(() => {
  const ticks = []
  const step = (chartHeight.value - margin.top - margin.bottom) / 8
  const maxValue = 10000 // 最大Y值
  
  for (let i = 0; i <= 8; i++) {
    const y = chartHeight.value - margin.bottom - (i * step)
    const value = Math.round((i / 8) * maxValue)
    ticks.push({
      y,
      label: value === 0 ? '0' : value.toLocaleString()
    })
  }
  
  return ticks
})

const currentPoint = computed(() => {
  const xScale = (chartWidth.value - margin.left - margin.right) / 1000
  const yScale = (chartHeight.value - margin.top - margin.bottom) / 10000
  
  let yValue = 0
  switch (chartType.value) {
    case 'time':
      // 以O(n log n)为例
      yValue = dataScale.value * Math.log2(dataScale.value)
      break
    case 'space':
      yValue = dataScale.value
      break
    default:
      yValue = dataScale.value
  }
  
  return {
    x: margin.left + (dataScale.value * xScale),
    y: chartHeight.value - margin.bottom - (yValue * yScale)
  }
})

const currentDataPoints = computed(() => {
  const points: ChartPoint[] = []
  const xScale = (chartWidth.value - margin.left - margin.right) / 1000
  const yScale = (chartHeight.value - margin.top - margin.bottom) / 10000
  
  // 添加各种复杂度在当前数据规模下的点
  const complexities = [
    { data: constantData.value, label: 'O(1)', color: '#67C23A' },
    { data: logData.value, label: 'O(log n)', color: '#409EFF' },
    { data: linearData.value, label: 'O(n)', color: '#E6A23C' },
    { data: nLogNData.value, label: 'O(n log n)', color: '#F56C6C' },
    { data: quadraticData.value, label: 'O(n²)', color: '#909399' }
  ]
  
  complexities.forEach(comp => {
    if (comp.data.length > 0) {
      const point = comp.data.find(p => Math.abs(p.x - dataScale.value) < 10)
      if (point) {
        points.push({
          ...point,
          label: comp.label,
          color: comp.color
        })
      }
    }
  })
  
  return points
})

const legendItems = computed(() => [
  { label: '常数复杂度', complexity: 'O(1)', color: '#67C23A' },
  { label: '对数复杂度', complexity: 'O(log n)', color: '#409EFF' },
  { label: '线性复杂度', complexity: 'O(n)', color: '#E6A23C' },
  { label: '线性对数复杂度', complexity: 'O(n log n)', color: '#F56C6C' },
  { label: '平方复杂度', complexity: 'O(n²)', color: '#909399' },
  { label: '指数复杂度', complexity: 'O(2ⁿ)', color: '#F56C6C' }
])

const analysisItems = computed<AnalysisItem[]>(() => [
  {
    type: 'O(1) - 常数复杂度',
    description: '算法的执行时间不随输入数据规模的增长而增长，无论数据规模多大，执行时间都是固定的。',
    examples: ['数组访问', '哈希表查找', '栈的push/pop操作']
  },
  {
    type: 'O(log n) - 对数复杂度',
    description: '算法的执行时间随输入数据规模的增长呈对数增长，增长非常缓慢。',
    examples: ['二分查找', '平衡二叉树的查找', '堆操作']
  },
  {
    type: 'O(n) - 线性复杂度',
    description: '算法的执行时间与输入数据规模成正比，数据规模增加一倍，执行时间也增加一倍。',
    examples: ['线性查找', '遍历数组', '计数排序']
  },
  {
    type: 'O(n log n) - 线性对数复杂度',
    description: '算法的执行时间比线性复杂度稍差，但比平方复杂度好很多，是很多高效算法的复杂度。',
    examples: ['快速排序', '归并排序', '堆排序']
  },
  {
    type: 'O(n²) - 平方复杂度',
    description: '算法的执行时间与输入数据规模的平方成正比，数据规模增加一倍，执行时间增加四倍。',
    examples: ['冒泡排序', '选择排序', '插入排序']
  },
  {
    type: 'O(2ⁿ) - 指数复杂度',
    description: '算法的执行时间随输入数据规模的增长呈指数增长，这种算法通常不可用于大规模数据。',
    examples: ['汉诺塔问题', '旅行商问题', '子集生成']
  }
])

// 方法
const generateChartData = () => {
  const dataPoints = 50
  const maxX = 1000
  const maxY = 10000
  
  // 清空数据
  constantData.value = []
  logData.value = []
  linearData.value = []
  nLogNData.value = []
  quadraticData.value = []
  exponentialData.value = []
  
  const xScale = (chartWidth.value - margin.left - margin.right) / maxX
  const yScale = (chartHeight.value - margin.top - margin.bottom) / maxY
  
  for (let i = 1; i <= dataPoints; i++) {
    const x = (i / dataPoints) * maxX
    const scaledX = margin.left + (x * xScale)
    
    // O(1) 常数复杂度
    constantData.value.push({
      x: scaledX,
      y: chartHeight.value - margin.bottom - (10 * yScale)
    })
    
    // O(log n) 对数复杂度
    const logY = Math.log2(x + 1) * 1000
    logData.value.push({
      x: scaledX,
      y: chartHeight.value - margin.bottom - (Math.min(logY, maxY) * yScale)
    })
    
    // O(n) 线性复杂度
    linearData.value.push({
      x: scaledX,
      y: chartHeight.value - margin.bottom - (x * yScale)
    })
    
    // O(n log n) 线性对数复杂度
    const nLogNY = x * Math.log2(x + 1)
    nLogNData.value.push({
      x: scaledX,
      y: chartHeight.value - margin.bottom - (Math.min(nLogNY, maxY) * yScale)
    })
    
    // O(n²) 平方复杂度
    const quadraticY = (x * x) / 100
    quadraticData.value.push({
      x: scaledX,
      y: chartHeight.value - margin.bottom - (Math.min(quadraticY, maxY) * yScale)
    })
    
    // O(2ⁿ) 指数复杂度（限制显示范围）
    if (x <= 20) {
      const exponentialY = Math.pow(2, x)
      exponentialData.value.push({
        x: scaledX,
        y: chartHeight.value - margin.bottom - (Math.min(exponentialY, maxY) * yScale)
      })
    }
  }
}

const generateCurvePath = (data: ChartPoint[]) => {
  if (data.length === 0) return ''
  
  let path = `M ${data[0].x} ${data[0].y}`
  
  for (let i = 1; i < data.length; i++) {
    path += ` L ${data[i].x} ${data[i].y}`
  }
  
  return path
}

const updateChartData = () => {
  generateChartData()
}

const getTagType = (type: string) => {
  if (type.includes('O(1)')) return 'success'
  if (type.includes('O(log n)')) return 'info'
  if (type.includes('O(n)')) return 'warning'
  if (type.includes('O(n log n)')) return 'warning'
  if (type.includes('O(n²)')) return 'danger'
  return 'danger'
}

// 生命周期
onMounted(() => {
  generateChartData()
})

// 监听数据规模变化
watch(dataScale, () => {
  updateChartData()
})

// 监听图表类型变化
watch(chartType, () => {
  updateChartData()
})
</script>

<style scoped lang="scss">
.complexity-chart {
  background: var(--bg-color-secondary);
  border-radius: 12px;
  padding: 20px;
  box-shadow: var(--shadow-medium);
}

.chart-controls {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  padding: 16px;
  background: var(--bg-color-primary);
  border-radius: 8px;
  border: 1px solid var(--border-color-light);
  flex-wrap: wrap;
  gap: 16px;
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
  white-space: nowrap;
}

.scale-display {
  font-size: 14px;
  color: var(--text-color-primary);
  min-width: 60px;
  font-weight: 600;
}

.chart-container {
  margin-bottom: 24px;
}

.chart-title {
  font-size: 20px;
  font-weight: 700;
  color: var(--text-color-primary);
  text-align: center;
  margin-bottom: 16px;
  padding: 12px;
  background: var(--bg-color-primary);
  border-radius: 8px;
  border: 1px solid var(--border-color-light);
}

.chart-content {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
  background: var(--bg-color-primary);
  border-radius: 8px;
  border: 1px solid var(--border-color-light);
  padding: 20px;
}

.chart-svg {
  border-radius: 6px;
  background: white;
  box-shadow: var(--shadow-small);
}

.axes {
  font-family: 'Monaco', 'Consolas', monospace;
}

.complexity-curves {
  path {
    transition: all 0.3s ease;
    
    &:hover {
      stroke-width: 4;
      filter: drop-shadow(0 0 4px currentColor);
    }
  }
}

.data-points {
  circle {
    transition: all 0.3s ease;
    
    &:hover {
      r: 6;
      filter: drop-shadow(0 0 4px currentColor);
    }
  }
}

.current-point {
  animation: pulse 1.5s infinite;
  filter: drop-shadow(0 0 6px #F56C6C);
}

.current-label {
  font-family: 'Monaco', 'Consolas', monospace;
  filter: drop-shadow(0 0 2px white);
}

.chart-legend {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 16px;
  margin-top: 20px;
  padding: 16px;
  background: var(--bg-color-primary);
  border-radius: 8px;
  border: 1px solid var(--border-color-light);
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: var(--bg-color-secondary);
  border-radius: 6px;
  border: 1px solid var(--border-color-light);
}

.legend-color {
  width: 16px;
  height: 16px;
  border-radius: 3px;
  border: 2px solid white;
  box-shadow: var(--shadow-small);
}

.legend-label {
  font-size: 14px;
  color: var(--text-color-primary);
  font-weight: 500;
}

.legend-complexity {
  font-size: 12px;
  color: var(--text-color-secondary);
  font-family: 'Monaco', 'Consolas', monospace;
  background: var(--bg-color-hover);
  padding: 2px 6px;
  border-radius: 3px;
}

.complexity-analysis {
  margin-top: 24px;
}

.analysis-card {
  background: var(--bg-color-primary);
  border: 1px solid var(--border-color-light);
  border-radius: 12px;
  box-shadow: var(--shadow-medium);
}

.card-header {
  font-weight: 600;
  color: var(--text-color-primary);
  font-size: 18px;
}

.analysis-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.analysis-item {
  padding: 16px;
  background: var(--bg-color-secondary);
  border-radius: 8px;
  border: 1px solid var(--border-color-light);
  
  h4 {
    color: var(--text-color-primary);
    margin-bottom: 8px;
    font-size: 16px;
    font-weight: 600;
  }
  
  p {
    color: var(--text-color-secondary);
    line-height: 1.6;
    margin-bottom: 12px;
  }
}

.examples {
  margin-top: 12px;
  
  strong {
    display: block;
    margin-bottom: 8px;
    color: var(--text-color-primary);
  }
}

.example-tag {
  margin: 2px 4px 2px 0;
}

.placeholder {
  text-align: center;
  color: var(--text-color-secondary);
  font-size: 16px;
  padding: 60px 20px;
  background: var(--bg-color-secondary);
  border-radius: 8px;
  border: 2px dashed var(--border-color-light);
}

// 动画
@keyframes pulse {
  0% {
    r: 6;
    opacity: 1;
  }
  50% {
    r: 8;
    opacity: 0.8;
  }
  100% {
    r: 6;
    opacity: 1;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .complexity-chart {
    padding: 12px;
  }
  
  .chart-controls {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }
  
  .control-group {
    justify-content: space-between;
  }
  
  .chart-content {
    padding: 12px;
    min-height: 300px;
  }
  
  .chart-svg {
    max-width: 100%;
    height: auto;
  }
  
  .analysis-content {
    grid-template-columns: 1fr;
  }
  
  .chart-legend {
    gap: 8px;
    padding: 12px;
  }
  
  .legend-item {
    flex: 1;
    min-width: 120px;
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .chart-title {
    font-size: 18px;
    padding: 8px;
  }
  
  .legend-item {
    flex: 0 0 100%;
    justify-content: flex-start;
  }
  
  .analysis-item {
    padding: 12px;
  }
}
</style>