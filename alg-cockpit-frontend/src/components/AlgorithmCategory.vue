<template>
  <div class="algorithm-category">
    <div class="category-header">
      <div class="header-left">
        <el-icon :size="24" :color="getCategoryColor(category)">
          <component :is="getCategoryIcon(category)" />
        </el-icon>
        <h2 class="category-title">{{ getCategoryName(category) }}</h2>
      </div>
      <span class="algorithm-count">{{ algorithms.length }} 个算法</span>
    </div>
    
    <div class="algorithms-grid">
      <AlgorithmCard
        v-for="algorithm in algorithms"
        :key="algorithm.id"
        :algorithm="algorithm"
        @execute="handleExecute"
        @detail="handleDetail"
        @click="handleCardClick"
      />
    </div>
    
    <div v-if="algorithms.length === 0" class="empty-state">
      <el-empty description="暂无算法数据" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { Algorithm } from '@/types/algorithm'
import AlgorithmCard from './base/AlgorithmCard.vue'

interface Props {
  category: string
  algorithms: Algorithm[]
}

const props = defineProps<Props>()
const emit = defineEmits<{
  execute: [algorithm: Algorithm]
  detail: [algorithm: Algorithm]
  click: [algorithm: Algorithm]
}>()

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

const handleExecute = (algorithm: Algorithm) => {
  emit('execute', algorithm)
}

const handleDetail = (algorithm: Algorithm) => {
  emit('detail', algorithm)
}

const handleCardClick = (algorithm: Algorithm) => {
  emit('click', algorithm)
}
</script>

<style scoped lang="scss">
.algorithm-category {
  margin-bottom: 32px;
  
  .category-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;
    padding: 0 8px;
    
    .header-left {
      display: flex;
      align-items: center;
      gap: 12px;
      
      .category-title {
        margin: 0;
        font-size: 20px;
        font-weight: 600;
        color: v-bind('$text-color-primary');
      }
    }
    
    .algorithm-count {
      color: v-bind('$text-color-secondary');
      font-size: 14px;
    }
  }
  
  .algorithms-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
    gap: 20px;
  }
  
  .empty-state {
    padding: 40px 0;
    text-align: center;
  }
}
</style>