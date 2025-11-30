// 算法数据定义和演示数据

// 算法分类定义
export interface AlgorithmCategory {
  id: string
  name: string
  description: string
  icon: string
  color: string
  count: number
}

export interface Algorithm {
  id: string
  name: string
  description: string
  category: string
  timeComplexity: string
  spaceComplexity: string
  difficulty: 'easy' | 'medium' | 'hard'
  tags: string[]
  implementations: AlgorithmImplementation[]
  testCases: TestCase[]
  visualizationType: 'bar' | 'graph' | 'path'
}

export interface AlgorithmImplementation {
  language: 'java' | 'python' | 'javascript' | 'cpp'
  code: string
  description: string
}

export interface TestCase {
  id: string
  input: any
  expectedOutput: any
  description: string
}

// 算法分类数据
export const algorithmCategories: AlgorithmCategory[] = [
  {
    id: 'sorting',
    name: '排序算法',
    description: '将一组数据按照特定顺序进行排列的算法',
    icon: '📊',
    color: '#409EFF',
    count: 6
  },
  {
    id: 'searching',
    name: '搜索算法',
    description: '在数据集中查找特定元素的算法',
    icon: '🔍',
    color: '#67C23A',
    count: 4
  },
  {
    id: 'graph',
    name: '图算法',
    description: '处理图结构数据的算法',
    icon: '🕸️',
    color: '#E6A23C',
    count: 5
  },
  {
    id: 'dynamic-programming',
    name: '动态规划',
    description: '通过将问题分解为子问题来求解的算法',
    icon: '🧩',
    color: '#F56C6C',
    count: 4
  },
  {
    id: 'greedy',
    name: '贪心算法',
    description: '在每一步选择中都采取当前状态下最优的选择',
    icon: '💰',
    color: '#909399',
    count: 3
  },
  {
    id: 'backtracking',
    name: '回溯算法',
    description: '通过尝试所有可能的解来求解问题的算法',
    icon: '🔄',
    color: '#8B5CF6',
    count: 3
  }
]

// 算法数据
export const algorithms: Algorithm[] = [
  // 排序算法
  {
    id: 'bubble-sort',
    name: '冒泡排序',
    description: '重复遍历要排序的数列，一次比较两个元素，如果顺序错误就交换它们。',
    category: 'sorting',
    timeComplexity: 'O(n²)',
    spaceComplexity: 'O(1)',
    difficulty: 'easy',
    tags: ['稳定排序', '原地排序', '教学演示'],
    visualizationType: 'bar',
    implementations: [
      {
        language: 'java',
        description: 'Java实现 - 基础版本',
        code: `public class BubbleSort {
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // 交换元素
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
    
    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        bubbleSort(arr);
        System.out.println("排序后的数组:");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
}`
      },
      {
        language: 'python',
        description: 'Python实现 - 优化版本',
        code: `def bubble_sort(arr):
    n = len(arr)
    # 遍历所有数组元素
    for i in range(n):
        swapped = False
        # 最后i个元素已经排好序
        for j in range(0, n - i - 1):
            # 如果当前元素大于下一个元素，则交换
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]
                swapped = True
        # 如果没有发生交换，说明数组已经有序
        if not swapped:
            break
    return arr

# 测试
if __name__ == "__main__":
    arr = [64, 34, 25, 12, 22, 11, 90]
    print("原始数组:", arr)
    bubble_sort(arr)
    print("排序后的数组:", arr)`
      }
    ],
    testCases: [
      {
        id: 'tc1',
        input: [5, 2, 8, 1, 9],
        expectedOutput: [1, 2, 5, 8, 9],
        description: '基本测试 - 随机数组'
      },
      {
        id: 'tc2',
        input: [1, 2, 3, 4, 5],
        expectedOutput: [1, 2, 3, 4, 5],
        description: '已排序数组'
      },
      {
        id: 'tc3',
        input: [5, 4, 3, 2, 1],
        expectedOutput: [1, 2, 3, 4, 5],
        description: '逆序数组'
      }
    ]
  },
  
  {
    id: 'quick-sort',
    name: '快速排序',
    description: '使用分治策略的排序算法，通过一趟排序将待排记录分隔成独立的两部分。',
    category: 'sorting',
    timeComplexity: 'O(n log n)',
    spaceComplexity: 'O(log n)',
    difficulty: 'medium',
    tags: ['分治算法', '高效排序', '不稳定排序'],
    visualizationType: 'bar',
    implementations: [
      {
        language: 'java',
        description: 'Java实现 - 递归版本',
        code: `public class QuickSort {
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // 找到分区点
            int pi = partition(arr, low, high);
            
            // 递归排序分区
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }
    
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                // 交换元素
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        
        // 将基准元素放到正确位置
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        
        return i + 1;
    }
    
    public static void main(String[] args) {
        int[] arr = {10, 7, 8, 9, 1, 5};
        quickSort(arr, 0, arr.length - 1);
        System.out.println("排序后的数组:");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
}`
      }
    ],
    testCases: [
      {
        id: 'tc1',
        input: [10, 7, 8, 9, 1, 5],
        expectedOutput: [1, 5, 7, 8, 9, 10],
        description: '基本测试'
      }
    ]
  },
  
  {
    id: 'merge-sort',
    name: '归并排序',
    description: '建立在归并操作上的一种有效的排序算法，采用分治法的一个非常典型的应用。',
    category: 'sorting',
    timeComplexity: 'O(n log n)',
    spaceComplexity: 'O(n)',
    difficulty: 'medium',
    tags: ['分治算法', '稳定排序', '外部排序'],
    visualizationType: 'bar',
    implementations: [
      {
        language: 'java',
        description: 'Java实现',
        code: `public class MergeSort {
    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            
            // 递归排序左右两部分
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            
            // 合并两个有序数组
            merge(arr, left, mid, right);
        }
    }
    
    private static void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        // 创建临时数组
        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];
        
        // 复制数据到临时数组
        for (int i = 0; i < n1; i++) {
            leftArr[i] = arr[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArr[j] = arr[mid + 1 + j];
        }
        
        // 合并临时数组
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }
        
        // 复制剩余元素
        while (i < n1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }
        while (j < n2) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
    }
    
    public static void main(String[] args) {
        int[] arr = {12, 11, 13, 5, 6, 7};
        mergeSort(arr, 0, arr.length - 1);
        System.out.println("排序后的数组:");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
}`
      }
    ],
    testCases: [
      {
        id: 'tc1',
        input: [12, 11, 13, 5, 6, 7],
        expectedOutput: [5, 6, 7, 11, 12, 13],
        description: '基本测试'
      }
    ]
  },
  
  // 搜索算法
  {
    id: 'binary-search',
    name: '二分查找',
    description: '在有序数组中查找某一特定元素的搜索算法，搜索过程从数组的中间元素开始。',
    category: 'searching',
    timeComplexity: 'O(log n)',
    spaceComplexity: 'O(1)',
    difficulty: 'easy',
    tags: ['有序数组', '高效查找', '分治算法'],
    visualizationType: 'path',
    implementations: [
      {
        language: 'java',
        description: 'Java实现 - 迭代版本',
        code: `public class BinarySearch {
    public static int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            // 检查目标是否在中间
            if (arr[mid] == target) {
                return mid;
            }
            
            // 如果目标更大，忽略左半部分
            if (arr[mid] < target) {
                left = mid + 1;
            } else { // 如果目标更小，忽略右半部分
                right = mid - 1;
            }
        }
        
        // 目标不存在于数组中
        return -1;
    }
    
    public static void main(String[] args) {
        int[] arr = {2, 3, 4, 10, 40};
        int target = 10;
        int result = binarySearch(arr, target);
        
        if (result == -1) {
            System.out.println("元素不存在于数组中");
        } else {
            System.out.println("元素找到，索引位置: " + result);
        }
    }
}`
      }
    ],
    testCases: [
      {
        id: 'tc1',
        input: { array: [1, 3, 5, 7, 9], target: 5 },
        expectedOutput: 2,
        description: '查找中间元素'
      },
      {
        id: 'tc2',
        input: { array: [1, 3, 5, 7, 9], target: 2 },
        expectedOutput: -1,
        description: '查找不存在的元素'
      }
    ]
  },
  
  // 图算法
  {
    id: 'bfs',
    name: '广度优先搜索',
    description: '一种图形搜索算法，从根节点开始，沿着树的宽度遍历树的节点，直到找到目标节点。',
    category: 'graph',
    timeComplexity: 'O(V+E)',
    spaceComplexity: 'O(V)',
    difficulty: 'medium',
    tags: ['图遍历', '最短路径', '队列'],
    visualizationType: 'graph',
    implementations: [
      {
        language: 'java',
        description: 'Java实现 - 邻接表表示',
        code: `import java.util.*;

public class BFS {
    private int V; // 顶点数量
    private LinkedList<Integer> adj[]; // 邻接表
    
    public BFS(int v) {
        V = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; i++) {
            adj[i] = new LinkedList<>();
        }
    }
    
    // 添加边
    public void addEdge(int v, int w) {
        adj[v].add(w);
    }
    
    // BFS遍历
    public void bfs(int start) {
        boolean[] visited = new boolean[V];
        LinkedList<Integer> queue = new LinkedList<>();
        
        visited[start] = true;
        queue.add(start);
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            System.out.print(current + " ");
            
            // 获取所有邻接顶点
            Iterator<Integer> i = adj[current].listIterator();
            while (i.hasNext()) {
                int n = i.next();
                if (!visited[n]) {
                    visited[n] = true;
                    queue.add(n);
                }
            }
        }
    }
    
    public static void main(String[] args) {
        BFS graph = new BFS(4);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);
        graph.addEdge(2, 3);
        graph.addEdge(3, 3);
        
        System.out.println("从顶点2开始的广度优先遍历:");
        graph.bfs(2);
    }
}`
      }
    ],
    testCases: [
      {
        id: 'tc1',
        input: { 
          vertices: 4, 
          edges: [[0,1], [0,2], [1,2], [2,0], [2,3], [3,3]],
          start: 2 
        },
        expectedOutput: [2, 0, 3, 1],
        description: '基本图遍历'
      }
    ]
  },
  
  {
    id: 'dijkstra',
    name: '迪杰斯特拉算法',
    description: '解决带权有向图中单源最短路径问题的贪心算法。',
    category: 'graph',
    timeComplexity: 'O(V²)',
    spaceComplexity: 'O(V)',
    difficulty: 'hard',
    tags: ['最短路径', '带权图', '贪心算法'],
    visualizationType: 'graph',
    implementations: [
      {
        language: 'java',
        description: 'Java实现',
        code: `import java.util.*;

public class Dijkstra {
    private static final int INF = Integer.MAX_VALUE;
    
    public static void dijkstra(int[][] graph, int source) {
        int V = graph.length;
        int[] dist = new int[V]; // 最短距离数组
        boolean[] visited = new boolean[V]; // 访问标记数组
        
        // 初始化距离数组
        Arrays.fill(dist, INF);
        dist[source] = 0;
        
        // 找到所有顶点的最短路径
        for (int count = 0; count < V - 1; count++) {
            // 选择未访问顶点中距离最小的顶点
            int u = minDistance(dist, visited);
            visited[u] = true;
            
            // 更新相邻顶点的距离
            for (int v = 0; v < V; v++) {
                if (!visited[v] && graph[u][v] != 0 && 
                    dist[u] != INF && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                }
            }
        }
        
        // 打印结果
        printSolution(dist, source);
    }
    
    private static int minDistance(int[] dist, boolean[] visited) {
        int min = INF, minIndex = -1;
        
        for (int v = 0; v < dist.length; v++) {
            if (!visited[v] && dist[v] <= min) {
                min = dist[v];
                minIndex = v;
            }
        }
        
        return minIndex;
    }
    
    private static void printSolution(int[] dist, int source) {
        System.out.println("顶点 \t 距离源点" + source + "的距离");
        for (int i = 0; i < dist.length; i++) {
            System.out.println(i + " \t\t " + dist[i]);
        }
    }
    
    public static void main(String[] args) {
        int[][] graph = {
            {0, 4, 0, 0, 0, 0, 0, 8, 0},
            {4, 0, 8, 0, 0, 0, 0, 11, 0},
            {0, 8, 0, 7, 0, 4, 0, 0, 2},
            {0, 0, 7, 0, 9, 14, 0, 0, 0},
            {0, 0, 0, 9, 0, 10, 0, 0, 0},
            {0, 0, 4, 14, 10, 0, 2, 0, 0},
            {0, 0, 0, 0, 0, 2, 0, 1, 6},
            {8, 11, 0, 0, 0, 0, 1, 0, 7},
            {0, 0, 2, 0, 0, 0, 6, 7, 0}
        };
        
        dijkstra(graph, 0);
    }
}`
      }
    ],
    testCases: [
      {
        id: 'tc1',
        input: {
          graph: [
            [0, 4, 0, 0, 0, 0, 0, 8, 0],
            [4, 0, 8, 0, 0, 0, 0, 11, 0],
            [0, 8, 0, 7, 0, 4, 0, 0, 2],
            [0, 0, 7, 0, 9, 14, 0, 0, 0],
            [0, 0, 0, 9, 0, 10, 0, 0, 0],
            [0, 0, 4, 14, 10, 0, 2, 0, 0],
            [0, 0, 0, 0, 0, 2, 0, 1, 6],
            [8, 11, 0, 0, 0, 0, 1, 0, 7],
            [0, 0, 2, 0, 0, 0, 6, 7, 0]
          ],
          source: 0
        },
        expectedOutput: [0, 4, 12, 19, 21, 11, 9, 8, 14],
        description: '经典9顶点图测试'
      }
    ]
  }
]

// 获取算法分类的函数
export function getCategoryById(categoryId: string): AlgorithmCategory | undefined {
  return algorithmCategories.find(cat => cat.id === categoryId)
}

// 获取分类下的所有算法
export function getAlgorithmsByCategory(categoryId: string): Algorithm[] {
  return algorithms.filter(algo => algo.category === categoryId)
}

// 根据ID获取算法
export function getAlgorithmById(algorithmId: string): Algorithm | undefined {
  return algorithms.find(algo => algo.id === algorithmId)
}

// 获取所有算法
export function getAllAlgorithms(): Algorithm[] {
  return algorithms
}

// 获取所有分类
export function getAllCategories(): AlgorithmCategory[] {
  return algorithmCategories
}

// 搜索算法
export function searchAlgorithms(query: string): Algorithm[] {
  const lowerQuery = query.toLowerCase()
  return algorithms.filter(algo => 
    algo.name.toLowerCase().includes(lowerQuery) ||
    algo.description.toLowerCase().includes(lowerQuery) ||
    algo.tags.some(tag => tag.toLowerCase().includes(lowerQuery))
  )
}

// 获取算法的测试用例
export function getTestCases(algorithmId: string): TestCase[] {
  const algorithm = getAlgorithmById(algorithmId)
  return algorithm ? algorithm.testCases : []
}

// 获取算法的实现代码
export function getImplementations(algorithmId: string): AlgorithmImplementation[] {
  const algorithm = getAlgorithmById(algorithmId)
  return algorithm ? algorithm.implementations : []
}

// 获取特定语言的实现代码
export function getImplementationByLanguage(algorithmId: string, language: string): AlgorithmImplementation | undefined {
  const implementations = getImplementations(algorithmId)
  return implementations.find(impl => impl.language === language)
}