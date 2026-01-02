import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', () => {
  // State
  const isLoggedInState = ref(localStorage.getItem('isLoggedIn') === 'true')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{"role": "buyer"}'))

  // Getters
  const isLoggedIn = computed(() => isLoggedInState.value)
  const role = computed(() => userInfo.value.role || 'buyer')
  const currentUserId = computed(() => {
      // Return buyer/seller ID if available, otherwise user ID
      if (role.value === 'buyer') return userInfo.value.b_id
      if (role.value === 'seller') return userInfo.value.s_id
      return userInfo.value.u_id
  })

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
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ username, password })
      })

      const contentType = response.headers.get('content-type')
      let data
      if (contentType && contentType.includes('application/json')) {
        data = await response.json()
      } else {
        // Handle non-JSON response (likely plain text error message)
        const text = await response.text()
        data = { message: text }
      }

      if (!response.ok) {
        throw new Error(data.message || '登录失败')
      }

      setLoginState(true)
      // data.user contains the main User object or specific role object depending on controller logic
      // AuthController returns: { message, role, user: (Buyer|Seller|User) }
      const userObj = data.user
      setUserInfo({ ...userObj, role: data.role })

      return data
    } catch (error) {
      console.error('Login error:', error)
      throw error
    }
  }

  async function register(registerData) {
    try {
      const response = await fetch('http://localhost:8080/api/auth/register', {
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
      await fetch('http://localhost:8080/api/auth/logout', {
        method: 'POST',
        credentials: 'include'
      })
    } catch (error) {
      console.error('Logout error:', error)
    } finally {
      clearState()
      window.location.href = '/login'
    }
  }

  async function fetchUserInfo() {
    try {
      const response = await fetch('http://localhost:8080/api/auth/current', {
        method: 'GET',
        credentials: 'include'
      })

      if (response.status === 401) {
        clearState()
        throw new Error('Session expired')
      }

      const data = await response.json()

      if (!response.ok) {
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

  // Buyer Profile Management
  async function fetchBuyerProfile(buyerId) {
      try {
          const response = await fetch('http://localhost:8080/api/buyer/profile', {
              headers: { 'buyerId': buyerId },
              credentials: 'include'
          })
          if (!response.ok) throw new Error('Failed to fetch profile')
          const data = await response.json()
          setUserInfo(data) // Update local info
          return data
      } catch (error) {
          console.error(error)
          throw error
      }
  }

  async function updateBuyerProfile(buyerId, profileData) {
      try {
          const response = await fetch('http://localhost:8080/api/buyer/profile', {
              method: 'POST',
              headers: {
                  'Content-Type': 'application/json',
                  'buyerId': buyerId
              },
              credentials: 'include',
              body: JSON.stringify(profileData)
          })
          if (!response.ok) throw new Error('Failed to update profile')

          // Refresh info
          await fetchBuyerProfile(buyerId)
      } catch (error) {
          console.error(error)
          throw error
      }
  }

  // Seller Profile Management
  async function updateSellerProfile(profileData) {
      try {
          const response = await fetch('http://localhost:8080/api/seller/profile', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              credentials: 'include',
              body: JSON.stringify(profileData)
          })
          if (!response.ok) throw new Error('Failed to update seller profile')

          // Refresh info if needed, though we don't have explicit getSellerProfile endpoint other than current
          await fetchUserInfo()
      } catch (error) {
          console.error(error)
          throw error
      }
  }

  return {
    isLoggedIn,
    userInfo,
    role,
    currentUserId,
    clearState,
    login,
    register,
    logout,
    fetchUserInfo,
    fetchBuyerProfile,
    updateBuyerProfile,
    updateSellerProfile
  }
})
