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

  // Buyer Profile Management
  async function fetchBuyerProfile(buyerId) {
      try {
          const response = await fetch('/api/buyer/profile', {
              headers: { 'buyerId': buyerId }
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
          const response = await fetch('/api/buyer/profile', {
              method: 'POST',
              headers: { 
                  'Content-Type': 'application/json',
                  'buyerId': buyerId
              },
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
          const response = await fetch('/api/seller/profile', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
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

  // Admin Management
  async function fetchAllUsers() {
    try {
      const response = await fetch('/api/admin/users')
      if (!response.ok) throw new Error('Failed to fetch users')
      return await response.json()
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  async function fetchUserById(id) {
    try {
      const response = await fetch(`/api/admin/user/${id}`)
      if (!response.ok) throw new Error('Failed to fetch user')
      return await response.json()
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  async function adminCreateUser(userData) {
    try {
      const response = await fetch('/api/admin/user', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData)
      })
      if (!response.ok) throw new Error('Failed to create user')
      return await response.json()
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  async function adminUpdateUser(id, userData) {
    try {
      const response = await fetch(`/api/admin/user/${id}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData)
      })
      if (!response.ok) throw new Error('Failed to update user')
      return await response.json()
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  async function deleteUser(id) {
    try {
      const response = await fetch(`/api/admin/user/${id}`, {
        method: 'DELETE'
      })
      if (!response.ok) throw new Error('Failed to delete user')
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  async function fetchBuyerById(id) {
    try {
      const response = await fetch(`/api/admin/user/buyers/${id}`)
      if (!response.ok) throw new Error('Failed to fetch buyer')
      return await response.json()
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  async function fetchSellerById(id) {
    try {
      const response = await fetch(`/api/admin/user/sellers/${id}`)
      if (!response.ok) throw new Error('Failed to fetch seller')
      return await response.json()
    } catch (error) {
      console.error(error)
      throw error
    }
  }
  
  async function searchSellers(name) {
    try {
        const url = name 
            ? `/api/admin/user/sellers?name=${encodeURIComponent(name)}`
            : '/api/admin/user/sellers'
        const response = await fetch(url)
        if (!response.ok) throw new Error('Failed to search sellers')
        return await response.json()
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
    login,
    register,
    logout,
    fetchUserInfo,
    fetchBuyerProfile,
    updateBuyerProfile,
    updateSellerProfile,
    fetchAllUsers,
    fetchUserById,
    adminCreateUser,
    adminUpdateUser,
    deleteUser,
    fetchBuyerById,
    fetchSellerById,
    searchSellers
  }
})
