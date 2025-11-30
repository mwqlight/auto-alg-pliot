<template>
  <TechLayout>
    <div class="profile-page">
      <!-- 页面头部 -->
      <div class="page-header">
        <h1 class="page-title">个人中心</h1>
        <p class="page-subtitle">管理您的个人信息、学习进度和系统设置</p>
      </div>

      <!-- 主要内容区域 -->
      <div class="profile-content">
        <!-- 左侧个人信息卡片 -->
        <div class="profile-sidebar">
          <TechCard class="user-card">
            <template #content>
              <div class="user-avatar-section">
                <div class="avatar-container">
                  <div class="user-avatar">
                    <span class="avatar-text">{{ userInitials }}</span>
                  </div>
                  <div class="avatar-status">
                    <div class="status-indicator online"></div>
                  </div>
                </div>
                
                <div class="user-info">
                  <h3 class="user-name">{{ userInfo.name }}</h3>
                  <p class="user-role">{{ userInfo.role }}</p>
                  <p class="user-email">{{ userInfo.email }}</p>
                  <div class="user-level">
                    <span class="level-text">Lv.{{ userInfo.level }}</span>
                    <div class="level-progress">
                      <div 
                        class="level-progress-fill" 
                        :style="{ width: userInfo.levelProgress + '%' }"
                      ></div>
                    </div>
                    <span class="level-percent">{{ userInfo.levelProgress }}%</span>
                  </div>
                </div>
              </div>

              <div class="user-stats">
                <div class="stat-item">
                  <div class="stat-icon">📚</div>
                  <div class="stat-content">
                    <div class="stat-value">{{ userStats.learnedAlgorithms }}</div>
                    <div class="stat-label">已学算法</div>
                  </div>
                </div>
                
                <div class="stat-item">
                  <div class="stat-icon">🎯</div>
                  <div class="stat-content">
                    <div class="stat-value">{{ userStats.practiceCount }}</div>
                    <div class="stat-label">练习次数</div>
                  </div>
                </div>
                
                <div class="stat-item">
                  <div class="stat-icon">⭐</div>
                  <div class="stat-content">
                    <div class="stat-value">{{ userStats.achievementCount }}</div>
                    <div class="stat-label">成就数量</div>
                  </div>
                </div>
              </div>

              <div class="user-actions">
                <TechButton type="primary" block @click="editProfile">
                  ✏️ 编辑资料
                </TechButton>
                <TechButton type="secondary" block @click="changePassword">
                  🔒 修改密码
                </TechButton>
              </div>
            </template>
          </TechCard>

          <TechCard class="quick-stats-card">
            <template #title>学习统计</template>
            <template #content>
              <div class="stats-chart">
                <div class="chart-item">
                  <div class="chart-label">学习时长</div>
                  <div class="chart-value">{{ formatTime(userStats.learningTime) }}</div>
                  <div class="chart-bar">
                    <div 
                      class="chart-fill" 
                      :style="{ width: Math.min(userStats.learningTime / 100, 100) + '%' }"
                    ></div>
                  </div>
                </div>
                
                <div class="chart-item">
                  <div class="chart-label">算法掌握度</div>
                  <div class="chart-value">{{ userStats.masteryRate }}%</div>
                  <div class="chart-bar">
                    <div 
                      class="chart-fill" 
                      :style="{ width: userStats.masteryRate + '%' }"
                    ></div>
                  </div>
                </div>
                
                <div class="chart-item">
                  <div class="chart-label">正确率</div>
                  <div class="chart-value">{{ userStats.accuracyRate }}%</div>
                  <div class="chart-bar">
                    <div 
                      class="chart-fill" 
                      :style="{ width: userStats.accuracyRate + '%' }"
                    ></div>
                  </div>
                </div>
              </div>
            </template>
          </TechCard>
        </div>

        <!-- 右侧内容区域 -->
        <div class="profile-main">
          <!-- 标签页导航 -->
          <div class="tab-navigation">
            <button 
              v-for="tab in tabs" 
              :key="tab.id"
              :class="['tab-button', { active: activeTab === tab.id }]"
              @click="activeTab = tab.id"
            >
              <span class="tab-icon">{{ tab.icon }}</span>
              <span class="tab-label">{{ tab.label }}</span>
            </button>
          </div>

          <!-- 标签页内容 -->
          <div class="tab-content">
            <!-- 基本信息标签页 -->
            <div v-if="activeTab === 'basic'" class="tab-panel">
              <TechCard>
                <template #title>基本信息</template>
                <template #content>
                  <div class="form-section">
                    <div class="form-row">
                      <div class="form-group">
                        <label>姓名</label>
                        <TechInput v-model="profileForm.name" placeholder="请输入姓名" />
                      </div>
                      <div class="form-group">
                        <label>邮箱</label>
                        <TechInput v-model="profileForm.email" type="email" placeholder="请输入邮箱" />
                      </div>
                    </div>
                    
                    <div class="form-row">
                      <div class="form-group">
                        <label>手机号</label>
                        <TechInput v-model="profileForm.phone" placeholder="请输入手机号" />
                      </div>
                      <div class="form-group">
                        <label>职位</label>
                        <TechInput v-model="profileForm.position" placeholder="请输入职位" />
                      </div>
                    </div>
                    
                    <div class="form-group">
                      <label>个人简介</label>
                      <textarea 
                        v-model="profileForm.bio" 
                        class="bio-textarea"
                        placeholder="请输入个人简介"
                        rows="4"
                      ></textarea>
                    </div>
                    
                    <div class="form-actions">
                      <TechButton type="primary" @click="saveProfile">保存修改</TechButton>
                      <TechButton type="secondary" @click="resetProfile">重置</TechButton>
                    </div>
                  </div>
                </template>
              </TechCard>
            </div>

            <!-- 学习进度标签页 -->
            <div v-if="activeTab === 'progress'" class="tab-panel">
              <TechCard>
                <template #title>学习进度</template>
                <template #content>
                  <div class="progress-section">
                    <div class="progress-overview">
                      <div class="overview-item">
                        <div class="overview-value">{{ learningProgress.totalAlgorithms }}</div>
                        <div class="overview-label">总算法数</div>
                      </div>
                      <div class="overview-item">
                        <div class="overview-value">{{ learningProgress.learnedAlgorithms }}</div>
                        <div class="overview-label">已学算法</div>
                      </div>
                      <div class="overview-item">
                        <div class="overview-value">{{ learningProgress.completionRate }}%</div>
                        <div class="overview-label">完成率</div>
                      </div>
                      <div class="overview-item">
                        <div class="overview-value">{{ learningProgress.currentStreak }}</div>
                        <div class="overview-label">连续学习天数</div>
                      </div>
                    </div>

                    <div class="algorithm-progress">
                      <h4>算法分类进度</h4>
                      <div 
                        v-for="category in learningProgress.categories" 
                        :key="category.name"
                        class="category-progress"
                      >
                        <div class="category-header">
                          <span class="category-name">{{ category.name }}</span>
                          <span class="category-percent">{{ category.progress }}%</span>
                        </div>
                        <div class="progress-bar">
                          <div 
                            class="progress-fill" 
                            :style="{ width: category.progress + '%' }"
                          ></div>
                        </div>
                        <div class="category-stats">
                          <span>{{ category.learned }}/{{ category.total }} 个算法</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </template>
              </TechCard>

              <TechCard>
                <template #title>最近学习记录</template>
                <template #content>
                  <div class="learning-history">
                    <div 
                      v-for="record in learningHistory" 
                      :key="record.id"
                      class="history-item"
                    >
                      <div class="history-icon">{{ record.icon }}</div>
                      <div class="history-content">
                        <div class="history-title">{{ record.title }}</div>
                        <div class="history-time">{{ formatRelativeTime(record.time) }}</div>
                      </div>
                      <div class="history-duration">{{ formatDuration(record.duration) }}</div>
                    </div>
                  </div>
                </template>
              </TechCard>
            </div>

            <!-- 成就标签页 -->
            <div v-if="activeTab === 'achievements'" class="tab-panel">
              <TechCard>
                <template #title>成就系统</template>
                <template #content>
                  <div class="achievements-section">
                    <div class="achievements-stats">
                      <div class="achievement-stat">
                        <div class="stat-value">{{ achievements.unlocked }}/{{ achievements.total }}</div>
                        <div class="stat-label">已解锁成就</div>
                      </div>
                      <div class="achievement-stat">
                        <div class="stat-value">{{ achievements.rareCount }}</div>
                        <div class="stat-label">稀有成就</div>
                      </div>
                      <div class="achievement-stat">
                        <div class="stat-value">{{ achievements.points }}</div>
                        <div class="stat-label">成就点数</div>
                      </div>
                    </div>

                    <div class="achievements-grid">
                      <div 
                        v-for="achievement in achievements.list" 
                        :key="achievement.id"
                        :class="['achievement-card', { locked: !achievement.unlocked }]"
                      >
                        <div class="achievement-icon">{{ achievement.icon }}</div>
                        <div class="achievement-content">
                          <h4>{{ achievement.name }}</h4>
                          <p>{{ achievement.description }}</p>
                          <div class="achievement-progress">
                            <div class="progress-text">
                              {{ achievement.unlocked ? '已解锁' : `${achievement.progress}%` }}
                            </div>
                            <div class="progress-bar">
                              <div 
                                class="progress-fill" 
                                :style="{ width: achievement.unlocked ? '100%' : achievement.progress + '%' }"
                              ></div>
                            </div>
                          </div>
                        </div>
                        <div 
                          v-if="achievement.rare" 
                          class="achievement-badge rare"
                        >
                          稀有
                        </div>
                      </div>
                    </div>
                  </div>
                </template>
              </TechCard>
            </div>

            <!-- 设置标签页 -->
            <div v-if="activeTab === 'settings'" class="tab-panel">
              <TechCard>
                <template #title>系统设置</template>
                <template #content>
                  <div class="settings-section">
                    <div class="setting-group">
                      <h4>界面设置</h4>
                      <div class="setting-item">
                        <label class="setting-label">
                          <span>主题模式</span>
                          <TechSelect 
                            v-model="settings.theme" 
                            :options="themeOptions"
                            @change="updateTheme"
                          />
                        </label>
                      </div>
                      
                      <div class="setting-item">
                        <label class="setting-label">
                          <span>语言</span>
                          <TechSelect 
                            v-model="settings.language" 
                            :options="languageOptions"
                          />
                        </label>
                      </div>
                      
                      <div class="setting-item">
                        <label class="toggle-label">
                          <input type="checkbox" v-model="settings.autoSave" />
                          <span>自动保存进度</span>
                        </label>
                      </div>
                      
                      <div class="setting-item">
                        <label class="toggle-label">
                          <input type="checkbox" v-model="settings.showAnimations" />
                          <span>显示动画效果</span>
                        </label>
                      </div>
                    </div>

                    <div class="setting-group">
                      <h4>通知设置</h4>
                      <div class="setting-item">
                        <label class="toggle-label">
                          <input type="checkbox" v-model="settings.emailNotifications" />
                          <span>邮件通知</span>
                        </label>
                      </div>
                      
                      <div class="setting-item">
                        <label class="toggle-label">
                          <input type="checkbox" v-model="settings.pushNotifications" />
                          <span>推送通知</span>
                        </label>
                      </div>
                      
                      <div class="setting-item">
                        <label class="toggle-label">
                          <input type="checkbox" v-model="settings.learningReminders" />
                          <span>学习提醒</span>
                        </label>
                      </div>
                    </div>

                    <div class="setting-group">
                      <h4>隐私设置</h4>
                      <div class="setting-item">
                        <label class="toggle-label">
                          <input type="checkbox" v-model="settings.publicProfile" />
                          <span>公开个人资料</span>
                        </label>
                      </div>
                      
                      <div class="setting-item">
                        <label class="toggle-label">
                          <input type="checkbox" v-model="settings.showActivity" />
                          <span>显示学习活动</span>
                        </label>
                      </div>
                    </div>

                    <div class="setting-actions">
                      <TechButton type="primary" @click="saveSettings">保存设置</TechButton>
                      <TechButton type="secondary" @click="resetSettings">恢复默认</TechButton>
                    </div>
                  </div>
                </template>
              </TechCard>
            </div>
          </div>
        </div>
      </div>
    </div>
  </TechLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useAppStore } from '@/store/app'
import TechLayout from '@/components/layout/TechLayout.vue'
import TechCard from '@/components/base/TechCard.vue'
import TechButton from '@/components/base/TechButton.vue'
import TechInput from '@/components/base/TechInput.vue'
import TechSelect from '@/components/base/TechSelect.vue'

const appStore = useAppStore()

// 标签页配置
const tabs = [
  { id: 'basic', label: '基本信息', icon: '👤' },
  { id: 'progress', label: '学习进度', icon: '📊' },
  { id: 'achievements', label: '成就', icon: '🏆' },
  { id: 'settings', label: '设置', icon: '⚙️' }
]

const activeTab = ref('basic')

// 用户信息
const userInfo = ref({
  name: '算法学习者',
  role: '高级用户',
  email: 'user@example.com',
  level: 5,
  levelProgress: 75
})

const userStats = ref({
  learnedAlgorithms: 24,
  practiceCount: 156,
  achievementCount: 8,
  learningTime: 3560, // 分钟
  masteryRate: 68,
  accuracyRate: 92
})

// 表单数据
const profileForm = ref({
  name: '算法学习者',
  email: 'user@example.com',
  phone: '+86 138****5678',
  position: '软件工程师',
  bio: '热爱算法学习，专注于数据结构与算法研究。希望通过不断练习提升编程能力。'
})

// 学习进度
const learningProgress = ref({
  totalAlgorithms: 35,
  learnedAlgorithms: 24,
  completionRate: 68,
  currentStreak: 7,
  categories: [
    { name: '排序算法', learned: 6, total: 8, progress: 75 },
    { name: '搜索算法', learned: 4, total: 5, progress: 80 },
    { name: '图算法', learned: 3, total: 7, progress: 43 },
    { name: '动态规划', learned: 5, total: 8, progress: 62 },
    { name: '贪心算法', learned: 3, total: 4, progress: 75 },
    { name: '回溯算法', learned: 3, total: 3, progress: 100 }
  ]
})

const learningHistory = ref([
  { id: 1, icon: '🔍', title: '二分查找算法练习', time: new Date(Date.now() - 2 * 60 * 60 * 1000), duration: 45 },
  { id: 2, icon: '📊', title: '快速排序可视化', time: new Date(Date.now() - 24 * 60 * 60 * 1000), duration: 30 },
  { id: 3, icon: '🌐', title: '图遍历算法学习', time: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000), duration: 60 },
  { id: 4, icon: '⚡', title: '动态规划练习', time: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000), duration: 75 }
])

// 成就系统
const achievements = ref({
  unlocked: 8,
  total: 15,
  rareCount: 2,
  points: 350,
  list: [
    { id: 1, name: '算法入门', description: '完成第一个算法练习', icon: '🎯', unlocked: true, progress: 100, rare: false },
    { id: 2, name: '排序大师', description: '掌握所有排序算法', icon: '📊', unlocked: true, progress: 100, rare: false },
    { id: 3, name: '搜索专家', description: '完成所有搜索算法练习', icon: '🔍', unlocked: true, progress: 100, rare: false },
    { id: 4, name: '图论探索者', description: '学习5个图算法', icon: '🌐', unlocked: true, progress: 100, rare: false },
    { id: 5, name: '连续学习', description: '连续学习7天', icon: '🔥', unlocked: true, progress: 100, rare: false },
    { id: 6, name: '速度之王', description: '算法练习正确率95%以上', icon: '⚡', unlocked: true, progress: 100, rare: true },
    { id: 7, name: '算法大师', description: '掌握所有核心算法', icon: '👑', unlocked: false, progress: 68, rare: true },
    { id: 8, name: '百题斩', description: '完成100道算法题', icon: '💯', unlocked: false, progress: 24, rare: false }
  ]
})

// 设置选项
const settings = ref({
  theme: 'dark',
  language: 'zh-CN',
  autoSave: true,
  showAnimations: true,
  emailNotifications: true,
  pushNotifications: false,
  learningReminders: true,
  publicProfile: true,
  showActivity: true
})

const themeOptions = [
  { label: '深色主题', value: 'dark' },
  { label: '浅色主题', value: 'light' },
  { label: '自动', value: 'auto' }
]

const languageOptions = [
  { label: '中文', value: 'zh-CN' },
  { label: 'English', value: 'en-US' }
]

// 计算属性
const userInitials = computed(() => {
  return userInfo.value.name.split('').slice(0, 2).join('').toUpperCase()
})

// 方法
const editProfile = () => {
  activeTab.value = 'basic'
}

const changePassword = () => {
  // 实现修改密码逻辑
  console.log('修改密码')
}

const saveProfile = () => {
  // 保存个人信息
  userInfo.value.name = profileForm.value.name
  userInfo.value.email = profileForm.value.email
  console.log('保存个人信息:', profileForm.value)
}

const resetProfile = () => {
  // 重置表单
  profileForm.value = {
    name: '算法学习者',
    email: 'user@example.com',
    phone: '+86 138****5678',
    position: '软件工程师',
    bio: '热爱算法学习，专注于数据结构与算法研究。希望通过不断练习提升编程能力。'
  }
}

const updateTheme = (theme: string) => {
  appStore.setTheme(theme)
}

const saveSettings = () => {
  // 保存设置
  console.log('保存设置:', settings.value)
}

const resetSettings = () => {
  // 恢复默认设置
  settings.value = {
    theme: 'dark',
    language: 'zh-CN',
    autoSave: true,
    showAnimations: true,
    emailNotifications: true,
    pushNotifications: false,
    learningReminders: true,
    publicProfile: true,
    showActivity: true
  }
}

const formatTime = (minutes: number) => {
  const hours = Math.floor(minutes / 60)
  const mins = minutes % 60
  return hours > 0 ? `${hours}小时${mins}分钟` : `${mins}分钟`
}

const formatRelativeTime = (date: Date) => {
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  
  const minutes = Math.floor(diff / (1000 * 60))
  const hours = Math.floor(diff / (1000 * 60 * 60))
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))
  
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  return `${days}天前`
}

const formatDuration = (minutes: number) => {
  return minutes >= 60 ? `${Math.floor(minutes / 60)}小时${minutes % 60}分钟` : `${minutes}分钟`
}

// 生命周期
onMounted(() => {
  // 初始化用户数据
  console.log('个人中心页面加载完成')
})
</script>

<style lang="scss" scoped>
@import '@/styles/tech-theme.scss';

.profile-page {
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

.profile-content {
  display: grid;
  grid-template-columns: 350px 1fr;
  gap: 2rem;
}

.profile-sidebar {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.user-card {
  .user-avatar-section {
    text-align: center;
    margin-bottom: 1.5rem;
  }
  
  .avatar-container {
    position: relative;
    display: inline-block;
    margin-bottom: 1rem;
  }
  
  .user-avatar {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    background: linear-gradient(135deg, var(--primary) 0%, var(--accent) 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 2rem;
    font-weight: 700;
    color: white;
    margin: 0 auto;
  }
  
  .avatar-status {
    position: absolute;
    bottom: 0;
    right: 0;
    
    .status-indicator {
      width: 16px;
      height: 16px;
      border-radius: 50%;
      border: 2px solid white;
      
      &.online {
        background: var(--success);
      }
    }
  }
  
  .user-info {
    text-align: center;
    
    .user-name {
      font-size: 1.5rem;
      font-weight: 700;
      color: var(--text-primary);
      margin: 0 0 0.25rem 0;
    }
    
    .user-role {
      color: var(--accent);
      font-weight: 600;
      margin: 0 0 0.5rem 0;
    }
    
    .user-email {
      color: var(--text-secondary);
      font-size: 0.9rem;
      margin: 0 0 1rem 0;
    }
    
    .user-level {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      margin-bottom: 1.5rem;
      
      .level-text {
        font-weight: 600;
        color: var(--text-primary);
      }
      
      .level-progress {
        flex: 1;
        height: 6px;
        background: var(--bg-tertiary);
        border-radius: 3px;
        overflow: hidden;
      }
      
      .level-progress-fill {
        height: 100%;
        background: linear-gradient(90deg, var(--primary) 0%, var(--accent) 100%);
        border-radius: 3px;
        transition: width 0.3s ease;
      }
      
      .level-percent {
        font-size: 0.8rem;
        color: var(--text-secondary);
      }
    }
  }
  
  .user-stats {
    display: flex;
    justify-content: space-around;
    margin-bottom: 1.5rem;
  }
  
  .stat-item {
    text-align: center;
    
    .stat-icon {
      font-size: 1.5rem;
      margin-bottom: 0.5rem;
    }
    
    .stat-value {
      font-size: 1.25rem;
      font-weight: 700;
      color: var(--text-primary);
    }
    
    .stat-label {
      font-size: 0.8rem;
      color: var(--text-secondary);
    }
  }
  
  .user-actions {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
}

.quick-stats-card {
  .stats-chart {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }
  
  .chart-item {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .chart-label {
    font-size: 0.9rem;
    color: var(--text-secondary);
  }
  
  .chart-value {
    font-weight: 600;
    color: var(--text-primary);
  }
  
  .chart-bar {
    height: 4px;
    background: var(--bg-tertiary);
    border-radius: 2px;
    overflow: hidden;
  }
  
  .chart-fill {
    height: 100%;
    background: linear-gradient(90deg, var(--primary) 0%, var(--accent) 100%);
    border-radius: 2px;
    transition: width 0.3s ease;
  }
}

.profile-main {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.tab-navigation {
  display: flex;
  background: var(--bg-card);
  border-radius: 12px;
  padding: 0.5rem;
  border: 1px solid var(--border-color);
}

.tab-button {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 0.75rem 1rem;
  border: none;
  background: transparent;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: var(--text-secondary);
  
  &:hover {
    background: var(--bg-tertiary);
  }
  
  &.active {
    background: var(--primary);
    color: white;
    
    .tab-icon {
      color: white;
    }
  }
  
  .tab-icon {
    font-size: 1.2rem;
  }
  
  .tab-label {
    font-weight: 600;
  }
}

.tab-content {
  flex: 1;
}

.tab-panel {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-section {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  
  label {
    font-weight: 600;
    color: var(--text-primary);
    font-size: 0.9rem;
  }
}

.bio-textarea {
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  background: var(--bg-card);
  color: var(--text-primary);
  resize: vertical;
  font-family: inherit;
  
  &:focus {
    outline: none;
    border-color: var(--primary);
  }
}

.form-actions {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
}

.progress-section {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1rem;
  margin-bottom: 2rem;
}

.overview-item {
  text-align: center;
  padding: 1rem;
  background: var(--bg-tertiary);
  border-radius: 8px;
  
  .overview-value {
    font-size: 1.5rem;
    font-weight: 700;
    color: var(--text-primary);
    margin-bottom: 0.25rem;
  }
  
  .overview-label {
    font-size: 0.9rem;
    color: var(--text-secondary);
  }
}

.algorithm-progress {
  h4 {
    margin-bottom: 1rem;
    color: var(--text-primary);
  }
}

.category-progress {
  margin-bottom: 1.5rem;
  
  .category-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 0.5rem;
  }
  
  .category-name {
    font-weight: 600;
    color: var(--text-primary);
  }
  
  .category-percent {
    color: var(--accent);
    font-weight: 600;
  }
  
  .progress-bar {
    height: 6px;
    background: var(--bg-tertiary);
    border-radius: 3px;
    overflow: hidden;
    margin-bottom: 0.25rem;
  }
  
  .progress-fill {
    height: 100%;
    background: linear-gradient(90deg, var(--primary) 0%, var(--accent) 100%);
    border-radius: 3px;
    transition: width 0.3s ease;
  }
  
  .category-stats {
    font-size: 0.8rem;
    color: var(--text-secondary);
  }
}

.learning-history {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.history-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: var(--bg-tertiary);
  border-radius: 8px;
  transition: all 0.3s ease;
  
  &:hover {
    background: var(--bg-secondary);
    transform: translateX(4px);
  }
  
  .history-icon {
    font-size: 1.5rem;
    width: 40px;
    text-align: center;
  }
  
  .history-content {
    flex: 1;
    
    .history-title {
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: 0.25rem;
    }
    
    .history-time {
      font-size: 0.8rem;
      color: var(--text-secondary);
    }
  }
  
  .history-duration {
    font-weight: 600;
    color: var(--accent);
  }
}

.achievements-section {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.achievements-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
  margin-bottom: 1rem;
}

.achievement-stat {
  text-align: center;
  padding: 1rem;
  background: var(--bg-tertiary);
  border-radius: 8px;
  
  .stat-value {
    font-size: 1.5rem;
    font-weight: 700;
    color: var(--text-primary);
    margin-bottom: 0.25rem;
  }
  
  .stat-label {
    font-size: 0.9rem;
    color: var(--text-secondary);
  }
}

.achievements-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1rem;
}

.achievement-card {
  position: relative;
  padding: 1.5rem;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  transition: all 0.3s ease;
  
  &.locked {
    opacity: 0.6;
    
    .achievement-icon {
      filter: grayscale(1);
    }
  }
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
  }
  
  .achievement-icon {
    font-size: 2rem;
    margin-bottom: 1rem;
    text-align: center;
  }
  
  .achievement-content {
    h4 {
      margin: 0 0 0.5rem 0;
      color: var(--text-primary);
    }
    
    p {
      margin: 0 0 1rem 0;
      color: var(--text-secondary);
      font-size: 0.9rem;
      line-height: 1.4;
    }
  }
  
  .achievement-progress {
    .progress-text {
      font-size: 0.8rem;
      color: var(--text-secondary);
      margin-bottom: 0.25rem;
    }
    
    .progress-bar {
      height: 4px;
      background: var(--bg-tertiary);
      border-radius: 2px;
      overflow: hidden;
    }
    
    .progress-fill {
      height: 100%;
      background: linear-gradient(90deg, var(--primary) 0%, var(--accent) 100%);
      border-radius: 2px;
      transition: width 0.3s ease;
    }
  }
  
  .achievement-badge {
    position: absolute;
    top: 1rem;
    right: 1rem;
    padding: 0.25rem 0.5rem;
    font-size: 0.7rem;
    font-weight: 600;
    border-radius: 12px;
    
    &.rare {
      background: var(--warning);
      color: white;
    }
  }
}

.settings-section {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.setting-group {
  h4 {
    margin-bottom: 1rem;
    color: var(--text-primary);
    border-bottom: 1px solid var(--border-color);
    padding-bottom: 0.5rem;
  }
}

.setting-item {
  margin-bottom: 1rem;
  
  .setting-label {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0.75rem 0;
    border-bottom: 1px solid var(--border-color);
    
    span:first-child {
      font-weight: 600;
      color: var(--text-primary);
    }
  }
  
  .toggle-label {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    cursor: pointer;
    padding: 0.75rem 0;
    border-bottom: 1px solid var(--border-color);
    
    input {
      margin: 0;
    }
    
    span {
      font-weight: 600;
      color: var(--text-primary);
    }
  }
}

.setting-actions {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
  margin-top: 1rem;
}

// 响应式设计
@media (max-width: 1024px) {
  .profile-content {
    grid-template-columns: 1fr;
  }
  
  .profile-sidebar {
    order: 2;
  }
  
  .profile-main {
    order: 1;
  }
}

@media (max-width: 768px) {
  .profile-page {
    padding: 1rem;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .progress-overview {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .achievements-stats {
    grid-template-columns: 1fr;
  }
  
  .achievements-grid {
    grid-template-columns: 1fr;
  }
  
  .tab-navigation {
    flex-direction: column;
  }
  
  .tab-button {
    justify-content: flex-start;
  }
}

@media (max-width: 480px) {
  .progress-overview {
    grid-template-columns: 1fr;
  }
  
  .user-stats {
    flex-direction: column;
    gap: 1rem;
  }
  
  .achievement-card {
    padding: 1rem;
  }
}
</style>