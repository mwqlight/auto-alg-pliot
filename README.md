# 算法驾驶舱 (Algorithm Cockpit)

> 一个现代化的算法学习与执行平台，基于SpringBoot + Vue3前后端分离架构

## 🚀 项目简介

算法驾驶舱是一个集算法学习、代码执行、性能分析于一体的在线平台，旨在帮助开发者更好地理解和实践各种算法。

### ✨ 核心特性

- **算法学习**: 提供丰富的算法库，包含详细的思想、应用场景和复杂度分析
- **在线执行**: 支持多种编程语言的算法代码在线执行
- **性能监控**: 实时监控算法执行时间和内存使用情况
- **用户管理**: 完整的用户认证和权限管理系统
- **响应式设计**: 支持PC端和移动端访问

## 🏗️ 技术架构

### 后端技术栈
- **框架**: SpringBoot 3.0+
- **ORM**: MyBatis-Plus
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **安全**: Spring Security + JWT
- **API文档**: SpringDoc OpenAPI 3.0

### 前端技术栈
- **框架**: Vue 3 + TypeScript
- **构建工具**: Vite
- **UI组件**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **HTTP客户端**: Axios

## 📁 项目结构

```
auto-alg-pliot/
├── alg-cockpit-backend/          # SpringBoot后端项目
│   ├── src/main/java/com/algcockpit/
│   │   ├── config/              # 配置类
│   │   ├── controller/          # 控制器层
│   │   ├── dto/                 # 数据传输对象
│   │   ├── entity/              # 实体类
│   │   ├── exception/           # 异常处理
│   │   ├── repository/          # 数据访问层
│   │   ├── service/             # 业务逻辑层
│   │   └── util/                # 工具类
│   ├── src/main/resources/      # 配置文件
│   └── Dockerfile               # 后端Docker配置
├── alg-cockpit-frontend/         # Vue3前端项目
│   ├── src/
│   │   ├── api/                 # API接口
│   │   ├── components/          # 组件
│   │   ├── views/               # 页面视图
│   │   ├── store/               # 状态管理
│   │   └── utils/               # 工具函数
│   ├── Dockerfile               # 前端Docker配置
│   └── nginx.conf               # Nginx配置
├── sql/                          # 数据库脚本
├── docker-compose.yml           # Docker Compose配置
└── README.md                    # 项目文档
```

## 🚀 快速开始

### 环境要求

- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Redis 7+
- Docker & Docker Compose (可选)

### 本地开发

#### 1. 启动后端服务
```bash
cd alg-cockpit-backend

# 安装依赖并构建
mvn clean install

# 启动应用
mvn spring-boot:run
```

#### 2. 启动前端服务
```bash
cd alg-cockpit-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

#### 3. 访问应用
- 前端地址: http://localhost:5173
- 后端API: http://localhost:8080/api
- API文档: http://localhost:8080/api/swagger-ui.html

### Docker部署

#### 1. 使用Docker Compose一键部署
```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 停止服务
docker-compose down
```

#### 2. 访问应用
- 前端地址: http://localhost
- 后端API: http://localhost:8080/api
- 数据库管理: localhost:3306
- Redis管理: localhost:6379

## 📊 数据库设计

### 核心表结构

- **user**: 用户表
- **algorithm_category**: 算法分类表
- **algorithm**: 算法信息表
- **algorithm_execution**: 算法执行记录表

### 初始化数据

项目启动时会自动创建数据库并插入初始数据，包括：
- 默认管理员账号 (admin/admin)
- 8个算法分类
- 3个示例算法

## 🔐 安全特性

- **JWT认证**: 无状态token认证机制
- **RBAC权限**: 基于角色的访问控制
- **密码加密**: BCrypt强密码加密
- **XSS防护**: 输入输出过滤
- **SQL注入防护**: MyBatis参数化查询

## 📡 API接口

### 认证接口
- `POST /api/v1/auth/login` - 用户登录
- `POST /api/v1/auth/register` - 用户注册
- `POST /api/v1/auth/logout` - 用户登出

### 算法接口
- `GET /api/v1/algorithms/` - 分页查询算法
- `GET /api/v1/algorithms/{id}` - 获取算法详情
- `POST /api/v1/algorithms/execute` - 执行算法
- `GET /api/v1/algorithms/executions` - 获取执行记录

### 用户接口
- `GET /api/v1/users/profile` - 获取用户信息
- `PUT /api/v1/users/profile` - 更新用户信息

## 🛠️ 开发指南

### 后端开发

#### 添加新的API接口
1. 在`controller`包下创建新的控制器类
2. 在`service`包下实现业务逻辑
3. 在`repository`包下定义数据访问接口
4. 在`dto`包下定义请求/响应对象

#### 示例代码
```java
@RestController
@RequestMapping("/api/v1/examples")
@Tag(name = "示例管理")
public class ExampleController {
    
    @Autowired
    private ExampleService exampleService;
    
    @GetMapping("/")
    @Operation(summary = "获取示例列表")
    public ApiResponse<List<Example>> getExamples() {
        return ApiResponse.success(exampleService.getExamples());
    }
}
```

### 前端开发

#### 添加新的页面
1. 在`views`目录下创建Vue组件
2. 在`router/index.ts`中添加路由配置
3. 在`api/modules`下定义API接口
4. 在`types`目录下定义TypeScript类型

#### 示例代码
```vue
<template>
  <div class="example-page">
    <h1>示例页面</h1>
    <el-button @click="fetchData">获取数据</el-button>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { getExamples } from '@/api/modules/example'

const data = ref([])

const fetchData = async () => {
  const response = await getExamples()
  data.value = response.data
}
</script>
```

## 🧪 测试

### 后端测试
```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify
```

### 前端测试
```bash
# 运行单元测试
npm run test:unit

# 运行E2E测试
npm run test:e2e
```

## 📈 性能优化

### 后端优化
- 使用Redis缓存热点数据
- 数据库查询优化和索引设计
- 异步处理和线程池配置
- 连接池和资源管理

### 前端优化
- 组件懒加载和路由分割
- 图片和资源压缩
- CDN加速和缓存策略
- 代码分割和Tree Shaking

## 🔧 配置说明

### 应用配置

#### 后端配置 (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/alg_cockpit
    username: root
    password: root
  redis:
    host: localhost
    port: 6379
server:
  port: 8080
```

#### 前端配置 (vite.config.ts)
```typescript
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

- 项目主页: [GitHub Repository]
- 问题反馈: [Issues]
- 邮箱: dev@algcockpit.com

## 🙏 致谢

感谢以下开源项目的支持：
- Spring Boot
- Vue.js
- Element Plus
- MyBatis-Plus
- Redis
- MySQL

---

**算法驾驶舱** - 让算法学习更简单！ 🎯
