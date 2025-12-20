import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', () => {
  // State
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{"role": "buyer"}'))
  
  // Getters
  const isLoggedIn = computed(() => !!token.value)
  const role = computed(() => userInfo.value.role || 'buyer')

  // Helper functions
  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  function setUserInfo(info) {
    userInfo.value = { ...userInfo.value, ...info }
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }

  function clearState() {
    token.value = ''
    userInfo.value = { role: 'buyer' }
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  // Actions
  async function login({ username, password }) {
    try {
      // 模拟 /api/user/login 请求
      const response = await fetch('/api/user/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
      })
      
      const data = await response.json()
      
      if (!response.ok) {
        throw new Error(data.message || '登录失败')
      }

      setToken(data.token)
      if (data.user) {
        setUserInfo(data.user)
      } else {
        await fetchUserInfo()
      }
      
      return data
    } catch (error) {
      console.error('Login error:', error)
      throw error
    }
  }

  async function register(registerData) {
    try {
      const response = await fetch('/api/user/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(registerData)
      })
      
      const data = await response.json()
      
      if (!response.ok) {
        throw new Error(data.message || '注册失败')
      }
      
      return data
    } catch (error) {
      console.error('Register error:', error)
      throw error
    }
  }

  async function fetchUserInfo() {
    if (!token.value) return

    try {
      const response = await fetch('/api/user/info', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token.value}`
        }
      })
      
      const data = await response.json()
      
      if (!response.ok) {
        if (response.status === 401) {
            logout()
        }
        throw new Error(data.message || '获取用户信息失败')
      }
      
      setUserInfo(data)
      return data
    } catch (error) {
      console.error('Fetch user info error:', error)
      throw error
    }
  }

  async function updateUserInfo(updateData) {
    try {
      const response = await fetch('/api/user/info', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token.value}`
        },
        body: JSON.stringify(updateData)
      })
      
      const data = await response.json()
      
      if (!response.ok) {
        throw new Error(data.message || '更新用户信息失败')
      }
      
      // 更新本地状态
      setUserInfo(updateData)
      return data
    } catch (error) {
      console.error('Update user info error:', error)
      throw error
    }
  }

  function logout() {
    clearState()
    // 这里可以添加路由跳转逻辑，但通常在组件层处理
  }

  function setRole(newRole) {
    setUserInfo({ role: newRole })
  }

  return {
    token,
    userInfo,
    role,
    isLoggedIn,
    login,
    register,
    fetchUserInfo,
    updateUserInfo,
    logout,
    setRole
  }
})
