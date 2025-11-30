<template>
  <div 
    :class="[
      'tech-select-wrapper',
      `tech-select--${size}`,
      {
        'tech-select--disabled': disabled,
        'tech-select--error': hasError,
        'tech-select--success': success,
        'tech-select--open': isOpen,
        'tech-select--multiple': multiple
      }
    ]"
  >
    <!-- 标签 -->
    <label 
      v-if="label" 
      :for="id" 
      class="tech-select__label"
    >
      {{ label }}
      <span v-if="required" class="tech-select__required">*</span>
    </label>

    <!-- 选择器容器 -->
    <div class="tech-select__container">
      <!-- 选择器触发器 -->
      <div 
        ref="triggerRef"
        :class="[
          'tech-select__trigger',
          { 'tech-select__trigger--focused': isOpen }
        ]"
        @click="toggleDropdown"
      >
        <!-- 前缀图标 -->
        <div v-if="$slots.prefix || prefixIcon" class="tech-select__prefix">
          <slot name="prefix">
            <span v-if="prefixIcon" class="tech-select__prefix-icon">{{ prefixIcon }}</span>
          </slot>
        </div>

        <!-- 选择的值显示 -->
        <div class="tech-select__value">
          <template v-if="multiple">
            <!-- 多选显示 -->
            <div v-if="selectedOptions.length > 0" class="tech-select__tags">
              <span 
                v-for="option in selectedOptions.slice(0, maxTagCount)" 
                :key="option.value"
                class="tech-select__tag"
              >
                {{ option.label }}
                <button 
                  v-if="!disabled"
                  class="tech-select__tag-remove"
                  @click.stop="removeOption(option.value)"
                >
                  ×
                </button>
              </span>
              <span 
                v-if="selectedOptions.length > maxTagCount" 
                class="tech-select__tag-count"
              >
                +{{ selectedOptions.length - maxTagCount }}
              </span>
            </div>
            <span v-else class="tech-select__placeholder">{{ placeholder }}</span>
          </template>
          <template v-else>
            <!-- 单选显示 -->
            <span v-if="selectedOption" class="tech-select__selected">
              {{ selectedOption.label }}
            </span>
            <span v-else class="tech-select__placeholder">{{ placeholder }}</span>
          </template>
        </div>

        <!-- 后缀图标 -->
        <div class="tech-select__suffix">
          <!-- 清除按钮 -->
          <button 
            v-if="clearable && hasValue && !disabled"
            class="tech-select__clear"
            @click.stop="handleClear"
          >
            <span class="tech-select__clear-icon">×</span>
          </button>
          
          <!-- 下拉箭头 -->
          <span 
            :class="[
              'tech-select__arrow',
              { 'tech-select__arrow--open': isOpen }
            ]"
          >
            ▼
          </span>
        </div>

        <!-- 边框高亮 -->
        <div class="tech-select__border"></div>
        <div class="tech-select__border-highlight"></div>
      </div>

      <!-- 下拉菜单 -->
      <transition name="tech-select-dropdown">
        <div 
          v-show="isOpen"
          ref="dropdownRef"
          class="tech-select__dropdown"
        >
          <!-- 搜索框 -->
          <div v-if="filterable" class="tech-select__search">
            <input
              ref="searchInputRef"
              v-model="searchQuery"
              :placeholder="searchPlaceholder"
              class="tech-select__search-input"
              @input="handleSearch"
            />
          </div>

          <!-- 选项列表 -->
          <div class="tech-select__options">
            <!-- 分组选项 -->
            <template v-if="hasGroups">
              <div 
                v-for="group in filteredGroups" 
                :key="group.label"
                class="tech-select__group"
              >
                <div class="tech-select__group-label">{{ group.label }}</div>
                <div class="tech-select__group-options">
                  <div
                    v-for="option in group.options"
                    :key="option.value"
                    :class="[
                      'tech-select__option',
                      {
                        'tech-select__option--selected': isSelected(option.value),
                        'tech-select__option--disabled': option.disabled,
                        'tech-select__option--highlighted': highlightedIndex === getOptionIndex(option)
                      }
                    ]"
                    @click="selectOption(option)"
                    @mouseenter="highlightOption(option)"
                  >
                    <span class="tech-select__option-check">
                      <span v-if="isSelected(option.value)" class="tech-select__option-checkmark">✓</span>
                    </span>
                    <span class="tech-select__option-label">{{ option.label }}</span>
                    <span v-if="option.description" class="tech-select__option-desc">{{ option.description }}</span>
                  </div>
                </div>
              </div>
            </template>
            
            <!-- 普通选项 -->
            <template v-else>
              <div
                v-for="option in filteredOptions"
                :key="option.value"
                :class="[
                  'tech-select__option',
                  {
                    'tech-select__option--selected': isSelected(option.value),
                    'tech-select__option--disabled': option.disabled,
                    'tech-select__option--highlighted': highlightedIndex === getOptionIndex(option)
                  }
                ]"
                @click="selectOption(option)"
                @mouseenter="highlightOption(option)"
              >
                <span class="tech-select__option-check">
                  <span v-if="isSelected(option.value)" class="tech-select__option-checkmark">✓</span>
                </span>
                <span class="tech-select__option-label">{{ option.label }}</span>
                <span v-if="option.description" class="tech-select__option-desc">{{ option.description }}</span>
              </div>
            </template>

            <!-- 空状态 -->
            <div v-if="filteredOptions.length === 0" class="tech-select__empty">
              {{ emptyText }}
            </div>
          </div>
        </div>
      </transition>
    </div>

    <!-- 错误信息 -->
    <div v-if="hasError && errorMessage" class="tech-select__error">
      {{ errorMessage }}
    </div>

    <!-- 帮助文本 -->
    <div v-if="helpText && !hasError" class="tech-select__help">
      {{ helpText }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, nextTick, onMounted, onUnmounted } from 'vue'

interface Option {
  value: string | number
  label: string
  disabled?: boolean
  description?: string
}

interface OptionGroup {
  label: string
  options: Option[]
}

interface Props {
  modelValue?: string | number | (string | number)[]
  options?: Option[]
  groups?: OptionGroup[]
  size?: 'small' | 'medium' | 'large'
  label?: string
  placeholder?: string
  disabled?: boolean
  required?: boolean
  clearable?: boolean
  filterable?: boolean
  multiple?: boolean
  maxTagCount?: number
  searchPlaceholder?: string
  emptyText?: string
  errorMessage?: string
  helpText?: string
  id?: string
}

interface Emits {
  (e: 'update:modelValue', value: string | number | (string | number)[]): void
  (e: 'change', value: string | number | (string | number)[]): void
  (e: 'visible-change', visible: boolean): void
  (e: 'clear'): void
}

const props = withDefaults(defineProps<Props>(), {
  size: 'medium',
  disabled: false,
  required: false,
  clearable: false,
  filterable: false,
  multiple: false,
  maxTagCount: 3,
  searchPlaceholder: '搜索...',
  emptyText: '暂无数据'
})

const emit = defineEmits<Emits>()

// Refs
const triggerRef = ref<HTMLElement>()
const dropdownRef = ref<HTMLElement>()
const searchInputRef = ref<HTMLInputElement>()

// 状态
const isOpen = ref(false)
const searchQuery = ref('')
const highlightedIndex = ref(-1)

// 计算属性
const hasError = computed(() => !!props.errorMessage)
const hasValue = computed(() => {
  if (props.multiple) {
    return Array.isArray(props.modelValue) && props.modelValue.length > 0
  }
  return props.modelValue !== undefined && props.modelValue !== ''
})

const hasGroups = computed(() => props.groups && props.groups.length > 0)

const allOptions = computed(() => {
  if (hasGroups.value) {
    return props.groups!.flatMap(group => group.options)
  }
  return props.options || []
})

const filteredOptions = computed(() => {
  if (!searchQuery.value) return allOptions.value
  
  const query = searchQuery.value.toLowerCase()
  return allOptions.value.filter(option => 
    option.label.toLowerCase().includes(query) ||
    (option.description && option.description.toLowerCase().includes(query))
  )
})

const filteredGroups = computed(() => {
  if (!hasGroups.value) return []
  
  const query = searchQuery.value.toLowerCase()
  return props.groups!.map(group => ({
    ...group,
    options: group.options.filter(option => 
      option.label.toLowerCase().includes(query) ||
      (option.description && option.description.toLowerCase().includes(query))
    )
  })).filter(group => group.options.length > 0)
})

const selectedOption = computed(() => {
  if (props.multiple || !props.modelValue) return null
  return allOptions.value.find(option => option.value === props.modelValue)
})

const selectedOptions = computed(() => {
  if (!props.multiple || !Array.isArray(props.modelValue)) return []
  return allOptions.value.filter(option => props.modelValue!.includes(option.value))
})

// 方法
const getOptionIndex = (option: Option) => {
  return allOptions.value.findIndex(opt => opt.value === option.value)
}

const isSelected = (value: string | number) => {
  if (props.multiple) {
    return Array.isArray(props.modelValue) && props.modelValue.includes(value)
  }
  return props.modelValue === value
}

const toggleDropdown = async () => {
  if (props.disabled) return
  
  isOpen.value = !isOpen.value
  
  if (isOpen.value) {
    await nextTick()
    if (props.filterable && searchInputRef.value) {
      searchInputRef.value.focus()
    }
    highlightedIndex.value = -1
  } else {
    searchQuery.value = ''
  }
  
  emit('visible-change', isOpen.value)
}

const selectOption = (option: Option) => {
  if (option.disabled) return
  
  let newValue: string | number | (string | number)[]
  
  if (props.multiple) {
    const currentValue = Array.isArray(props.modelValue) ? [...props.modelValue] : []
    const index = currentValue.indexOf(option.value)
    
    if (index > -1) {
      currentValue.splice(index, 1)
    } else {
      currentValue.push(option.value)
    }
    
    newValue = currentValue
  } else {
    newValue = option.value
    isOpen.value = false
  }
  
  emit('update:modelValue', newValue)
  emit('change', newValue)
}

const removeOption = (value: string | number) => {
  if (props.multiple && Array.isArray(props.modelValue)) {
    const newValue = props.modelValue.filter(v => v !== value)
    emit('update:modelValue', newValue)
    emit('change', newValue)
  }
}

const handleClear = () => {
  const newValue = props.multiple ? [] : undefined
  emit('update:modelValue', newValue)
  emit('change', newValue)
  emit('clear')
  
  if (!props.multiple) {
    isOpen.value = false
  }
}

const handleSearch = () => {
  highlightedIndex.value = -1
}

const highlightOption = (option: Option) => {
  highlightedIndex.value = getOptionIndex(option)
}

// 键盘导航
const handleKeydown = (event: KeyboardEvent) => {
  if (!isOpen.value) return
  
  switch (event.key) {
    case 'ArrowDown':
      event.preventDefault()
      highlightedIndex.value = Math.min(highlightedIndex.value + 1, filteredOptions.value.length - 1)
      break
    case 'ArrowUp':
      event.preventDefault()
      highlightedIndex.value = Math.max(highlightedIndex.value - 1, 0)
      break
    case 'Enter':
      event.preventDefault()
      if (highlightedIndex.value >= 0 && highlightedIndex.value < filteredOptions.value.length) {
        selectOption(filteredOptions.value[highlightedIndex.value])
      }
      break
    case 'Escape':
      event.preventDefault()
      isOpen.value = false
      break
  }
}

// 点击外部关闭
const handleClickOutside = (event: MouseEvent) => {
  if (!triggerRef.value || !dropdownRef.value) return
  
  const target = event.target as Node
  if (!triggerRef.value.contains(target) && !dropdownRef.value.contains(target)) {
    isOpen.value = false
  }
}

// 生命周期
onMounted(() => {
  document.addEventListener('click', handleClickOutside)
  document.addEventListener('keydown', handleKeydown)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
  document.removeEventListener('keydown', handleKeydown)
})

// 监听
watch(isOpen, (newValue) => {
  if (newValue && props.filterable) {
    nextTick(() => {
      searchInputRef.value?.focus()
    })
  } else {
    searchQuery.value = ''
  }
})

// 公开方法
defineExpose({
  focus: () => triggerRef.value?.focus(),
  blur: () => triggerRef.value?.blur(),
  open: () => { if (!props.disabled) isOpen.value = true },
  close: () => { isOpen.value = false }
})
</script>

<style scoped lang="scss">
@import '@/styles/tech-theme.scss';

.tech-select-wrapper {
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 100%;
  position: relative;
}

.tech-select__label {
  font-size: 0.875rem;
  font-weight: 600;
  color: $tech-text-secondary;
  margin-bottom: 4px;
}

.tech-select__required {
  color: $tech-error;
  margin-left: 2px;
}

.tech-select__container {
  position: relative;
}

.tech-select__trigger {
  position: relative;
  display: flex;
  align-items: center;
  background: rgba($tech-bg-dark, 0.6);
  border: $tech-border-light;
  border-radius: 8px;
  cursor: pointer;
  transition: all $tech-transition-fast;
  min-height: 40px;

  // 尺寸变体
  &.tech-select--small {
    min-height: 32px;
    font-size: 0.875rem;
  }

  &.tech-select--medium {
    min-height: 40px;
    font-size: 1rem;
  }

  &.tech-select--large {
    min-height: 48px;
    font-size: 1.125rem;
  }

  // 状态变体
  &.tech-select__trigger--focused {
    border-color: $tech-primary;
    box-shadow: 0 0 0 3px rgba($tech-primary, 0.1);
  }

  &.tech-select--error & {
    border-color: $tech-error;
    
    &.tech-select__trigger--focused {
      box-shadow: 0 0 0 3px rgba($tech-error, 0.1);
    }
  }

  &.tech-select--disabled & {
    opacity: 0.6;
    cursor: not-allowed;
  }
}

.tech-select__prefix,
.tech-select__suffix {
  display: flex;
  align-items: center;
  padding: 0 12px;
  color: $tech-text-tertiary;
}

.tech-select__value {
  flex: 1;
  display: flex;
  align-items: center;
  padding: 0 12px;
  min-height: inherit;
}

.tech-select__placeholder {
  color: $tech-text-tertiary;
}

.tech-select__tags {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px;
  padding: 4px 0;
}

.tech-select__tag {
  display: inline-flex;
  align-items: center;
  background: rgba($tech-primary, 0.1);
  color: $tech-primary;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 0.875rem;
  gap: 4px;
}

.tech-select__tag-remove {
  background: none;
  border: none;
  color: inherit;
  cursor: pointer;
  padding: 0;
  font-size: 1.2em;
  line-height: 1;
  
  &:hover {
    opacity: 0.8;
  }
}

.tech-select__tag-count {
  color: $tech-text-tertiary;
  font-size: 0.875rem;
}

.tech-select__clear {
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

.tech-select__arrow {
  transition: transform $tech-transition-fast;
  
  &.tech-select__arrow--open {
    transform: rotate(180deg);
  }
}

.tech-select__border {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: rgba($tech-primary, 0.2);
}

.tech-select__border-highlight {
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: $tech-primary;
  transition: all $tech-transition-medium;
  transform: translateX(-50%);
}

.tech-select__trigger--focused .tech-select__border-highlight {
  width: 100%;
}

.tech-select__dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: $tech-bg-dark;
  border: $tech-border-light;
  border-radius: 8px;
  box-shadow: $tech-shadow-strong;
  margin-top: 4px;
  z-index: 1000;
  max-height: 300px;
  overflow: hidden;
}

.tech-select__search {
  padding: 12px;
  border-bottom: $tech-border-light;
}

.tech-select__search-input {
  width: 100%;
  background: rgba($tech-bg-darker, 0.6);
  border: $tech-border-light;
  border-radius: 4px;
  padding: 8px 12px;
  color: $tech-text-primary;
  font-size: 0.875rem;
  
  &:focus {
    outline: none;
    border-color: $tech-primary;
  }
}

.tech-select__options {
  max-height: 256px;
  overflow-y: auto;
}

.tech-select__group {
  border-bottom: $tech-border-light;
  
  &:last-child {
    border-bottom: none;
  }
}

.tech-select__group-label {
  padding: 8px 12px;
  font-size: 0.75rem;
  font-weight: 600;
  color: $tech-text-tertiary;
  background: rgba($tech-bg-darker, 0.3);
}

.tech-select__group-options {
  padding: 4px 0;
}

.tech-select__option {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  cursor: pointer;
  transition: background $tech-transition-fast;
  gap: 8px;
  
  &:hover {
    background: rgba($tech-primary, 0.1);
  }
  
  &.tech-select__option--selected {
    background: rgba($tech-primary, 0.2);
    color: $tech-primary;
  }
  
  &.tech-select__option--highlighted {
    background: rgba($tech-primary, 0.15);
  }
  
  &.tech-select__option--disabled {
    opacity: 0.5;
    cursor: not-allowed;
    
    &:hover {
      background: transparent;
    }
  }
}

.tech-select__option-check {
  width: 16px;
  height: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid $tech-border-color;
  border-radius: 2px;
  transition: all $tech-transition-fast;
}

.tech-select__option--selected .tech-select__option-check {
  background: $tech-primary;
  border-color: $tech-primary;
}

.tech-select__option-checkmark {
  color: white;
  font-size: 0.75rem;
  font-weight: bold;
}

.tech-select__option-label {
  flex: 1;
  font-size: 0.875rem;
}

.tech-select__option-desc {
  font-size: 0.75rem;
  color: $tech-text-tertiary;
}

.tech-select__empty {
  padding: 20px;
  text-align: center;
  color: $tech-text-tertiary;
  font-size: 0.875rem;
}

.tech-select__error,
.tech-select__help {
  font-size: 0.75rem;
  line-height: 1.4;
}

.tech-select__error {
  color: $tech-error;
}

.tech-select__help {
  color: $tech-text-tertiary;
}

// 动画
.tech-select-dropdown-enter-active,
.tech-select-dropdown-leave-active {
  transition: all $tech-transition-fast;
}

.tech-select-dropdown-enter-from,
.tech-select-dropdown-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

// 响应式设计
@media (max-width: 768px) {
  .tech-select__trigger.tech-select--medium {
    min-height: 36px;
    font-size: 0.9rem;
  }

  .tech-select__prefix,
  .tech-select__suffix {
    padding: 0 10px;
  }

  .tech-select__value {
    padding: 0 10px;
  }
}
</style>