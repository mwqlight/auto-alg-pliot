<template>
  <div :class="['tech-layout', `tech-layout--${theme}"]">
    <!-- 头部导航 -->
    <header v-if="showHeader" class="tech-layout__header">
      <div class="tech-layout__header-container">
        <!-- Logo区域 -->
        <div class="tech-layout__logo">
          <slot name="logo">
            <div class="tech-layout__logo-content">
              <span class="tech-layout__logo-icon">⚡</span>
              <span class="tech-layout__logo-text">{{ title }}</span>
            </div>
          </slot>
        </div>

        <!-- 导航菜单 -->
        <nav class="tech-layout__nav">
          <slot name="nav">
            <ul class="tech-layout__nav-menu">
              <li
                v-for="item in navItems"
                :key="item.key"
                :class="[
                  'tech-layout__nav-item',
                  { 'tech-layout__nav-item--active': activeNav === item.key }
                ]"
                @click="handleNavClick(item)"
              >
                <span class="tech-layout__nav-icon">{{ item.icon }}</span>
                <span class="tech-layout__nav-text">{{ item.label }}</span>
                <div class="tech-layout__nav-indicator"></div>
              </li>
            </ul>
          </slot>
        </nav>

        <!-- 用户操作区域 -->
        <div class="tech-layout__actions">
          <slot name="actions">
            <!-- 主题切换 -->
            <button
              class="tech-layout__theme-toggle"
              @click="toggleTheme"
              :title="theme === 'dark' ? '切换至浅色模式' : '切换至深色模式'"
            >
              <span class="tech-layout__theme-icon">
                {{ theme === 'dark' ? '☀️' : '🌙' }}
              </span>
            </button>

            <!-- 用户菜单 -->
            <div class="tech-layout__user-menu">
              <slot name="user-menu">
                <div class="tech-layout__user-avatar">
                  <span class="tech-layout__user-initials">U</span>
                </div>
              </slot>
            </div>
          </slot>
        </div>

        <!-- 移动端菜单按钮 -->
        <button
          v-if="showMobileMenu"
          class="tech-layout__mobile-toggle"
          @click="toggleMobileMenu"
        >
          <span class="tech-layout__mobile-icon">☰</span>
        </button>
      </div>

      <!-- 移动端菜单 -->
      <transition name="tech-layout-mobile">
        <div v-show="isMobileMenuOpen" class="tech-layout__mobile-menu">
          <div class="tech-layout__mobile-container">
            <slot name="mobile-nav">
              <ul class="tech-layout__mobile-nav">
                <li
                  v-for="item in navItems"
                  :key="item.key"
                  :class="[
                    'tech-layout__mobile-item',
                    { 'tech-layout__mobile-item--active': activeNav === item.key }
                  ]"
                  @click="handleMobileNavClick(item)"
                >
                  <span class="tech-layout__mobile-icon">{{ item.icon }}</span>
                  <span class="tech-layout__mobile-text">{{ item.label }}</span>
                </li>
              </ul>
            </slot>
          </div>
        </div>
      </transition>
    </header>

    <!-- 主体内容 -->
    <main class="tech-layout__main">
      <!-- 侧边栏 -->
      <aside v-if="showSidebar" class="tech-layout__sidebar">
        <slot name="sidebar">
          <div class="tech-layout__sidebar-content">
            <!-- 侧边栏标题 -->
            <div v-if="sidebarTitle" class="tech-layout__sidebar-title">
              {{ sidebarTitle }}
            </div>

            <!-- 侧边栏菜单 -->
            <nav class="tech-layout__sidebar-nav">
              <ul class="tech-layout__sidebar-menu">
                <li
                  v-for="item in sidebarItems"
                  :key="item.key"
                  :class="[
                    'tech-layout__sidebar-item',
                    { 
                      'tech-layout__sidebar-item--active': activeSidebar === item.key,
                      'tech-layout__sidebar-item--collapsed': sidebarCollapsed
                    }
                  ]"
                  @click="handleSidebarClick(item)"
                >
                  <span class="tech-layout__sidebar-icon">{{ item.icon }}</span>
                  <span class="tech-layout__sidebar-text">{{ item.label }}</span>
                  <span 
                    v-if="item.badge" 
                    class="tech-layout__sidebar-badge"
                  >
                    {{ item.badge }}
                  </span>
                </li>
              </ul>
            </nav>

            <!-- 侧边栏折叠按钮 -->
            <button
              v-if="sidebarCollapsible"
              class="tech-layout__sidebar-toggle"
              @click="toggleSidebar"
            >
              <span class="tech-layout__sidebar-toggle-icon">
                {{ sidebarCollapsed ? '▶' : '◀' }}
              </span>
            </button>
          </div>
        </slot>
      </aside>

      <!-- 内容区域 -->
      <section class="tech-layout__content">
        <!-- 面包屑 -->
        <div v-if="showBreadcrumb" class="tech-layout__breadcrumb">
          <slot name="breadcrumb">
            <nav class="tech-layout__breadcrumb-nav">
              <ol class="tech-layout__breadcrumb-list">
                <li
                  v-for="(crumb, index) in breadcrumbs"
                  :key="crumb.key"
                  :class="[
                    'tech-layout__breadcrumb-item',
                    { 'tech-layout__breadcrumb-item--last': index === breadcrumbs.length - 1 }
                  ]"
                >
                  <span 
                    v-if="index < breadcrumbs.length - 1"
                    class="tech-layout__breadcrumb-link"
                    @click="handleBreadcrumbClick(crumb)"
                  >
                    {{ crumb.label }}
                  </span>
                  <span v-else class="tech-layout__breadcrumb-current">
                    {{ crumb.label }}
                  </span>
                  
                  <span 
                    v-if="index < breadcrumbs.length - 1"
                    class="tech-layout__breadcrumb-separator"
                  >
                    /
                  </span>
                </li>
              </ol>
            </nav>
          </slot>
        </div>

        <!-- 页面内容 -->
        <div class="tech-layout__page">
          <slot></slot>
        </div>
      </section>
    </main>

    <!-- 底部 -->
    <footer v-if="showFooter" class="tech-layout__footer">
      <slot name="footer">
        <div class="tech-layout__footer-container">
          <div class="tech-layout__footer-content">
            <span class="tech-layout__footer-text">{{ footerText }}</span>
            <span class="tech-layout__footer-version">v{{ version }}</span>
          </div>
        </div>
      </slot>
    </footer>

    <!-- 全局加载状态 -->
    <transition name="tech-layout-loading">
      <div v-if="loading" class="tech-layout__loading">
        <div class="tech-layout__loading-spinner">
          <div class="tech-layout__loading-ring"></div>
          <div class="tech-layout__loading-text">加载中...</div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'

interface NavItem {
  key: string
  label: string
  icon: string
  path?: string
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

interface Props {
  title?: string
  theme?: 'light' | 'dark'
  showHeader?: boolean
  showSidebar?: boolean
  showFooter?: boolean
  showBreadcrumb?: boolean
  showMobileMenu?: boolean
  sidebarCollapsible?: boolean
  sidebarCollapsed?: boolean
  sidebarTitle?: string
  footerText?: string
  version?: string
  loading?: boolean
  navItems?: NavItem[]
  sidebarItems?: SidebarItem[]
  breadcrumbs?: BreadcrumbItem[]
  activeNav?: string
  activeSidebar?: string
}

interface Emits {
  (e: 'nav-click', item: NavItem): void
  (e: 'sidebar-click', item: SidebarItem): void
  (e: 'breadcrumb-click', item: BreadcrumbItem): void
  (e: 'theme-change', theme: 'light' | 'dark'): void
  (e: 'sidebar-toggle', collapsed: boolean): void
  (e: 'mobile-menu-toggle', open: boolean): void
}

const props = withDefaults(defineProps<Props>(), {
  title: 'Tech Platform',
  theme: 'dark',
  showHeader: true,
  showSidebar: true,
  showFooter: true,
  showBreadcrumb: true,
  showMobileMenu: true,
  sidebarCollapsible: true,
  sidebarCollapsed: false,
  footerText: '© 2024 Tech Platform. All rights reserved.',
  version: '1.0.0',
  loading: false,
  navItems: () => [],
  sidebarItems: () => [],
  breadcrumbs: () => []
})

const emit = defineEmits<Emits>()

// 响应式状态
const isMobileMenuOpen = ref(false)
const sidebarCollapsed = ref(props.sidebarCollapsed)
const currentTheme = ref(props.theme)

// 计算属性
const isMobile = computed(() => window.innerWidth < 768)

// 方法
const handleNavClick = (item: NavItem) => {
  if (item.disabled) return
  emit('nav-click', item)
  
  if (isMobile.value) {
    isMobileMenuOpen.value = false
  }
}

const handleMobileNavClick = (item: NavItem) => {
  handleNavClick(item)
}

const handleSidebarClick = (item: SidebarItem) => {
  if (item.disabled) return
  emit('sidebar-click', item)
}

const handleBreadcrumbClick = (item: BreadcrumbItem) => {
  emit('breadcrumb-click', item)
}

const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value
  emit('mobile-menu-toggle', isMobileMenuOpen.value)
}

const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value
  emit('sidebar-toggle', sidebarCollapsed.value)
}

const toggleTheme = () => {
  currentTheme.value = currentTheme.value === 'dark' ? 'light' : 'dark'
  emit('theme-change', currentTheme.value)
}

// 响应式处理
const handleResize = () => {
  if (!isMobile.value && isMobileMenuOpen.value) {
    isMobileMenuOpen.value = false
  }
}

// 生命周期
onMounted(() => {
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})

// 监听
watch(() => props.theme, (newTheme) => {
  currentTheme.value = newTheme
})

watch(() => props.sidebarCollapsed, (newCollapsed) => {
  sidebarCollapsed.value = newCollapsed
})
</script>

<style scoped lang="scss">
@import '@/styles/tech-theme.scss';

.tech-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: $tech-bg-dark;
  color: $tech-text-primary;
  
  &.tech-layout--light {
    background: #f8fafc;
    color: #1e293b;
    
    .tech-layout__header {
      background: white;
      border-bottom-color: #e2e8f0;
    }
    
    .tech-layout__sidebar {
      background: white;
      border-right-color: #e2e8f0;
    }
    
    .tech-layout__content {
      background: #f8fafc;
    }
    
    .tech-layout__footer {
      background: white;
      border-top-color: #e2e8f0;
    }
  }
}

.tech-layout__header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: $tech-bg-darker;
  border-bottom: $tech-border-light;
  backdrop-filter: blur(10px);
}

.tech-layout__header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
  padding: 0 24px;
  max-width: 100%;
}

.tech-layout__logo {
  display: flex;
  align-items: center;
}

.tech-layout__logo-content {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 700;
  font-size: 1.25rem;
  color: $tech-primary;
}

.tech-layout__logo-icon {
  font-size: 1.5rem;
}

.tech-layout__logo-text {
  background: $tech-gradient-primary;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.tech-layout__nav {
  flex: 1;
  display: flex;
  justify-content: center;
}

.tech-layout__nav-menu {
  display: flex;
  align-items: center;
  gap: 32px;
  list-style: none;
  margin: 0;
  padding: 0;
}

.tech-layout__nav-item {
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  transition: all $tech-transition-fast;
  color: $tech-text-secondary;
  
  &:hover {
    color: $tech-text-primary;
    background: rgba($tech-primary, 0.1);
  }
  
  &.tech-layout__nav-item--active {
    color: $tech-primary;
    background: rgba($tech-primary, 0.15);
    
    .tech-layout__nav-indicator {
      width: 100%;
    }
  }
}

.tech-layout__nav-icon {
  font-size: 1.1rem;
}

.tech-layout__nav-text {
  font-weight: 500;
  font-size: 0.875rem;
}

.tech-layout__nav-indicator {
  position: absolute;
  bottom: -2px;
  left: 50%;
  width: 0;
  height: 2px;
  background: $tech-primary;
  border-radius: 1px;
  transition: all $tech-transition-fast;
  transform: translateX(-50%);
}

.tech-layout__actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.tech-layout__theme-toggle {
  background: none;
  border: none;
  color: $tech-text-secondary;
  cursor: pointer;
  padding: 8px;
  border-radius: 6px;
  transition: all $tech-transition-fast;
  
  &:hover {
    color: $tech-text-primary;
    background: rgba($tech-primary, 0.1);
  }
}

.tech-layout__theme-icon {
  font-size: 1.2rem;
}

.tech-layout__user-menu {
  display: flex;
  align-items: center;
}

.tech-layout__user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: $tech-gradient-primary;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 600;
  font-size: 0.875rem;
}

.tech-layout__mobile-toggle {
  display: none;
  background: none;
  border: none;
  color: $tech-text-primary;
  cursor: pointer;
  padding: 8px;
  font-size: 1.5rem;
}

.tech-layout__mobile-menu {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: $tech-bg-darker;
  border-top: $tech-border-light;
  box-shadow: $tech-shadow-strong;
}

.tech-layout__mobile-container {
  padding: 16px;
}

.tech-layout__mobile-nav {
  list-style: none;
  margin: 0;
  padding: 0;
}

.tech-layout__mobile-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 6px;
  cursor: pointer;
  transition: background $tech-transition-fast;
  color: $tech-text-secondary;
  
  &:hover {
    background: rgba($tech-primary, 0.1);
    color: $tech-text-primary;
  }
  
  &.tech-layout__mobile-item--active {
    color: $tech-primary;
    background: rgba($tech-primary, 0.15);
  }
}

.tech-layout__mobile-icon {
  font-size: 1.2rem;
}

.tech-layout__mobile-text {
  font-weight: 500;
}

.tech-layout__main {
  flex: 1;
  display: flex;
  min-height: calc(100vh - 64px);
}

.tech-layout__sidebar {
  width: 280px;
  background: $tech-bg-darker;
  border-right: $tech-border-light;
  transition: width $tech-transition-medium;
  position: relative;
  
  &.tech-layout__sidebar--collapsed {
    width: 64px;
  }
}

.tech-layout__sidebar-content {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.tech-layout__sidebar-title {
  padding: 20px 24px 16px;
  font-size: 1rem;
  font-weight: 600;
  color: $tech-text-primary;
  border-bottom: $tech-border-light;
}

.tech-layout__sidebar-nav {
  flex: 1;
  padding: 16px 0;
}

.tech-layout__sidebar-menu {
  list-style: none;
  margin: 0;
  padding: 0;
}

.tech-layout__sidebar-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 24px;
  cursor: pointer;
  transition: all $tech-transition-fast;
  color: $tech-text-secondary;
  position: relative;
  
  &:hover {
    color: $tech-text-primary;
    background: rgba($tech-primary, 0.1);
  }
  
  &.tech-layout__sidebar-item--active {
    color: $tech-primary;
    background: rgba($tech-primary, 0.15);
    
    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 0;
      bottom: 0;
      width: 3px;
      background: $tech-primary;
    }
  }
  
  &.tech-layout__sidebar-item--collapsed {
    justify-content: center;
    padding: 12px;
    
    .tech-layout__sidebar-text,
    .tech-layout__sidebar-badge {
      display: none;
    }
  }
}

.tech-layout__sidebar-icon {
  font-size: 1.2rem;
  flex-shrink: 0;
}

.tech-layout__sidebar-text {
  flex: 1;
  font-weight: 500;
  font-size: 0.875rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.tech-layout__sidebar-badge {
  background: $tech-error;
  color: white;
  padding: 2px 6px;
  border-radius: 10px;
  font-size: 0.75rem;
  font-weight: 600;
  min-width: 20px;
  text-align: center;
}

.tech-layout__sidebar-toggle {
  background: none;
  border: none;
  color: $tech-text-tertiary;
  cursor: pointer;
  padding: 16px;
  border-top: $tech-border-light;
  transition: all $tech-transition-fast;
  
  &:hover {
    color: $tech-text-primary;
    background: rgba($tech-primary, 0.1);
  }
}

.tech-layout__sidebar-toggle-icon {
  font-size: 0.875rem;
}

.tech-layout__content {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: $tech-bg-dark;
  overflow: auto;
}

.tech-layout__breadcrumb {
  padding: 16px 24px;
  border-bottom: $tech-border-light;
}

.tech-layout__breadcrumb-nav {
  display: flex;
  align-items: center;
}

.tech-layout__breadcrumb-list {
  display: flex;
  align-items: center;
  gap: 8px;
  list-style: none;
  margin: 0;
  padding: 0;
  font-size: 0.875rem;
}

.tech-layout__breadcrumb-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.tech-layout__breadcrumb-link {
  color: $tech-text-secondary;
  cursor: pointer;
  transition: color $tech-transition-fast;
  
  &:hover {
    color: $tech-primary;
  }
}

.tech-layout__breadcrumb-current {
  color: $tech-text-primary;
  font-weight: 500;
}

.tech-layout__breadcrumb-separator {
  color: $tech-text-tertiary;
}

.tech-layout__page {
  flex: 1;
  padding: 24px;
  overflow: auto;
}

.tech-layout__footer {
  background: $tech-bg-darker;
  border-top: $tech-border-light;
  padding: 16px 24px;
}

.tech-layout__footer-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.tech-layout__footer-content {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 0.875rem;
  color: $tech-text-tertiary;
}

.tech-layout__footer-version {
  background: rgba($tech-primary, 0.1);
  color: $tech-primary;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 0.75rem;
}

.tech-layout__loading {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba($tech-bg-darker, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

.tech-layout__loading-spinner {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.tech-layout__loading-ring {
  width: 40px;
  height: 40px;
  border: 3px solid rgba($tech-primary, 0.3);
  border-top: 3px solid $tech-primary;
  border-radius: 50%;
  animation: tech-layout-spin 1s linear infinite;
}

.tech-layout__loading-text {
  color: $tech-text-primary;
  font-size: 0.875rem;
}

// 动画
.tech-layout-mobile-enter-active,
.tech-layout-mobile-leave-active {
  transition: all $tech-transition-fast;
}

.tech-layout-mobile-enter-from,
.tech-layout-mobile-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

.tech-layout-loading-enter-active,
.tech-layout-loading-leave-active {
  transition: all $tech-transition-fast;
}

.tech-layout-loading-enter-from,
.tech-layout-loading-leave-to {
  opacity: 0;
}

@keyframes tech-layout-spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

// 响应式设计
@media (max-width: 1024px) {
  .tech-layout__sidebar {
    width: 240px;
  }
}

@media (max-width: 768px) {
  .tech-layout__header-container {
    padding: 0 16px;
    height: 56px;
  }

  .tech-layout__nav {
    display: none;
  }

  .tech-layout__actions {
    gap: 12px;
  }

  .tech-layout__mobile-toggle {
    display: block;
  }

  .tech-layout__sidebar {
    position: fixed;
    top: 56px;
    left: 0;
    bottom: 0;
    z-index: 90;
    transform: translateX(-100%);
    transition: transform $tech-transition-medium;
    
    &.tech-layout__sidebar--open {
      transform: translateX(0);
    }
  }

  .tech-layout__content {
    margin-left: 0;
  }

  .tech-layout__breadcrumb,
  .tech-layout__page {
    padding: 16px;
  }
}

@media (max-width: 480px) {
  .tech-layout__logo-text {
    display: none;
  }

  .tech-layout__footer-content {
    flex-direction: column;
    gap: 8px;
    text-align: center;
  }
}
</style>