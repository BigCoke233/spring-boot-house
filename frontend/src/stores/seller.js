import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useSellerStore = defineStore('seller', () => {
  const houses = ref([])
  const sellers = ref([])
  const isLoading = ref(false)
  const error = ref(null)

  async function fetchSellers() {
    isLoading.value = true
    try {
      const response = await fetch('http://localhost:8080/api/public/sellers')
      if (!response.ok) throw new Error('Failed to fetch sellers')
      const data = await response.json()

      // Map backend fields to frontend expected format
      sellers.value = data.map(seller => ({
        name: seller.s_name,
        description: seller.s_describe,
        phone: seller.s_phone,
        email: seller.s_email,
        website: seller.s_website
      }))
      return sellers.value
    } catch (err) {
      console.error('Fetch sellers error:', err)
      error.value = err.message
      return []
    } finally {
      isLoading.value = false
    }
  }

  async function fetchSellerHouses() {
    isLoading.value = true
    try {
      const response = await fetch('http://localhost:8080/api/seller/houses', {
        credentials: 'include'
      })
      if (!response.ok) throw new Error('Failed to fetch seller houses')
      const data = await response.json()
      houses.value = data
      return data
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function createHouse(houseData) {
    isLoading.value = true
    error.value = null
    try {
      const response = await fetch('http://localhost:8080/api/seller/house', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(houseData)
      })
      if (!response.ok) throw new Error('Failed to create house')
      const data = await response.json()
      houses.value.push(data)
      return data
    } catch (err) {
      console.error('Create house error:', err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function updateHouse(id, houseData) {
    isLoading.value = true
    error.value = null
    try {
      const response = await fetch(`http://localhost:8080/api/seller/house/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(houseData)
      })
      if (!response.ok) throw new Error('Failed to update house')
      const data = await response.json()

      const index = houses.value.findIndex(h => h.h_id === id)
      if (index !== -1) houses.value[index] = data

      return data
    } catch (err) {
      console.error('Update house error:', err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function deleteHouse(id) {
    isLoading.value = true
    error.value = null
    try {
      const response = await fetch(`http://localhost:8080/api/seller/house/${id}`, {
        method: 'DELETE',
        credentials: 'include'
      })
      if (!response.ok) throw new Error('Failed to delete house')

      const index = houses.value.findIndex(h => h.h_id === id)
      if (index !== -1) houses.value.splice(index, 1)

      return true
    } catch (err) {
      console.error('Delete house error:', err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  return {
    houses,
    sellers,
    isLoading,
    error,
    fetchSellers,
    fetchSellerHouses,
    createHouse,
    updateHouse,
    deleteHouse
  }
})
