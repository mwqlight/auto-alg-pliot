import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { UserInfo } from '@/types/user'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>('')
  const userInfo = ref<UserInfo | null>(null)
  const isLoggedIn = ref<boolean>(false)

  // 设置用户信息
  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
    isLoggedIn.value = true
  }

  // 设置token
  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  // 清除用户信息
  const clearUserInfo = () => {
    token.value = ''
    userInfo.value = null
    isLoggedIn.value = false
    localStorage.removeItem('token')
  }

  // 初始化用户状态
  const initUserState = () => {
    const storedToken = localStorage.getItem('token')
    if (storedToken) {
      token.value = storedToken
      isLoggedIn.value = true
    }
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    setUserInfo,
    setToken,
    clearUserInfo,
    initUserState
  }
})