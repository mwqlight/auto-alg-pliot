import { createPinia } from 'pinia'
import type { App } from 'vue'

// 创建 Pinia 实例
const pinia = createPinia()

// 安装 Pinia
export function setupStore(app: App) {
  app.use(pinia)
}

export default pinia