<template>
  <div class="app-layout">
    <AppHeader />
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import AppHeader from './AppHeader.vue'
</script>

<style scoped lang="scss">
.app-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
  
  .main-content {
    flex: 1;
    overflow: auto;
    background: v-bind('$bg-color');
  }
}
</style>