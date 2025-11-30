<template>
  <div class="visualization-page">
    <!-- 页面标题和算法选择 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <el-icon class="title-icon"><Monitor /></el-icon>
          算法可视化
        </h1>
        <p class="page-description">
          通过动态可视化理解算法执行过程，直观展示排序、搜索、图算法等经典算法的执行步骤
        </p>
      </div>
      
      <div class="algorithm-selector">
        <el-card class="selector-card">
          <template #header>
            <div class="card-header">
              <span>算法选择</span>
            </div>
          </template>
          
          <div class="selector-content">
            <el-radio-group v-model="selectedAlgorithm" class="algorithm-tabs">
              <el-radio-button value="bubble-sort">冒泡排序</el-radio-button>
              <el-radio-button value="quick-sort">快速排序</el-radio-button>
              <el-radio-button value="merge-sort">归并排序</el-radio-button>
              <el-radio-button value="binary-search">二分查找</el-radio-button>
              <el-radio-button value="bfs">广度优先搜索</el-radio-button>
              <el-radio-button value="dijkstra">最短路径</el-radio-button>
            </el-radio-group>
            
            <div class="algorithm-info">
              <h3>{{ algorithmInfo.name }}</h3>
              <p class="algorithm-description">{{ algorithmInfo.description }}</p>
              <div class="algorithm-stats">
                <el-tag :type="getComplexityType(algorithmInfo.timeComplexity)">
                  时间复杂度: {{ algorithmInfo.timeComplexity }}
                </el-tag>
                <el-tag :type="getComplexityType(algorithmInfo.spaceComplexity)">
                  空间复杂度: {{ algorithmInfo.spaceComplexity }}
                </el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 主内容区域 -->
    <div class="main-content">
      <!-- 可视化组件 -->
      <div class="visualization-section">
        <AlgorithmVisualization
          :algorithm-type="visualizationType"
          :visualization-type="getVisualizationType(selectedAlgorithm)"
          ref="visualizationRef"
        />
      </div>

      <!-- 算法说明和控制面板 -->
      <div class="control-section">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card class="explanation-card">
              <template #header>
                <div class="card-header">
                  <span>算法原理</span>
                </div>
              </template>
              
              <div class="explanation-content">
                <div v-html="algorithmInfo.principle" class="principle-text"></div>
                
                <div class="key-points">
                  <h4>关键特点:</h4>
                  <ul>
                    <li v-for="point in algorithmInfo.keyPoints" :key="point">
                      {{ point }}
                    </li>
                  </ul>
                </div>
                
                <div class="use-cases">
                  <h4>适用场景:</h4>
                  <el-tag 
                    v-for="scene in algorithmInfo.useCases" 
                    :key="scene"
                    type="info"
                    size="small"
                    class="scene-tag"
                  >
                    {{ scene }}
                  </el-tag>
                </div>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="12">
            <el-card class="advanced-controls-card">
              <template #header>
                <div class="card-header">
                  <span>高级控制</span>
                </div>
              </template>
              
              <div class="advanced-controls">
                <div class="control-group">
                  <h4>数据配置</h4>
                  <el-button-group>
                    <el-button @click="generateRandomData">
                      <el-icon><Refresh /></el-icon>
                      随机数据
                    </el-button>
                    <el-button @click="generateSortedData">
                      <el-icon><Sort /></el-icon>
                      有序数据
                    </el-button>
                    <el-button @click="generateReverseData">
                      <el-icon><SortDown /></el-icon>
                      逆序数据
                    </el-button>
                  </el-button-group>
                </div>
                
                <div class="control-group">
                  <h4>执行模式</h4>
                  <el-radio-group v-model="executionMode">
                    <el-radio label="step">单步执行</el-radio>
                    <el-radio label="auto">自动执行</el-radio>
                    <el-radio label="compare">对比模式</el-radio>
                  </el-radio-group>
                </div>
                
                <div class="control-group">
                  <h4>性能分析</h4>
                  <el-button @click="startPerformanceTest" type="primary">
                    <el-icon><TrendCharts /></el-icon>
                    开始性能测试
                  </el-button>
                  <div v-if="performanceResults" class="performance-results">
                    <p>测试结果:</p>
                    <ul>
                      <li>平均执行时间: {{ performanceResults.avgTime }}ms</li>
                      <li>比较次数: {{ performanceResults.comparisons }}</li>
                      <li>交换次数: {{ performanceResults.swaps }}</li>
                    </ul>
                  </div>
                </div>
                
                <div class="control-group">
                  <h4>导出结果</h4>
                  <el-button-group>
                    <el-button @click="exportAsImage">
                      <el-icon><Picture /></el-icon>
                      导出图片
                    </el-button>
                    <el-button @click="exportAsGif">
                      <el-icon><VideoCamera /></el-icon>
                      导出GIF
                    </el-button>
                    <el-button @click="exportData">
                      <el-icon><Document /></el-icon>
                      导出数据
                    </el-button>
                  </el-button-group>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 算法对比区域 -->
      <div v-if="executionMode === 'compare'" class="comparison-section">
        <el-card class="comparison-card">
          <template #header>
            <div class="card-header">
              <span>算法对比</span>
            </div>
          </template>
          
          <div class="comparison-content">
            <el-table :data="comparisonData" style="width: 100%">
              <el-table-column prop="algorithm" label="算法" width="150" />
              <el-table-column prop="time" label="执行时间(ms)" width="120" />
              <el-table-column prop="comparisons" label="比较次数" width="120" />
              <el-table-column prop="swaps" label="交换次数" width="120" />
              <el-table-column prop="efficiency" label="效率评分" width="120">
                <template #default="{ row }">
                  <el-rate
                    v-model="row.efficiency"
                    disabled
                    show-score
                    text-color="#ff9900"
                    score-template="{value}"
                  />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120">
                <template #default="{ row }">
                  <el-button size="small" @click="viewAlgorithmDetails(row.algorithm)">
                    查看详情
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  Monitor,
  Refresh,
  Sort,
  SortDown,
  TrendCharts,
  Picture,
  VideoCamera,
  Document
} from '@element-plus/icons-vue'
import AlgorithmVisualization from '@/components/AlgorithmVisualization.vue'

// 路由
const router = useRouter()

// 响应式数据
const selectedAlgorithm = ref('bubble-sort')
const executionMode = ref('step')
const performanceResults = ref(null)
const visualizationRef = ref()

// 算法对比数据
const comparisonData = ref([
  {
    algorithm: '冒泡排序',
    time: 245,
    comparisons: 105,
    swaps: 45,
    efficiency: 3
  },
  {
    algorithm: '快速排序',
    time: 89,
    comparisons: 78,
    swaps: 32,
    efficiency: 5
  },
  {
    algorithm: '归并排序',
    time: 102,
    comparisons: 92,
    swaps: 0,
    efficiency: 4
  }
])

// 算法信息配置
const algorithmConfig = {
  'bubble-sort': {
    name: '冒泡排序',
    description: '一种简单的排序算法，重复遍历要排序的数列，一次比较两个元素，如果顺序错误就交换它们。',
    timeComplexity: 'O(n²)',
    spaceComplexity: 'O(1)',
    principle: `
      <p>冒泡排序的基本思想是：</p>
      <ol>
        <li>比较相邻的元素，如果第一个比第二个大，就交换它们</li>
        <li>对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对</li>
        <li>针对所有的元素重复以上的步骤，除了最后一个</li>
        <li>重复步骤1~3，直到排序完成</li>
      </ol>
    `,
    keyPoints: [
      '稳定排序算法',
      '原地排序，空间复杂度为O(1)',
      '适合小规模数据排序',
      '最坏情况时间复杂度为O(n²)'
    ],
    useCases: ['小规模数据排序', '教学演示', '数据基本有序的情况']
  },
  'quick-sort': {
    name: '快速排序',
    description: '使用分治策略的排序算法，通过一趟排序将待排记录分隔成独立的两部分，其中一部分记录的关键字均比另一部分的关键字小。',
    timeComplexity: 'O(n log n)',
    spaceComplexity: 'O(log n)',
    principle: `
      <p>快速排序的基本思想是：</p>
      <ol>
        <li>从数列中挑出一个元素，称为"基准"</li>
        <li>重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面</li>
        <li>递归地把小于基准值元素的子数列和大于基准值元素的子数列排序</li>
      </ol>
    `,
    keyPoints: [
      '不稳定排序算法',
      '平均时间复杂度为O(n log n)',
      '在实际应用中效率较高',
      '需要额外的栈空间'
    ],
    useCases: ['大规模数据排序', '通用排序需求', '性能要求较高的场景']
  },
  'merge-sort': {
    name: '归并排序',
    description: '建立在归并操作上的一种有效的排序算法，采用分治法的一个非常典型的应用。',
    timeComplexity: 'O(n log n)',
    spaceComplexity: 'O(n)',
    principle: `
      <p>归并排序的基本思想是：</p>
      <ol>
        <li>将序列每相邻两个数字进行归并操作，形成floor(n/2)个序列，排序后每个序列包含两个元素</li>
        <li>将上述序列再次归并，形成floor(n/4)个序列，每个序列包含四个元素</li>
        <li>重复步骤2，直到所有元素排序完毕</li>
      </ol>
    `,
    keyPoints: [
      '稳定排序算法',
      '时间复杂度稳定为O(n log n)',
      '需要额外的存储空间',
      '适合链表排序'
    ],
    useCases: ['大规模数据排序', '外部排序', '需要稳定排序的场景']
  },
  'binary-search': {
    name: '二分查找',
    description: '在有序数组中查找某一特定元素的搜索算法，搜索过程从数组的中间元素开始。',
    timeComplexity: 'O(log n)',
    spaceComplexity: 'O(1)',
    principle: `
      <p>二分查找的基本思想是：</p>
      <ol>
        <li>设置查找区间，low=0，high=n-1</li>
        <li>若low>high，则查找失败，否则继续</li>
        <li>取中间位置mid=(low+high)/2</li>
        <li>比较中间位置元素与目标值</li>
        <li>根据比较结果调整查找区间，重复步骤2-4</li>
      </ol>
    `,
    keyPoints: [
      '要求数组必须有序',
      '查找效率非常高',
      '只能应用于顺序存储结构',
      '不适合频繁插入删除的场景'
    ],
    useCases: ['有序数组查找', '数据库索引', '游戏中的快速查找']
  },
  'bfs': {
    name: '广度优先搜索',
    description: '一种图形搜索算法，从根节点开始，沿着树的宽度遍历树的节点，直到找到目标节点。',
    timeComplexity: 'O(V+E)',
    spaceComplexity: 'O(V)',
    principle: `
      <p>广度优先搜索的基本思想是：</p>
      <ol>
        <li>从图中某顶点v出发，在访问了v之后依次访问v的各个未曾访问过的邻接点</li>
        <li>然后分别从这些邻接点出发依次访问它们的邻接点</li>
        <li>依此类推，直到图中所有已被访问的顶点的邻接点都被访问到</li>
      </ol>
    `,
    keyPoints: [
      '使用队列实现',
      '可以找到最短路径',
      '需要存储所有访问过的节点',
      '适合无权图的最短路径查找'
    ],
    useCases: ['社交网络分析', '最短路径查找', '网络爬虫']
  },
  'dijkstra': {
    name: '迪杰斯特拉算法',
    description: '解决带权有向图中单源最短路径问题的贪心算法，通过逐步扩展已知最短路径集合来求解。',
    timeComplexity: 'O(V²)',
    spaceComplexity: 'O(V)',
    principle: `
      <p>迪杰斯特拉算法的基本思想是：</p>
      <ol>
        <li>设置两个顶点集合S和U，S存放已求出最短路径的顶点，U存放未确定最短路径的顶点</li>
        <li>初始时，S只包含源点，U包含其余顶点</li>
        <li>从U中选取一个距离源点最近的顶点k，把k加入S中</li>
        <li>以k为中间点，修改源点到U中各顶点的距离</li>
        <li>重复步骤3和4直到所有顶点都包含在S中</li>
      </ol>
    `,
    keyPoints: [
      '贪心算法思想',
      '不能处理负权边',
      '使用优先队列可以优化到O(E log V)',
      '适合带权图的最短路径查找'
    ],
    useCases: ['路由算法', '地图导航', '网络优化']
  }
}

// 计算属性
const algorithmInfo = computed(() => {
  return algorithmConfig[selectedAlgorithm.value] || algorithmConfig['bubble-sort']
})

const visualizationType = computed(() => {
  if (selectedAlgorithm.value.includes('sort')) return 'sorting'
  if (selectedAlgorithm.value.includes('search')) return 'searching'
  return 'graph'
})

// 方法
const getVisualizationType = (algorithm: string) => {
  if (algorithm.includes('sort')) return 'bar'
  if (algorithm.includes('search')) return 'path'
  return 'graph'
}

const getComplexityType = (complexity: string) => {
  if (complexity.includes('O(1)')) return 'success'
  if (complexity.includes('O(log n)')) return 'info'
  if (complexity.includes('O(n)')) return 'warning'
  return 'danger'
}

const generateRandomData = () => {
  if (visualizationRef.value) {
    visualizationRef.value.generateData()
  }
}

const generateSortedData = () => {
  // 实现生成有序数据的逻辑
  console.log('生成有序数据')
}

const generateReverseData = () => {
  // 实现生成逆序数据的逻辑
  console.log('生成逆序数据')
}

const startPerformanceTest = () => {
  // 实现性能测试逻辑
  performanceResults.value = {
    avgTime: 156,
    comparisons: 234,
    swaps: 89
  }
}

const exportAsImage = () => {
  // 实现导出图片功能
  console.log('导出图片')
}

const exportAsGif = () => {
  // 实现导出GIF功能
  console.log('导出GIF')
}

const exportData = () => {
  // 实现导出数据功能
  console.log('导出数据')
}

const viewAlgorithmDetails = (algorithm: string) => {
  // 跳转到算法详情页面
  router.push(`/algorithms/${algorithm}`)
}

// 生命周期
onMounted(() => {
  // 页面加载时的初始化逻辑
})
</script>

<style scoped lang="scss">
.visualization-page {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--bg-color-primary) 0%, var(--bg-color-secondary) 100%);
  padding: 20px;
}

.page-header {
  margin-bottom: 24px;
}

.header-content {
  text-align: center;
  margin-bottom: 24px;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  color: var(--text-color-primary);
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.title-icon {
  font-size: 36px;
  color: var(--primary-color);
}

.page-description {
  font-size: 16px;
  color: var(--text-color-secondary);
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.6;
}

.algorithm-selector {
  max-width: 800px;
  margin: 0 auto;
}

.selector-card {
  background: var(--bg-color-card);
  border: 1px solid var(--border-color-light);
  border-radius: 12px;
  box-shadow: var(--shadow-medium);
}

.card-header {
  font-weight: 600;
  color: var(--text-color-primary);
  font-size: 18px;
}

.selector-content {
  padding: 16px;
}

.algorithm-tabs {
  width: 100%;
  margin-bottom: 20px;
  
  :deep(.el-radio-button) {
    flex: 1;
    
    .el-radio-button__inner {
      width: 100%;
      text-align: center;
    }
  }
}

.algorithm-info {
  padding: 16px;
  background: var(--bg-color-secondary);
  border-radius: 8px;
  
  h3 {
    color: var(--text-color-primary);
    margin-bottom: 8px;
    font-size: 20px;
  }
}

.algorithm-description {
  color: var(--text-color-secondary);
  line-height: 1.6;
  margin-bottom: 12px;
}

.algorithm-stats {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
}

.visualization-section {
  margin-bottom: 24px;
  
  :deep(.algorithm-visualization) {
    height: 500px;
    border-radius: 12px;
    box-shadow: var(--shadow-medium);
  }
}

.control-section {
  margin-bottom: 24px;
}

.explanation-card,
.advanced-controls-card {
  height: 100%;
  background: var(--bg-color-card);
  border: 1px solid var(--border-color-light);
  border-radius: 12px;
  box-shadow: var(--shadow-medium);
}

.explanation-content {
  line-height: 1.6;
  color: var(--text-color-primary);
}

.principle-text {
  margin-bottom: 16px;
  
  p, ol {
    margin-bottom: 12px;
  }
  
  li {
    margin-bottom: 4px;
  }
}

.key-points,
.use-cases {
  margin-top: 16px;
  
  h4 {
    color: var(--text-color-primary);
    margin-bottom: 8px;
    font-size: 16px;
  }
  
  ul {
    padding-left: 20px;
    
    li {
      color: var(--text-color-secondary);
      margin-bottom: 4px;
    }
  }
}

.scene-tag {
  margin: 4px 8px 4px 0;
}

.advanced-controls {
  .control-group {
    margin-bottom: 20px;
    
    h4 {
      color: var(--text-color-primary);
      margin-bottom: 12px;
      font-size: 16px;
    }
    
    .el-button-group {
      margin-bottom: 8px;
    }
  }
}

.performance-results {
  margin-top: 12px;
  padding: 12px;
  background: var(--bg-color-secondary);
  border-radius: 6px;
  
  p {
    font-weight: 600;
    margin-bottom: 8px;
    color: var(--text-color-primary);
  }
  
  ul {
    padding-left: 20px;
    
    li {
      color: var(--text-color-secondary);
      margin-bottom: 4px;
    }
  }
}

.comparison-section {
  margin-bottom: 24px;
}

.comparison-card {
  background: var(--bg-color-card);
  border: 1px solid var(--border-color-light);
  border-radius: 12px;
  box-shadow: var(--shadow-medium);
}

// 响应式设计
@media (max-width: 768px) {
  .visualization-page {
    padding: 12px;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .algorithm-tabs {
    :deep(.el-radio-button) {
      flex: 0 0 50%;
      margin-bottom: 8px;
    }
  }
  
  .control-section {
    .el-col {
      margin-bottom: 16px;
    }
  }
  
  .visualization-section {
    :deep(.algorithm-visualization) {
      height: 400px;
    }
  }
}

@media (max-width: 480px) {
  .algorithm-tabs {
    :deep(.el-radio-button) {
      flex: 0 0 100%;
    }
  }
  
  .visualization-section {
    :deep(.algorithm-visualization) {
      height: 350px;
    }
  }
}
</style>