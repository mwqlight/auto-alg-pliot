import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export interface User {
  id: string
  username: string
  email: string
  avatar?: string
  role: 'admin' | 'user' | 'guest'
  permissions: string[]
}

export interface AppSettings {
  theme: 'light' | 'dark'
  language: 'zh-CN' | 'en-US'
  sidebarCollapsed: boolean
  notifications: boolean
  sound: boolean
  autoSave: boolean
}

export interface Notification {
  id: string
  type: 'success' | 'error' | 'warning' | 'info'
  title: string
  message: string
  timestamp: number
  read: boolean
}

export const useAppStore = defineStore('app', () => {
  // 状态
  const user = ref<User | null>(null)
  const settings = ref<AppSettings>({
    theme: 'dark',
    language: 'zh-CN',
    sidebarCollapsed: false,
    notifications: true,
    sound: true,
    autoSave: true
  })
  const notifications = ref<Notification[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  const sidebarOpen = ref(true)

  // 计算属性
  const isAuthenticated = computed(() => user.value !== null)
  const isAdmin = computed(() => user.value?.role === 'admin')
  const unreadNotifications = computed(() => 
    notifications.value.filter(n => !n.read)
  )
  const currentTheme = computed(() => settings.value.theme)

  // 用户相关方法
  const setUser = (userData: User | null) => {
    user.value = userData
    if (userData) {
      localStorage.setItem('user', JSON.stringify(userData))
    } else {
      localStorage.removeItem('user')
    }
  }

  const login = async (credentials: { username: string; password: string }) => {
    loading.value = true
    error.value = null
    
    try {
      // 模拟登录API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      // 模拟用户数据
      const userData: User = {
        id: '1',
        username: credentials.username,
        email: `${credentials.username}@example.com`,
        role: credentials.username === 'admin' ? 'admin' : 'user',
        permissions: ['algorithm:read', 'algorithm:execute', 'visualization:view']
      }
      
      setUser(userData)
      addNotification({
        type: 'success',
        title: '登录成功',
        message: `欢迎回来，${userData.username}！`
      })
      
      return userData
    } catch (err) {
      error.value = '登录失败，请检查用户名和密码'
      addNotification({
        type: 'error',
        title: '登录失败',
        message: '用户名或密码错误'
      })
      throw err
    } finally {
      loading.value = false
    }
  }

  const logout = async () => {
    loading.value = true
    
    try {
      // 模拟登出API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      setUser(null)
      localStorage.removeItem('auth-token')
      
      addNotification({
        type: 'success',
        title: '退出成功',
        message: '您已安全退出系统'
      })
    } catch (err) {
      error.value = '退出失败'
      addNotification({
        type: 'error',
        title: '退出失败',
        message: '退出过程中发生错误'
      })
    } finally {
      loading.value = false
    }
  }

  // 设置相关方法
  const updateSettings = (newSettings: Partial<AppSettings>) => {
    settings.value = { ...settings.value, ...newSettings }
    localStorage.setItem('app-settings', JSON.stringify(settings.value))
  }

  const toggleTheme = () => {
    const newTheme = settings.value.theme === 'dark' ? 'light' : 'dark'
    updateSettings({ theme: newTheme })
  }

  const toggleSidebar = () => {
    sidebarOpen.value = !sidebarOpen.value
  }

  // 通知相关方法
  const addNotification = (notification: Omit<Notification, 'id' | 'timestamp' | 'read'>) => {
    const newNotification: Notification = {
      id: Date.now().toString(),
      timestamp: Date.now(),
      read: false,
      ...notification
    }
    
    notifications.value.unshift(newNotification)
    
    // 限制通知数量
    if (notifications.value.length > 50) {
      notifications.value = notifications.value.slice(0, 50)
    }
    
    // 自动移除旧通知（5分钟后）
    setTimeout(() => {
      removeNotification(newNotification.id)
    }, 5 * 60 * 1000)
  }

  const removeNotification = (id: string) => {
    const index = notifications.value.findIndex(n => n.id === id)
    if (index > -1) {
      notifications.value.splice(index, 1)
    }
  }

  const markNotificationAsRead = (id: string) => {
    const notification = notifications.value.find(n => n.id === id)
    if (notification) {
      notification.read = true
    }
  }

  const markAllNotificationsAsRead = () => {
    notifications.value.forEach(n => {
      n.read = true
    })
  }

  const clearNotifications = () => {
    notifications.value = []
  }

  // 错误处理方法
  const setError = (message: string) => {
    error.value = message
    addNotification({
      type: 'error',
      title: '错误',
      message
    })
  }

  const clearError = () => {
    error.value = null
  }

  // 初始化方法
  const initialize = () => {
    // 从本地存储加载用户数据
    const savedUser = localStorage.getItem('user')
    if (savedUser) {
      try {
        user.value = JSON.parse(savedUser)
      } catch (err) {
        console.error('Failed to parse saved user data:', err)
      }
    }

    // 从本地存储加载设置
    const savedSettings = localStorage.getItem('app-settings')
    if (savedSettings) {
      try {
        settings.value = { ...settings.value, ...JSON.parse(savedSettings) }
      } catch (err) {
        console.error('Failed to parse saved settings:', err)
      }
    }
  }

  // 权限检查方法
  const hasPermission = (permission: string) => {
    if (!user.value) return false
    return user.value.permissions.includes(permission)
  }

  const canExecuteAlgorithm = computed(() => hasPermission('algorithm:execute'))
  const canViewVisualization = computed(() => hasPermission('visualization:view'))
  const canManageAlgorithms = computed(() => hasPermission('algorithm:manage'))

  return {
    // 状态
    user,
    settings,
    notifications,
    loading,
    error,
    sidebarOpen,
    
    // 计算属性
    isAuthenticated,
    isAdmin,
    unreadNotifications,
    currentTheme,
    canExecuteAlgorithm,
    canViewVisualization,
    canManageAlgorithms,
    
    // 方法
    setUser,
    login,
    logout,
    updateSettings,
    toggleTheme,
    toggleSidebar,
    addNotification,
    removeNotification,
    markNotificationAsRead,
    markAllNotificationsAsRead,
    clearNotifications,
    setError,
    clearError,
    initialize,
    hasPermission
  }
})