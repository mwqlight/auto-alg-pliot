<template>
  <div class="algorithms-page">
    <div class="page-header">
      <h1 class="page-title">
        <el-icon><List /></el-icon>
        算法管理
      </h1>
      <el-button type="primary" @click="handleCreate">
        <el-icon><Plus /></el-icon>
        新增算法
      </el-button>
    </div>
    
    <div class="page-content">
      <el-card class="filter-card">
        <div class="filter-row">
          <el-input
            v-model="filterParams.keyword"
            placeholder="搜索算法名称或描述"
            prefix-icon="Search"
            style="width: 300px"
            clearable
          />
          
          <el-select
            v-model="filterParams.category"
            placeholder="选择分类"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="category in categoryOptions"
              :key="category.value"
              :label="category.label"
              :value="category.value"
            />
          </el-select>
          
          <el-select
            v-model="filterParams.complexity"
            placeholder="选择复杂度"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="complexity in complexityOptions"
              :key="complexity.value"
              :label="complexity.label"
              :value="complexity.value"
            />
          </el-select>
          
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </div>
      </el-card>
      
      <el-card class="table-card">
        <template #header>
          <div class="table-header">
            <span>算法列表</span>
            <div class="header-actions">
              <el-button size="small" @click="handleRefresh">
                <el-icon><Refresh /></el-icon>
                刷新
              </el-button>
            </div>
          </div>
        </template>
        
        <el-table
          :data="tableData"
          v-loading="loading"
          style="width: 100%"
          @sort-change="handleSortChange"
        >
          <el-table-column prop="name" label="算法名称" min-width="120">
            <template #default="{ row }">
              <div class="algorithm-name-cell">
                <el-icon :color="getCategoryColor(row.category)">
                  <component :is="getCategoryIcon(row.category)" />
                </el-icon>
                <span class="name-text">{{ row.name }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="category" label="分类" width="120">
            <template #default="{ row }">
              <el-tag :color="getCategoryColor(row.category)" effect="light">
                {{ getCategoryName(row.category) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="complexity" label="时间复杂度" width="120">
            <template #default="{ row }">
              <el-tag :type="getComplexityType(row.complexity)" size="small">
                {{ row.complexity }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="spaceComplexity" label="空间复杂度" width="120" />
          
          <el-table-column prop="averageTime" label="平均时间(ms)" width="120" sortable />
          
          <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
          
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="{ row }">
              <el-button size="small" @click="handleView(row)">
                <el-icon><View /></el-icon>
                查看
              </el-button>
              <el-button size="small" type="primary" @click="handleExecute(row)">
                <el-icon><Play /></el-icon>
                执行
              </el-button>
              <el-button size="small" type="danger" @click="handleDelete(row)">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :total="pagination.total"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAlgorithmsStore } from '@/store/algorithms'
import type { Algorithm } from '@/types/algorithm'

const router = useRouter()
const algorithmsStore = useAlgorithmsStore()

const loading = ref(false)
const tableData = ref<Algorithm[]>([])

const filterParams = reactive({
  keyword: '',
  category: '',
  complexity: ''
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const categoryOptions = [
  { label: '排序算法', value: 'sorting' },
  { label: '查找算法', value: 'searching' },
  { label: '图算法', value: 'graph' },
  { label: '动态规划', value: 'dynamic-programming' },
  { label: '字符串算法', value: 'string' },
  { label: '数学算法', value: 'math' }
]

const complexityOptions = [
  { label: 'O(1)', value: 'O(1)' },
  { label: 'O(log n)', value: 'O(log n)' },
  { label: 'O(n)', value: 'O(n)' },
  { label: 'O(n log n)', value: 'O(n log n)' },
  { label: 'O(n²)', value: 'O(n²)' },
  { label: 'O(2^n)', value: 'O(2^n)' }
]

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
    'sorting': '排序',
    'searching': '查找',
    'graph': '图算法',
    'dynamic-programming': '动态规划',
    'string': '字符串',
    'math': '数学'
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

const loadTableData = async () => {
  loading.value = true
  try {
    await algorithmsStore.getAlgorithms({
      keyword: filterParams.keyword,
      category: filterParams.category,
      complexity: filterParams.complexity,
      page: pagination.current,
      size: pagination.size
    })
    
    tableData.value = algorithmsStore.algorithms
    pagination.total = algorithmsStore.total || 0
  } catch (error) {
    ElMessage.error('加载算法列表失败')
    console.error('Failed to load algorithms:', error)
    
    // 使用模拟数据作为后备
    tableData.value = [
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
    pagination.total = tableData.value.length
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  loadTableData()
}

const handleReset = () => {
  filterParams.keyword = ''
  filterParams.category = ''
  filterParams.complexity = ''
  pagination.current = 1
  loadTableData()
}

const handleRefresh = () => {
  loadTableData()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.current = 1
  loadTableData()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  loadTableData()
}

const handleSortChange = (sort: any) => {
  console.log('Sort changed:', sort)
  // 实现排序逻辑
}

const handleCreate = () => {
  router.push('/algorithms/create')
}

const handleView = (row: Algorithm) => {
  router.push(`/algorithms/${row.id}`)
}

const handleExecute = (row: Algorithm) => {
  router.push({
    path: '/execution',
    query: { algorithmId: row.id }
  })
}

const handleDelete = async (row: Algorithm) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除算法"${row.name}"吗？此操作不可恢复。`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 这里需要实现实际的删除API调用
    // 由于状态管理模块没有删除方法，暂时使用模拟删除
    ElMessage.success('删除功能待实现')
    
    // 模拟删除效果
    tableData.value = tableData.value.filter(item => item.id !== row.id)
    pagination.total -= 1
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error('Failed to delete algorithm:', error)
    }
  }
}

onMounted(() => {
  loadTableData()
})
</script>

<style scoped lang="scss">
.algorithms-page {
  padding: 24px;
  
  .page-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 24px;
    
    .page-title {
      display: flex;
      align-items: center;
      gap: 12px;
      font-size: 24px;
      font-weight: 600;
      color: v-bind('$text-color-primary');
      margin: 0;
    }
  }
  
  .page-content {
    .filter-card {
      margin-bottom: 16px;
      
      .filter-row {
        display: flex;
        align-items: center;
        gap: 12px;
        flex-wrap: wrap;
      }
    }
    
    .table-card {
      .table-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        
        .header-actions {
          display: flex;
          gap: 8px;
        }
      }
      
      .algorithm-name-cell {
        display: flex;
        align-items: center;
        gap: 8px;
        
        .name-text {
          font-weight: 500;
        }
      }
      
      .pagination-container {
        display: flex;
        justify-content: flex-end;
        margin-top: 16px;
      }
    }
  }
}
</style>