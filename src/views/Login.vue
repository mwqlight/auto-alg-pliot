<template>
  <div class="login-page">
    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="decoration-circle circle-1"></div>
      <div class="decoration-circle circle-2"></div>
      <div class="decoration-circle circle-3"></div>
      <div class="decoration-grid"></div>
    </div>

    <!-- 登录表单容器 -->
    <div class="login-container">
      <!-- 左侧品牌信息 -->
      <div class="brand-section">
        <div class="brand-logo">
          <div class="logo-icon">⚡</div>
          <h1 class="logo-text">算法飞行员</h1>
        </div>
        
        <div class="brand-description">
          <h2>探索算法的无限可能</h2>
          <p>通过可视化学习，深入理解数据结构与算法的精髓</p>
        </div>

        <div class="feature-list">
          <div class="feature-item">
            <div class="feature-icon">🎯</div>
            <div class="feature-content">
              <h4>智能算法可视化</h4>
              <p>实时观察算法执行过程，每一步都清晰可见</p>
            </div>
          </div>
          
          <div class="feature-item">
            <div class="feature-icon">📊</div>
            <div class="feature-content">
              <h4>个性化学习路径</h4>
              <p>根据你的进度和能力推荐最适合的学习内容</p>
            </div>
          </div>
          
          <div class="feature-item">
            <div class="feature-icon">🏆</div>
            <div class="feature-content">
              <h4>成就系统激励</h4>
              <p>通过挑战和成就保持学习动力</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧登录表单 -->
      <div class="form-section">
        <TechCard class="login-card">
          <template #content>
            <div class="login-header">
              <h2>欢迎回来</h2>
              <p>请登录您的账户</p>
            </div>

            <!-- 登录表单 -->
            <form @submit.prevent="handleLogin" class="login-form">
              <div class="form-group">
                <label for="username">用户名或邮箱</label>
                <TechInput
                  id="username"
                  v-model="loginForm.username"
                  placeholder="请输入用户名或邮箱"
                  :error="errors.username"
                  @input="clearError('username')"
                  autocomplete="username"
                />
              </div>

              <div class="form-group">
                <label for="password">密码</label>
                <TechInput
                  id="password"
                  v-model="loginForm.password"
                  type="password"
                  placeholder="请输入密码"
                  :error="errors.password"
                  @input="clearError('password')"
                  autocomplete="current-password"
                />
              </div>

              <div class="form-options">
                <label class="remember-me">
                  <input type="checkbox" v-model="loginForm.rememberMe" />
                  <span>记住我</span>
                </label>
                <a href="#" class="forgot-password" @click.prevent="showForgotPassword">忘记密码？</a>
              </div>

              <TechButton 
                type="primary" 
                size="large" 
                :loading="loading"
                :disabled="!canSubmit"
                block
                class="login-button"
              >
                {{ loading ? '登录中...' : '登录' }}
              </TechButton>

              <div class="login-divider">
                <span>或</span>
              </div>

              <div class="social-login">
                <TechButton type="secondary" block class="social-button">
                  <span class="social-icon">🐧</span>
                  <span>QQ登录</span>
                </TechButton>
                
                <TechButton type="secondary" block class="social-button">
                  <span class="social-icon">💬</span>
                  <span>微信登录</span>
                </TechButton>
                
                <TechButton type="secondary" block class="social-button">
                  <span class="social-icon">📱</span>
                  <span>手机验证码登录</span>
                </TechButton>
              </div>
            </form>

            <div class="register-link">
              <span>还没有账户？</span>
              <a href="#" @click.prevent="showRegister">立即注册</a>
            </div>
          </template>
        </TechCard>

        <!-- 注册表单 -->
        <TechCard v-if="showRegisterForm" class="register-card">
          <template #content>
            <div class="register-header">
              <h2>创建新账户</h2>
              <p>加入算法飞行员社区</p>
            </div>

            <form @submit.prevent="handleRegister" class="register-form">
              <div class="form-row">
                <div class="form-group">
                  <label for="firstName">姓氏</label>
                  <TechInput
                    id="firstName"
                    v-model="registerForm.firstName"
                    placeholder="请输入姓氏"
                    :error="errors.firstName"
                    @input="clearError('firstName')"
                  />
                </div>
                
                <div class="form-group">
                  <label for="lastName">名字</label>
                  <TechInput
                    id="lastName"
                    v-model="registerForm.lastName"
                    placeholder="请输入名字"
                    :error="errors.lastName"
                    @input="clearError('lastName')"
                  />
                </div>
              </div>

              <div class="form-group">
                <label for="email">邮箱</label>
                <TechInput
                  id="email"
                  v-model="registerForm.email"
                  type="email"
                  placeholder="请输入邮箱地址"
                  :error="errors.email"
                  @input="clearError('email')"
                  autocomplete="email"
                />
              </div>

              <div class="form-group">
                <label for="password">密码</label>
                <TechInput
                  id="password"
                  v-model="registerForm.password"
                  type="password"
                  placeholder="请输入密码（至少8位）"
                  :error="errors.password"
                  @input="clearError('password')"
                  autocomplete="new-password"
                />
                <div class="password-strength">
                  <div 
                    :class="['strength-bar', strengthClass]"
                    :style="{ width: passwordStrength + '%' }"
                  ></div>
                </div>
              </div>

              <div class="form-group">
                <label for="confirmPassword">确认密码</label>
                <TechInput
                  id="confirmPassword"
                  v-model="registerForm.confirmPassword"
                  type="password"
                  placeholder="请再次输入密码"
                  :error="errors.confirmPassword"
                  @input="clearError('confirmPassword')"
                  autocomplete="new-password"
                />
              </div>

              <div class="form-options">
                <label class="agree-terms">
                  <input type="checkbox" v-model="registerForm.agreeTerms" />
                  <span>我已阅读并同意 <a href="#" @click.prevent="showTerms">服务条款</a> 和 <a href="#" @click.prevent="showPrivacy">隐私政策</a></span>
                </label>
              </div>

              <TechButton 
                type="primary" 
                size="large" 
                :loading="registerLoading"
                :disabled="!canRegister"
                block
                class="register-button"
              >
                {{ registerLoading ? '注册中...' : '注册' }}
              </TechButton>
            </form>

            <div class="login-link">
              <span>已有账户？</span>
              <a href="#" @click.prevent="showRegisterForm = false">立即登录</a>
            </div>
          </template>
        </TechCard>

        <!-- 忘记密码表单 -->
        <TechCard v-if="showForgotPasswordForm" class="forgot-password-card">
          <template #content>
            <div class="forgot-password-header">
              <h2>重置密码</h2>
              <p>请输入您的邮箱地址，我们将发送重置链接</p>
            </div>

            <form @submit.prevent="handleForgotPassword" class="forgot-password-form">
              <div class="form-group">
                <label for="resetEmail">邮箱地址</label>
                <TechInput
                  id="resetEmail"
                  v-model="forgotPasswordForm.email"
                  type="email"
                  placeholder="请输入注册时使用的邮箱"
                  :error="errors.email"
                  @input="clearError('email')"
                />
              </div>

              <TechButton 
                type="primary" 
                size="large" 
                :loading="forgotPasswordLoading"
                :disabled="!forgotPasswordForm.email"
                block
                class="reset-button"
              >
                {{ forgotPasswordLoading ? '发送中...' : '发送重置链接' }}
              </TechButton>
            </form>

            <div class="back-to-login">
              <a href="#" @click.prevent="showForgotPasswordForm = false">← 返回登录</a>
            </div>
          </template>
        </TechCard>
      </div>
    </div>

    <!-- 底部信息 -->
    <div class="footer">
      <p>© 2024 算法飞行员. 保留所有权利.</p>
      <div class="footer-links">
        <a href="#" @click.prevent="showTerms">服务条款</a>
        <a href="#" @click.prevent="showPrivacy">隐私政策</a>
        <a href="#" @click.prevent="showContact">联系我们</a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAppStore } from '@/store/app'
import TechCard from '@/components/base/TechCard.vue'
import TechButton from '@/components/base/TechButton.vue'
import TechInput from '@/components/base/TechInput.vue'

const router = useRouter()
const appStore = useAppStore()

// 登录表单状态
const loginForm = reactive({
  username: '',
  password: '',
  rememberMe: false
})

const registerForm = reactive({
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  confirmPassword: '',
  agreeTerms: false
})

const forgotPasswordForm = reactive({
  email: ''
})

// UI状态
const showRegisterForm = ref(false)
const showForgotPasswordForm = ref(false)
const loading = ref(false)
const registerLoading = ref(false)
const forgotPasswordLoading = ref(false)

// 错误处理
const errors = reactive({
  username: '',
  password: '',
  firstName: '',
  lastName: '',
  email: '',
  confirmPassword: ''
})

// 计算属性
const canSubmit = computed(() => {
  return loginForm.username.trim() && loginForm.password.trim()
})

const canRegister = computed(() => {
  return (
    registerForm.firstName.trim() &&
    registerForm.lastName.trim() &&
    registerForm.email.trim() &&
    registerForm.password.trim() &&
    registerForm.confirmPassword.trim() &&
    registerForm.agreeTerms &&
    registerForm.password === registerForm.confirmPassword
  )
})

const passwordStrength = computed(() => {
  const password = registerForm.password
  if (!password) return 0
  
  let strength = 0
  if (password.length >= 8) strength += 25
  if (/[a-z]/.test(password)) strength += 25
  if (/[A-Z]/.test(password)) strength += 25
  if (/[0-9]/.test(password)) strength += 25
  
  return strength
})

const strengthClass = computed(() => {
  if (passwordStrength.value < 25) return 'weak'
  if (passwordStrength.value < 50) return 'fair'
  if (passwordStrength.value < 75) return 'good'
  return 'strong'
})

// 方法
const clearError = (field: string) => {
  errors[field as keyof typeof errors] = ''
}

const validateLoginForm = () => {
  let valid = true
  
  if (!loginForm.username.trim()) {
    errors.username = '请输入用户名或邮箱'
    valid = false
  }
  
  if (!loginForm.password.trim()) {
    errors.password = '请输入密码'
    valid = false
  }
  
  return valid
}

const validateRegisterForm = () => {
  let valid = true
  
  if (!registerForm.firstName.trim()) {
    errors.firstName = '请输入姓氏'
    valid = false
  }
  
  if (!registerForm.lastName.trim()) {
    errors.lastName = '请输入名字'
    valid = false
  }
  
  if (!registerForm.email.trim()) {
    errors.email = '请输入邮箱地址'
    valid = false
  } else if (!/\S+@\S+\.\S+/.test(registerForm.email)) {
    errors.email = '请输入有效的邮箱地址'
    valid = false
  }
  
  if (!registerForm.password.trim()) {
    errors.password = '请输入密码'
    valid = false
  } else if (registerForm.password.length < 8) {
    errors.password = '密码长度至少8位'
    valid = false
  }
  
  if (!registerForm.confirmPassword.trim()) {
    errors.confirmPassword = '请确认密码'
    valid = false
  } else if (registerForm.password !== registerForm.confirmPassword) {
    errors.confirmPassword = '两次输入的密码不一致'
    valid = false
  }
  
  if (!registerForm.agreeTerms) {
    // 这里可以添加条款同意的错误提示
    valid = false
  }
  
  return valid
}

const handleLogin = async () => {
  if (!validateLoginForm()) return
  
  loading.value = true
  
  try {
    // 模拟登录API调用
    await new Promise(resolve => setTimeout(resolve, 1500))
    
    // 设置用户信息
    const userInfo = {
      id: '1',
      name: loginForm.username,
      email: loginForm.username.includes('@') ? loginForm.username : `${loginForm.username}@example.com`,
      role: 'user'
    }
    
    // 存储认证信息到localStorage
    localStorage.setItem('auth-token', 'mock-jwt-token-' + Date.now())
    localStorage.setItem('user-info', JSON.stringify(userInfo))
    
    // 设置用户信息到store
    appStore.setUser(userInfo)
    
    // 跳转到首页
    router.push('/dashboard')
    
  } catch (error) {
    errors.username = '用户名或密码错误'
    errors.password = '用户名或密码错误'
  } finally {
    loading.value = false
  }
}

const handleRegister = async () => {
  if (!validateRegisterForm()) return
  
  registerLoading.value = true
  
  try {
    // 模拟注册API调用
    await new Promise(resolve => setTimeout(resolve, 2000))
    
    // 注册成功后自动登录
    const userInfo = {
      id: '2',
      name: `${registerForm.firstName} ${registerForm.lastName}`,
      email: registerForm.email,
      role: 'user'
    }
    
    // 存储认证信息到localStorage
    localStorage.setItem('auth-token', 'mock-jwt-token-' + Date.now())
    localStorage.setItem('user-info', JSON.stringify(userInfo))
    
    // 设置用户信息到store
    appStore.setUser(userInfo)
    
    // 跳转到首页
    router.push('/dashboard')
    
  } catch (error) {
    errors.email = '该邮箱已被注册'
  } finally {
    registerLoading.value = false
  }
}

const handleForgotPassword = async () => {
  forgotPasswordLoading.value = true
  
  try {
    // 模拟发送重置邮件
    await new Promise(resolve => setTimeout(resolve, 1500))
    
    // 显示成功消息
    alert('重置链接已发送到您的邮箱，请查收')
    showForgotPasswordForm.value = false
    
  } catch (error) {
    errors.email = '发送失败，请稍后重试'
  } finally {
    forgotPasswordLoading.value = false
  }
}

const showRegister = () => {
  showRegisterForm.value = true
  showForgotPasswordForm.value = false
}

const showForgotPassword = () => {
  showForgotPasswordForm.value = true
  showRegisterForm.value = false
}

const showTerms = () => {
  alert('服务条款页面')
}

const showPrivacy = () => {
  alert('隐私政策页面')
}

const showContact = () => {
  alert('联系我们页面')
}
</script>

<style lang="scss" scoped>
@import '@/styles/tech-theme.scss';

.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 100%);
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
}

.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  
  .decoration-circle {
    position: absolute;
    border-radius: 50%;
    background: radial-gradient(circle, var(--primary) 0%, transparent 70%);
    opacity: 0.1;
    
    &.circle-1 {
      width: 300px;
      height: 300px;
      top: -150px;
      left: -150px;
    }
    
    &.circle-2 {
      width: 200px;
      height: 200px;
      bottom: -100px;
      right: -100px;
    }
    
    &.circle-3 {
      width: 150px;
      height: 150px;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }
  }
  
  .decoration-grid {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: 
      linear-gradient(rgba(var(--primary-rgb), 0.03) 1px, transparent 1px),
      linear-gradient(90deg, rgba(var(--primary-rgb), 0.03) 1px, transparent 1px);
    background-size: 50px 50px;
  }
}

.login-container {
  flex: 1;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0;
  min-height: 100vh;
  position: relative;
  z-index: 1;
}

.brand-section {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 4rem;
  color: white;
  
  .brand-logo {
    display: flex;
    align-items: center;
    gap: 1rem;
    margin-bottom: 2rem;
    
    .logo-icon {
      font-size: 3rem;
      background: linear-gradient(135deg, var(--primary) 0%, var(--accent) 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }
    
    .logo-text {
      font-size: 2.5rem;
      font-weight: 700;
      background: linear-gradient(135deg, var(--primary) 0%, var(--accent) 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      margin: 0;
    }
  }
  
  .brand-description {
    margin-bottom: 3rem;
    
    h2 {
      font-size: 1.8rem;
      font-weight: 600;
      margin: 0 0 1rem 0;
      opacity: 0.9;
    }
    
    p {
      font-size: 1.1rem;
      opacity: 0.7;
      line-height: 1.6;
      margin: 0;
    }
  }
  
  .feature-list {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
  }
  
  .feature-item {
    display: flex;
    align-items: flex-start;
    gap: 1rem;
    
    .feature-icon {
      font-size: 1.5rem;
      width: 40px;
      text-align: center;
      flex-shrink: 0;
    }
    
    .feature-content {
      h4 {
        font-size: 1.1rem;
        font-weight: 600;
        margin: 0 0 0.5rem 0;
        opacity: 0.9;
      }
      
      p {
        font-size: 0.9rem;
        opacity: 0.7;
        line-height: 1.5;
        margin: 0;
      }
    }
  }
}

.form-section {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  background: rgba(255, 255, 255, 0.02);
  backdrop-filter: blur(10px);
  border-left: 1px solid rgba(255, 255, 255, 0.1);
}

.login-card, .register-card, .forgot-password-card {
  width: 100%;
  max-width: 400px;
  
  :deep(.card-content) {
    padding: 2rem;
  }
}

.login-header, .register-header, .forgot-password-header {
  text-align: center;
  margin-bottom: 2rem;
  
  h2 {
    font-size: 1.8rem;
    font-weight: 700;
    color: var(--text-primary);
    margin: 0 0 0.5rem 0;
  }
  
  p {
    color: var(--text-secondary);
    margin: 0;
  }
}

.login-form, .register-form, .forgot-password-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  
  label {
    font-weight: 600;
    color: var(--text-primary);
    font-size: 0.9rem;
  }
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.password-strength {
  height: 4px;
  background: var(--bg-tertiary);
  border-radius: 2px;
  overflow: hidden;
  margin-top: 0.5rem;
  
  .strength-bar {
    height: 100%;
    border-radius: 2px;
    transition: all 0.3s ease;
    
    &.weak {
      background: var(--error);
      width: 25% !important;
    }
    
    &.fair {
      background: var(--warning);
      width: 50% !important;
    }
    
    &.good {
      background: var(--accent);
      width: 75% !important;
    }
    
    &.strong {
      background: var(--success);
      width: 100% !important;
    }
  }
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.9rem;
}

.remember-me, .agree-terms {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  color: var(--text-secondary);
  
  input {
    margin: 0;
  }
  
  a {
    color: var(--primary);
    text-decoration: none;
    
    &:hover {
      text-decoration: underline;
    }
  }
}

.forgot-password {
  color: var(--primary);
  text-decoration: none;
  
  &:hover {
    text-decoration: underline;
  }
}

.login-button, .register-button, .reset-button {
  margin-top: 0.5rem;
}

.login-divider {
  display: flex;
  align-items: center;
  gap: 1rem;
  color: var(--text-secondary);
  font-size: 0.9rem;
  
  &::before, &::after {
    content: '';
    flex: 1;
    height: 1px;
    background: var(--border-color);
  }
}

.social-login {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.social-button {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  justify-content: center;
  
  .social-icon {
    font-size: 1.2rem;
  }
}

.register-link, .login-link, .back-to-login {
  text-align: center;
  margin-top: 1.5rem;
  padding-top: 1.5rem;
  border-top: 1px solid var(--border-color);
  color: var(--text-secondary);
  font-size: 0.9rem;
  
  a {
    color: var(--primary);
    text-decoration: none;
    font-weight: 600;
    margin-left: 0.5rem;
    
    &:hover {
      text-decoration: underline;
    }
  }
}

.footer {
  padding: 1.5rem 2rem;
  text-align: center;
  color: var(--text-secondary);
  font-size: 0.9rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  
  p {
    margin: 0 0 0.5rem 0;
  }
}

.footer-links {
  display: flex;
  justify-content: center;
  gap: 1.5rem;
  
  a {
    color: var(--text-secondary);
    text-decoration: none;
    
    &:hover {
      color: var(--primary);
    }
  }
}

// 响应式设计
@media (max-width: 1024px) {
  .login-container {
    grid-template-columns: 1fr;
  }
  
  .brand-section {
    padding: 2rem;
    text-align: center;
    
    .brand-logo {
      justify-content: center;
    }
  }
  
  .form-section {
    border-left: none;
    border-top: 1px solid rgba(255, 255, 255, 0.1);
  }
}

@media (max-width: 768px) {
  .login-page {
    min-height: 100vh;
  }
  
  .brand-section {
    padding: 1.5rem;
    
    .brand-logo {
      flex-direction: column;
      gap: 0.5rem;
      
      .logo-text {
        font-size: 2rem;
      }
    }
    
    .brand-description {
      h2 {
        font-size: 1.5rem;
      }
      
      p {
        font-size: 1rem;
      }
    }
  }
  
  .form-section {
    padding: 1.5rem;
  }
  
  .login-card, .register-card, .forgot-password-card {
    :deep(.card-content) {
      padding: 1.5rem;
    }
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .footer {
    padding: 1rem 1.5rem;
    
    .footer-links {
      flex-direction: column;
      gap: 0.5rem;
    }
  }
}

@media (max-width: 480px) {
  .brand-section {
    padding: 1rem;
    
    .brand-logo {
      .logo-text {
        font-size: 1.8rem;
      }
    }
  }
  
  .form-section {
    padding: 1rem;
  }
  
  .login-card, .register-card, .forgot-password-card {
    :deep(.card-content) {
      padding: 1rem;
    }
  }
}
</style>