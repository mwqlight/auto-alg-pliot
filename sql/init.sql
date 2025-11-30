-- 算法驾驶舱数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS alg_cockpit DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE alg_cockpit;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    email VARCHAR(100) UNIQUE COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像',
    role VARCHAR(20) DEFAULT 'USER' COMMENT '角色: ADMIN, USER',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 算法分类表
CREATE TABLE IF NOT EXISTS algorithm_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    description TEXT COMMENT '分类描述',
    parent_id BIGINT DEFAULT 0 COMMENT '父级分类ID',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='算法分类表';

-- 算法信息表
CREATE TABLE IF NOT EXISTS algorithm (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '算法ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    name VARCHAR(200) NOT NULL COMMENT '算法名称',
    description TEXT COMMENT '算法描述',
    algorithm_thought TEXT COMMENT '算法思想',
    application_scenarios TEXT COMMENT '应用场景',
    variants_optimizations TEXT COMMENT '变种与优化',
    thinking_questions TEXT COMMENT '思考题',
    difficulty_level TINYINT DEFAULT 1 COMMENT '难度等级: 1-简单, 2-中等, 3-困难',
    popularity INT DEFAULT 0 COMMENT '热度',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    code TEXT COMMENT '算法代码',
    language VARCHAR(50) DEFAULT 'java' COMMENT '算法语言',
    input_params TEXT COMMENT '输入参数说明',
    output_params TEXT COMMENT '输出结果说明',
    time_complexity VARCHAR(50) COMMENT '时间复杂度',
    space_complexity VARCHAR(50) COMMENT '空间复杂度',
    create_user_id BIGINT COMMENT '创建用户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    INDEX idx_category_id (category_id),
    INDEX idx_difficulty (difficulty_level),
    INDEX idx_popularity (popularity),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='算法信息表';

-- 算法执行记录表
CREATE TABLE IF NOT EXISTS algorithm_execution (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '执行记录ID',
    algorithm_id BIGINT NOT NULL COMMENT '算法ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    input_params TEXT COMMENT '输入参数',
    output_result TEXT COMMENT '输出结果',
    status TINYINT DEFAULT 0 COMMENT '执行状态: 0-执行中, 1-成功, 2-失败, 3-超时',
    execution_time BIGINT DEFAULT 0 COMMENT '执行时间(毫秒)',
    memory_usage BIGINT DEFAULT 0 COMMENT '内存使用(字节)',
    error_message TEXT COMMENT '错误信息',
    environment_info TEXT COMMENT '环境信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    INDEX idx_algorithm_id (algorithm_id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='算法执行记录表';

-- 插入初始数据
-- 插入默认管理员用户
INSERT IGNORE INTO user (id, username, password, email, nickname, role, status) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVwE.', 'admin@algcockpit.com', '系统管理员', 'ADMIN', 1);

-- 插入算法分类数据
INSERT IGNORE INTO algorithm_category (id, name, description, parent_id, sort_order) VALUES
(1, '排序算法', '各种排序算法的实现和比较', 0, 1),
(2, '查找算法', '各种查找算法的实现和应用', 0, 2),
(3, '图算法', '图论相关算法', 0, 3),
(4, '动态规划', '动态规划算法', 0, 4),
(5, '贪心算法', '贪心算法', 0, 5),
(6, '分治算法', '分治算法', 0, 6),
(7, '回溯算法', '回溯算法', 0, 7),
(8, '字符串算法', '字符串处理相关算法', 0, 8);

-- 插入示例算法数据
INSERT IGNORE INTO algorithm (id, category_id, name, description, difficulty_level, code, language, time_complexity, space_complexity) VALUES
(1, 1, '冒泡排序', '通过重复遍历要排序的数列，比较相邻元素并交换位置', 1, 'public class BubbleSort {\n    public void sort(int[] arr) {\n        int n = arr.length;\n        for (int i = 0; i < n-1; i++) {\n            for (int j = 0; j < n-i-1; j++) {\n                if (arr[j] > arr[j+1]) {\n                    int temp = arr[j];\n                    arr[j] = arr[j+1];\n                    arr[j+1] = temp;\n                }\n            }\n        }\n    }\n}', 'java', 'O(n²)', 'O(1)'),
(2, 1, '快速排序', '通过分治策略将数组分为较小和较大的两个子数组，然后递归排序', 2, 'public class QuickSort {\n    public void sort(int[] arr, int low, int high) {\n        if (low < high) {\n            int pi = partition(arr, low, high);\n            sort(arr, low, pi-1);\n            sort(arr, pi+1, high);\n        }\n    }\n    \n    private int partition(int[] arr, int low, int high) {\n        int pivot = arr[high];\n        int i = (low-1);\n        for (int j = low; j < high; j++) {\n            if (arr[j] < pivot) {\n                i++;\n                int temp = arr[i];\n                arr[i] = arr[j];\n                arr[j] = temp;\n            }\n        }\n        int temp = arr[i+1];\n        arr[i+1] = arr[high];\n        arr[high] = temp;\n        return i+1;\n    }\n}', 'java', 'O(n log n)', 'O(log n)'),
(3, 2, '二分查找', '在有序数组中查找特定元素的搜索算法', 1, 'public class BinarySearch {\n    public int search(int[] arr, int target) {\n        int left = 0, right = arr.length - 1;\n        while (left <= right) {\n            int mid = left + (right - left) / 2;\n            if (arr[mid] == target) return mid;\n            else if (arr[mid] < target) left = mid + 1;\n            else right = mid - 1;\n        }\n        return -1;\n    }\n}', 'java', 'O(log n)', 'O(1)');