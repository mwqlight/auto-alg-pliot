<template>
  <header class="app-header">
    <div class="header-left">
      <h1 class="logo">
        <el-icon><Monitor /></el-icon>
        Algorithm Cockpit
      </h1>
    </div>
    
    <div class="header-center">
      <el-menu
        :default-active="activeIndex"
        mode="horizontal"
        :ellipsis="false"
        @select="handleSelect"
        class="nav-menu"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataBoard /></el-icon>
          仪表板
        </el-menu-item>
        <el-menu-item index="/algorithms">
          <el-icon><List /></el-icon>
          算法管理
        </el-menu-item>
        <el-menu-item index="/execution">
          <el-icon><Play /></el-icon>
          代码执行
        </el-menu-item>
        <el-menu-item index="/visualization">
          <el-icon><TrendCharts /></el-icon>
          可视化分析
        </el-menu-item>
      </el-menu>
    </div>
    
    <div class="header-right">
      <el-dropdown @command="handleCommand">
        <span class="user-info">
          <el-avatar :size="32" :src="userStore.userInfo?.avatar" />
          <span class="username">{{ userStore.userInfo?.username }}</span>
          <el-icon><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>
              个人信息
            </el-dropdown-item>
            <el-dropdown-item command="settings">
              <el-icon><Setting /></el-icon>
              设置
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </header>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeIndex = computed(() => route.path)

const handleSelect = (index: string) => {
  router.push(index)
}

const handleCommand = async (command: string) => {
  switch (command) {
    case 'profile':
      // 跳转到个人信息页面
      break
    case 'settings':
      // 跳转到设置页面
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        userStore.clearUserInfo()
        router.push('/login')
        ElMessage.success('退出登录成功')
      } catch {
        // 用户取消操作
      }
      break
  }
}
</script>

<style scoped lang="scss">
.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
  padding: 0 20px;
  background: linear-gradient(90deg, v-bind('$bg-color-light'), v-bind('$bg-color-lighter'));
  border-bottom: 1px solid v-bind('$border-color-base');
  
  .header-left {
    .logo {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 18px;
      font-weight: 600;
      color: v-bind('$primary-color');
      margin: 0;
    }
  }
  
  .header-center {
    flex: 1;
    display: flex;
    justify-content: center;
    
    .nav-menu {
      background: transparent;
      border: none;
      
      :deep(.el-menu-item) {
        color: v-bind('$text-color-regular');
        border: none;
        
        &:hover {
          color: v-bind('$primary-color');
          background: rgba(64, 158, 255, 0.1);
        }
        
        &.is-active {
          color: v-bind('$primary-color');
          border-bottom: 2px solid v-bind('$primary-color');
        }
      }
    }
  }
  
  .header-right {
    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;
      padding: 4px 8px;
      border-radius: v-bind('$border-radius-base');
      transition: background-color 0.3s;
      
      &:hover {
        background: rgba(255, 255, 255, 0.1);
      }
      
      .username {
        color: v-bind('$text-color-regular');
        font-weight: 500;
      }
    }
  }
}
</style>