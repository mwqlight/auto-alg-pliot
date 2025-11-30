<template>
  <div class="algorithm-detail">
    <div class="detail-header">
      <el-button @click="handleBack" class="back-btn">
        <el-icon><ArrowLeft /></el-icon>
        返回列表
      </el-button>
      <h1 class="detail-title">{{ algorithm.name }}</h1>
      <div class="header-actions">
        <el-button type="primary" @click="handleExecute">
          <el-icon><Play /></el-icon>
          执行算法
        </el-button>
        <el-button @click="handleEdit">
          <el-icon><Edit /></el-icon>
          编辑
        </el-button>
      </div>
    </div>
    
    <el-row :gutter="20" class="detail-content">
      <el-col :xs="24" :lg="16">
        <el-card class="info-card">
          <template #header>
            <div class="card-header">
              <span>算法信息</span>
            </div>
          </template>
          
          <div class="algorithm-info">
            <div class="info-item">
              <label>算法名称：</label>
              <span>{{ algorithm.name }}</span>
            </div>
            <div class="info-item">
              <label>算法分类：</label>
              <el-tag :color="getCategoryColor(algorithm.category)" effect="light">
                {{ getCategoryName(algorithm.category) }}
              </el-tag>
            </div>
            <div class="info-item">
              <label>时间复杂度：</label>
              <el-tag :type="getComplexityType(algorithm.complexity)">
                {{ algorithm.complexity }}
              </el-tag>
            </div>
            <div class="info-item">
              <label>空间复杂度：</label>
              <span>{{ algorithm.spaceComplexity }}</span>
            </div>
            <div class="info-item">
              <label>平均执行时间：</label>
              <span>{{ algorithm.averageTime }}ms</span>
            </div>
            <div class="info-item full-width">
              <label>算法描述：</label>
              <p class="description">{{ algorithm.description }}</p>
            </div>
          </div>
        </el-card>
        
        <el-card class="code-card">
          <template #header>
            <div class="card-header">
              <span>算法实现</span>
              <div class="header-actions">
                <el-select v-model="selectedLanguage" size="small" style="width: 120px">
                  <el-option label="JavaScript" value="javascript" />
                  <el-option label="Python" value="python" />
                  <el-option label="Java" value="java" />
                </el-select>
                <el-button size="small" @click="handleCopyCode">
                  <el-icon><DocumentCopy /></el-icon>
                  复制代码
                </el-button>
              </div>
            </div>
          </template>
          
          <CodeEditor
            v-model="currentCode"
            :language="selectedLanguage"
            :readonly="true"
          />
        </el-card>
        
        <el-card class="test-cases-card">
          <template #header>
            <div class="card-header">
              <span>测试用例</span>
            </div>
          </template>
          
          <div class="test-cases">
            <div
              v-for="(testCase, index) in algorithm.testCases"
              :key="index"
              class="test-case"
            >
              <div class="case-header">
                <span>测试用例 {{ index + 1 }}</span>
              </div>
              <div class="case-content">
                <div class="case-input">
                  <label>输入：</label>
                  <pre>{{ testCase.input }}</pre>
                </div>
                <div class="case-expected">
                  <label>期望输出：</label>
                  <pre>{{ testCase.expected }}</pre>
                </div>
              </div>
            </div>
            
            <div v-if="!algorithm.testCases || algorithm.testCases.length === 0" class="no-test-cases">
              <el-empty description="暂无测试用例" />
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :lg="8">
        <el-card class="complexity-card">
          <template #header>
            <div class="card-header">
              <span>复杂度分析</span>
            </div>
          </template>
          
          <div class="complexity-chart">
            <div class="chart-placeholder">
              <el-icon :size="48" color="#409EFF">
                <TrendCharts />
              </el-icon>
              <p>复杂度分析图表</p>
              <small>（此处将展示时间复杂度、空间复杂度的可视化分析）</small>
            </div>
          </div>
          
          <div class="complexity-info">
            <div class="complexity-item">
              <label>最优情况：</label>
              <span>{{ algorithm.bestCase || 'O(1)' }}</span>
            </div>
            <div class="complexity-item">
              <label>平均情况：</label>
              <span>{{ algorithm.complexity }}</span>
            </div>
            <div class="complexity-item">
              <label>最坏情况：</label>
              <span>{{ algorithm.worstCase || algorithm.complexity }}</span>
            </div>
            <div class="complexity-item">
              <label>稳定性：</label>
              <el-tag :type="algorithm.stable ? 'success' : 'warning'" size="small">
                {{ algorithm.stable ? '稳定' : '不稳定' }}
              </el-tag>
            </div>
          </div>
        </el-card>
        
        <el-card class="execution-stats-card">
          <template #header>
            <div class="card-header">
              <span>执行统计</span>
            </div>
          </template>
          
          <div class="stats-info">
            <div class="stat-item">
              <label>总执行次数：</label>
              <span class="stat-value">{{ executionStats.totalExecutions || 0 }}</span>
            </div>
            <div class="stat-item">
              <label>平均执行时间：</label>
              <span class="stat-value">{{ executionStats.avgExecutionTime || algorithm.averageTime }}ms</span>
            </div>
            <div class="stat-item">
              <label>成功率：</label>
              <span class="stat-value">{{ executionStats.successRate || '100%' }}</span>
            </div>
            <div class="stat-item">
              <label>最近执行：</label>
              <span class="stat-value">{{ executionStats.lastExecution || '暂无' }}</span>
            </div>
          </div>
        </el-card>
        
        <el-card class="related-algorithms-card">
          <template #header>
            <div class="card-header">
              <span>相关算法</span>
            </div>
          </template>
          
          <div class="related-algorithms">
            <div
              v-for="relatedAlg in relatedAlgorithms"
              :key="relatedAlg.id"
              class="related-algorithm"
              @click="handleRelatedAlgorithmClick(relatedAlg)"
            >
              <div class="alg-info">
                <el-icon :color="getCategoryColor(relatedAlg.category)">
                  <component :is="getCategoryIcon(relatedAlg.category)" />
                </el-icon>
                <span class="alg-name">{{ relatedAlg.name }}</span>
              </div>
              <el-tag :type="getComplexityType(relatedAlg.complexity)" size="small">
                {{ relatedAlg.complexity }}
              </el-tag>
            </div>
            
            <div v-if="relatedAlgorithms.length === 0" class="no-related">
              <el-empty description="暂无相关算法" :image-size="60" />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import CodeEditor from '@/components/CodeEditor.vue'
import { useAlgorithmsStore } from '@/store/algorithms'
import type { Algorithm } from '@/types/algorithm'

const route = useRoute()
const router = useRouter()
const algorithmsStore = useAlgorithmsStore()

const algorithmId = route.params.id as string
const algorithm = ref<Algorithm>({} as Algorithm)
const selectedLanguage = ref('javascript')
const currentCode = ref('')

const executionStats = reactive({
  totalExecutions: 0,
  avgExecutionTime: 0,
  successRate: '100%',
  lastExecution: '暂无'
})

const relatedAlgorithms = ref<Algorithm[]>([])

const getCategoryIcon = (category: string) => {
  const icons: Record<string, string> = {
    'sorting': 'Sort',
    'searching': 'Search',
    'graph': 'Connection',
    'dynamic-programming': 'Cpu',
    'string': 'Document',
    'math': 'DataAnalysis'
  }
  return icons[category] || 'Document'
}

const getCategoryColor = (category: string) => {
  const colors: Record<string, string> = {
    'sorting': '#409EFF',
    'searching': '#67C23A',
    'graph': '#E6A23C',
    'dynamic-programming': '#F56C6C',
    'string': '#909399',
    'math': '#9C27B0'
  }
  return colors[category] || '#409EFF'
}

const getCategoryName = (category: string) => {
  const names: Record<string, string> = {
    'sorting': '排序算法',
    'searching': '查找算法',
    'graph': '图算法',
    'dynamic-programming': '动态规划',
    'string': '字符串算法',
    'math': '数学算法'
  }
  return names[category] || category
}

const getComplexityType = (complexity: string) => {
  const types: Record<string, 'success' | 'warning' | 'danger' | 'info'> = {
    'O(1)': 'success',
    'O(log n)': 'info',
    'O(n)': 'info',
    'O(n log n)': 'warning',
    'O(n²)': 'danger',
    'O(2^n)': 'danger'
  }
  return types[complexity] || 'info'
}

const handleBack = () => {
  router.push('/algorithms')
}

const handleExecute = () => {
  router.push({
    path: '/execution',
    query: { algorithmId: algorithmId }
  })
}

const handleEdit = () => {
  router.push(`/algorithms/edit/${algorithmId}`)
}

const handleCopyCode = async () => {
  try {
    await navigator.clipboard.writeText(currentCode.value)
    ElMessage.success('代码已复制到剪贴板')
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

const handleRelatedAlgorithmClick = (relatedAlg: Algorithm) => {
  router.push(`/algorithms/${relatedAlg.id}`)
}

const loadAlgorithmDetail = async () => {
  try {
    await algorithmsStore.getAlgorithmDetail(algorithmId)
    algorithm.value = algorithmsStore.currentAlgorithm || {} as Algorithm
    
    // 加载算法代码
    // 由于状态管理模块没有代码获取方法，使用模拟代码
    currentCode.value = `// ${algorithm.value.name} 算法实现
function ${algorithm.value.name?.toLowerCase().replace(/\s+/g, '')}(arr) {
  // 算法实现代码
  return arr;
}`
    
    // 加载相关算法
    loadRelatedAlgorithms()
    
    // 加载执行统计
    loadExecutionStats()
  } catch (error) {
    ElMessage.error('加载算法详情失败')
    console.error('Failed to load algorithm detail:', error)
    
    // 使用模拟数据
    algorithm.value = {
      id: algorithmId,
      name: '快速排序',
      category: 'sorting',
      description: '快速排序是一种高效的排序算法，采用分治策略。它通过选择一个基准元素，将数组分为两部分，一部分包含小于基准的元素，另一部分包含大于基准的元素，然后递归地对这两部分进行排序。',
      complexity: 'O(n log n)',
      spaceComplexity: 'O(log n)',
      averageTime: 15,
      bestCase: 'O(n log n)',
      worstCase: 'O(n²)',
      stable: false,
      testCases: [
        { input: '[3, 6, 8, 10, 1, 2, 1]', expected: '[1, 1, 2, 3, 6, 8, 10]' },
        { input: '[5, 2, 3, 1]', expected: '[1, 2, 3, 5]' }
      ]
    }
    
    currentCode.value = `// 快速排序算法实现
function quickSort(arr) {
  if (arr.length <= 1) return arr;
  
  const pivot = arr[Math.floor(arr.length / 2)];
  const left = [];
  const right = [];
  const equal = [];
  
  for (let num of arr) {
    if (num < pivot) left.push(num);
    else if (num > pivot) right.push(num);
    else equal.push(num);
  }
  
  return [...quickSort(left), ...equal, ...quickSort(right)];
}`
    
    loadRelatedAlgorithms()
    loadExecutionStats()
  }
}

const loadRelatedAlgorithms = () => {
  // 模拟相关算法数据
  relatedAlgorithms.value = [
    {
      id: '2',
      name: '归并排序',
      category: 'sorting',
      complexity: 'O(n log n)',
      averageTime: 20
    },
    {
      id: '3',
      name: '堆排序',
      category: 'sorting',
      complexity: 'O(n log n)',
      averageTime: 25
    },
    {
      id: '4',
      name: '冒泡排序',
      category: 'sorting',
      complexity: 'O(n²)',
      averageTime: 100
    }
  ]
}

const loadExecutionStats = () => {
  // 模拟执行统计数据
  executionStats.totalExecutions = Math.floor(Math.random() * 100) + 10
  executionStats.avgExecutionTime = algorithm.value.averageTime
  executionStats.successRate = '98.5%'
  executionStats.lastExecution = '2024-01-15 14:30:22'
}

onMounted(() => {
  loadAlgorithmDetail()
})
</script>

<style scoped lang="scss">
.algorithm-detail {
  padding: 24px;
  
  .detail-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 24px;
    
    .back-btn {
      margin-right: auto;
    }
    
    .detail-title {
      margin: 0;
      font-size: 24px;
      font-weight: 600;
      color: v-bind('$text-color-primary');
    }
  }
  
  .detail-content {
    .info-card, .code-card, .test-cases-card, .complexity-card, .execution-stats-card, .related-algorithms-card {
      margin-bottom: 20px;
      
      .card-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        
        .header-actions {
          display: flex;
          gap: 8px;
        }
      }
    }
    
    .algorithm-info {
      .info-item {
        display: flex;
        align-items: center;
        margin-bottom: 16px;
        
        label {
          width: 120px;
          color: v-bind('$text-color-secondary');
          font-weight: 500;
        }
        
        span, p {
          color: v-bind('$text-color-primary');
        }
        
        &.full-width {
          flex-direction: column;
          align-items: flex-start;
          
          label {
            width: auto;
            margin-bottom: 8px;
          }
          
          .description {
            margin: 0;
            line-height: 1.6;
          }
        }
      }
    }
    
    .test-cases {
      .test-case {
        margin-bottom: 16px;
        padding: 12px;
        border: 1px solid v-bind('$border-color-light');
        border-radius: v-bind('$border-radius-base');
        
        .case-header {
          margin-bottom: 8px;
          font-weight: 500;
        }
        
        .case-content {
          .case-input, .case-expected {
            margin-bottom: 8px;
            
            label {
              color: v-bind('$text-color-secondary');
              font-weight: 500;
              margin-right: 8px;
            }
            
            pre {
              margin: 4px 0 0 0;
              padding: 8px;
              background: v-bind('$bg-color-light');
              border-radius: v-bind('$border-radius-base');
              font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
              font-size: 13px;
              line-height: 1.4;
              white-space: pre-wrap;
            }
          }
        }
      }
      
      .no-test-cases {
        padding: 20px 0;
      }
    }
    
    .complexity-chart {
      .chart-placeholder {
        text-align: center;
        padding: 40px 0;
        color: v-bind('$text-color-secondary');
        
        p {
          margin: 12px 0 4px 0;
          font-weight: 500;
        }
        
        small {
          font-size: 12px;
        }
      }
    }
    
    .complexity-info, .stats-info {
      .complexity-item, .stat-item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 12px;
        
        label {
          color: v-bind('$text-color-secondary');
        }
        
        .stat-value {
          font-weight: 600;
          color: v-bind('$primary-color');
        }
      }
    }
    
    .related-algorithms {
      .related-algorithm {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 12px;
        margin-bottom: 8px;
        border: 1px solid v-bind('$border-color-light');
        border-radius: v-bind('$border-radius-base');
        cursor: pointer;
        transition: all 0.3s;
        
        &:hover {
          border-color: v-bind('$primary-color');
          background: rgba(64, 158, 255, 0.05);
        }
        
        .alg-info {
          display: flex;
          align-items: center;
          gap: 8px;
          
          .alg-name {
            font-weight: 500;
          }
        }
      }
      
      .no-related {
        padding: 20px 0;
      }
    }
  }
}
</style>