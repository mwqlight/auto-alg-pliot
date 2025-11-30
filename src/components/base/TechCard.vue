<template>
  <div 
    :class="[
      'tech-card-component',
      `tech-card--${type}`,
      { 
        'tech-card--interactive': interactive,
        'tech-card--glow': glow,
        'tech-card--hover': hoverEffect
      }
    ]"
    @click="handleClick"
  >
    <!-- 顶部装饰线 -->
    <div v-if="showTopLine" class="tech-card__top-line"></div>
    
    <!-- 图标区域 -->
    <div v-if="icon || $slots.icon" class="tech-card__icon">
      <slot name="icon">
        <span v-if="icon" class="tech-card__icon-content">{{ icon }}</span>
      </slot>
    </div>
    
    <!-- 内容区域 -->
    <div class="tech-card__content">
      <!-- 标题区域 -->
      <div v-if="title || $slots.title" class="tech-card__header">
        <slot name="title">
          <h3 class="tech-card__title">{{ title }}</h3>
        </slot>
        
        <!-- 右上角操作区域 -->
        <div v-if="$slots.actions" class="tech-card__actions">
          <slot name="actions"></slot>
        </div>
      </div>
      
      <!-- 副标题 -->
      <div v-if="subtitle || $slots.subtitle" class="tech-card__subtitle">
        <slot name="subtitle">{{ subtitle }}</slot>
      </div>
      
      <!-- 主要内容 -->
      <div class="tech-card__body">
        <slot></slot>
      </div>
      
      <!-- 底部区域 -->
      <div v-if="$slots.footer" class="tech-card__footer">
        <slot name="footer"></slot>
      </div>
    </div>
    
    <!-- 底部装饰线 -->
    <div v-if="showBottomLine" class="tech-card__bottom-line"></div>
    
    <!-- 悬停效果层 -->
    <div v-if="hoverEffect" class="tech-card__hover-effect"></div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  title?: string
  subtitle?: string
  icon?: string
  type?: 'default' | 'primary' | 'secondary' | 'accent'
  interactive?: boolean
  glow?: boolean
  hoverEffect?: boolean
  showTopLine?: boolean
  showBottomLine?: boolean
}

interface Emits {
  (e: 'click', event: MouseEvent): void
}

const props = withDefaults(defineProps<Props>(), {
  type: 'default',
  interactive: false,
  glow: false,
  hoverEffect: true,
  showTopLine: true,
  showBottomLine: false
})

const emit = defineEmits<Emits>()

// 计算样式类
const cardClasses = computed(() => ({
  'tech-card--interactive': props.interactive,
  'tech-card--glow': props.glow,
  'tech-card--hover': props.hoverEffect
}))

// 点击事件处理
const handleClick = (event: MouseEvent) => {
  if (props.interactive) {
    emit('click', event)
  }
}
</script>

<style scoped lang="scss">
@import '@/styles/tech-theme.scss';

.tech-card-component {
  position: relative;
  background: rgba($tech-bg-light, 0.8);
  backdrop-filter: blur(10px);
  border: $tech-border-light;
  border-radius: 16px;
  padding: 24px;
  transition: all $tech-transition-medium;
  overflow: hidden;

  // 不同类型样式
  &.tech-card--primary {
    border-color: rgba($tech-primary, 0.4);
    background: rgba($tech-primary, 0.05);
  }

  &.tech-card--secondary {
    border-color: rgba($tech-secondary, 0.4);
    background: rgba($tech-secondary, 0.05);
  }

  &.tech-card--accent {
    border-color: rgba($tech-accent, 0.4);
    background: rgba($tech-accent, 0.05);
  }

  // 交互效果
  &.tech-card--interactive {
    cursor: pointer;
    
    &:hover {
      border-color: rgba($tech-primary, 0.6);
      transform: translateY(-2px);
    }

    &:active {
      transform: translateY(0);
    }
  }

  // 发光效果
  &.tech-card--glow {
    box-shadow: $tech-glow-primary;
    
    &:hover {
      box-shadow: $tech-glow-accent;
    }
  }

  // 悬停效果
  &.tech-card--hover {
    &:hover {
      .tech-card__hover-effect {
        opacity: 1;
      }
    }
  }
}

.tech-card__top-line {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: $tech-gradient-primary;
  border-radius: 2px 2px 0 0;
}

.tech-card__bottom-line {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: $tech-gradient-accent;
  border-radius: 0 0 2px 2px;
}

.tech-card__icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  background: rgba($tech-primary, 0.1);
  border-radius: 12px;
  margin-bottom: 16px;
  
  .tech-card__icon-content {
    font-size: 24px;
    color: $tech-primary;
  }
}

.tech-card__content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.tech-card__header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.tech-card__title {
  font-size: 1.25rem;
  font-weight: 600;
  color: $tech-text-primary;
  margin: 0;
  line-height: 1.4;
}

.tech-card__subtitle {
  font-size: 0.875rem;
  color: $tech-text-secondary;
  line-height: 1.5;
}

.tech-card__body {
  flex: 1;
  
  :deep(*) {
    color: $tech-text-secondary;
  }
}

.tech-card__footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  padding-top: 16px;
  border-top: 1px solid rgba($tech-primary, 0.1);
}

.tech-card__actions {
  display: flex;
  gap: 8px;
  align-items: center;
}

.tech-card__hover-effect {
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba($tech-primary, 0.05),
    transparent
  );
  transition: left 0.6s ease-in-out;
  opacity: 0;
  pointer-events: none;
}

.tech-card--hover:hover .tech-card__hover-effect {
  left: 100%;
  opacity: 1;
}

// 响应式设计
@media (max-width: 768px) {
  .tech-card-component {
    padding: 20px 16px;
    border-radius: 12px;
  }

  .tech-card__header {
    flex-direction: column;
    gap: 12px;
  }

  .tech-card__title {
    font-size: 1.125rem;
  }

  .tech-card__icon {
    width: 40px;
    height: 40px;
    margin-bottom: 12px;
    
    .tech-card__icon-content {
      font-size: 20px;
    }
  }
}
</style>