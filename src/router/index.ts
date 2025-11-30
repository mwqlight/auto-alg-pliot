import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

// 页面组件导入
import Dashboard from '@/views/dashboard/index.vue'
import Algorithms from '@/views/algorithms/index.vue'
import AlgorithmDetail from '@/views/algorithms/Detail.vue'
import Visualization from '@/views/visualization/index.vue'
import Execution from '@/views/execution/index.vue'

// 路由配置
const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: {
      title: '控制台',
      icon: '📊',
      requiresAuth: true
    }
  },
  {
    path: '/algorithms',
    name: 'Algorithms',
    component: Algorithms,
    meta: {
      title: '算法库',
      icon: '🧮',
      requiresAuth: true
    }
  },
  {
    path: '/algorithms/:id',
    name: 'AlgorithmDetail',
    component: AlgorithmDetail,
    meta: {
      title: '算法详情',
      requiresAuth: true
    },
    props: true
  },
  {
    path: '/visualization',
    name: 'Visualization',
    component: Visualization,
    meta: {
      title: '可视化',
      icon: '🎯',
      requiresAuth: true
    }
  },
  {
    path: '/execution',
    name: 'Execution',
    component: Execution,
    meta: {
      title: '代码执行',
      icon: '⚡',
      requiresAuth: true
    }
  },
  {
    path: '/analysis',
    name: 'Analysis',
    component: () => import('@/views/analysis/index.vue'),
    meta: {
      title: '复杂度分析',
      icon: '📈',
      requiresAuth: true
    }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/profile/index.vue'),
    meta: {
      title: '个人资料',
      requiresAuth: true
    }
  },
  {
    path: '/settings',
    name: 'Settings',
    component: () => import('@/views/settings/index.vue'),
    meta: {
      title: '系统设置',
      requiresAuth: true
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: {
      title: '登录',
      requiresAuth: false
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/Register.vue'),
    meta: {
      title: '注册',
      requiresAuth: false
    }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue'),
    meta: {
      title: '页面未找到',
      requiresAuth: false
    }
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    // 返回页面顶部或保存的位置
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  const title = to.meta.title as string || '算法驾驶舱'
  document.title = `${title} - 算法驾驶舱平台`
  
  // 检查认证状态
  const isAuthenticated = localStorage.getItem('auth-token') !== null
  
  if (to.meta.requiresAuth && !isAuthenticated) {
    // 需要认证但未登录，跳转到登录页
    next('/login')
  } else if ((to.path === '/login' || to.path === '/register') && isAuthenticated) {
    // 已登录但访问登录/注册页，跳转到首页
    next('/dashboard')
  } else {
    // 正常导航
    next()
  }
})

// 路由后置钩子
router.afterEach((to, from) => {
  // 可以在这里添加页面统计等逻辑
  console.log(`导航到: ${to.name?.toString()}`)
})

export default router