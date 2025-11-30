import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/dashboard/index.vue'),
    meta: {
      title: '算法驾驶舱 - 仪表板',
      requiresAuth: true
    }
  },
  {
    path: '/algorithms',
    name: 'Algorithms',
    component: () => import('@/views/algorithms/index.vue'),
    meta: {
      title: '算法管理',
      requiresAuth: true
    }
  },
  {
    path: '/algorithms/:id',
    name: 'AlgorithmDetail',
    component: () => import('@/views/algorithms/detail.vue'),
    meta: {
      title: '算法详情',
      requiresAuth: true
    }
  },
  {
    path: '/execution',
    name: 'Execution',
    component: () => import('@/views/execution/index.vue'),
    meta: {
      title: '代码执行',
      requiresAuth: true
    }
  },
  {
    path: '/visualization',
    name: 'Visualization',
    component: () => import('@/views/visualization/index.vue'),
    meta: {
      title: '可视化分析',
      requiresAuth: true
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: {
      title: '登录',
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

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title as string
  }

  // 检查是否需要认证
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('token')
    if (!token) {
      next('/login')
      return
    }
  }

  next()
})

export default router