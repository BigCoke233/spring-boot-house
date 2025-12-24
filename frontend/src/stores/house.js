import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

// Mock Data
const MOCK_HOUSES = [
  {
    h_id: 1,
    h_name: '映月城 · A1',
    h_description: '位于市中心的高端住宅，交通便利，周边设施齐全。拥有超大阳台和落地窗，采光极佳。',
    h_address: '广州市天河区珠江新城花城大道1号',
    h_price: 98000,
    h_square: 92,
    h_seller_id: 1,
    tags: [1, 3]
  }
]

const MOCK_TAGS = [
  { id: 1, name: '近地铁' },
  { id: 2, name: '海景' },
  { id: 3, name: '商圈' },
  { id: 4, name: '学区' }
]

export const useHouseStore = defineStore('house', () => {
  // State
  const houseList = ref([])
  const currentHouse = ref(null)
  const favorites = ref([])
  const isLoading = ref(false)
  const error = ref(null)
  const useMock = ref(false) // Default to false

  // Getters
  const getHouseById = computed(() => (id) => {
    return houseList.value.find(h => h.h_id == id) || (useMock.value ? MOCK_HOUSES.find(h => h.h_id == id) : null)
  })

  const isFavorite = computed(() => (id) => {
    return favorites.value.some(f => f.h_id === id)
  })

  // Actions
  async function fetchHouseList(filters = {}) {
    isLoading.value = true
    error.value = null
    try {
      if (useMock.value) {
        await new Promise(resolve => setTimeout(resolve, 500))
        houseList.value = MOCK_HOUSES
        return MOCK_HOUSES
      }

      const query = new URLSearchParams(filters).toString()
      const response = await fetch(`/api/public/houses?${query}`)
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
      if (useMock.value) {
        await new Promise(resolve => setTimeout(resolve, 300))
        const house = MOCK_HOUSES.find(h => h.h_id == id)
        if (!house) throw new Error('House not found')
        currentHouse.value = house
        return house
      }

      const response = await fetch(`/api/public/houses/${id}`)
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

  async function fetchSellerHouses() {
      isLoading.value = true
      try {
          const response = await fetch('/api/seller/houses')
          if (!response.ok) throw new Error('Failed to fetch seller houses')
          const data = await response.json()
          houseList.value = data
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
        const response = await fetch('/api/seller/house', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(houseData)
        })
        if (!response.ok) throw new Error('Failed to create house')
        const data = await response.json()
        houseList.value.push(data)
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
        const response = await fetch(`/api/seller/house/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(houseData)
        })
        if (!response.ok) throw new Error('Failed to update house')
        const data = await response.json()
        
        const index = houseList.value.findIndex(h => h.h_id === id)
        if (index !== -1) houseList.value[index] = data
        if (currentHouse.value && currentHouse.value.h_id === id) currentHouse.value = data
        
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
        const response = await fetch(`/api/seller/house/${id}`, {
            method: 'DELETE'
        })
        if (!response.ok) throw new Error('Failed to delete house')
        
        const index = houseList.value.findIndex(h => h.h_id === id)
        if (index !== -1) houseList.value.splice(index, 1)
        if (currentHouse.value && currentHouse.value.h_id === id) currentHouse.value = null
        
        return true
    } catch (err) {
        console.error('Delete house error:', err)
        error.value = err.message
        throw err
    } finally {
        isLoading.value = false
    }
  }

  async function fetchFavorites(buyerId) {
      if (!buyerId) return
      isLoading.value = true
      try {
          const response = await fetch('/api/buyer/follows', {
              headers: { 'buyerId': buyerId }
          })
          if (!response.ok) throw new Error('Failed to fetch favorites')
          const data = await response.json()
          favorites.value = data
      } catch (err) {
          console.error('Fetch favorites error', err)
      } finally {
          isLoading.value = false
      }
  }

  async function toggleFavorite(houseId, buyerId) {
    if (!buyerId) return

    const isFav = favorites.value.some(f => f.h_id === houseId)
    const method = isFav ? 'DELETE' : 'POST'
    
    try {
        const response = await fetch(`/api/buyer/follows/${houseId}`, {
            method: method,
            headers: { 'buyerId': buyerId }
        })
        if (!response.ok) throw new Error('Failed to toggle favorite')
        
        // Refresh favorites
        await fetchFavorites(buyerId)
    } catch (e) {
        console.error(e)
        throw e
    }
  }

  async function fetchTags() {
    // Return mocks for now as no tag API endpoint was found in controller scan
    return MOCK_TAGS
  }

  return {
    houseList,
    currentHouse,
    favorites,
    isLoading,
    error,
    useMock,
    isFavorite,
    fetchHouseList,
    fetchHouseById,
    fetchSellerHouses,
    updateHouse,
    createHouse,
    deleteHouse,
    fetchTags,
    toggleFavorite,
    fetchFavorites,
    getHouseById
  }
})
