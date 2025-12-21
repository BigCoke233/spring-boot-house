import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', () => {
  // State
  // We don't need to store token manually for session-based auth, but we might want to track if we are logged in
  const isLoggedInState = ref(localStorage.getItem('isLoggedIn') === 'true')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{"role": "buyer"}'))

  // Getters
  const isLoggedIn = computed(() => isLoggedInState.value)
  const role = computed(() => userInfo.value.role || 'buyer')

  // Helper functions
  function setLoginState(status) {
    isLoggedInState.value = status
    localStorage.setItem('isLoggedIn', status)
  }

  function setUserInfo(info) {
    userInfo.value = { ...userInfo.value, ...info }
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }

  function clearState() {
    setLoginState(false)
    userInfo.value = { role: 'buyer' }
    localStorage.removeItem('userInfo')
  }

  // Actions
  async function login({ username, password }) {
    try {
      const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
      })

      const data = await response.json()

      if (!response.ok) {
        throw new Error(data.message || '登录失败')
      }

      setLoginState(true)
      if (data.user) {
        // Backend returns "user" (basic info) and potentially "profile" (buyer/seller info) inside data
        // But the response structure in AuthController is: { message, role, user (which is buyer/seller obj or user obj)
        // Let's adapt based on the actual response.
        // AuthController: return ResponseEntity.ok(Map.of("message", "Login successful", "role", "buyer", "user", buyer));
        setUserInfo({ ...data.user, role: data.role })
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
      const response = await fetch('/api/auth/register', {
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

  async function logout() {
    try {
      await fetch('/api/auth/logout', { method: 'POST' })
    } catch (error) {
      console.error('Logout error:', error)
    } finally {
      clearState()
      // Optional: redirect to login page
      window.location.href = '/login'
    }
  }

  async function fetchUserInfo() {
    try {
      const response = await fetch('/api/auth/current', {
        method: 'GET'
      })

      const data = await response.json()

      if (!response.ok) {
        if (response.status === 401) {
            clearState()
        }
        throw new Error(data.message || '获取用户信息失败')
      }

      // /api/auth/current returns { user: User, profile: Buyer/Seller }
      // We merge them for the frontend
      const fullInfo = { ...data.user, ...data.profile, role: data.user.u_type }
      setUserInfo(fullInfo)
      setLoginState(true)

      return fullInfo
    } catch (error) {
      console.error('Fetch user info error:', error)
      throw error
    }
  }

  async function changePassword(passwordData) {
      // TODO: Implement change password API if available
      console.warn('Change password not implemented yet', passwordData)
  }

  return {
    isLoggedIn,
    userInfo,
    role,
    login,
    register,
    logout,
    fetchUserInfo,
    changePassword
  }
})
