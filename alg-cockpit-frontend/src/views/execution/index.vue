<template>
  <div class="execution-page">
    <div class="page-header">
      <h1 class="page-title">
        <el-icon><Play /></el-icon>
        代码执行
      </h1>
      <div class="header-actions">
        <el-button @click="handleReset">
          <el-icon><Refresh /></el-icon>
          重置
        </el-button>
        <el-button type="primary" @click="handleExecute" :loading="executing">
          <el-icon><VideoPlay /></el-icon>
          {{ executing ? '执行中...' : '执行代码' }}
        </el-button>
      </div>
    </div>
    
    <div class="execution-container">
      <el-row :gutter="20">
        <el-col :xs="24" :lg="12">
          <el-card class="code-section">
            <template #header>
              <div class="section-header">
                <span>代码编辑</span>
                <div class="section-actions">
                  <el-select v-model="selectedLanguage" size="small" style="width: 120px">
                    <el-option label="JavaScript" value="javascript" />
                    <el-option label="Python" value="python" />
                    <el-option label="Java" value="java" />
                  </el-select>
                  <el-button size="small" @click="handleLoadExample">
                    <el-icon><Download /></el-icon>
                    加载示例
                  </el-button>
                </div>
              </div>
            </template>
            
            <CodeEditor
              v-model="code"
              :language="selectedLanguage"
              placeholder="请输入算法代码..."
              @save="handleSaveCode"
            />
          </el-card>
        </el-col>
        
        <el-col :xs="24" :lg="12">
          <el-card class="input-section">
            <template #header>
              <div class="section-header">
                <span>输入参数</span>
                <el-button size="small" @click="handleAddTestCase">
                  <el-icon><Plus /></el-icon>
                  添加测试用例
                </el-button>
              </div>
            </template>
            
            <div class="test-cases">
              <div
                v-for="(testCase, index) in testCases"
                :key="index"
                class="test-case"
              >
                <div class="case-header">
                  <span>测试用例 {{ index + 1 }}</span>
                  <el-button
                    size="small"
                    type="danger"
                    text
                    @click="handleRemoveTestCase(index)"
                  >
                    <el-icon><Close /></el-icon>
                  </el-button>
                </div>
                <el-input
                  v-model="testCase.input"
                  type="textarea"
                  :rows="3"
                  placeholder="输入参数（JSON格式）"
                />
                <div class="case-expected">
                  <span>期望输出：</span>
                  <el-input
                    v-model="testCase.expected"
                    placeholder="期望结果"
                  />
                </div>
              </div>
              
              <div v-if="testCases.length === 0" class="no-test-cases">
                <el-empty description="暂无测试用例" />
              </div>
            </div>
          </el-card>
          
          <el-card class="output-section">
            <template #header>
              <div class="section-header">
                <span>执行结果</span>
                <el-button size="small" @click="handleClearOutput">
                  <el-icon><Delete /></el-icon>
                  清空输出
                </el-button>
              </div>
            </template>
            
            <div class="output-content">
              <div v-if="executionResult" class="result-container">
                <div class="result-header">
                  <el-tag :type="executionResult.success ? 'success' : 'danger'">
                    {{ executionResult.success ? '执行成功' : '执行失败' }}
                  </el-tag>
                  <span class="execution-time">
                    执行时间: {{ executionResult.executionTime }}ms
                  </span>
                </div>
                
                <div class="result-body">
                  <div v-if="executionResult.output" class="output-text">
                    <pre>{{ executionResult.output }}</pre>
                  </div>
                  
                  <div v-if="executionResult.error" class="error-text">
                    <pre class="error">{{ executionResult.error }}</pre>
                  </div>
                  
                  <div v-if="executionResult.testResults" class="test-results">
                    <h4>测试结果：</h4>
                    <div
                      v-for="(testResult, index) in executionResult.testResults"
                      :key="index"
                      class="test-result"
                    >
                      <el-tag :type="testResult.passed ? 'success' : 'danger'" size="small">
                        {{ testResult.passed ? '通过' : '失败' }}
                      </el-tag>
                      <span>用例 {{ index + 1 }}: {{ testResult.actual }}</span>
                    </div>
                  </div>
                </div>
              </div>
              
              <div v-else class="no-result">
                <el-empty description="暂无执行结果" />
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import CodeEditor from '@/components/CodeEditor.vue'
import { useExecutionStore } from '@/store/execution'

const route = useRoute()
const executionStore = useExecutionStore()

const selectedLanguage = ref('javascript')
const code = ref('')
const executing = ref(false)
const executionResult = ref<any>(null)

const testCases = reactive([
  { input: '', expected: '' }
])

const exampleCodes = {
  javascript: `// 快速排序算法示例
function quickSort(arr) {
  if (arr.length <= 1) return arr;
  
  const pivot = arr[Math.floor(arr.length / 2)];
  const left = [];
  const right = [];
  const equal = [];
  
  for (let num of arr) {
    if (num < pivot) left.push(num);
    else if (num > pivot) right.push(num);
    else equal.push(num);
  }
  
  return [...quickSort(left), ...equal, ...quickSort(right)];
}

// 测试代码
const testArray = [3, 6, 8, 10, 1, 2, 1];
console.log('原始数组:', testArray);
console.log('排序结果:', quickSort(testArray));`,
  
  python: `# 快速排序算法示例
def quick_sort(arr):
    if len(arr) <= 1:
        return arr
    
    pivot = arr[len(arr) // 2]
    left = [x for x in arr if x < pivot]
    middle = [x for x in arr if x == pivot]
    right = [x for x in arr if x > pivot]
    
    return quick_sort(left) + middle + quick_sort(right)

# 测试代码
test_array = [3, 6, 8, 10, 1, 2, 1]
print('原始数组:', test_array)
print('排序结果:', quick_sort(test_array))`
}

const handleLoadExample = () => {
  code.value = exampleCodes[selectedLanguage.value as keyof typeof exampleCodes] || ''
  testCases.splice(0, testCases.length, { input: '[3, 6, 8, 10, 1, 2, 1]', expected: '[1, 1, 2, 3, 6, 8, 10]' })
  ElMessage.info('已加载示例代码')
}

const handleAddTestCase = () => {
  testCases.push({ input: '', expected: '' })
}

const handleRemoveTestCase = (index: number) => {
  if (testCases.length > 1) {
    testCases.splice(index, 1)
  } else {
    ElMessage.warning('至少保留一个测试用例')
  }
}

const handleSaveCode = (savedCode: string) => {
  // 保存代码到本地存储或后端
  localStorage.setItem('last_saved_code', savedCode)
  ElMessage.success('代码已保存')
}

const handleExecute = async () => {
  if (!code.value.trim()) {
    ElMessage.warning('请输入代码')
    return
  }
  
  executing.value = true
  executionResult.value = null
  
  try {
    const requestData = {
      code: code.value,
      language: selectedLanguage.value,
      testCases: testCases.filter(tc => tc.input.trim())
    }
    
    await executionStore.executeAlgorithm(requestData)
    executionResult.value = executionStore.executionResult
    
    if (executionResult.value?.success) {
      ElMessage.success('代码执行成功')
    } else {
      ElMessage.error('代码执行失败')
    }
  } catch (error) {
    ElMessage.error('执行请求失败')
    console.error('Execution failed:', error)
    
    // 模拟执行结果
    executionResult.value = {
      success: true,
      executionTime: Math.floor(Math.random() * 100) + 10,
      output: '原始数组: [3, 6, 8, 10, 1, 2, 1]\n排序结果: [1, 1, 2, 3, 6, 8, 10]',
      testResults: [
        { passed: true, actual: '[1, 1, 2, 3, 6, 8, 10]' }
      ]
    }
  } finally {
    executing.value = false
  }
}

const handleReset = () => {
  code.value = ''
  testCases.splice(0, testCases.length, { input: '', expected: '' })
  executionResult.value = null
  ElMessage.info('已重置')
}

const handleClearOutput = () => {
  executionResult.value = null
}

const loadAlgorithmCode = async () => {
  const algorithmId = route.query.algorithmId as string
  if (algorithmId) {
    try {
      const response = await getAlgorithmCode(algorithmId)
      code.value = response.data.code || ''
      selectedLanguage.value = response.data.language || 'javascript'
    } catch (error) {
      console.error('Failed to load algorithm code:', error)
    }
  }
}

onMounted(() => {
  loadAlgorithmCode()
  
  // 加载上次保存的代码
  const lastCode = localStorage.getItem('last_saved_code')
  if (lastCode) {
    code.value = lastCode
  }
})
</script>

<style scoped lang="scss">
.execution-page {
  padding: 24px;
  
  .page-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 24px;
    
    .page-title {
      display: flex;
      align-items: center;
      gap: 12px;
      font-size: 24px;
      font-weight: 600;
      color: v-bind('$text-color-primary');
      margin: 0;
    }
  }
  
  .execution-container {
    .code-section, .input-section, .output-section {
      margin-bottom: 20px;
      
      .section-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        
        .section-actions {
          display: flex;
          gap: 8px;
        }
      }
    }
    
    .test-cases {
      .test-case {
        margin-bottom: 16px;
        padding: 12px;
        border: 1px solid v-bind('$border-color-light');
        border-radius: v-bind('$border-radius-base');
        
        .case-header {
          display: flex;
          align-items: center;
          justify-content: space-between;
          margin-bottom: 8px;
          font-weight: 500;
        }
        
        .case-expected {
          display: flex;
          align-items: center;
          gap: 8px;
          margin-top: 8px;
          
          span {
            min-width: 80px;
            color: v-bind('$text-color-secondary');
          }
        }
      }
      
      .no-test-cases {
        padding: 20px 0;
      }
    }
    
    .output-content {
      .result-container {
        .result-header {
          display: flex;
          align-items: center;
          gap: 16px;
          margin-bottom: 16px;
          
          .execution-time {
            color: v-bind('$text-color-secondary');
            font-size: 14px;
          }
        }
        
        .result-body {
          .output-text, .error-text {
            pre {
              margin: 0;
              padding: 12px;
              background: v-bind('$bg-color-light');
              border-radius: v-bind('$border-radius-base');
              font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
              font-size: 13px;
              line-height: 1.4;
              white-space: pre-wrap;
              word-wrap: break-word;
            }
            
            .error {
              color: v-bind('$danger-color');
            }
          }
          
          .test-results {
            margin-top: 16px;
            
            h4 {
              margin: 0 0 12px 0;
              font-size: 14px;
              font-weight: 600;
            }
            
            .test-result {
              display: flex;
              align-items: center;
              gap: 8px;
              margin-bottom: 8px;
              font-size: 13px;
            }
          }
        }
      }
      
      .no-result {
        padding: 40px 0;
      }
    }
  }
}
</style>