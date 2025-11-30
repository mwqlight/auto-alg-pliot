<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()

// 应用启动时初始化用户状态
onMounted(() => {
  userStore.initUserState()
})

// 全局错误处理
window.addEventListener('unhandledrejection', (event) => {
  console.error('未处理的Promise拒绝:', event.reason)
})

window.addEventListener('error', (event) => {
  console.error('全局错误:', event.error)
})
</script>

<style scoped>
#app {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}
</style>