import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useFavoriteStore = defineStore('favorite', () => {
  const favorites = ref([])
  const isLoading = ref(false)
  const error = ref(null)

  const isFavorite = computed(() => (id) => {
    return favorites.value.some(f => f.h_id === id)
  })

  async function fetchFavorites(buyerId) {
    if (!buyerId) return
    isLoading.value = true
    try {
      const response = await fetch('http://localhost:8080/api/buyer/follows', {
        headers: { 'buyerId': buyerId }
      })
      if (!response.ok) throw new Error('Failed to fetch favorites')
      const data = await response.json()
      favorites.value = data
    } catch (err) {
      console.error('Fetch favorites error', err)
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }

  async function toggleFavorite(houseId, buyerId) {
    if (!buyerId) return

    const isFav = favorites.value.some(f => f.h_id === houseId)
    const method = isFav ? 'DELETE' : 'POST'

    try {
      const response = await fetch(`http://localhost:8080/api/buyer/follows/${houseId}`, {
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

  return {
    favorites,
    isLoading,
    error,
    isFavorite,
    fetchFavorites,
    toggleFavorite
  }
})
