<template>
  <TechLayout>
    <div class="dashboard">
      <!-- 页面标题和统计信息 -->
      <div class="dashboard-header">
        <div class="header-content">
          <h1 class="dashboard-title">算法学习平台控制台</h1>
          <p class="dashboard-subtitle">欢迎使用算法可视化学习平台</p>
        </div>
        <div class="stats-grid">
          <TechCard class="stat-card" type="primary">
            <template #icon>
              <div class="stat-icon">📚</div>
            </template>
            <template #title>算法总数</template>
            <template #content>
              <div class="stat-value">{{ algorithmStats.total }}</div>
              <div class="stat-label">个算法</div>
            </template>
          </TechCard>
          
          <TechCard class="stat-card" type="success">
            <template #icon>
              <div class="stat-icon">✅</div>
            </template>
            <template #title>已学习</template>
            <template #content>
              <div class="stat-value">{{ algorithmStats.learned }}</div>
              <div class="stat-label">个算法</div>
            </template>
          </TechCard>
          
          <TechCard class="stat-card" type="warning">
            <template #icon>
              <div class="stat-icon">⏱️</div>
            </template>
            <template #title>总执行次数</template>
            <template #content>
              <div class="stat-value">{{ executionStats.total }}</div>
              <div class="stat-label">次执行</div>
            </template>
          </TechCard>
          
          <TechCard class="stat-card" type="info">
            <template #icon>
              <div class="stat-icon">📈</div>
            </template>
            <template #title>成功率</template>
            <template #content>
              <div class="stat-value">{{ executionStats.successRate }}%</div>
              <div class="stat-label">平均执行时间 {{ executionStats.avgExecutionTime.toFixed(2) }}ms</div>
            </template>
          </TechCard>
        </div>
      </div>

      <!-- 主要内容区域 -->
      <div class="dashboard-content">
        <!-- 快速访问区域 -->
        <div class="quick-access">
          <h2 class="section-title">快速访问</h2>
          <div class="quick-grid">
            <TechCard class="quick-card" type="gradient" @click="navigateTo('algorithms')">
              <template #icon>
                <div class="quick-icon">🔢</div>
              </template>
              <template #title>算法库</template>
              <template #content>
                <p>浏览所有算法，按分类筛选</p>
              </template>
            </TechCard>
            
            <TechCard class="quick-card" type="gradient" @click="navigateTo('visualization')">
              <template #icon>
                <div class="quick-icon">🎯</div>
              </template>
              <template #title>可视化</template>
              <template #content>
                <p>算法执行过程可视化演示</p>
              </template>
            </TechCard>
            
            <TechCard class="quick-card" type="gradient" @click="navigateTo('complexity')">
              <template #icon>
                <div class="quick-icon">📊</div>
              </template>
              <template #title>复杂度分析</template>
              <template #content>
                <p>算法性能对比和分析</p>
              </template>
            </TechCard>
            
            <TechCard class="quick-card" type="gradient" @click="navigateTo('practice')">
              <template #icon>
                <div class="quick-icon">💡</div>
              </template>
              <template #title>练习模式</template>
              <template #content>
                <p>算法实现和测试练习</p>
              </template>
            </TechCard>
          </div>
        </div>

        <!-- 最近活动 -->
        <div class="recent-activity">
          <h2 class="section-title">最近活动</h2>
          <TechCard class="activity-card">
            <template #title>执行历史</template>
            <template #content>
              <div v-if="recentExecutions.length > 0" class="execution-list">
                <div 
                  v-for="execution in recentExecutions" 
                  :key="execution.id"
                  class="execution-item"
                >
                  <div class="execution-info">
                    <span class="algorithm-name">{{ execution.algorithmName }}</span>
                    <span class="execution-time">{{ formatTime(execution.timestamp) }}</span>
                  </div>
                  <div class="execution-result">
                    <span :class="['status', execution.success ? 'success' : 'error']">
                      {{ execution.success ? '成功' : '失败' }}
                    </span>
                    <span class="duration">{{ execution.executionTime }}ms</span>
                  </div>
                </div>
              </div>
              <div v-else class="empty-state">
                <p>暂无执行记录</p>
                <TechButton @click="navigateTo('algorithms')" type="primary">开始学习</TechButton>
              </div>
            </template>
          </TechCard>
        </div>

        <!-- 学习进度 -->
        <div class="learning-progress">
          <h2 class="section-title">学习进度</h2>
          <div class="progress-grid">
            <TechCard class="progress-card">
              <template #title>算法分类进度</template>
              <template #content>
                <div v-for="category in categories" :key="category.id" class="category-progress">
                  <div class="category-info">
                    <span class="category-name">{{ category.name }}</span>
                    <span class="category-count">{{ getLearnedCount(category.id) }}/{{ category.count }}</span>
                  </div>
                  <div class="progress-bar">
                    <div 
                      class="progress-fill" 
                      :style="{ width: getProgressPercentage(category.id) + '%' }"
                    ></div>
                  </div>
                </div>
              </template>
            </TechCard>
            
            <TechCard class="progress-card">
              <template #title>难度分布</template>
              <template #content>
                <div class="difficulty-chart">
                  <div v-for="diff in difficultyStats" :key="diff.level" class="difficulty-item">
                    <span class="difficulty-label">{{ diff.label }}</span>
                    <span class="difficulty-count">{{ diff.count }}</span>
                    <div class="difficulty-bar">
                      <div 
                        class="difficulty-fill" 
                        :class="diff.level"
                        :style="{ width: diff.percentage + '%' }"
                      ></div>
                    </div>
                  </div>
                </div>
              </template>
            </TechCard>
          </div>
        </div>
      </div>
    </div>
  </TechLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import TechLayout from '@/components/layout/TechLayout.vue'
import TechCard from '@/components/base/TechCard.vue'
import TechButton from '@/components/base/TechButton.vue'
import { useAlgorithmStore } from '@/store/algorithms'
import { useAppStore } from '@/store/app'

const router = useRouter()
const algorithmStore = useAlgorithmStore()
const appStore = useAppStore()

// 统计数据
const algorithmStats = computed(() => {
  const total = algorithmStore.algorithms.length
  const learned = algorithmStore.favoriteAlgorithms.length // 简化：收藏的算法视为已学习
  return { total, learned }
})

const executionStats = computed(() => {
  return algorithmStore.executionStats
})

const recentExecutions = computed(() => {
  return algorithmStore.recentExecutions
})

const categories = computed(() => {
  return algorithmStore.categories
})

const difficultyStats = computed(() => {
  const algorithms = algorithmStore.algorithms
  const total = algorithms.length
  
  const easy = algorithms.filter(a => a.difficulty === 'easy').length
  const medium = algorithms.filter(a => a.difficulty === 'medium').length
  const hard = algorithms.filter(a => a.difficulty === 'hard').length
  
  return [
    { level: 'easy', label: '简单', count: easy, percentage: total > 0 ? (easy / total) * 100 : 0 },
    { level: 'medium', label: '中等', count: medium, percentage: total > 0 ? (medium / total) * 100 : 0 },
    { level: 'hard', label: '困难', count: hard, percentage: total > 0 ? (hard / total) * 100 : 0 }
  ]
})

// 方法
const navigateTo = (route: string) => {
  router.push(`/${route}`)
}

const formatTime = (timestamp: number) => {
  const date = new Date(timestamp)
  return date.toLocaleString('zh-CN')
}

const getLearnedCount = (categoryId: string) => {
  const algorithms = algorithmStore.getAlgorithmsByCategory(categoryId)
  const favorites = algorithmStore.favorites
  return algorithms.filter(a => favorites.includes(a.id)).length
}

const getProgressPercentage = (categoryId: string) => {
  const category = categories.value.find(c => c.id === categoryId)
  if (!category || category.count === 0) return 0
  
  const learned = getLearnedCount(categoryId)
  return (learned / category.count) * 100
}

// 生命周期
onMounted(() => {
  algorithmStore.initialize()
})
</script>

<style lang="scss" scoped>
@import '@/styles/tech-theme.scss';

.dashboard {
  padding: 2rem;
  min-height: 100vh;
  background: linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 100%);
}

.dashboard-header {
  margin-bottom: 3rem;
}

.header-content {
  text-align: center;
  margin-bottom: 2rem;
}

.dashboard-title {
  font-size: 2.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, var(--primary) 0%, var(--accent) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 0.5rem;
}

.dashboard-subtitle {
  font-size: 1.1rem;
  color: var(--text-secondary);
  opacity: 0.8;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-top: 2rem;
}

.stat-card {
  text-align: center;
  padding: 1.5rem;
}

.stat-icon {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.stat-value {
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 0.5rem;
}

.stat-label {
  font-size: 0.9rem;
  color: var(--text-secondary);
  opacity: 0.8;
}

.dashboard-content {
  display: grid;
  gap: 2rem;
  grid-template-columns: 2fr 1fr;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 1rem;
  border-left: 4px solid var(--primary);
  padding-left: 1rem;
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 1.5rem;
}

.quick-card {
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
  }
}

.quick-icon {
  font-size: 2rem;
  margin-bottom: 1rem;
}

.activity-card {
  height: 100%;
}

.execution-list {
  max-height: 300px;
  overflow-y: auto;
}

.execution-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border-bottom: 1px solid var(--border-color);
  
  &:last-child {
    border-bottom: none;
  }
}

.execution-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.algorithm-name {
  font-weight: 600;
  color: var(--text-primary);
}

.execution-time {
  font-size: 0.8rem;
  color: var(--text-secondary);
  opacity: 0.7;
}

.execution-result {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 0.25rem;
}

.status {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 600;
  
  &.success {
    background: var(--success-light);
    color: var(--success);
  }
  
  &.error {
    background: var(--error-light);
    color: var(--error);
  }
}

.duration {
  font-size: 0.8rem;
  color: var(--text-secondary);
  opacity: 0.7;
}

.empty-state {
  text-align: center;
  padding: 2rem;
  color: var(--text-secondary);
}

.progress-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

.category-progress {
  margin-bottom: 1.5rem;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.category-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.category-name {
  font-weight: 600;
  color: var(--text-primary);
}

.category-count {
  font-size: 0.9rem;
  color: var(--text-secondary);
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

.difficulty-item {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.difficulty-label {
  width: 60px;
  font-weight: 600;
  color: var(--text-primary);
}

.difficulty-count {
  width: 40px;
  text-align: right;
  font-size: 0.9rem;
  color: var(--text-secondary);
}

.difficulty-bar {
  flex: 1;
  height: 8px;
  background: var(--bg-tertiary);
  border-radius: 4px;
  overflow: hidden;
  margin: 0 1rem;
}

.difficulty-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.3s ease;
  
  &.easy {
    background: var(--success);
  }
  
  &.medium {
    background: var(--warning);
  }
  
  &.hard {
    background: var(--error);
  }
}

// 响应式设计
@media (max-width: 1024px) {
  .dashboard-content {
    grid-template-columns: 1fr;
  }
  
  .progress-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .dashboard {
    padding: 1rem;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .quick-grid {
    grid-template-columns: 1fr;
  }
}
</style>