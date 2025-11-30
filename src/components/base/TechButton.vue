<template>
  <button
    :class="[
      'tech-button',
      `tech-button--${type}`,
      `tech-button--${size}`,
      {
        'tech-button--disabled': disabled,
        'tech-button--loading': loading,
        'tech-button--block': block,
        'tech-button--outline': outline,
        'tech-button--glow': glow,
        'tech-button--rounded': rounded
      }
    ]"
    :disabled="disabled || loading"
    @click="handleClick"
  >
    <!-- 加载状态 -->
    <div v-if="loading" class="tech-button__loading">
      <div class="tech-button__spinner"></div>
    </div>
    
    <!-- 图标 -->
    <span v-if="(icon || $slots.icon) && !loading" class="tech-button__icon">
      <slot name="icon">
        <span v-if="icon" class="tech-button__icon-content">{{ icon }}</span>
      </slot>
    </span>
    
    <!-- 按钮文本 -->
    <span v-if="!loading" class="tech-button__content">
      <slot></slot>
      {{ label }}
    </span>
    
    <!-- 悬停效果层 -->
    <div class="tech-button__hover-effect"></div>
    
    <!-- 点击涟漪效果 -->
    <div v-if="ripple" class="tech-button__ripple" ref="rippleRef"></div>
  </button>
</template>

<script setup lang="ts">
import { ref } from 'vue'

interface Props {
  type?: 'primary' | 'secondary' | 'accent' | 'success' | 'warning' | 'error'
  size?: 'small' | 'medium' | 'large'
  label?: string
  icon?: string
  disabled?: boolean
  loading?: boolean
  block?: boolean
  outline?: boolean
  glow?: boolean
  rounded?: boolean
  ripple?: boolean
}

interface Emits {
  (e: 'click', event: MouseEvent): void
}

const props = withDefaults(defineProps<Props>(), {
  type: 'primary',
  size: 'medium',
  disabled: false,
  loading: false,
  block: false,
  outline: false,
  glow: false,
  rounded: false,
  ripple: true
})

const emit = defineEmits<Emits>()
const rippleRef = ref<HTMLDivElement>()

// 点击事件处理
const handleClick = (event: MouseEvent) => {
  if (props.ripple && !props.disabled && !props.loading) {
    createRippleEffect(event)
  }
  emit('click', event)
}

// 创建点击涟漪效果
const createRippleEffect = (event: MouseEvent) => {
  const button = event.currentTarget as HTMLElement
  const ripple = document.createElement('span')
  
  const diameter = Math.max(button.clientWidth, button.clientHeight)
  const radius = diameter / 2
  
  ripple.style.width = ripple.style.height = `${diameter}px`
  ripple.style.left = `${event.clientX - button.getBoundingClientRect().left - radius}px`
  ripple.style.top = `${event.clientY - button.getBoundingClientRect().top - radius}px`
  ripple.classList.add('tech-button__ripple-effect')
  
  button.appendChild(ripple)
  
  setTimeout(() => {
    ripple.remove()
  }, 600)
}
</script>

<style scoped lang="scss">
@import '@/styles/tech-theme.scss';

.tech-button {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  border: none;
  border-radius: 8px;
  font-family: inherit;
  font-weight: 600;
  cursor: pointer;
  transition: all $tech-transition-fast;
  overflow: hidden;
  user-select: none;
  outline: none;

  // 尺寸变体
  &.tech-button--small {
    padding: 8px 16px;
    font-size: 0.875rem;
    min-height: 32px;
  }

  &.tech-button--medium {
    padding: 12px 24px;
    font-size: 1rem;
    min-height: 40px;
  }

  &.tech-button--large {
    padding: 16px 32px;
    font-size: 1.125rem;
    min-height: 48px;
  }

  // 类型变体
  &.tech-button--primary {
    background: $tech-gradient-primary;
    color: $tech-text-primary;
    
    &:not(.tech-button--outline):hover {
      box-shadow: $tech-glow-primary;
      transform: translateY(-1px);
    }
    
    &.tech-button--outline {
      background: transparent;
      border: 2px solid $tech-primary;
      color: $tech-primary;
      
      &:hover {
        background: rgba($tech-primary, 0.1);
        box-shadow: $tech-glow-primary;
      }
    }
  }

  &.tech-button--secondary {
    background: rgba($tech-secondary, 0.8);
    color: $tech-text-primary;
    
    &:not(.tech-button--outline):hover {
      box-shadow: $tech-glow-secondary;
      transform: translateY(-1px);
    }
    
    &.tech-button--outline {
      background: transparent;
      border: 2px solid $tech-secondary;
      color: $tech-secondary;
      
      &:hover {
        background: rgba($tech-secondary, 0.1);
        box-shadow: $tech-glow-secondary;
      }
    }
  }

  &.tech-button--accent {
    background: $tech-gradient-accent;
    color: $tech-text-primary;
    
    &:not(.tech-button--outline):hover {
      box-shadow: $tech-glow-accent;
      transform: translateY(-1px);
    }
    
    &.tech-button--outline {
      background: transparent;
      border: 2px solid $tech-accent;
      color: $tech-accent;
      
      &:hover {
        background: rgba($tech-accent, 0.1);
        box-shadow: $tech-glow-accent;
      }
    }
  }

  // 功能类型
  &.tech-button--success {
    background: rgba($tech-success, 0.9);
    color: $tech-text-primary;
    
    &:hover {
      box-shadow: 0 0 20px rgba($tech-success, 0.5);
    }
  }

  &.tech-button--warning {
    background: rgba($tech-warning, 0.9);
    color: $tech-text-primary;
    
    &:hover {
      box-shadow: 0 0 20px rgba($tech-warning, 0.5);
    }
  }

  &.tech-button--error {
    background: rgba($tech-error, 0.9);
    color: $tech-text-primary;
    
    &:hover {
      box-shadow: 0 0 20px rgba($tech-error, 0.5);
    }
  }

  // 状态变体
  &.tech-button--disabled {
    opacity: 0.6;
    cursor: not-allowed;
    
    &:hover {
      transform: none;
      box-shadow: none;
    }
  }

  &.tech-button--loading {
    cursor: wait;
  }

  &.tech-button--block {
    display: flex;
    width: 100%;
  }

  &.tech-button--glow {
    box-shadow: $tech-glow-primary;
    
    &:hover {
      box-shadow: $tech-glow-accent;
    }
  }

  &.tech-button--rounded {
    border-radius: 24px;
  }

  // 悬停效果
  &:hover:not(.tech-button--disabled):not(.tech-button--loading) {
    .tech-button__hover-effect {
      opacity: 1;
    }
  }

  // 激活状态
  &:active:not(.tech-button--disabled):not(.tech-button--loading) {
    transform: translateY(0);
  }
}

.tech-button__loading {
  display: flex;
  align-items: center;
  justify-content: center;
}

.tech-button__spinner {
  width: 16px;
  height: 16px;
  border: 2px solid transparent;
  border-top: 2px solid currentColor;
  border-radius: 50%;
  animation: tech-button-spin 1s linear infinite;
}

.tech-button__icon {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2em;
}

.tech-button__content {
  display: flex;
  align-items: center;
  gap: 4px;
}

.tech-button__hover-effect {
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.2),
    transparent
  );
  transition: left 0.5s ease-in-out;
  opacity: 0;
  pointer-events: none;
}

.tech-button:hover:not(.tech-button--disabled):not(.tech-button--loading) .tech-button__hover-effect {
  left: 100%;
  opacity: 1;
}

.tech-button__ripple-effect {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.6);
  transform: scale(0);
  animation: tech-button-ripple 0.6s linear;
  pointer-events: none;
}

// 动画定义
@keyframes tech-button-spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@keyframes tech-button-ripple {
  to {
    transform: scale(4);
    opacity: 0;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .tech-button {
    &.tech-button--medium {
      padding: 10px 20px;
      min-height: 36px;
      font-size: 0.9rem;
    }

    &.tech-button--large {
      padding: 14px 28px;
      min-height: 44px;
      font-size: 1rem;
    }
  }
}
</style>