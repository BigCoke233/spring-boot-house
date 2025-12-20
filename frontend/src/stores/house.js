import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

// Mock Data
const MOCK_HOUSES = [
  {
    id: 1,
    name: '映月城 · A1',
    description: '位于市中心的高端住宅，交通便利，周边设施齐全。拥有超大阳台和落地窗，采光极佳。',
    address: '广州市天河区珠江新城花城大道1号',
    price: 98000, // Unit price
    square: 92,
    image: '',
    sellerId: 1,
    developer: '万科地产',
    coordinates: [23.1291, 113.2644]
  },
  {
    id: 2,
    name: '星河湾 · B2',
    description: '一线江景房，豪华装修，拎包入住。社区环境优美，配套国际学校。',
    address: '深圳市南山区深圳湾1号',
    price: 125000,
    square: 105,
    image: '',
    sellerId: 1,
    developer: '恒大地产',
    coordinates: [22.5431, 114.0579]
  },
  {
    id: 3,
    name: '绿地花园 · C3',
    description: '高性价比刚需盘，适合年轻家庭。绿化率高，空气清新。',
    address: '上海市浦东新区张江高科园区',
    price: 88000,
    square: 89,
    image: '',
    sellerId: 2,
    developer: '绿地集团',
    coordinates: [31.2304, 121.4737]
  },
  {
    id: 4,
    name: '阳光雅居 · D4',
    description: '学区房，紧邻重点小学。小区安保严密，居住舒心。',
    address: '北京市海淀区中关村大街',
    price: 110000,
    square: 120,
    image: '',
    sellerId: 2,
    developer: '保利地产',
    coordinates: [39.9042, 116.4074]
  },
  {
    id: 5,
    name: '滨海豪庭 · E5',
    description: '坐拥无敌海景，私家沙滩。度假养老首选之地。',
    address: '三亚市亚龙湾度假区',
    price: 65000,
    square: 135,
    image: '',
    sellerId: 3,
    developer: '碧桂园',
    coordinates: [18.2528, 109.5120]
  }
]

export const useHouseStore = defineStore('house', () => {
  // State
  const houseList = ref([])
  const currentHouse = ref(null)
  const favorites = ref(JSON.parse(localStorage.getItem('house_favorites') || '[]'))
  const isLoading = ref(false)
  const error = ref(null)
  const useMock = ref(true) // Toggle for mock data

  // Getters
  const getHouseById = computed(() => (id) => {
    return houseList.value.find(h => h.id == id) || MOCK_HOUSES.find(h => h.id == id)
  })

  const isFavorite = computed(() => (id) => {
    return favorites.value.some(f => f.id === id)
  })

  // Actions
  async function fetchHouseList(filters = {}) {
    isLoading.value = true
    error.value = null
    try {
      if (useMock.value) {
        // Simulate API delay
        await new Promise(resolve => setTimeout(resolve, 500))
        houseList.value = MOCK_HOUSES
        return MOCK_HOUSES
      }

      const query = new URLSearchParams(filters).toString()
      const response = await fetch(`/api/house?${query}`)
      if (!response.ok) throw new Error('Failed to fetch house list')
      const data = await response.json()
      houseList.value = data
      return data
    } catch (err) {
      console.error('Fetch house list error:', err)
      error.value = err.message
      // Fallback to mock on error if desired, or just throw
      if (!useMock.value) {
          console.warn('Falling back to mock data due to API error')
          houseList.value = MOCK_HOUSES
          return MOCK_HOUSES
      }
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
        const house = MOCK_HOUSES.find(h => h.id == id)
        if (!house) throw new Error('House not found')
        currentHouse.value = house
        return house
      }

      const response = await fetch(`/api/house/${id}`)
      if (!response.ok) throw new Error('Failed to fetch house details')
      const data = await response.json()
      currentHouse.value = data
      return data
    } catch (err) {
      console.error(`Fetch house ${id} error:`, err)
      error.value = err.message
       if (!useMock.value) {
          const house = MOCK_HOUSES.find(h => h.id == id)
          if (house) {
             console.warn('Falling back to mock data')
             currentHouse.value = house
             return house
          }
      }
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function toggleFavorite(house) {
    if (!house) return
    
    // In a real app, you would also call an API endpoint here
    // try {
    //    await fetch(`/api/user/favorites/${house.id}`, { method: isFav ? 'DELETE' : 'POST' })
    // } catch (e) { ... }

    const index = favorites.value.findIndex(f => f.id === house.id)
    if (index === -1) {
      favorites.value.push(house)
    } else {
      favorites.value.splice(index, 1)
    }
    
    // Persist to localStorage
    localStorage.setItem('house_favorites', JSON.stringify(favorites.value))
  }

  // Helper compatibility methods from previous version
  function setHouses(items) {
    houseList.value = items
  }
  
  function setHouse(h) {
    currentHouse.value = h
    // Also update in list if exists
    const index = houseList.value.findIndex(item => item.id === h.id)
    if (index !== -1) {
      houseList.value[index] = h
    } else {
      houseList.value.push(h)
    }
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
    toggleFavorite,
    getHouseById,
    setHouses,
    setHouse
  }
})
