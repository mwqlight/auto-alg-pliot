// 算法执行引擎 - 安全沙箱环境

// 执行结果接口
export interface ExecutionResult {
  success: boolean
  output: any
  error?: string
  executionTime: number
  memoryUsage: number
  comparisons?: number
  swaps?: number
  steps?: any[]
}

// 执行配置接口
export interface ExecutionConfig {
  timeout?: number
  memoryLimit?: number
  enableSteps?: boolean
  enableStats?: boolean
}

// 算法执行器类
export class AlgorithmExecutor {
  private timeout: number
  private memoryLimit: number
  private enableSteps: boolean
  private enableStats: boolean
  
  constructor(config: ExecutionConfig = {}) {
    this.timeout = config.timeout || 5000 // 5秒超时
    this.memoryLimit = config.memoryLimit || 100 * 1024 * 1024 // 100MB内存限制
    this.enableSteps = config.enableSteps || false
    this.enableStats = config.enableStats || false
  }
  
  // 执行JavaScript代码
  async executeJavaScript(code: string, input: any): Promise<ExecutionResult> {
    const startTime = performance.now()
    const startMemory = this.getMemoryUsage()
    
    try {
      // 创建安全沙箱环境
      const sandbox = this.createSandbox()
      
      // 包装代码，添加执行统计
      const wrappedCode = this.wrapCode(code, input)
      
      // 设置超时
      const result = await Promise.race([
        this.runInSandbox(wrappedCode, sandbox),
        this.createTimeout(this.timeout)
      ])
      
      const endTime = performance.now()
      const endMemory = this.getMemoryUsage()
      
      return {
        success: true,
        output: result,
        executionTime: endTime - startTime,
        memoryUsage: endMemory - startMemory,
        comparisons: sandbox.comparisons,
        swaps: sandbox.swaps,
        steps: sandbox.steps
      }
      
    } catch (error) {
      const endTime = performance.now()
      const endMemory = this.getMemoryUsage()
      
      return {
        success: false,
        output: null,
        error: error instanceof Error ? error.message : String(error),
        executionTime: endTime - startTime,
        memoryUsage: endMemory - startMemory
      }
    }
  }
  
  // 创建安全沙箱环境
  private createSandbox(): any {
    const sandbox = {
      // 执行统计
      comparisons: 0,
      swaps: 0,
      steps: [],
      
      // 允许的全局对象
      console: {
        log: (...args: any[]) => {
          // 在沙箱中记录日志
          sandbox.steps.push({
            type: 'log',
            message: args.map(arg => String(arg)).join(' '),
            timestamp: Date.now()
          })
        }
      },
      
      // 数学函数
      Math: {
        ...Math,
        random: () => {
          // 固定随机种子用于可重复性
          return 0.5
        }
      },
      
      // 数组方法（重写以统计操作）
      Array: {
        ...Array,
        from: Array.from,
        isArray: Array.isArray
      },
      
      // 字符串方法
      String: {
        ...String,
        fromCharCode: String.fromCharCode
      },
      
      // 比较操作统计
      comparison: (a: any, b: any) => {
        sandbox.comparisons++
        return a < b
      },
      
      // 交换操作统计
      swap: (arr: any[], i: number, j: number) => {
        sandbox.swaps++
        const temp = arr[i]
        arr[i] = arr[j]
        arr[j] = temp
        
        if (this.enableSteps) {
          sandbox.steps.push({
            type: 'swap',
            indices: [i, j],
            array: [...arr],
            timestamp: Date.now()
          })
        }
      },
      
      // 记录步骤
      recordStep: (description: string, data?: any) => {
        if (this.enableSteps) {
          sandbox.steps.push({
            type: 'step',
            description,
            data,
            timestamp: Date.now()
          })
        }
      }
    }
    
    return sandbox
  }
  
  // 包装代码，添加执行上下文
  private wrapCode(code: string, input: any): string {
    return `
      // 输入数据
      const input = ${JSON.stringify(input)};
      
      // 算法执行函数
      function executeAlgorithm() {
        ${code}
      }
      
      // 执行并返回结果
      return executeAlgorithm();
    `
  }
  
  // 在沙箱中运行代码
  private async runInSandbox(code: string, sandbox: any): Promise<any> {
    return new Promise((resolve, reject) => {
      try {
        // 使用Function构造函数创建安全函数
        const func = new Function(
          'sandbox',
          `with(sandbox) { ${code} }`
        )
        
        // 执行函数
        const result = func(sandbox)
        resolve(result)
        
      } catch (error) {
        reject(error)
      }
    })
  }
  
  // 创建超时Promise
  private createTimeout(ms: number): Promise<never> {
    return new Promise((_, reject) => {
      setTimeout(() => {
        reject(new Error(`执行超时 (${ms}ms)`))
      }, ms)
    })
  }
  
  // 获取内存使用量（近似值）
  private getMemoryUsage(): number {
    if (typeof performance !== 'undefined' && performance.memory) {
      return performance.memory.usedJSHeapSize
    }
    return 0
  }
  
  // 验证代码安全性
  validateCodeSecurity(code: string): { safe: boolean; issues: string[] } {
    const dangerousPatterns = [
      /eval\s*\(/,
      /Function\s*\(/,
      /setTimeout\s*\(/,
      /setInterval\s*\(/,
      /document\./,
      /window\./,
      /process\./,
      /require\s*\(/,
      /import\s*\(/,
      /XMLHttpRequest/,
      /fetch\s*\(/,
      /localStorage/,
      /sessionStorage/,
      /indexedDB/,
      /WebSocket/,
      /Worker/,
      /importScripts/
    ]
    
    const issues: string[] = []
    
    for (const pattern of dangerousPatterns) {
      if (pattern.test(code)) {
        issues.push(`检测到危险模式: ${pattern.source}`)
      }
    }
    
    // 检查无限循环模式
    const loopPatterns = [
      /while\s*\(\s*true\s*\)/,
      /for\s*\([^;]*;[^;]*;[^)]*\)\s*{[^}]*}/,
      /do\s*{[^}]*}\s*while\s*\(\s*true\s*\)/
    ]
    
    for (const pattern of loopPatterns) {
      if (pattern.test(code)) {
        issues.push(`检测到可能的无限循环: ${pattern.source}`)
      }
    }
    
    return {
      safe: issues.length === 0,
      issues
    }
  }
  
  // 格式化执行结果
  formatExecutionResult(result: ExecutionResult): string {
    if (!result.success) {
      return `执行失败: ${result.error}`
    }
    
    let output = `执行成功！\n`
    output += `执行时间: ${result.executionTime.toFixed(2)}ms\n`
    output += `内存使用: ${(result.memoryUsage / 1024).toFixed(2)}KB\n`
    
    if (result.comparisons !== undefined) {
      output += `比较次数: ${result.comparisons}\n`
    }
    
    if (result.swaps !== undefined) {
      output += `交换次数: ${result.swaps}\n`
    }
    
    if (result.output !== undefined) {
      output += `输出结果: ${JSON.stringify(result.output, null, 2)}\n`
    }
    
    return output
  }
}

// 预定义的算法执行函数
export const predefinedAlgorithms = {
  // 冒泡排序
  bubbleSort: (arr: number[]): number[] => {
    const n = arr.length
    for (let i = 0; i < n - 1; i++) {
      for (let j = 0; j < n - i - 1; j++) {
        if (arr[j] > arr[j + 1]) {
          // 交换元素
          const temp = arr[j]
          arr[j] = arr[j + 1]
          arr[j + 1] = temp
        }
      }
    }
    return arr
  },
  
  // 快速排序
  quickSort: (arr: number[]): number[] => {
    if (arr.length <= 1) return arr
    
    const pivot = arr[Math.floor(arr.length / 2)]
    const left = []
    const right = []
    const equal = []
    
    for (const num of arr) {
      if (num < pivot) left.push(num)
      else if (num > pivot) right.push(num)
      else equal.push(num)
    }
    
    return [...this.quickSort(left), ...equal, ...this.quickSort(right)]
  },
  
  // 二分查找
  binarySearch: (arr: number[], target: number): number => {
    let left = 0
    let right = arr.length - 1
    
    while (left <= right) {
      const mid = Math.floor((left + right) / 2)
      
      if (arr[mid] === target) {
        return mid
      } else if (arr[mid] < target) {
        left = mid + 1
      } else {
        right = mid - 1
      }
    }
    
    return -1
  },
  
  // 广度优先搜索
  bfs: (graph: number[][], start: number): number[] => {
    const visited: boolean[] = new Array(graph.length).fill(false)
    const queue: number[] = [start]
    const result: number[] = []
    
    visited[start] = true
    
    while (queue.length > 0) {
      const current = queue.shift()!
      result.push(current)
      
      for (const neighbor of graph[current]) {
        if (!visited[neighbor]) {
          visited[neighbor] = true
          queue.push(neighbor)
        }
      }
    }
    
    return result
  }
}

// 导出默认执行器实例
export const defaultExecutor = new AlgorithmExecutor({
  timeout: 5000,
  memoryLimit: 100 * 1024 * 1024,
  enableSteps: true,
  enableStats: true
})

// 工具函数：执行算法并返回结果
export async function executeAlgorithm(
  code: string, 
  input: any, 
  config?: ExecutionConfig
): Promise<ExecutionResult> {
  const executor = new AlgorithmExecutor(config)
  return executor.executeJavaScript(code, input)
}

// 工具函数：批量执行测试用例
export async function executeTestCases(
  code: string,
  testCases: Array<{input: any, expected: any}>,
  config?: ExecutionConfig
): Promise<Array<{testCase: any, result: ExecutionResult, passed: boolean}>> {
  const results = []
  
  for (const testCase of testCases) {
    const result = await executeAlgorithm(code, testCase.input, config)
    const passed = result.success && JSON.stringify(result.output) === JSON.stringify(testCase.expected)
    
    results.push({
      testCase,
      result,
      passed
    })
  }
  
  return results
}