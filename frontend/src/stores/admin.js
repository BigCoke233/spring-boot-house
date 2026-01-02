import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAdminStore = defineStore('admin', () => {
  const isLoading = ref(false)
  const error = ref(null)

  async function fetchAllUsers() {
    isLoading.value = true
    try {
      const response = await fetch('http://localhost:8080/api/admin/users', {
        credentials: 'include'
      })
      if (!response.ok) throw new Error('Failed to fetch users')
      return await response.json()
    } catch (err) {
      console.error(err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function fetchUserById(id) {
    isLoading.value = true
    try {
      const response = await fetch(`http://localhost:8080/api/admin/user/${id}`, {
        credentials: 'include'
      })
      if (!response.ok) throw new Error('Failed to fetch user')
      return await response.json()
    } catch (err) {
      console.error(err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function createUser(userData) {
    isLoading.value = true
    try {
      const response = await fetch('http://localhost:8080/api/admin/user', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData),
        credentials: 'include'
      })
      if (!response.ok) throw new Error('Failed to create user')
      return await response.json()
    } catch (err) {
      console.error(err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function updateUser(id, userData) {
    isLoading.value = true
    try {
      const response = await fetch(`http://localhost:8080/api/admin/user/${id}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData),
        credentials: 'include'
      })
      if (!response.ok) throw new Error('Failed to update user')
      return await response.json()
    } catch (err) {
      console.error(err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function deleteUser(id) {
    isLoading.value = true
    try {
      const response = await fetch(`http://localhost:8080/api/admin/user/${id}`, {
        method: 'DELETE',
        credentials: 'include'
      })
      if (!response.ok) throw new Error('Failed to delete user')
    } catch (err) {
      console.error(err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function fetchBuyerById(id) {
    isLoading.value = true
    try {
      const response = await fetch(`http://localhost:8080/api/admin/user/buyers/${id}`, {
        credentials: 'include'
      })
      if (!response.ok) throw new Error('Failed to fetch buyer')
      return await response.json()
    } catch (err) {
      console.error(err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function fetchSellerById(id) {
    isLoading.value = true
    try {
      const response = await fetch(`http://localhost:8080/api/admin/user/sellers/${id}`, {
        credentials: 'include'
      })
      if (!response.ok) throw new Error('Failed to fetch seller')
      return await response.json()
    } catch (err) {
      console.error(err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function searchSellers(name) {
    isLoading.value = true
    try {
        const url = name
            ? `http://localhost:8080/api/admin/user/sellers?name=${encodeURIComponent(name)}`
            : 'http://localhost:8080/api/admin/user/sellers'
        const response = await fetch(url, {
            credentials: 'include'
        })
        if (!response.ok) throw new Error('Failed to search sellers')
        return await response.json()
    } catch (err) {
        console.error(err)
        error.value = err.message
        throw err
    } finally {
        isLoading.value = false
    }
  }

  return {
    isLoading,
    error,
    fetchAllUsers,
    fetchUserById,
    createUser,
    updateUser,
    deleteUser,
    fetchBuyerById,
    fetchSellerById,
    searchSellers
  }
})
