<template>
  <div 
    :class="[
      'tech-input-wrapper',
      `tech-input--${size}`,
      {
        'tech-input--disabled': disabled,
        'tech-input--error': hasError,
        'tech-input--success': success,
        'tech-input--focused': isFocused,
        'tech-input--with-icon': hasIcon,
        'tech-input--with-prefix': hasPrefix,
        'tech-input--with-suffix': hasSuffix
      }
    ]"
  >
    <!-- 标签 -->
    <label 
      v-if="label" 
      :for="id" 
      class="tech-input__label"
    >
      {{ label }}
      <span v-if="required" class="tech-input__required">*</span>
    </label>

    <!-- 输入框容器 -->
    <div class="tech-input__container">
      <!-- 前缀内容 -->
      <div v-if="$slots.prefix || prefix" class="tech-input__prefix">
        <slot name="prefix">
          <span v-if="prefix" class="tech-input__prefix-text">{{ prefix }}</span>
        </slot>
      </div>

      <!-- 图标 -->
      <div v-if="$slots.icon || icon" class="tech-input__icon">
        <slot name="icon">
          <span v-if="icon" class="tech-input__icon-content">{{ icon }}</span>
        </slot>
      </div>

      <!-- 输入框 -->
      <input
        :id="id"
        ref="inputRef"
        :type="type"
        :value="modelValue"
        :placeholder="placeholder"
        :disabled="disabled"
        :readonly="readonly"
        :maxlength="maxlength"
        :minlength="minlength"
        :autocomplete="autocomplete"
        :autofocus="autofocus"
        class="tech-input"
        @input="handleInput"
        @focus="handleFocus"
        @blur="handleBlur"
        @keydown="handleKeydown"
      />

      <!-- 清除按钮 -->
      <button
        v-if="clearable && modelValue && !disabled"
        class="tech-input__clear"
        @click="handleClear"
        type="button"
      >
        <span class="tech-input__clear-icon">×</span>
      </button>

      <!-- 后缀内容 -->
      <div v-if="$slots.suffix || suffix" class="tech-input__suffix">
        <slot name="suffix">
          <span v-if="suffix" class="tech-input__suffix-text">{{ suffix }}</span>
        </slot>
      </div>

      <!-- 边框高亮 -->
      <div class="tech-input__border"></div>
      <div class="tech-input__border-highlight"></div>
    </div>

    <!-- 错误信息 -->
    <div v-if="hasError && errorMessage" class="tech-input__error">
      {{ errorMessage }}
    </div>

    <!-- 帮助文本 -->
    <div v-if="helpText && !hasError" class="tech-input__help">
      {{ helpText }}
    </div>

    <!-- 字符计数 -->
    <div v-if="showCount && maxlength" class="tech-input__count">
      {{ modelValue?.length || 0 }} / {{ maxlength }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, nextTick } from 'vue'

interface Props {
  modelValue?: string | number
  type?: 'text' | 'password' | 'email' | 'number' | 'tel' | 'url' | 'search'
  size?: 'small' | 'medium' | 'large'
  label?: string
  placeholder?: string
  disabled?: boolean
  readonly?: boolean
  required?: boolean
  clearable?: boolean
  showCount?: boolean
  maxlength?: number
  minlength?: number
  autocomplete?: string
  autofocus?: boolean
  icon?: string
  prefix?: string
  suffix?: string
  errorMessage?: string
  helpText?: string
  id?: string
}

interface Emits {
  (e: 'update:modelValue', value: string): void
  (e: 'input', value: string): void
  (e: 'focus', event: FocusEvent): void
  (e: 'blur', event: FocusEvent): void
  (e: 'keydown', event: KeyboardEvent): void
  (e: 'clear'): void
}

const props = withDefaults(defineProps<Props>(), {
  type: 'text',
  size: 'medium',
  disabled: false,
  readonly: false,
  required: false,
  clearable: false,
  showCount: false,
  autofocus: false
})

const emit = defineEmits<Emits>()

const inputRef = ref<HTMLInputElement>()
const isFocused = ref(false)

// 计算属性
const hasError = computed(() => !!props.errorMessage)
const hasIcon = computed(() => !!props.icon || !!useSlots().icon)
const hasPrefix = computed(() => !!props.prefix || !!useSlots().prefix)
const hasSuffix = computed(() => !!props.suffix || !!useSlots().suffix)

// 事件处理
const handleInput = (event: Event) => {
  const target = event.target as HTMLInputElement
  const value = target.value
  
  emit('update:modelValue', value)
  emit('input', value)
}

const handleFocus = (event: FocusEvent) => {
  isFocused.value = true
  emit('focus', event)
}

const handleBlur = (event: FocusEvent) => {
  isFocused.value = false
  emit('blur', event)
}

const handleKeydown = (event: KeyboardEvent) => {
  emit('keydown', event)
}

const handleClear = () => {
  emit('update:modelValue', '')
  emit('input', '')
  emit('clear')
  
  nextTick(() => {
    inputRef.value?.focus()
  })
}

// 公开方法
defineExpose({
  focus: () => inputRef.value?.focus(),
  blur: () => inputRef.value?.blur(),
  select: () => inputRef.value?.select()
})
</script>

<style scoped lang="scss">
@import '@/styles/tech-theme.scss';

.tech-input-wrapper {
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 100%;
}

.tech-input__label {
  font-size: 0.875rem;
  font-weight: 600;
  color: $tech-text-secondary;
  margin-bottom: 4px;
  transition: color $tech-transition-fast;
}

.tech-input__required {
  color: $tech-error;
  margin-left: 2px;
}

.tech-input__container {
  position: relative;
  display: flex;
  align-items: center;
  background: rgba($tech-bg-dark, 0.6);
  border: $tech-border-light;
  border-radius: 8px;
  transition: all $tech-transition-fast;
  overflow: hidden;

  // 尺寸变体
  &.tech-input--small {
    min-height: 32px;
    
    .tech-input {
      padding: 6px 12px;
      font-size: 0.875rem;
    }
    
    .tech-input__icon,
    .tech-input__prefix,
    .tech-input__suffix {
      padding: 0 8px;
    }
  }

  &.tech-input--medium {
    min-height: 40px;
    
    .tech-input {
      padding: 10px 16px;
      font-size: 1rem;
    }
    
    .tech-input__icon,
    .tech-input__prefix,
    .tech-input__suffix {
      padding: 0 12px;
    }
  }

  &.tech-input--large {
    min-height: 48px;
    
    .tech-input {
      padding: 12px 20px;
      font-size: 1.125rem;
    }
    
    .tech-input__icon,
    .tech-input__prefix,
    .tech-input__suffix {
      padding: 0 16px;
    }
  }

  // 状态变体
  &.tech-input--focused {
    border-color: $tech-primary;
    box-shadow: 0 0 0 3px rgba($tech-primary, 0.1);
    
    .tech-input__border-highlight {
      width: 100%;
    }
  }

  &.tech-input--error {
    border-color: $tech-error;
    
    &.tech-input--focused {
      box-shadow: 0 0 0 3px rgba($tech-error, 0.1);
    }
    
    .tech-input__border-highlight {
      background: $tech-error;
    }
  }

  &.tech-input--success {
    border-color: $tech-success;
    
    &.tech-input--focused {
      box-shadow: 0 0 0 3px rgba($tech-success, 0.1);
    }
    
    .tech-input__border-highlight {
      background: $tech-success;
    }
  }

  &.tech-input--disabled {
    opacity: 0.6;
    cursor: not-allowed;
    
    .tech-input {
      cursor: not-allowed;
    }
  }
}

.tech-input {
  flex: 1;
  background: transparent;
  border: none;
  outline: none;
  color: $tech-text-primary;
  font-family: inherit;
  
  &::placeholder {
    color: $tech-text-tertiary;
  }
  
  &:disabled {
    cursor: not-allowed;
  }
}

.tech-input__icon,
.tech-input__prefix,
.tech-input__suffix {
  display: flex;
  align-items: center;
  color: $tech-text-tertiary;
  transition: color $tech-transition-fast;
}

.tech-input__icon-content {
  font-size: 1.2em;
}

.tech-input__clear {
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
  border: none;
  color: $tech-text-tertiary;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  transition: all $tech-transition-fast;
  
  &:hover {
    color: $tech-text-secondary;
    background: rgba($tech-primary, 0.1);
  }
}

.tech-input__clear-icon {
  font-size: 1.2em;
  font-weight: bold;
  line-height: 1;
}

.tech-input__border {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: rgba($tech-primary, 0.2);
}

.tech-input__border-highlight {
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: $tech-primary;
  transition: all $tech-transition-medium;
  transform: translateX(-50%);
}

.tech-input--focused .tech-input__border-highlight {
  width: 100%;
}

.tech-input__error,
.tech-input__help,
.tech-input__count {
  font-size: 0.75rem;
  line-height: 1.4;
}

.tech-input__error {
  color: $tech-error;
}

.tech-input__help {
  color: $tech-text-tertiary;
}

.tech-input__count {
  color: $tech-text-tertiary;
  text-align: right;
  
  &.tech-input__count--warning {
    color: $tech-warning;
  }
  
  &.tech-input__count--error {
    color: $tech-error;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .tech-input-wrapper {
    gap: 6px;
  }

  .tech-input__container.tech-input--medium {
    min-height: 36px;
    
    .tech-input {
      padding: 8px 14px;
      font-size: 0.9rem;
    }
    
    .tech-input__icon,
    .tech-input__prefix,
    .tech-input__suffix {
      padding: 0 10px;
    }
  }

  .tech-input__label {
    font-size: 0.8rem;
  }
}
</style>