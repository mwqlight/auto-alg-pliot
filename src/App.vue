<template>
  <TechLayout
    :title="appTitle"
    :theme="theme"
    :nav-items="navItems"
    :sidebar-items="sidebarItems"
    :breadcrumbs="breadcrumbs"
    :active-nav="activeNav"
    :active-sidebar="activeSidebar"
    :loading="globalLoading"
    @nav-click="handleNavClick"
    @sidebar-click="handleSidebarClick"
    @theme-change="handleThemeChange"
  >
    <!-- 面包屑插槽 -->
    <template #breadcrumb>
      <nav class="app-breadcrumb">
        <ol class="app-breadcrumb-list">
          <li
            v-for="(crumb, index) in breadcrumbs"
            :key="crumb.key"
            :class="[
              'app-breadcrumb-item',
              { 'app-breadcrumb-item--last': index === breadcrumbs.length - 1 }
            ]"
          >
            <span 
              v-if="index < breadcrumbs.length - 1"
              class="app-breadcrumb-link"
              @click="handleBreadcrumbClick(crumb)"
            >
              {{ crumb.label }}
            </span>
            <span v-else class="app-breadcrumb-current">
              {{ crumb.label }}
            </span>
            
            <span 
              v-if="index < breadcrumbs.length - 1"
              class="app-breadcrumb-separator"
            >
              ›
            </span>
          </li>
        </ol>
      </nav>
    </template>

    <!-- 主内容区域 -->
    <div class="app-main">
      <router-view v-slot="{ Component }">
        <transition
          name="page-fade"
          mode="out-in"
        >
          <component :is="Component" />
        </transition>
      </router-view>
    </div>

    <!-- 用户菜单插槽 -->
    <template #user-menu>
      <div class="app-user-menu">
        <div class="app-user-info">
          <div class="app-user-avatar">
            <span class="app-user-initials">A</span>
          </div>
          <div class="app-user-details">
            <div class="app-user-name">算法管理员</div>
            <div class="app-user-role">高级用户</div>
          </div>
        </div>
        
        <div class="app-user-actions">
          <button class="app-user-action" @click="handleUserAction('profile')">
            <span class="app-user-action-icon">👤</span>
            <span class="app-user-action-text">个人资料</span>
          </button>
          
          <button class="app-user-action" @click="handleUserAction('settings')">
            <span class="app-user-action-icon">⚙️</span>
            <span class="app-user-action-text">系统设置</span>
          </button>
          
          <div class="app-user-divider"></div>
          
          <button class="app-user-action app-user-action--logout" @click="handleUserAction('logout')">
            <span class="app-user-action-icon">🚪</span>
            <span class="app-user-action-text">退出登录</span>
          </button>
        </div>
      </div>
    </template>
  </TechLayout>

  <!-- 全局通知 -->
  <div class="app-notifications">
    <transition-group name="notification-fade">
      <div
        v-for="notification in notifications"
        :key="notification.id"
        :class="[
          'app-notification',
          `app-notification--${notification.type}`
        ]"
      >
        <div class="app-notification-content">
          <span class="app-notification-icon">{{ getNotificationIcon(notification.type) }}</span>
          <div class="app-notification-message">
            <div class="app-notification-title">{{ notification.title }}</div>
            <div class="app-notification-description">{{ notification.message }}</div>
          </div>
          <button class="app-notification-close" @click="removeNotification(notification.id)">
            <span class="app-notification-close-icon">×</span>
          </button>
        </div>
        <div class="app-notification-progress"></div>
      </div>
    </transition-group>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import TechLayout from '@/components/layout/TechLayout.vue'

interface NavItem {
  key: string
  label: string
  icon: string
  path: string
  disabled?: boolean
}

interface SidebarItem extends NavItem {
  badge?: string | number
}

interface BreadcrumbItem {
  key: string
  label: string
  path?: string
}

interface Notification {
  id: string
  type: 'success' | 'error' | 'warning' | 'info'
  title: string
  message: string
  duration?: number
}

// 路由和状态
const route = useRoute()
const router = useRouter()

// 应用状态
const appTitle = ref('算法驾驶舱')
const theme = ref<'light' | 'dark'>('dark')
const globalLoading = ref(false)
const activeNav = ref('dashboard')
const activeSidebar = ref('')
const notifications = ref<Notification[]>([])

// 导航配置
const navItems = ref<NavItem[]>([
  {
    key: 'dashboard',
    label: '控制台',
    icon: '📊',
    path: '/dashboard'
  },
  {
    key: 'algorithms',
    label: '算法库',
    icon: '🧮',
    path: '/algorithms'
  },
  {
    key: 'visualization',
    label: '可视化',
    icon: '🎯',
    path: '/visualization'
  },
  {
    key: 'execution',
    label: '代码执行',
    icon: '⚡',
    path: '/execution'
  },
  {
    key: 'analysis',
    label: '复杂度分析',
    icon: '📈',
    path: '/analysis'
  }
])

// 侧边栏配置
const sidebarItems = computed<SidebarItem[]>(() => {
  const items: SidebarItem[] = []
  
  switch (activeNav.value) {
    case 'dashboard':
      items.push(
        { key: 'overview', label: '概览', icon: '👁️', path: '/dashboard/overview' },
        { key: 'statistics', label: '统计', icon: '📊', path: '/dashboard/statistics' },
        { key: 'recent', label: '最近使用', icon: '🕒', path: '/dashboard/recent' }
      )
      break
    case 'algorithms':
      items.push(
        { key: 'all', label: '全部算法', icon: '📚', path: '/algorithms/all' },
        { key: 'sorting', label: '排序算法', icon: '🔢', path: '/algorithms/sorting' },
        { key: 'searching', label: '搜索算法', icon: '🔍', path: '/algorithms/searching' },
        { key: 'graph', label: '图算法', icon: '🕸️', path: '/algorithms/graph' },
        { key: 'dynamic', label: '动态规划', icon: '🧩', path: '/algorithms/dynamic' },
        { key: 'favorites', label: '我的收藏', icon: '⭐', path: '/algorithms/favorites', badge: 3 }
      )
      break
    case 'visualization':
      items.push(
        { key: 'sorting-viz', label: '排序可视化', icon: '🔄', path: '/visualization/sorting' },
        { key: 'search-viz', label: '搜索可视化', icon: '🎯', path: '/visualization/searching' },
        { key: 'graph-viz', label: '图算法可视化', icon: '🕸️', path: '/visualization/graph' },
        { key: 'custom-viz', label: '自定义可视化', icon: '🎨', path: '/visualization/custom' }
      )
      break
    case 'execution':
      items.push(
        { key: 'editor', label: '代码编辑器', icon: '📝', path: '/execution/editor' },
        { key: 'test-cases', label: '测试用例', icon: '🧪', path: '/execution/test-cases' },
        { key: 'results', label: '执行结果', icon: '📋', path: '/execution/results' },
        { key: 'history', label: '执行历史', icon: '📜', path: '/execution/history' }
      )
      break
    case 'analysis':
      items.push(
        { key: 'time-complexity', label: '时间复杂度', icon: '⏱️', path: '/analysis/time' },
        { key: 'space-complexity', label: '空间复杂度', icon: '💾', path: '/analysis/space' },
        { key: 'comparison', label: '算法对比', icon: '⚖️', path: '/analysis/comparison' },
        { key: 'optimization', label: '优化建议', icon: '🚀', path: '/analysis/optimization' }
      )
      break
  }
  
  return items
})

// 面包屑配置
const breadcrumbs = computed<BreadcrumbItem[]>(() => {
  const crumbs: BreadcrumbItem[] = [
    { key: 'home', label: '首页', path: '/dashboard' }
  ]
  
  // 根据当前路由动态生成面包屑
  const pathSegments = route.path.split('/').filter(segment => segment)
  
  pathSegments.forEach((segment, index) => {
    const path = '/' + pathSegments.slice(0, index + 1).join('/')
    const label = getBreadcrumbLabel(segment, index)
    
    if (label) {
      crumbs.push({
        key: segment,
        label,
        path: index < pathSegments.length - 1 ? path : undefined
      })
    }
  })
  
  return crumbs
})

// 方法
const getBreadcrumbLabel = (segment: string, index: number): string => {
  const labelMap: Record<string, string> = {
    'dashboard': '控制台',
    'algorithms': '算法库',
    'visualization': '可视化',
    'execution': '代码执行',
    'analysis': '复杂度分析',
    'overview': '概览',
    'statistics': '统计',
    'recent': '最近使用',
    'all': '全部算法',
    'sorting': '排序算法',
    'searching': '搜索算法',
    'graph': '图算法',
    'dynamic': '动态规划',
    'favorites': '我的收藏',
    'editor': '代码编辑器',
    'test-cases': '测试用例',
    'results': '执行结果',
    'history': '执行历史',
    'time': '时间复杂度',
    'space': '空间复杂度',
    'comparison': '算法对比',
    'optimization': '优化建议'
  }
  
  return labelMap[segment] || segment
}

const handleNavClick = (item: NavItem) => {
  activeNav.value = item.key
  activeSidebar.value = ''
  router.push(item.path)
}

const handleSidebarClick = (item: SidebarItem) => {
  activeSidebar.value = item.key
  router.push(item.path)
}

const handleBreadcrumbClick = (item: BreadcrumbItem) => {
  if (item.path) {
    router.push(item.path)
  }
}

const handleThemeChange = (newTheme: 'light' | 'dark') => {
  theme.value = newTheme
  // 保存主题偏好到本地存储
  localStorage.setItem('app-theme', newTheme)
}

const handleUserAction = (action: string) => {
  switch (action) {
    case 'profile':
      router.push('/profile')
      break
    case 'settings':
      router.push('/settings')
      break
    case 'logout':
      handleLogout()
      break
  }
}

const handleLogout = () => {
  globalLoading.value = true
  
  // 模拟登出过程
  setTimeout(() => {
    // 清除认证信息
    localStorage.removeItem('auth-token')
    localStorage.removeItem('user-info')
    
    globalLoading.value = false
    showNotification({
      type: 'success',
      title: '退出成功',
      message: '您已安全退出系统'
    })
    
    // 跳转到登录页
    router.push('/login')
  }, 1000)
}

const showNotification = (notification: Omit<Notification, 'id'>) => {
  const id = Date.now().toString()
  const newNotification: Notification = {
    id,
    duration: 5000,
    ...notification
  }
  
  notifications.value.push(newNotification)
  
  // 自动移除通知
  setTimeout(() => {
    removeNotification(id)
  }, newNotification.duration)
}

const removeNotification = (id: string) => {
  const index = notifications.value.findIndex(n => n.id === id)
  if (index > -1) {
    notifications.value.splice(index, 1)
  }
}

const getNotificationIcon = (type: string): string => {
  const icons: Record<string, string> = {
    success: '✅',
    error: '❌',
    warning: '⚠️',
    info: 'ℹ️'
  }
  return icons[type] || 'ℹ️'
}

// 认证检查
const checkAuthStatus = () => {
  const token = localStorage.getItem('auth-token')
  const isAuthenticated = !!token
  
  // 如果未认证且当前不在登录页，跳转到登录页
  if (!isAuthenticated && route.path !== '/login') {
    router.push('/login')
    return false
  }
  
  // 如果已认证且在登录页，跳转到首页
  if (isAuthenticated && route.path === '/login') {
    router.push('/dashboard')
    return false
  }
  
  return isAuthenticated
}

// 初始化
onMounted(() => {
  // 检查认证状态
  checkAuthStatus()
  
  // 从本地存储加载主题偏好
  const savedTheme = localStorage.getItem('app-theme') as 'light' | 'dark'
  if (savedTheme) {
    theme.value = savedTheme
  }
  
  // 根据当前路由设置激活状态
  const currentPath = route.path
  const navItem = navItems.value.find(item => currentPath.startsWith(item.path))
  if (navItem) {
    activeNav.value = navItem.key
  }
})
</script>

<style scoped lang="scss">
@import '@/styles/tech-theme.scss';

.app-main {
  height: 100%;
  overflow: auto;
}

.app-breadcrumb {
  padding: 16px 24px;
}

.app-breadcrumb-list {
  display: flex;
  align-items: center;
  gap: 8px;
  list-style: none;
  margin: 0;
  padding: 0;
  font-size: 0.875rem;
}

.app-breadcrumb-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.app-breadcrumb-link {
  color: $tech-text-secondary;
  cursor: pointer;
  transition: color $tech-transition-fast;
  
  &:hover {
    color: $tech-primary;
  }
}

.app-breadcrumb-current {
  color: $tech-text-primary;
  font-weight: 500;
}

.app-breadcrumb-separator {
  color: $tech-text-tertiary;
}

.app-user-menu {
  min-width: 200px;
}

.app-user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  border-bottom: $tech-border-light;
}

.app-user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: $tech-gradient-primary;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 600;
  font-size: 1rem;
}

.app-user-details {
  flex: 1;
}

.app-user-name {
  font-weight: 600;
  font-size: 0.875rem;
  color: $tech-text-primary;
}

.app-user-role {
  font-size: 0.75rem;
  color: $tech-text-tertiary;
}

.app-user-actions {
  padding: 8px 0;
}

.app-user-action {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 100%;
  background: none;
  border: none;
  padding: 12px 16px;
  cursor: pointer;
  transition: background $tech-transition-fast;
  color: $tech-text-secondary;
  
  &:hover {
    background: rgba($tech-primary, 0.1);
    color: $tech-text-primary;
  }
}

.app-user-action--logout {
  color: $tech-error;
  
  &:hover {
    background: rgba($tech-error, 0.1);
    color: $tech-error;
  }
}

.app-user-action-icon {
  font-size: 1.1rem;
}

.app-user-action-text {
  font-size: 0.875rem;
  font-weight: 500;
}

.app-user-divider {
  height: 1px;
  background: $tech-border-color;
  margin: 8px 0;
}

.app-notifications {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 10000;
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-width: 400px;
}

.app-notification {
  background: $tech-bg-darker;
  border: $tech-border-light;
  border-radius: 8px;
  box-shadow: $tech-shadow-strong;
  overflow: hidden;
  backdrop-filter: blur(10px);
}

.app-notification--success {
  border-left: 3px solid $tech-success;
}

.app-notification--error {
  border-left: 3px solid $tech-error;
}

.app-notification--warning {
  border-left: 3px solid $tech-warning;
}

.app-notification--info {
  border-left: 3px solid $tech-info;
}

.app-notification-content {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px;
}

.app-notification-icon {
  font-size: 1.2rem;
  flex-shrink: 0;
}

.app-notification-message {
  flex: 1;
}

.app-notification-title {
  font-weight: 600;
  font-size: 0.875rem;
  color: $tech-text-primary;
  margin-bottom: 4px;
}

.app-notification-description {
  font-size: 0.75rem;
  color: $tech-text-secondary;
  line-height: 1.4;
}

.app-notification-close {
  background: none;
  border: none;
  color: $tech-text-tertiary;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  transition: all $tech-transition-fast;
  
  &:hover {
    color: $tech-text-primary;
    background: rgba($tech-primary, 0.1);
  }
}

.app-notification-close-icon {
  font-size: 1.2rem;
  font-weight: bold;
  line-height: 1;
}

.app-notification-progress {
  height: 2px;
  background: rgba($tech-primary, 0.3);
  animation: notification-progress 5s linear forwards;
}

.app-notification--success .app-notification-progress {
  background: rgba($tech-success, 0.3);
}

.app-notification--error .app-notification-progress {
  background: rgba($tech-error, 0.3);
}

.app-notification--warning .app-notification-progress {
  background: rgba($tech-warning, 0.3);
}

.app-notification--info .app-notification-progress {
  background: rgba($tech-info, 0.3);
}

// 页面过渡动画
.page-fade-enter-active,
.page-fade-leave-active {
  transition: all $tech-transition-medium;
}

.page-fade-enter-from {
  opacity: 0;
  transform: translateX(20px);
}

.page-fade-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}

// 通知动画
.notification-fade-enter-active,
.notification-fade-leave-active {
  transition: all $tech-transition-fast;
}

.notification-fade-enter-from {
  opacity: 0;
  transform: translateX(100%);
}

.notification-fade-leave-to {
  opacity: 0;
  transform: translateX(100%);
}

@keyframes notification-progress {
  from { width: 100%; }
  to { width: 0%; }
}

// 响应式设计
@media (max-width: 768px) {
  .app-breadcrumb {
    padding: 12px 16px;
  }
  
  .app-notifications {
    top: 16px;
    right: 16px;
    left: 16px;
    max-width: none;
  }
}
</style>