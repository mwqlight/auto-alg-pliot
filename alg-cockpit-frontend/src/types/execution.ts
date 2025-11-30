export interface ExecuteRequest {
  algorithmId: number
  language: string
  code: string
  inputData?: string
  timeout?: number
  memoryLimit?: number
}

export interface ExecuteResponse {
  success: boolean
  output?: string
  error?: string
  executionTime?: number
  memoryUsed?: number
  exitCode?: number
  timeout?: boolean
  memoryExceeded?: boolean
  status: string
  executionId?: string
}

export interface ExecutionStatistics {
  totalExecutions: number
  averageExecutionTime: number
  successRate: number
  averageMemoryUsage: number
}