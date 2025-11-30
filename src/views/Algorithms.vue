<template>
  <TechLayout>
    <div class="algorithms-page">
      <!-- 页面头部 -->
      <div class="page-header">
        <h1 class="page-title">算法库</h1>
        <p class="page-subtitle">探索各种算法，按分类浏览和学习</p>
        
        <!-- 搜索和过滤区域 -->
        <div class="filters-section">
          <div class="search-box">
            <TechInput
              v-model="searchQuery"
              placeholder="搜索算法名称、描述或标签..."
              size="large"
              prefix-icon="🔍"
              @input="handleSearch"
            />
          </div>
          
          <div class="filter-controls">
            <TechSelect
              v-model="selectedCategory"
              :options="categoryOptions"
              placeholder="选择分类"
              clearable
              @change="handleCategoryChange"
            />
            
            <TechSelect
              v-model="selectedDifficulty"
              :options="difficultyOptions"
              placeholder="选择难度"
              clearable
              multiple
              @change="handleDifficultyChange"
            />
            
            <TechSelect
              v-model="selectedLanguage"
              :options="languageOptions"
              placeholder="选择语言"
              clearable
              multiple
              @change="handleLanguageChange"
            />
            
            <TechButton 
              type="secondary" 
              @click="clearFilters"
              size="medium"
            >
              清除筛选
            </TechButton>
          </div>
        </div>
      </div>

      <!-- 主要内容区域 -->
      <div class="algorithms-content">
        <!-- 分类导航 -->
        <div class="categories-section">
          <div class="categories-grid">
            <div 
              v-for="category in categories" 
              :key="category.id"
              :class="['category-card', { active: selectedCategory === category.id }]"
              @click="selectCategory(category.id)"
            >
              <div class="category-icon">{{ category.icon }}</div>
              <div class="category-info">
                <h3 class="category-name">{{ category.name }}</h3>
                <span class="algorithm-count">{{ category.count }} 个算法</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 算法列表 -->
        <div class="algorithms-section">
          <div class="algorithms-header">
            <h2 class="section-title">
              {{ getSectionTitle() }}
              <span class="result-count">({{ filteredAlgorithms.length }} 个结果)</span>
            </h2>
            
            <div class="view-controls">
              <TechButton 
                :type="viewMode === 'grid' ? 'primary' : 'secondary'"
                @click="viewMode = 'grid'"
                size="small"
              >
                📱 网格视图
              </TechButton>
              <TechButton 
                :type="viewMode === 'list' ? 'primary' : 'secondary'"
                @click="viewMode = 'list'"
                size="small"
              >
                📋 列表视图
              </TechButton>
            </div>
          </div>

          <!-- 加载状态 -->
          <div v-if="loading" class="loading-state">
            <div class="loading-spinner"></div>
            <p>正在加载算法数据...</p>
          </div>

          <!-- 空状态 -->
          <div v-else-if="filteredAlgorithms.length === 0" class="empty-state">
            <div class="empty-icon">🔍</div>
            <h3>未找到匹配的算法</h3>
            <p>尝试调整搜索条件或选择其他分类</p>
            <TechButton @click="clearFilters" type="primary">
              显示所有算法
            </TechButton>
          </div>

          <!-- 算法网格视图 -->
          <div v-else-if="viewMode === 'grid'" class="algorithms-grid">
            <TechCard 
              v-for="algorithm in filteredAlgorithms" 
              :key="algorithm.id"
              class="algorithm-card"
              :type="getCardType(algorithm.difficulty)"
            >
              <template #header>
                <div class="card-header">
                  <div class="algorithm-title">
                    <h3>{{ algorithm.name }}</h3>
                    <span :class="['difficulty-badge', algorithm.difficulty]">
                      {{ getDifficultyLabel(algorithm.difficulty) }}
                    </span>
                  </div>
                  <div class="card-actions">
                    <button 
                      :class="['favorite-btn', { active: isFavorite(algorithm.id) }]"
                      @click="toggleFavorite(algorithm.id)"
                      title="收藏"
                    >
                      ⭐
                    </button>
                  </div>
                </div>
              </template>
              
              <template #content>
                <div class="algorithm-content">
                  <p class="algorithm-description">{{ algorithm.description }}</p>
                  
                  <div class="algorithm-meta">
                    <div class="meta-item">
                      <span class="meta-label">时间复杂度:</span>
                      <code class="complexity-code">{{ algorithm.timeComplexity }}</code>
                    </div>
                    <div class="meta-item">
                      <span class="meta-label">空间复杂度:</span>
                      <code class="complexity-code">{{ algorithm.spaceComplexity }}</code>
                    </div>
                    <div class="meta-item">
                      <span class="meta-label">语言:</span>
                      <span class="language-tag">{{ algorithm.language.toUpperCase() }}</span>
                    </div>
                  </div>
                  
                  <div class="algorithm-tags">
                    <span 
                      v-for="tag in algorithm.tags" 
                      :key="tag"
                      class="tag"
                    >
                      {{ tag }}
                    </span>
                  </div>
                </div>
              </template>
              
              <template #footer>
                <div class="card-footer">
                  <TechButton 
                    type="primary" 
                    size="small"
                    @click="viewAlgorithm(algorithm.id)"
                  >
                    查看详情
                  </TechButton>
                  <TechButton 
                    type="secondary" 
                    size="small"
                    @click="runAlgorithm(algorithm.id)"
                  >
                    运行演示
                  </TechButton>
                </div>
              </template>
            </TechCard>
          </div>

          <!-- 算法列表视图 -->
          <div v-else class="algorithms-list">
            <div 
              v-for="algorithm in filteredAlgorithms" 
              :key="algorithm.id"
              class="algorithm-item"
            >
              <div class="item-main">
                <div class="item-header">
                  <h3 class="algorithm-name">{{ algorithm.name }}</h3>
                  <span :class="['difficulty-badge', algorithm.difficulty]">
                    {{ getDifficultyLabel(algorithm.difficulty) }}
                  </span>
                </div>
                <p class="algorithm-description">{{ algorithm.description }}</p>
                <div class="algorithm-meta">
                  <span class="complexity">
                    <strong>时间复杂度:</strong> {{ algorithm.timeComplexity }}
                  </span>
                  <span class="complexity">
                    <strong>空间复杂度:</strong> {{ algorithm.spaceComplexity }}
                  </span>
                  <span class="language">{{ algorithm.language.toUpperCase() }}</span>
                </div>
                <div class="algorithm-tags">
                  <span 
                    v-for="tag in algorithm.tags" 
                    :key="tag"
                    class="tag"
                  >
                    {{ tag }}
                  </span>
                </div>
              </div>
              
              <div class="item-actions">
                <button 
                  :class="['favorite-btn', { active: isFavorite(algorithm.id) }]"
                  @click="toggleFavorite(algorithm.id)"
                  title="收藏"
                >
                  ⭐
                </button>
                <TechButton 
                  type="primary" 
                  size="small"
                  @click="viewAlgorithm(algorithm.id)"
                >
                  详情
                </TechButton>
                <TechButton 
                  type="secondary" 
                  size="small"
                  @click="runAlgorithm(algorithm.id)"
                >
                  运行
                </TechButton>
              </div>
            </div>
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
import TechInput from '@/components/base/TechInput.vue'
import TechSelect from '@/components/base/TechSelect.vue'
import { useAlgorithmStore } from '@/store/algorithms'
import type { Algorithm } from '@/types/algorithm'

const router = useRouter()
const algorithmStore = useAlgorithmStore()

// 状态
const searchQuery = ref('')
const selectedCategory = ref('all')
const selectedDifficulty = ref<string[]>([])
const selectedLanguage = ref<string[]>([])
const viewMode = ref<'grid' | 'list'>('grid')
const loading = ref(false)

// 计算属性
const categories = computed(() => {
  return algorithmStore.categories
})

const algorithms = computed(() => {
  return algorithmStore.filteredAlgorithms
})

const filteredAlgorithms = computed(() => {
  let filtered = algorithms.value
  
  // 难度过滤
  if (selectedDifficulty.value.length > 0) {
    filtered = filtered.filter(alg => selectedDifficulty.value.includes(alg.difficulty))
  }
  
  // 语言过滤
  if (selectedLanguage.value.length > 0) {
    filtered = filtered.filter(alg => selectedLanguage.value.includes(alg.language))
  }
  
  return filtered
})

// 选项配置
const categoryOptions = computed(() => {
  return categories.value.map(cat => ({
    label: `${cat.icon} ${cat.name} (${cat.count})`,
    value: cat.id
  }))
})

const difficultyOptions = [
  { label: '简单', value: 'easy' },
  { label: '中等', value: 'medium' },
  { label: '困难', value: 'hard' }
]

const languageOptions = [
  { label: 'Java', value: 'java' },
  { label: 'Python', value: 'python' },
  { label: 'JavaScript', value: 'javascript' }
]

// 方法
const handleSearch = () => {
  algorithmStore.setSearchQuery(searchQuery.value)
}

const handleCategoryChange = () => {
  algorithmStore.setSelectedCategory(selectedCategory.value)
}

const handleDifficultyChange = () => {
  // 难度过滤在计算属性中处理
}

const handleLanguageChange = () => {
  // 语言过滤在计算属性中处理
}

const clearFilters = () => {
  searchQuery.value = ''
  selectedCategory.value = 'all'
  selectedDifficulty.value = []
  selectedLanguage.value = []
  algorithmStore.setSearchQuery('')
  algorithmStore.setSelectedCategory('all')
}

const selectCategory = (categoryId: string) => {
  selectedCategory.value = categoryId
  algorithmStore.setSelectedCategory(categoryId)
}

const getSectionTitle = () => {
  if (selectedCategory.value !== 'all') {
    const category = categories.value.find(c => c.id === selectedCategory.value)
    return category ? category.name : '算法'
  }
  return searchQuery.value ? `搜索 "${searchQuery.value}"` : '所有算法'
}

const getCardType = (difficulty: string) => {
  switch (difficulty) {
    case 'easy': return 'success'
    case 'medium': return 'warning'
    case 'hard': return 'error'
    default: return 'primary'
  }
}

const getDifficultyLabel = (difficulty: string) => {
  switch (difficulty) {
    case 'easy': return '简单'
    case 'medium': return '中等'
    case 'hard': return '困难'
    default: return difficulty
  }
}

const isFavorite = (algorithmId: string) => {
  return algorithmStore.isFavorite(algorithmId)
}

const toggleFavorite = (algorithmId: string) => {
  algorithmStore.toggleFavorite(algorithmId)
}

const viewAlgorithm = (algorithmId: string) => {
  router.push(`/algorithm/${algorithmId}`)
}

const runAlgorithm = (algorithmId: string) => {
  router.push(`/visualization?algorithm=${algorithmId}`)
}

// 生命周期
onMounted(() => {
  algorithmStore.initialize()
})
</script>

<style lang="scss" scoped>
@import '@/styles/tech-theme.scss';

.algorithms-page {
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
  margin-bottom: 2rem;
}

.filters-section {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 1.5rem;
  align-items: end;
  margin-bottom: 2rem;
}

.search-box {
  min-width: 300px;
}

.filter-controls {
  display: flex;
  gap: 1rem;
  align-items: center;
  flex-wrap: wrap;
}

.categories-section {
  margin-bottom: 2rem;
}

.categories-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.category-card {
  display: flex;
  align-items: center;
  padding: 1.5rem;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    border-color: var(--primary);
  }
  
  &.active {
    border-color: var(--primary);
    background: linear-gradient(135deg, var(--primary-light) 0%, transparent 100%);
  }
}

.category-icon {
  font-size: 2rem;
  margin-right: 1rem;
}

.category-info {
  flex: 1;
}

.category-name {
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 0.25rem;
}

.algorithm-count {
  font-size: 0.9rem;
  color: var(--text-secondary);
  opacity: 0.8;
}

.algorithms-section {
  margin-top: 2rem;
}

.algorithms-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-primary);
}

.result-count {
  font-size: 1rem;
  font-weight: 400;
  color: var(--text-secondary);
  margin-left: 0.5rem;
}

.view-controls {
  display: flex;
  gap: 0.5rem;
}

.loading-state {
  text-align: center;
  padding: 3rem;
  color: var(--text-secondary);
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid var(--border-color);
  border-top: 3px solid var(--primary);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: var(--text-secondary);
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.algorithms-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 1.5rem;
}

.algorithm-card {
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.algorithm-title {
  flex: 1;
}

.algorithm-title h3 {
  font-size: 1.2rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 0.5rem;
}

.difficulty-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 600;
  
  &.easy {
    background: var(--success-light);
    color: var(--success);
  }
  
  &.medium {
    background: var(--warning-light);
    color: var(--warning);
  }
  
  &.hard {
    background: var(--error-light);
    color: var(--error);
  }
}

.card-actions {
  margin-left: 1rem;
}

.favorite-btn {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  opacity: 0.5;
  transition: all 0.3s ease;
  
  &:hover {
    opacity: 1;
    transform: scale(1.1);
  }
  
  &.active {
    opacity: 1;
    color: gold;
  }
}

.algorithm-content {
  margin-bottom: 1rem;
}

.algorithm-description {
  color: var(--text-secondary);
  line-height: 1.5;
  margin-bottom: 1rem;
}

.algorithm-meta {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.meta-label {
  font-size: 0.9rem;
  color: var(--text-secondary);
  min-width: 80px;
}

.complexity-code {
  background: var(--bg-tertiary);
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 0.9rem;
  color: var(--accent);
}

.language-tag {
  background: var(--primary-light);
  color: var(--primary);
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 600;
}

.algorithm-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.tag {
  background: var(--bg-tertiary);
  color: var(--text-secondary);
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.8rem;
}

.card-footer {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
}

.algorithms-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.algorithm-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateX(4px);
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  }
}

.item-main {
  flex: 1;
  margin-right: 2rem;
}

.item-header {
  display: flex;
  align-items: center;
  margin-bottom: 0.5rem;
  gap: 1rem;
}

.algorithm-name {
  font-size: 1.2rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.algorithm-meta {
  display: flex;
  gap: 1rem;
  margin: 0.5rem 0;
  flex-wrap: wrap;
}

.complexity {
  font-size: 0.9rem;
  color: var(--text-secondary);
}

.language {
  background: var(--primary-light);
  color: var(--primary);
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 600;
}

.item-actions {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

// 响应式设计
@media (max-width: 1024px) {
  .filters-section {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .filter-controls {
    justify-content: flex-start;
  }
  
  .algorithms-grid {
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  }
}

@media (max-width: 768px) {
  .algorithms-page {
    padding: 1rem;
  }
  
  .categories-grid {
    grid-template-columns: 1fr;
  }
  
  .algorithms-grid {
    grid-template-columns: 1fr;
  }
  
  .algorithm-item {
    flex-direction: column;
    align-items: stretch;
    gap: 1rem;
  }
  
  .item-main {
    margin-right: 0;
  }
  
  .item-actions {
    justify-content: flex-end;
  }
  
  .algorithms-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .view-controls {
    align-self: flex-end;
  }
}
</style>