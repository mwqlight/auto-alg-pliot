<template>
  <div class="dashboard">
    <div class="dashboard-header">
      <h1 class="dashboard-title">
        <el-icon><Monitor /></el-icon>
        算法驾驶舱
      </h1>
      <p class="dashboard-subtitle">探索、学习、执行各类算法</p>
    </div>
    
    <div class="dashboard-stats">
      <el-row :gutter="20">
        <el-col :xs="12" :sm="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon primary">
                <el-icon><List /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ totalAlgorithms }}</div>
                <div class="stat-label">总算法数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="12" :sm="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon success">
                <el-icon><Category /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ categories.length }}</div>
                <div class="stat-label">分类数量</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="12" :sm="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon warning">
                <el-icon><Play /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ executionCount }}</div>
                <div class="stat-label">执行次数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="12" :sm="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon danger">
                <el-icon><Clock /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ avgExecutionTime }}ms</div>
                <div class="stat-label">平均执行时间</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <div class="dashboard-content">
      <div class="content-header">
        <h2>算法分类</h2>
        <el-input
          v-model="searchKeyword"
          placeholder="搜索算法..."
          prefix-icon="Search"
          style="width: 300px"
          clearable
        />
      </div>
      
      <div class="categories-section">
        <AlgorithmCategory
          v-for="category in filteredCategories"
          :key="category"
          :category="category"
          :algorithms="getAlgorithmsByCategory(category)"
          @execute="handleExecute"
          @detail="handleDetail"
          @click="handleCardClick"
        />
      </div>
      
      <div v-if="filteredCategories.length === 0" class="empty-search">
        <el-empty description="未找到相关算法" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import AlgorithmCategory from '@/components/AlgorithmCategory.vue'
import { Algorithm } from '@/types/algorithm'
import { useAlgorithmsStore } from '@/store/algorithms'

const router = useRouter()
const algorithmsStore = useAlgorithmsStore()

const searchKeyword = ref('')
const algorithms = ref<Algorithm[]>([])
const executionCount = ref(0)
const avgExecutionTime = ref(0)

const categories = computed(() => {
  const uniqueCategories = new Set(algorithms.value.map(alg => alg.category))
  return Array.from(uniqueCategories)
})

const totalAlgorithms = computed(() => algorithms.value.length)

const filteredCategories = computed(() => {
  if (!searchKeyword.value) {
    return categories.value
  }
  
  return categories.value.filter(category => {
    const categoryAlgorithms = getAlgorithmsByCategory(category)
    return categoryAlgorithms.some(alg => 
      alg.name.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
      alg.description.toLowerCase().includes(searchKeyword.value.toLowerCase())
    )
  })
})

const getAlgorithmsByCategory = (category: string) => {
  let filtered = algorithms.value.filter(alg => alg.category === category)
  
  if (searchKeyword.value) {
    filtered = filtered.filter(alg => 
      alg.name.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
      alg.description.toLowerCase().includes(searchKeyword.value.toLowerCase())
    )
  }
  
  return filtered
}

const handleExecute = (algorithm: Algorithm) => {
  router.push({
    path: '/execution',
    query: { algorithmId: algorithm.id }
  })
}

const handleDetail = (algorithm: Algorithm) => {
  router.push({
    path: `/algorithms/${algorithm.id}`
  })
}

const handleCardClick = (algorithm: Algorithm) => {
  handleDetail(algorithm)
}

const loadAlgorithms = async () => {
  try {
    await algorithmsStore.getAlgorithms()
    algorithms.value = algorithmsStore.tableData || []
    
    // 模拟统计数据
    executionCount.value = Math.floor(Math.random() * 1000) + 100
    avgExecutionTime.value = Math.floor(Math.random() * 50) + 10
  } catch (error) {
    ElMessage.error('加载算法数据失败')
    console.error('Failed to load algorithms:', error)
    
    // 使用模拟数据
    algorithms.value = [
      {
        id: '1',
        name: '快速排序',
        category: 'sorting',
        description: '一种高效的排序算法，采用分治策略',
        complexity: 'O(n log n)',
        spaceComplexity: 'O(log n)',
        averageTime: 15
      },
      {
        id: '2',
        name: '二分查找',
        category: 'searching',
        description: '在有序数组中查找元素的算法',
        complexity: 'O(log n)',
        spaceComplexity: 'O(1)',
        averageTime: 5
      },
      {
        id: '3',
        name: 'Dijkstra算法',
        category: 'graph',
        description: '寻找图中最短路径的经典算法',
        complexity: 'O(V²)',
        spaceComplexity: 'O(V)',
        averageTime: 25
      }
    ]
  }
}

onMounted(() => {
  loadAlgorithms()
})
</script>

<style scoped lang="scss">
.dashboard {
  padding: 24px;
  
  .dashboard-header {
    margin-bottom: 32px;
    
    .dashboard-title {
      display: flex;
      align-items: center;
      gap: 12px;
      font-size: 28px;
      font-weight: 700;
      color: v-bind('$text-color-primary');
      margin: 0 0 8px 0;
    }
    
    .dashboard-subtitle {
      color: v-bind('$text-color-secondary');
      font-size: 16px;
      margin: 0;
    }
  }
  
  .dashboard-stats {
    margin-bottom: 32px;
    
    .stat-card {
      .stat-content {
        display: flex;
        align-items: center;
        gap: 16px;
        
        .stat-icon {
          width: 60px;
          height: 60px;
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
          
          &.primary {
            background: rgba(64, 158, 255, 0.1);
            color: v-bind('$primary-color');
          }
          
          &.success {
            background: rgba(103, 194, 58, 0.1);
            color: v-bind('$success-color');
          }
          
          &.warning {
            background: rgba(230, 162, 60, 0.1);
            color: v-bind('$warning-color');
          }
          
          &.danger {
            background: rgba(245, 108, 108, 0.1);
            color: v-bind('$danger-color');
          }
          
          .el-icon {
            font-size: 24px;
          }
        }
        
        .stat-info {
          .stat-value {
            font-size: 24px;
            font-weight: 700;
            color: v-bind('$text-color-primary');
            line-height: 1;
          }
          
          .stat-label {
            font-size: 14px;
            color: v-bind('$text-color-secondary');
            margin-top: 4px;
          }
        }
      }
    }
  }
  
  .dashboard-content {
    .content-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 24px;
      
      h2 {
        margin: 0;
        font-size: 20px;
        font-weight: 600;
        color: v-bind('$text-color-primary');
      }
    }
    
    .categories-section {
      margin-bottom: 24px;
    }
    
    .empty-search {
      padding: 60px 0;
      text-align: center;
    }
  }
}
</style>