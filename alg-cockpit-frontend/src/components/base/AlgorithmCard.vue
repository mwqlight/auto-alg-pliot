<template>
  <el-card class="algorithm-card" shadow="hover" @click="handleClick">
    <template #header>
      <div class="card-header">
        <div class="header-left">
          <el-icon :size="20" :color="getCategoryColor(algorithm.category)">
            <component :is="getCategoryIcon(algorithm.category)" />
          </el-icon>
          <span class="algorithm-name">{{ algorithm.name }}</span>
        </div>
        <el-tag 
          :type="getComplexityType(algorithm.complexity)"
          size="small"
        >
          {{ algorithm.complexity }}
        </el-tag>
      </div>
    </template>
    
    <div class="card-content">
      <p class="algorithm-description">{{ algorithm.description }}</p>
      
      <div class="algorithm-meta">
        <div class="meta-item">
          <el-icon><Clock /></el-icon>
          <span>平均时间: {{ algorithm.averageTime }}ms</span>
        </div>
        <div class="meta-item">
          <el-icon><DataLine /></el-icon>
          <span>空间复杂度: {{ algorithm.spaceComplexity }}</span>
        </div>
      </div>
    </div>
    
    <template #footer>
      <div class="card-footer">
        <el-button type="primary" size="small" @click.stop="handleExecute">
          <el-icon><Play /></el-icon>
          执行算法
        </el-button>
        <el-button size="small" @click.stop="handleDetail">
          <el-icon><View /></el-icon>
          查看详情
        </el-button>
      </div>
    </template>
  </el-card>
</template>

<script setup lang="ts">
import { Algorithm } from '@/types/algorithm'

interface Props {
  algorithm: Algorithm
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

const handleClick = () => {
  emit('click', props.algorithm)
}

const handleExecute = () => {
  emit('execute', props.algorithm)
}

const handleDetail = () => {
  emit('detail', props.algorithm)
}
</script>

<style scoped lang="scss">
.algorithm-card {
  height: 100%;
  transition: all 0.3s ease;
  border: 1px solid v-bind('$border-color-light');
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    border-color: v-bind('$primary-color');
  }
  
  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    
    .header-left {
      display: flex;
      align-items: center;
      gap: 8px;
      
      .algorithm-name {
        font-size: 16px;
        font-weight: 600;
        color: v-bind('$text-color-primary');
      }
    }
  }
  
  .card-content {
    .algorithm-description {
      color: v-bind('$text-color-regular');
      line-height: 1.5;
      margin-bottom: 16px;
      display: -webkit-box;
      -webkit-line-clamp: 3;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }
    
    .algorithm-meta {
      display: flex;
      flex-direction: column;
      gap: 8px;
      
      .meta-item {
        display: flex;
        align-items: center;
        gap: 6px;
        font-size: 12px;
        color: v-bind('$text-color-secondary');
        
        .el-icon {
          font-size: 14px;
        }
      }
    }
  }
  
  .card-footer {
    display: flex;
    gap: 8px;
    justify-content: flex-end;
  }
}
</style>