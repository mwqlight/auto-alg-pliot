<template>
  <div class="error-container">
    <!-- 背景装饰 -->
    <div class="background-effects">
      <div class="gradient-circles">
        <div class="circle circle-1"></div>
        <div class="circle circle-2"></div>
        <div class="circle circle-3"></div>
      </div>
      <div class="grid-overlay"></div>
    </div>

    <!-- 错误内容 -->
    <div class="error-content">
      <div class="error-code">404</div>
      <h1 class="error-title">页面未找到</h1>
      <p class="error-description">
        抱歉，您访问的页面不存在或已被移动
      </p>
      <div class="error-actions">
        <button @click="goBack" class="action-btn secondary">
          <span class="btn-icon">←</span>
          返回上一页
        </button>
        <button @click="goHome" class="action-btn primary">
          <span class="btn-icon">🏠</span>
          返回首页
        </button>
      </div>
    </div>

    <!-- 浮动元素 -->
    <div class="floating-elements">
      <div class="floating-element element-1">🔍</div>
      <div class="floating-element element-2">📄</div>
      <div class="floating-element element-3">🚀</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push('/')
  }
}

const goHome = () => {
  router.push('/')
}
</script>

<style scoped lang="scss">
.error-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
  padding: 20px;
}

.background-effects {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 0;
}

.gradient-circles {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  
  .circle {
    position: absolute;
    border-radius: 50%;
    background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 70%);
    
    &.circle-1 {
      width: 500px;
      height: 500px;
      top: -250px;
      left: -250px;
      animation: float 8s ease-in-out infinite;
    }
    
    &.circle-2 {
      width: 300px;
      height: 300px;
      top: -150px;
      left: 150px;
      animation: float 10s ease-in-out infinite reverse;
    }
    
    &.circle-3 {
      width: 200px;
      height: 200px;
      top: 100px;
      left: -100px;
      animation: float 12s ease-in-out infinite;
    }
  }
}

.grid-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    linear-gradient(rgba(255,255,255,0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255,255,255,0.03) 1px, transparent 1px);
  background-size: 50px 50px;
}

.error-content {
  text-align: center;
  color: white;
  z-index: 1;
  max-width: 600px;
  
  .error-code {
    font-size: 120px;
    font-weight: 900;
    margin-bottom: 20px;
    background: linear-gradient(45deg, #fff, rgba(255,255,255,0.8));
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    text-shadow: 0 10px 20px rgba(0,0,0,0.2);
  }
  
  .error-title {
    font-size: 32px;
    font-weight: 700;
    margin-bottom: 16px;
    text-shadow: 0 2px 4px rgba(0,0,0,0.3);
  }
  
  .error-description {
    font-size: 18px;
    opacity: 0.9;
    margin-bottom: 40px;
    line-height: 1.6;
    text-shadow: 0 1px 2px rgba(0,0,0,0.2);
  }
  
  .error-actions {
    display: flex;
    gap: 20px;
    justify-content: center;
    flex-wrap: wrap;
    
    .action-btn {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 12px 24px;
      border: none;
      border-radius: 8px;
      font-size: 16px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.3s;
      text-decoration: none;
      
      &.secondary {
        background: rgba(255, 255, 255, 0.2);
        color: white;
        backdrop-filter: blur(10px);
        
        &:hover {
          background: rgba(255, 255, 255, 0.3);
          transform: translateY(-2px);
          box-shadow: 0 10px 20px rgba(0,0,0,0.2);
        }
      }
      
      &.primary {
        background: white;
        color: #667eea;
        
        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 10px 20px rgba(0,0,0,0.2);
        }
      }
      
      .btn-icon {
        font-size: 18px;
      }
    }
  }
}

.floating-elements {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  
  .floating-element {
    position: absolute;
    font-size: 24px;
    opacity: 0.3;
    animation: float-random 15s ease-in-out infinite;
    
    &.element-1 {
      top: 20%;
      left: 10%;
      animation-delay: 0s;
    }
    
    &.element-2 {
      top: 60%;
      right: 15%;
      animation-delay: 5s;
    }
    
    &.element-3 {
      bottom: 30%;
      left: 20%;
      animation-delay: 10s;
    }
  }
}

@keyframes float {
  0%, 100% { transform: translateY(0px) translateX(0px); }
  33% { transform: translateY(-20px) translateX(10px); }
  66% { transform: translateY(10px) translateX(-10px); }
}

@keyframes float-random {
  0%, 100% { 
    transform: translateY(0px) translateX(0px) rotate(0deg);
    opacity: 0.3;
  }
  25% { 
    transform: translateY(-30px) translateX(20px) rotate(90deg);
    opacity: 0.5;
  }
  50% { 
    transform: translateY(20px) translateX(-15px) rotate(180deg);
    opacity: 0.2;
  }
  75% { 
    transform: translateY(-10px) translateX(25px) rotate(270deg);
    opacity: 0.4;
  }
}

@media (max-width: 768px) {
  .error-content {
    .error-code {
      font-size: 80px;
    }
    
    .error-title {
      font-size: 24px;
    }
    
    .error-description {
      font-size: 16px;
    }
    
    .error-actions {
      flex-direction: column;
      align-items: center;
      gap: 15px;
      
      .action-btn {
        width: 200px;
        justify-content: center;
      }
    }
  }
}
</style>