export interface AlgorithmCategory {
  id: number
  name: string
  description?: string
  icon?: string
  sort: number
  createTime: string
  updateTime: string
}

export interface Algorithm {
  id: number
  name: string
  description?: string
  categoryId: number
  categoryName: string
  difficulty: 'easy' | 'medium' | 'hard'
  timeComplexity: string
  spaceComplexity: string
  tags: string[]
  codeTemplate: Record<string, string> // 语言 -> 代码模板
  testCases: TestCase[]
  createTime: string
  updateTime: string
}

export interface AlgorithmCode {
  id: number
  algorithmId: number
  language: string
  code: string
  description?: string
  createTime: string
  updateTime: string
}

export interface TestCase {
  input: string
  expectedOutput: string
  description?: string
}

export interface AlgorithmComplexity {
  algorithmId: number
  bestCase: string
  averageCase: string
  worstCase: string
  spaceComplexity: string
  explanation: string
}