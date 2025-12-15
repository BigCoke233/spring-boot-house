import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useHouseStore = defineStore('house', () => {
  const housesById = ref({})

  function setHouses(items) {
    const map = {}
    for (const h of items || []) map[h.id] = h
    housesById.value = map
  }

  function setHouse(h) {
    if (!h || h.id == null) return
    housesById.value[h.id] = h
  }

  function getHouseById(id) {
    if (id == null) return null
    return housesById.value[id] ?? null
  }

  return { housesById, setHouses, setHouse, getHouseById }
})

