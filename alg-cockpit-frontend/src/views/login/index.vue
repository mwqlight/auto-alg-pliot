<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="background-effects">
      <div class="gradient-circles">
        <div class="circle circle-1"></div>
        <div class="circle circle-2"></div>
        <div class="circle circle-3"></div>
      </div>
      <div class="grid-overlay"></div>
      <div class="particles"></div>
    </div>

    <!-- 登录表单容器 -->
    <div class="login-form-container">
      <!-- 品牌信息区 -->
      <div class="brand-section">
        <div class="logo">
          <div class="logo-icon">
            <svg viewBox="0 0 100 100" class="tech-logo">
              <path d="M50 20 L80 50 L50 80 L20 50 Z" fill="none" stroke="var(--primary-color)" stroke-width="3"/>
              <circle cx="50" cy="50" r="15" fill="var(--primary-color)" opacity="0.3"/>
              <circle cx="50" cy="50" r="8" fill="var(--primary-color)"/>
            </svg>
          </div>
          <h1 class="brand-name">算法驾驶舱</h1>
        </div>
        <p class="brand-description">智能算法分析与可视化平台</p>
        <div class="feature-list">
          <div class="feature-item">
            <span class="feature-icon">⚡</span>
            <span>高性能算法执行</span>
          </div>
          <div class="feature-item">
            <span class="feature-icon">📊</span>
            <span>实时数据可视化</span>
          </div>
          <div class="feature-item">
            <span class="feature-icon">🔒</span>
            <span>企业级安全保障</span>
          </div>
        </div>
      </div>

      <!-- 登录表单 -->
      <div class="form-section">
        <div class="form-tabs">
          <button 
            :class="['tab-btn', { active: activeTab === 'login' }]"
            @click="activeTab = 'login'"
          >
            登录
          </button>
          <button 
            :class="['tab-btn', { active: activeTab === 'register' }]"
            @click="activeTab = 'register'"
          >
            注册
          </button>
        </div>

        <!-- 登录表单 -->
        <div v-if="activeTab === 'login'" class="form-content">
          <form @submit.prevent="handleLogin" class="login-form">
            <div class="form-group">
              <label class="form-label">用户名</label>
              <input 
                v-model="loginForm.username"
                type="text" 
                class="form-input"
                placeholder="请输入用户名"
                required
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">密码</label>
              <input 
                v-model="loginForm.password"
                type="password" 
                class="form-input"
                placeholder="请输入密码"
                required
              />
            </div>

            <div class="form-options">
              <label class="checkbox-label">
                <input type="checkbox" v-model="loginForm.rememberMe" />
                <span class="checkmark"></span>
                记住我
              </label>
              <a href="#" class="forgot-password" @click.prevent="showForgotPassword = true">忘记密码？</a>
            </div>

            <button type="submit" class="submit-btn" :disabled="loginLoading">
              <span v-if="loginLoading" class="loading-spinner"></span>
              {{ loginLoading ? '登录中...' : '登录' }}
            </button>
          </form>
        </div>

        <!-- 注册表单 -->
        <div v-if="activeTab === 'register'" class="form-content">
          <form @submit.prevent="handleRegister" class="register-form">
            <div class="form-group">
              <label class="form-label">用户名</label>
              <input 
                v-model="registerForm.username"
                type="text" 
                class="form-input"
                placeholder="请输入用户名"
                required
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">邮箱</label>
              <input 
                v-model="registerForm.email"
                type="email" 
                class="form-input"
                placeholder="请输入邮箱"
                required
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">密码</label>
              <input 
                v-model="registerForm.password"
                type="password" 
                class="form-input"
                placeholder="请输入密码"
                required
                @input="checkPasswordStrength"
              />
              <div v-if="registerForm.password" class="password-strength">
                <div class="strength-bar" :class="passwordStrengthClass"></div>
                <span class="strength-text">{{ passwordStrengthText }}</span>
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">确认密码</label>
              <input 
                v-model="registerForm.confirmPassword"
                type="password" 
                class="form-input"
                placeholder="请再次输入密码"
                required
              />
            </div>

            <div class="form-options">
              <label class="checkbox-label">
                <input type="checkbox" v-model="registerForm.agreeTerms" />
                <span class="checkmark"></span>
                我已阅读并同意 <a href="#" class="terms-link">服务条款</a>
              </label>
            </div>

            <button type="submit" class="submit-btn" :disabled="registerLoading">
              <span v-if="registerLoading" class="loading-spinner"></span>
              {{ registerLoading ? '注册中...' : '注册' }}
            </button>
          </form>
        </div>
      </div>
    </div>

    <!-- 忘记密码弹窗 -->
    <div v-if="showForgotPassword" class="modal-overlay" @click="showForgotPassword = false">
      <div class="modal-content" @click.stop>
        <h3>忘记密码</h3>
        <form @submit.prevent="handleForgotPassword" class="forgot-password-form">
          <div class="form-group">
            <label class="form-label">邮箱</label>
            <input 
              v-model="forgotPasswordForm.email"
              type="email" 
              class="form-input"
              placeholder="请输入注册邮箱"
              required
            />
          </div>
          <div class="modal-actions">
            <button type="button" class="btn-secondary" @click="showForgotPassword = false">取消</button>
            <button type="submit" class="submit-btn">发送重置链接</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login, register } from '@/api/modules/auth'
import { useUserStore } from '@/store/user'
import type { LoginRequest, RegisterRequest } from '@/types/user'

const router = useRouter()
const userStore = useUserStore()

// 表单状态
const activeTab = ref<'login' | 'register'>('login')
const showForgotPassword = ref(false)

// 登录表单
const loginForm = reactive({
  username: '',
  password: '',
  rememberMe: false
})

// 注册表单
const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  agreeTerms: false
})

// 忘记密码表单
const forgotPasswordForm = reactive({
  email: ''
})

// 加载状态
const loginLoading = ref(false)
const registerLoading = ref(false)

// 密码强度检测
const passwordStrength = computed(() => {
  const password = registerForm.password
  if (!password) return 0
  
  let strength = 0
  if (password.length >= 8) strength += 1
  if (/[a-z]/.test(password)) strength += 1
  if (/[A-Z]/.test(password)) strength += 1
  if (/[0-9]/.test(password)) strength += 1
  if (/[^a-zA-Z0-9]/.test(password)) strength += 1
  
  return Math.min(strength, 5)
})

const passwordStrengthClass = computed(() => {
  const strength = passwordStrength.value
  if (strength <= 2) return 'weak'
  if (strength <= 4) return 'medium'
  return 'strong'
})

const passwordStrengthText = computed(() => {
  const strength = passwordStrength.value
  if (strength <= 2) return '弱'
  if (strength <= 4) return '中'
  return '强'
})

const checkPasswordStrength = () => {
  // 密码强度检测逻辑已通过计算属性实现
}

// 登录处理
const handleLogin = async () => {
  if (!loginForm.username || !loginForm.password) {
    ElMessage.error('请输入用户名和密码')
    return
  }

  loginLoading.value = true
  
  try {
    const loginRequest: LoginRequest = {
      username: loginForm.username,
      password: loginForm.password
    }
    
    const response = await login(loginRequest)
    
    if (response.code === 200) {
      // 存储用户信息和token
      userStore.setToken(response.data.token)
      userStore.setUserInfo(response.data.userInfo)
      
      ElMessage.success('登录成功')
      router.push('/dashboard')
    } else {
      ElMessage.error(response.message || '登录失败')
    }
  } catch (error) {
    console.error('登录错误:', error)
    ElMessage.error('登录失败，请检查网络连接')
  } finally {
    loginLoading.value = false
  }
}

// 注册处理
const handleRegister = async () => {
  if (!registerForm.username || !registerForm.email || !registerForm.password) {
    ElMessage.error('请填写完整信息')
    return
  }

  if (registerForm.password !== registerForm.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }

  if (!registerForm.agreeTerms) {
    ElMessage.error('请同意服务条款')
    return
  }

  registerLoading.value = true
  
  try {
    const registerRequest: RegisterRequest = {
      username: registerForm.username,
      email: registerForm.email,
      password: registerForm.password
    }
    
    const response = await register(registerRequest)
    
    if (response.code === 200) {
      ElMessage.success('注册成功，请登录')
      activeTab.value = 'login'
      
      // 清空注册表单
      Object.assign(registerForm, {
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        agreeTerms: false
      })
    } else {
      ElMessage.error(response.message || '注册失败')
    }
  } catch (error) {
    console.error('注册错误:', error)
    ElMessage.error('注册失败，请稍后重试')
  } finally {
    registerLoading.value = false
  }
}

// 忘记密码处理
const handleForgotPassword = async () => {
  if (!forgotPasswordForm.email) {
    ElMessage.error('请输入邮箱')
    return
  }

  try {
    // 模拟发送重置邮件
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success('重置链接已发送到您的邮箱')
    showForgotPassword.value = false
    forgotPasswordForm.email = ''
  } catch (error) {
    ElMessage.error('发送失败，请检查邮箱是否正确')
  }
}
</script>

<style scoped lang="scss">
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.background-effects {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 0;
}

.gradient-circles {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  
  .circle {
    position: absolute;
    border-radius: 50%;
    background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 70%);
    
    &.circle-1 {
      width: 400px;
      height: 400px;
      top: -200px;
      left: -200px;
      animation: float 6s ease-in-out infinite;
    }
    
    &.circle-2 {
      width: 300px;
      height: 300px;
      top: -150px;
      left: 100px;
      animation: float 8s ease-in-out infinite reverse;
    }
    
    &.circle-3 {
      width: 200px;
      height: 200px;
      top: 50px;
      left: -100px;
      animation: float 10s ease-in-out infinite;
    }
  }
}

.grid-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    linear-gradient(rgba(255,255,255,0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255,255,255,0.03) 1px, transparent 1px);
  background-size: 50px 50px;
}

.login-form-container {
  display: flex;
  max-width: 1000px;
  width: 90%;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  z-index: 1;
  overflow: hidden;
}

.brand-section {
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 60px 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  
  .logo {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    
    .logo-icon {
      width: 60px;
      height: 60px;
      margin-right: 15px;
      
      .tech-logo {
        width: 100%;
        height: 100%;
        filter: drop-shadow(0 2px 4px rgba(0,0,0,0.2));
      }
    }
    
    .brand-name {
      font-size: 28px;
      font-weight: 700;
      margin: 0;
    }
  }
  
  .brand-description {
    font-size: 16px;
    opacity: 0.9;
    margin-bottom: 30px;
  }
  
  .feature-list {
    .feature-item {
      display: flex;
      align-items: center;
      margin-bottom: 15px;
      font-size: 14px;
      
      .feature-icon {
        margin-right: 10px;
        font-size: 16px;
      }
    }
  }
}

.form-section {
  flex: 1;
  padding: 60px 40px;
  
  .form-tabs {
    display: flex;
    margin-bottom: 40px;
    border-bottom: 1px solid #e0e0e0;
    
    .tab-btn {
      flex: 1;
      padding: 15px 0;
      background: none;
      border: none;
      font-size: 16px;
      font-weight: 600;
      color: #666;
      cursor: pointer;
      position: relative;
      transition: color 0.3s;
      
      &.active {
        color: #667eea;
        
        &::after {
          content: '';
          position: absolute;
          bottom: -1px;
          left: 0;
          right: 0;
          height: 3px;
          background: #667eea;
          border-radius: 3px 3px 0 0;
        }
      }
      
      &:hover {
        color: #667eea;
      }
    }
  }
  
  .form-content {
    .form-group {
      margin-bottom: 25px;
      
      .form-label {
        display: block;
        margin-bottom: 8px;
        font-weight: 600;
        color: #333;
        font-size: 14px;
      }
      
      .form-input {
        width: 100%;
        padding: 12px 16px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 14px;
        transition: border-color 0.3s;
        
        &:focus {
          outline: none;
          border-color: #667eea;
          box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        &::placeholder {
          color: #999;
        }
      }
      
      .password-strength {
        margin-top: 8px;
        
        .strength-bar {
          height: 4px;
          border-radius: 2px;
          margin-bottom: 4px;
          transition: all 0.3s;
          
          &.weak {
            width: 33%;
            background: #ff4757;
          }
          
          &.medium {
            width: 66%;
            background: #ffa502;
          }
          
          &.strong {
            width: 100%;
            background: #2ed573;
          }
        }
        
        .strength-text {
          font-size: 12px;
          color: #666;
        }
      }
    }
    
    .form-options {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 30px;
      
      .checkbox-label {
        display: flex;
        align-items: center;
        font-size: 14px;
        color: #666;
        cursor: pointer;
        
        input[type="checkbox"] {
          display: none;
        }
        
        .checkmark {
          width: 18px;
          height: 18px;
          border: 2px solid #ddd;
          border-radius: 4px;
          margin-right: 8px;
          position: relative;
          transition: all 0.3s;
          
          &::after {
            content: '';
            position: absolute;
            left: 5px;
            top: 2px;
            width: 4px;
            height: 8px;
            border: solid white;
            border-width: 0 2px 2px 0;
            transform: rotate(45deg);
            opacity: 0;
            transition: opacity 0.3s;
          }
        }
        
        input[type="checkbox"]:checked + .checkmark {
          background: #667eea;
          border-color: #667eea;
          
          &::after {
            opacity: 1;
          }
        }
      }
      
      .forgot-password {
        font-size: 14px;
        color: #667eea;
        text-decoration: none;
        
        &:hover {
          text-decoration: underline;
        }
      }
      
      .terms-link {
        color: #667eea;
        text-decoration: none;
        
        &:hover {
          text-decoration: underline;
        }
      }
    }
    
    .submit-btn {
      width: 100%;
      padding: 14px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      border: none;
      border-radius: 8px;
      font-size: 16px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.3s;
      position: relative;
      
      &:hover:not(:disabled) {
        transform: translateY(-2px);
        box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
      }
      
      &:disabled {
        opacity: 0.6;
        cursor: not-allowed;
      }
      
      .loading-spinner {
        display: inline-block;
        width: 16px;
        height: 16px;
        border: 2px solid transparent;
        border-top: 2px solid white;
        border-radius: 50%;
        animation: spin 1s linear infinite;
        margin-right: 8px;
      }
    }
  }
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
  max-width: 400px;
  width: 90%;
  
  h3 {
    margin: 0 0 20px 0;
    color: #333;
    font-size: 20px;
  }
  
  .modal-actions {
    display: flex;
    gap: 15px;
    margin-top: 20px;
    
    .btn-secondary {
      flex: 1;
      padding: 12px;
      background: #f8f9fa;
      color: #666;
      border: 1px solid #ddd;
      border-radius: 6px;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        background: #e9ecef;
      }
    }
    
    .submit-btn {
      flex: 1;
      padding: 12px;
      background: #667eea;
      color: white;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        background: #5a6fd8;
      }
    }
  }
}

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@media (max-width: 768px) {
  .login-form-container {
    flex-direction: column;
    width: 95%;
  }
  
  .brand-section {
    padding: 40px 20px;
    text-align: center;
    
    .logo {
      justify-content: center;
    }
  }
  
  .form-section {
    padding: 40px 20px;
  }
}
</style>