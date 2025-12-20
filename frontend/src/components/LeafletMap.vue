<script setup>
import { ref } from 'vue'
import { VMap, VMapOsmTileLayer, VMapZoomControl, VMapMarker } from 'vue-map-ui';

const props = defineProps({
  center: {
    type: Array,
    default: () => [48.8566, 2.3522]
  },
  zoom: {
    type: Number,
    default: 18
  },
  editable: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:center'])

const searchQuery = ref('')
const isSearching = ref(false)
const searchError = ref(null)

function handleMapClick(e) {
  if (props.editable && e.latlng) {
    emit('update:center', [e.latlng.lat, e.latlng.lng])
  }
}

async function handleSearch() {
  if (!searchQuery.value.trim()) return
  
  isSearching.value = true
  searchError.value = null
  
  const query = searchQuery.value.trim()
  
  // Try parsing as coordinates "lat, lng"
  // Supports "lat,lng", "lat, lng", "lat lng"
  const coordMatch = query.match(/^(-?\d+(\.\d+)?)[,\s]\s*(-?\d+(\.\d+)?)$/)
  if (coordMatch) {
    const lat = parseFloat(coordMatch[1])
    const lng = parseFloat(coordMatch[3])
    if (lat >= -90 && lat <= 90 && lng >= -180 && lng <= 180) {
      emit('update:center', [lat, lng])
      isSearching.value = false
      return
    }
  }

  // Try geocoding via Nominatim
  try {
    const response = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(query)}`)
    if (!response.ok) throw new Error('Search failed')
    const data = await response.json()
    
    if (data && data.length > 0) {
      const lat = parseFloat(data[0].lat)
      const lon = parseFloat(data[0].lon)
      emit('update:center', [lat, lon])
    } else {
      searchError.value = '未找到该地点'
    }
  } catch (e) {
    console.error(e)
    searchError.value = '搜索服务暂不可用'
  } finally {
    isSearching.value = false
  }
}
</script>

<template>
  <div class="rd-lg overflow-hidden shadow relative group">
    <div v-if="props.editable" class="absolute top-3 right-3 z-[1000] flex flex-col items-end gap-1 max-w-[240px]">
        <div class="bg-white p-1.5 rd shadow-md flex gap-2 items-center">
            <input 
                v-model="searchQuery" 
                @keyup.enter="handleSearch" 
                placeholder="搜索地名或 '纬度,经度'" 
                class="text-sm px-2 py-1 outline-none border border-transparent focus:border-neutral-300 rd w-40"
            />
            <button 
                @click="handleSearch" 
                class="bg-blue-600 text-white text-xs px-3 py-1.5 rd hover:bg-blue-700 transition disabled:opacity-50"
                :disabled="isSearching"
            >
                {{ isSearching ? '...' : '搜索' }}
            </button>
        </div>
        <div v-if="searchError" class="bg-red-100 text-red-600 text-xs px-2 py-1 rd shadow-sm">
            {{ searchError }}
        </div>
    </div>

    <VMap :center="props.center" :zoom="props.zoom" style="height: 500px;" @click="handleMapClick">
      <VMapOsmTileLayer />
      <VMapZoomControl />
      <VMapMarker v-if="props.center" :lat-lng="props.center" />
    </VMap>
  </div>
</template>
