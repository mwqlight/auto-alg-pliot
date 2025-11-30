<template>
  <div class="code-editor">
    <div class="editor-header">
      <div class="header-left">
        <span class="editor-title">代码编辑器</span>
        <el-tag v-if="language" size="small">{{ language.toUpperCase() }}</el-tag>
      </div>
      <div class="header-right">
        <el-button size="small" @click="handleFormat">
          <el-icon><Magic /></el-icon>
          格式化
        </el-button>
        <el-button size="small" @click="handleClear">
          <el-icon><Delete /></el-icon>
          清空
        </el-button>
      </div>
    </div>
    
    <div class="editor-container">
      <textarea
        ref="textareaRef"
        v-model="code"
        class="code-textarea"
        :placeholder="placeholder"
        @input="handleInput"
        @keydown="handleKeydown"
      />
      <pre class="code-highlight" v-html="highlightedCode"></pre>
    </div>
    
    <div class="editor-footer">
      <div class="footer-left">
        <span class="line-info">行数: {{ lineCount }}</span>
        <span class="char-info">字符数: {{ charCount }}</span>
      </div>
      <div class="footer-right">
        <el-button size="small" @click="handleSave">
          <el-icon><Document /></el-icon>
          保存
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'

interface Props {
  modelValue: string
  language?: string
  placeholder?: string
  readonly?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  language: 'javascript',
  placeholder: '请输入代码...',
  readonly: false
})

const emit = defineEmits<{
  'update:modelValue': [value: string]
  save: [code: string]
  change: [code: string]
}>()

const textareaRef = ref<HTMLTextAreaElement>()
const code = ref(props.modelValue)

const lineCount = computed(() => {
  return code.value.split('\n').length
})

const charCount = computed(() => {
  return code.value.length
})

const highlightedCode = computed(() => {
  // 简单的代码高亮实现
  return highlightCode(code.value, props.language)
})

const highlightCode = (code: string, language: string): string => {
  if (!code) return ''
  
  // 简单的关键词高亮（实际项目中应该使用专业的代码高亮库）
  const keywords = {
    javascript: ['function', 'const', 'let', 'var', 'return', 'if', 'else', 'for', 'while', 'class', 'import', 'export'],
    python: ['def', 'class', 'import', 'from', 'if', 'else', 'for', 'while', 'return', 'True', 'False', 'None']
  }
  
  const langKeywords = keywords[language as keyof typeof keywords] || []
  
  let highlighted = code
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;')
  
  // 高亮关键词
  langKeywords.forEach(keyword => {
    const regex = new RegExp(`\\b${keyword}\\b`, 'g')
    highlighted = highlighted.replace(regex, `<span class="keyword">${keyword}</span>`)
  })
  
  // 高亮字符串
  highlighted = highlighted.replace(/('.*?'|".*?")/g, '<span class="string">$1</span>')
  
  // 高亮注释
  if (language === 'javascript') {
    highlighted = highlighted.replace(/\/\/.*$/gm, '<span class="comment">$&</span>')
    highlighted = highlighted.replace(/\/\*[\s\S]*?\*\//g, '<span class="comment">$&</span>')
  } else if (language === 'python') {
    highlighted = highlighted.replace(/#.*$/gm, '<span class="comment">$&</span>')
  }
  
  // 高亮数字
  highlighted = highlighted.replace(/\b\d+\b/g, '<span class="number">$&</span>')
  
  return highlighted
}

const handleInput = () => {
  emit('update:modelValue', code.value)
  emit('change', code.value)
}

const handleKeydown = (event: KeyboardEvent) => {
  if (event.key === 'Tab') {
    event.preventDefault()
    const start = textareaRef.value!.selectionStart
    const end = textareaRef.value!.selectionEnd
    
    const newCode = code.value.substring(0, start) + '  ' + code.value.substring(end)
    code.value = newCode
    
    nextTick(() => {
      textareaRef.value!.selectionStart = textareaRef.value!.selectionEnd = start + 2
    })
    
    handleInput()
  }
}

const handleFormat = () => {
  // 简单的格式化实现
  const formatted = code.value
    .replace(/\n\s*\n/g, '\n\n') // 移除多余的空行
    .replace(/\s+$/gm, '') // 移除行尾空格
    .replace(/^\s+/gm, '  ') // 统一缩进为2个空格
  
  code.value = formatted
  handleInput()
  ElMessage.success('格式化完成')
}

const handleClear = () => {
  code.value = ''
  handleInput()
  ElMessage.info('已清空代码')
}

const handleSave = () => {
  emit('save', code.value)
  ElMessage.success('代码已保存')
}

watch(() => props.modelValue, (newValue) => {
  if (newValue !== code.value) {
    code.value = newValue
  }
})
</script>

<style scoped lang="scss">
.code-editor {
  border: 1px solid v-bind('$border-color-base');
  border-radius: v-bind('$border-radius-base');
  background: v-bind('$bg-color-light');
  
  .editor-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 16px;
    border-bottom: 1px solid v-bind('$border-color-light');
    background: v-bind('$bg-color-lighter');
    
    .header-left {
      display: flex;
      align-items: center;
      gap: 12px;
      
      .editor-title {
        font-weight: 600;
        color: v-bind('$text-color-primary');
      }
    }
  }
  
  .editor-container {
    position: relative;
    height: 400px;
    overflow: auto;
    
    .code-textarea {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      padding: 16px;
      border: none;
      background: transparent;
      color: transparent;
      caret-color: v-bind('$text-color-primary');
      font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
      font-size: 14px;
      line-height: 1.5;
      resize: none;
      outline: none;
      tab-size: 2;
      
      &::placeholder {
        color: v-bind('$text-color-placeholder');
      }
    }
    
    .code-highlight {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      padding: 16px;
      margin: 0;
      border: none;
      background: transparent;
      font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
      font-size: 14px;
      line-height: 1.5;
      white-space: pre-wrap;
      word-wrap: break-word;
      pointer-events: none;
      
      .keyword {
        color: v-bind('$primary-color');
        font-weight: 600;
      }
      
      .string {
        color: v-bind('$success-color');
      }
      
      .comment {
        color: v-bind('$text-color-secondary');
        font-style: italic;
      }
      
      .number {
        color: v-bind('$warning-color');
      }
    }
  }
  
  .editor-footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 8px 16px;
    border-top: 1px solid v-bind('$border-color-light');
    background: v-bind('$bg-color-lighter');
    
    .footer-left {
      display: flex;
      gap: 16px;
      font-size: 12px;
      color: v-bind('$text-color-secondary');
    }
  }
}
</style>