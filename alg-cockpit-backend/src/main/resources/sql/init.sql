-- 算法驾驶舱平台数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS `alg_cockpit` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `alg_cockpit`;

-- 算法分类表
CREATE TABLE IF NOT EXISTS `algorithm_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '分类名称',
    `description` TEXT COMMENT '分类描述',
    `icon` VARCHAR(200) COMMENT '分类图标',
    `sort_order` INT DEFAULT 0 COMMENT '排序序号',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_sort_order` (`sort_order`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='算法分类表';

-- 算法信息表
CREATE TABLE IF NOT EXISTS `algorithm` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `category_id` BIGINT NOT NULL COMMENT '分类ID',
    `name` VARCHAR(200) NOT NULL COMMENT '算法名称',
    `description` TEXT COMMENT '算法描述',
    `algorithm_thought` TEXT COMMENT '算法思想',
    `application_scenarios` TEXT COMMENT '应用场景',
    `variants_optimizations` TEXT COMMENT '变种与优化',
    `thinking_questions` TEXT COMMENT '思考题',
    `difficulty_level` TINYINT DEFAULT 1 COMMENT '难度等级：1-简单，2-中等，3-困难',
    `popularity` INT DEFAULT 0 COMMENT '热度',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_difficulty_level` (`difficulty_level`),
    KEY `idx_popularity` (`popularity`),
    CONSTRAINT `fk_algorithm_category` FOREIGN KEY (`category_id`) REFERENCES `algorithm_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='算法信息表';

-- 算法代码表
CREATE TABLE IF NOT EXISTS `algorithm_code` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `algorithm_id` BIGINT NOT NULL COMMENT '算法ID',
    `language` VARCHAR(50) NOT NULL COMMENT '编程语言：java, python, javascript',
    `code_content` LONGTEXT NOT NULL COMMENT '代码内容',
    `code_comment` LONGTEXT COMMENT '代码注释',
    `execution_example` TEXT COMMENT '执行示例',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_algorithm_id` (`algorithm_id`),
    KEY `idx_language` (`language`),
    CONSTRAINT `fk_code_algorithm` FOREIGN KEY (`algorithm_id`) REFERENCES `algorithm` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='算法代码表';

-- 算法复杂度表
CREATE TABLE IF NOT EXISTS `algorithm_complexity` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `algorithm_id` BIGINT NOT NULL COMMENT '算法ID',
    `time_best` VARCHAR(50) COMMENT '最佳时间复杂度',
    `time_average` VARCHAR(50) COMMENT '平均时间复杂度',
    `time_worst` VARCHAR(50) COMMENT '最坏时间复杂度',
    `space_complexity` VARCHAR(50) COMMENT '空间复杂度',
    `stability` TINYINT COMMENT '稳定性：0-不稳定，1-稳定',
    `complexity_analysis` TEXT COMMENT '复杂度分析说明',
    `performance_data` JSON COMMENT '性能测试数据',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_algorithm_id` (`algorithm_id`),
    CONSTRAINT `fk_complexity_algorithm` FOREIGN KEY (`algorithm_id`) REFERENCES `algorithm` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='算法复杂度表';

-- 演示数据表
CREATE TABLE IF NOT EXISTS `demo_data` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `algorithm_id` BIGINT NOT NULL COMMENT '算法ID',
    `data_type` VARCHAR(50) COMMENT '数据类型：array, graph, tree, string',
    `input_data` JSON NOT NULL COMMENT '输入数据',
    `output_data` JSON COMMENT '输出数据',
    `visualization_config` JSON COMMENT '可视化配置',
    `description` TEXT COMMENT '数据描述',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_algorithm_id` (`algorithm_id`),
    CONSTRAINT `fk_demo_algorithm` FOREIGN KEY (`algorithm_id`) REFERENCES `algorithm` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='演示数据表';

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `email` VARCHAR(100) COMMENT '邮箱',
    `nickname` VARCHAR(100) COMMENT '昵称',
    `avatar` VARCHAR(200) COMMENT '头像',
    `role` TINYINT DEFAULT 1 COMMENT '角色：1-普通用户，2-管理员',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `last_login_time` DATETIME COMMENT '最后登录时间',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_username` (`username`),
    KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 用户学习记录表
CREATE TABLE IF NOT EXISTS `learning_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `algorithm_id` BIGINT NOT NULL COMMENT '算法ID',
    `study_duration` INT DEFAULT 0 COMMENT '学习时长(秒)',
    `completion_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '完成度',
    `last_study_time` DATETIME COMMENT '最后学习时间',
    `study_count` INT DEFAULT 0 COMMENT '学习次数',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_algorithm_id` (`algorithm_id`),
    KEY `idx_last_study_time` (`last_study_time`),
    CONSTRAINT `fk_record_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_record_algorithm` FOREIGN KEY (`algorithm_id`) REFERENCES `algorithm` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户学习记录表';

-- 收藏表
CREATE TABLE IF NOT EXISTS `collection` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `algorithm_id` BIGINT NOT NULL COMMENT '算法ID',
    `collection_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_algorithm_id` (`algorithm_id`),
    UNIQUE KEY `uk_user_algorithm` (`user_id`, `algorithm_id`),
    CONSTRAINT `fk_collection_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_collection_algorithm` FOREIGN KEY (`algorithm_id`) REFERENCES `algorithm` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';

-- 插入初始数据
-- 算法分类数据
INSERT INTO `algorithm_category` (`name`, `description`, `icon`, `sort_order`) VALUES
('排序算法', '各种排序算法的实现和比较', 'sort', 1),
('搜索算法', '搜索和查找相关算法', 'search', 2),
('图论算法', '图论相关算法和数据结构', 'graph', 3),
('动态规划', '动态规划算法和优化', 'dp', 4),
('贪心算法', '贪心策略相关算法', 'greedy', 5),
('分治算法', '分治策略相关算法', 'divide', 6),
('回溯算法', '回溯算法和搜索', 'backtrack', 7),
('特殊算法', '特殊用途和高级算法', 'special', 8),
('数学算法', '数学计算相关算法', 'math', 9),
('机器学习算法', '机器学习和AI基础算法', 'ml', 10),
('数据结构算法', '数据结构相关算法', 'ds', 11),
('实用算法', '工程实践中的实用算法', 'practical', 12);

-- 插入示例用户
INSERT INTO `user` (`username`, `password`, `email`, `nickname`, `role`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTV6UiC', 'admin@algcockpit.com', '系统管理员', 2),
('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTV6UiC', 'user1@algcockpit.com', '测试用户1', 1);

-- 创建索引优化查询性能
CREATE INDEX idx_algorithm_category_status ON algorithm_category(status, sort_order);
CREATE INDEX idx_algorithm_category_status_popularity ON algorithm(category_id, status, popularity);
CREATE INDEX idx_learning_record_user_algorithm ON learning_record(user_id, algorithm_id);
CREATE INDEX idx_collection_user_time ON collection(user_id, collection_time);