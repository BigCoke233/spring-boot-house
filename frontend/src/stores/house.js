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
    coordinates: [23.1291, 113.2644],
    auditStatus: 'approved',
    tags: [1, 3]
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
    coordinates: [22.5431, 114.0579],
    auditStatus: 'pending',
    tags: [2, 4]
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
    coordinates: [31.2304, 121.4737],
    auditStatus: 'approved',
    tags: [5, 6]
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
    coordinates: [39.9042, 116.4074],
    auditStatus: 'rejected',
    tags: [1, 4]
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
    coordinates: [18.2528, 109.5120],
    auditStatus: 'approved',
    tags: [2, 7]
  },
  {
    id: 6,
    name: '云顶天宫 · F6',
    description: '云端之上的奢华享受，俯瞰全城美景。',
    address: '成都市锦江区天府广场',
    price: 105000,
    square: 200,
    image: '',
    sellerId: 1,
    developer: '万科地产',
    coordinates: [30.6586, 104.0648],
    auditStatus: 'approved',
    tags: [3, 4]
  },
  {
    id: 7,
    name: '湖畔雅苑 · G7',
    description: '临湖而居，宁静致远。',
    address: '杭州市西湖区南山路',
    price: 110000,
    square: 150,
    image: '',
    sellerId: 2,
    developer: '绿城中国',
    coordinates: [30.2458, 120.1479],
    auditStatus: 'approved',
    tags: [5, 7]
  },
  {
    id: 8,
    name: '古城别院 · H8',
    description: '传统中式庭院，传承千年文化。',
    address: '苏州市姑苏区平江路',
    price: 95000,
    square: 180,
    image: '',
    sellerId: 3,
    developer: '融创中国',
    coordinates: [31.3095, 120.6256],
    auditStatus: 'pending',
    tags: [6, 8]
  },
  {
    id: 9,
    name: '都会中心 · I9',
    description: '双地铁上盖，繁华商圈。',
    address: '重庆市渝中区解放碑',
    price: 85000,
    square: 100,
    image: '',
    sellerId: 1,
    developer: '龙湖地产',
    coordinates: [29.5630, 106.5516],
    auditStatus: 'approved',
    tags: [1, 3]
  },
  {
    id: 10,
    name: '森林氧吧 · J10',
    description: '置身森林公园，呼吸纯净氧气。',
    address: '昆明市盘龙区世博园',
    price: 70000,
    square: 110,
    image: '',
    sellerId: 2,
    developer: '华侨城',
    coordinates: [25.0706, 102.7609],
    auditStatus: 'approved',
    tags: [2, 5]
  }
]

const MOCK_TAGS = [
  { id: 1, name: '近地铁' },
  { id: 2, name: '海景' },
  { id: 3, name: '商圈' },
  { id: 4, name: '学区' },
  { id: 5, name: '公园' },
  { id: 6, name: '古镇' },
  { id: 7, name: '度假' },
  { id: 8, name: '别墅' }
]

const MOCK_DEVELOPERS = [
  {
    name: "万科地产",
    description: "万科企业股份有限公司成立于1984年，经过三十余年的发展，已成为国内领先的城乡建设与生活服务商。"
  },
  {
    name: "恒大地产",
    description: "恒大地产集团有限公司是中国恒大集团的下属控股企业，是集团的地产业务主体。"
  },
  {
    name: "绿地集团",
    description: "绿地控股集团有限公司是一家全球经营的多元化企业集团，创立于1992年7月18日。"
  },
  {
    name: "保利地产",
    description: "保利发展控股集团股份有限公司（600048.SH）致力于打造“不动产生态发展平台”。"
  },
  {
    name: "碧桂园",
    description: "碧桂园是为全世界创造美好生活产品的高科技综合性企业。"
  },
  {
    name: "融创中国",
    description: "融创中国控股有限公司（01918.HK），香港联交所上市企业。"
  },
  {
    name: "龙湖地产",
    description: "龙湖集团1993年创建于重庆，发展于全国，业务涵盖地产开发、商业投资、租赁住房等。"
  },
  {
    name: "华侨城",
    description: "华侨城集团是国务院国资委直接管理的大型中央企业。"
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

        let results = [...MOCK_HOUSES]

        // Filter by keyword (name or address)
        if (filters.keyword) {
          const lowerKeyword = filters.keyword.toLowerCase()
          results = results.filter(h =>
            h.name.toLowerCase().includes(lowerKeyword) ||
            h.address.toLowerCase().includes(lowerKeyword)
          )
        }

        // Filter by price
        if (filters.minPrice) results = results.filter(h => h.price >= filters.minPrice)
        if (filters.maxPrice) results = results.filter(h => h.price <= filters.maxPrice)

        // Filter by square
        if (filters.minSquare) results = results.filter(h => h.square >= filters.minSquare)
        if (filters.maxSquare) results = results.filter(h => h.square <= filters.maxSquare)

        // Filter by auditStatus
        if (filters.auditStatus) results = results.filter(h => h.auditStatus === filters.auditStatus)

        // Filter by tags
        if (filters.tagIds && filters.tagIds.length > 0) {
           results = results.filter(h =>
             h.tags && filters.tagIds.some(tagId => h.tags.includes(tagId))
           )
        }

        houseList.value = results
        return results
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

  async function updateHouse(houseData) {
    isLoading.value = true
    error.value = null
    try {
        if (useMock.value) {
            await new Promise(resolve => setTimeout(resolve, 500))
            const index = MOCK_HOUSES.findIndex(h => h.id === houseData.id)
            if (index !== -1) {
                MOCK_HOUSES[index] = { ...MOCK_HOUSES[index], ...houseData }
                // Also update local list if present
                const localIndex = houseList.value.findIndex(h => h.id === houseData.id)
                if (localIndex !== -1) {
                    houseList.value[localIndex] = { ...houseList.value[localIndex], ...houseData }
                }
                if (currentHouse.value && currentHouse.value.id === houseData.id) {
                    currentHouse.value = { ...currentHouse.value, ...houseData }
                }
            }
            return MOCK_HOUSES[index]
        }

        const response = await fetch(`/api/house/${houseData.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(houseData)
        })
        if (!response.ok) throw new Error('Failed to update house')
        const data = await response.json()

        // Update local state
        const index = houseList.value.findIndex(h => h.id === data.id)
        if (index !== -1) houseList.value[index] = data
        if (currentHouse.value && currentHouse.value.id === data.id) currentHouse.value = data

        return data
    } catch (err) {
        console.error('Update house error:', err)
        error.value = err.message
        if (!useMock.value) {
            // Fallback to mock logic for seamless testing if API fails
            const index = MOCK_HOUSES.findIndex(h => h.id === houseData.id)
            if (index !== -1) {
                 MOCK_HOUSES[index] = { ...MOCK_HOUSES[index], ...houseData }
                 return MOCK_HOUSES[index]
            }
        }
        throw err
    } finally {
        isLoading.value = false
    }
  }

  async function createHouse(houseData) {
    isLoading.value = true
    error.value = null
    try {
        if (useMock.value) {
            await new Promise(resolve => setTimeout(resolve, 500))
            const newId = Math.max(...MOCK_HOUSES.map(h => h.id), 0) + 1
            const newHouse = {
                ...houseData,
                id: newId,
                auditStatus: 'pending',
                sellerId: houseData.sellerId || 1 // Default to 1 if not provided
            }
            MOCK_HOUSES.push(newHouse)
            houseList.value.push(newHouse)
            return newHouse
        }

        const response = await fetch('/api/house', {
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
        if (!useMock.value) {
             const newId = Math.max(...MOCK_HOUSES.map(h => h.id), 0) + 1
             const newHouse = {
                ...houseData,
                id: newId,
                auditStatus: 'pending',
                sellerId: houseData.sellerId || 1
            }
            MOCK_HOUSES.push(newHouse)
            houseList.value.push(newHouse)
            return newHouse
        }
        throw err
    } finally {
        isLoading.value = false
    }
  }

  async function deleteHouse(id) {
    isLoading.value = true
    error.value = null
    try {
        if (useMock.value) {
            await new Promise(resolve => setTimeout(resolve, 500))
            const index = MOCK_HOUSES.findIndex(h => h.id === id)
            if (index !== -1) {
                MOCK_HOUSES.splice(index, 1)
                const localIndex = houseList.value.findIndex(h => h.id === id)
                if (localIndex !== -1) {
                    houseList.value.splice(localIndex, 1)
                }
                if (currentHouse.value && currentHouse.value.id === id) {
                    currentHouse.value = null
                }
            }
            return true
        }

        const response = await fetch(`/api/house/${id}`, {
            method: 'DELETE'
        })
        if (!response.ok) throw new Error('Failed to delete house')

        const localIndex = houseList.value.findIndex(h => h.id === id)
        if (localIndex !== -1) {
            houseList.value.splice(localIndex, 1)
        }
        if (currentHouse.value && currentHouse.value.id === id) {
            currentHouse.value = null
        }
        return true
    } catch (err) {
        console.error('Delete house error:', err)
        error.value = err.message
        if (!useMock.value) {
             const index = MOCK_HOUSES.findIndex(h => h.id === id)
             if (index !== -1) {
                 MOCK_HOUSES.splice(index, 1)
                 const localIndex = houseList.value.findIndex(h => h.id === id)
                 if (localIndex !== -1) {
                     houseList.value.splice(localIndex, 1)
                 }
                 return true
             }
        }
        throw err
    } finally {
        isLoading.value = false
    }
  }

  async function fetchTags() {
    if (useMock.value) {
      return MOCK_TAGS
    }
    // Real API implementation if needed
    try {
        const response = await fetch('/api/tags')
        if (!response.ok) throw new Error('Failed to fetch tags')
        return await response.json()
    } catch (err) {
        console.error('Fetch tags error:', err)
        return MOCK_TAGS // Fallback
    }
  }

  async function fetchDevelopers() {
    if (useMock.value) {
      return MOCK_DEVELOPERS
    }
    // Real API implementation if needed
    // return []
    return MOCK_DEVELOPERS // Fallback for now
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
    updateHouse,
    createHouse,
    deleteHouse,
    fetchTags,
    fetchDevelopers,
    toggleFavorite,
    getHouseById,
    setHouses,
    setHouse
  }
})
