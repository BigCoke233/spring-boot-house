import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useHouseStore = defineStore('house', () => {
  // State
  const houseList = ref([])
  const currentHouse = ref(null)
  const isLoading = ref(false)
  const error = ref(null)

  // Getters
  const getHouseById = computed(() => (id) => {
    return houseList.value.find(h => h.h_id == id) || (currentHouse.value && currentHouse.value.h_id == id ? currentHouse.value : null)
  })

  // Actions
  async function fetchHouseList(filters = {}) {
    isLoading.value = true
    error.value = null
    try {
      const params = new URLSearchParams()
      Object.keys(filters).forEach(key => {
        const value = filters[key]
        if (value !== null && value !== undefined && value !== '') {
          if (key === 'keyword') {
            params.append('name', value)
          } else {
            params.append(key, value)
          }
        }
      })
      const query = params.toString()
      const response = await fetch(`http://localhost:8080/api/public/houses?${query}`)
      if (!response.ok) throw new Error('Failed to fetch house list')
      const data = await response.json()
      houseList.value = data.content || data // Handle Page<House>
      return houseList.value
    } catch (err) {
      console.error('Fetch house list error:', err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function fetchHouseById(id) {
    isLoading.value = true
    error.value = null
    try {
      const response = await fetch(`http://localhost:8080/api/public/houses/${id}`)
      if (!response.ok) throw new Error('Failed to fetch house details')
      const data = await response.json()
      currentHouse.value = data
      return data
    } catch (err) {
      console.error(`Fetch house ${id} error:`, err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function fetchHousesByTag(tagName, pageNum = 1, pageSize = 10) {
    isLoading.value = true
    error.value = null
    try {
      const response = await fetch(`http://localhost:8080/api/public/houses/by-tag-name?tag=${encodeURIComponent(tagName)}&pageNum=${pageNum}&pageSize=${pageSize}`)
      if (!response.ok) throw new Error('Failed to fetch houses by tag')
      const data = await response.json()
      houseList.value = data.content || data
      return houseList.value
    } catch (err) {
      console.error('Fetch houses by tag error:', err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function fetchTags() {
    // API endpoint not yet implemented, returning empty for now
    return []
  }

  return {
    houseList,
    currentHouse,
    isLoading,
    error,
    fetchHouseList,
    fetchHouseById,
    fetchHousesByTag,
    getHouseById,
    fetchTags
  }
})
