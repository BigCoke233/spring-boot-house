<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useHouseStore } from '@/stores/house.js'
import HouseCard from '@/components/HouseCard.vue'
import PageContainer from '@/layouts/PageContainer.vue'
import AppButton from '@/components/AppButton.vue'

const houseStore = useHouseStore()

const filters = reactive({
  keyword: '',
  minPrice: null,
  maxPrice: null,
  minSquare: null,
  maxSquare: null,
  auditStatus: '',
  tagIds: []
})

const availableTags = ref([])

onMounted(async () => {
  handleSearch()
})

watch(() => houseStore.houseList, (newVal) => {
  const existingTags = new Set(availableTags.value.map(t => t.id))
  newVal.forEach(house => {
    if (house.tagNames) {
      house.tagNames.forEach(tagName => {
        if (!existingTags.has(tagName)) {
           availableTags.value.push({ id: tagName, name: tagName })
           existingTags.add(tagName)
        }
      })
    }
  })
}, { deep: true })

const handleSearch = () => {
  const payload = { ...filters }
  if (payload.tagIds && payload.tagIds.length > 0) {
    payload.tag = payload.tagIds[0]
  }
  delete payload.tagIds
  houseStore.fetchHouseList(payload)
}

const toggleTag = (tagId) => {
  if (filters.tagIds.includes(tagId)) {
    filters.tagIds = []
  } else {
    filters.tagIds = [tagId]
  }
  handleSearch()
}
</script>

<template>
  <PageContainer class="my-20">
    <header class="mb-8">
      <h1 class="text-3xl font-serif font-extrabold text-gray-900">房源列表</h1>
      <p class="text-gray-500 mt-2">浏览所有可用房源，发现您的梦想家园</p>
    </header>

    <!-- Search & Filters -->
    <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 mb-8">
      <!-- Top Row: Search -->
      <div class="flex gap-4 mb-6">
        <div class="flex-1 relative">
          <input
            v-model="filters.keyword"
            @keyup.enter="handleSearch"
            type="text"
            placeholder="搜索房源名称或地址..."
            class="w-full pl-4 pr-4 py-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-black focus:border-transparent transition-all"
          >
        </div>
        <AppButton @click="handleSearch" class="px-8">搜索</AppButton>
      </div>

      <!-- Filter Grid -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
        <!-- Price Range -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">价格范围 (元/m²)</label>
          <div class="flex items-center gap-2">
            <input v-model.number="filters.minPrice" @change="handleSearch" type="number" placeholder="最低" class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-1 focus:ring-black">
            <span class="text-gray-400">-</span>
            <input v-model.number="filters.maxPrice" @change="handleSearch" type="number" placeholder="最高" class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-1 focus:ring-black">
          </div>
        </div>

        <!-- Square Range -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">面积范围 (m²)</label>
          <div class="flex items-center gap-2">
            <input v-model.number="filters.minSquare" @change="handleSearch" type="number" placeholder="最小" class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-1 focus:ring-black">
            <span class="text-gray-400">-</span>
            <input v-model.number="filters.maxSquare" @change="handleSearch" type="number" placeholder="最大" class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-1 focus:ring-black">
          </div>
        </div>
      </div>

      <!-- Tags -->
      <div v-if="availableTags.length > 0">
        <label class="block text-sm font-medium text-gray-700 mb-2">特色标签</label>
        <div class="flex flex-wrap gap-2">
          <button
            v-for="tag in availableTags"
            :key="tag.id"
            @click="toggleTag(tag.id)"
            class="px-3 py-1.5 rounded-full text-sm border transition-all duration-200"
            :class="filters.tagIds.includes(tag.id)
              ? 'bg-black text-white border-black'
              : 'bg-white border-gray-200 text-gray-600 hover:border-gray-400'"
          >
            {{ tag.name }}
          </button>
        </div>
      </div>

      <!-- Apply Filters Button -->
      <!-- <div class="mt-8 flex justify-end border-t border-gray-100 pt-6">
        <AppButton variant="secondary" @click="handleSearch">应用筛选条件</AppButton>
      </div> -->
    </div>

    <!-- Results -->
    <div v-if="houseStore.isLoading" class="flex justify-center items-center py-20">
      <div class="animate-spin rounded-full h-10 w-10 border-4 border-gray-200 border-t-black"></div>
    </div>

    <div v-else-if="houseStore.houseList.length === 0" class="text-center py-20 bg-gray-50 rounded-xl border border-dashed border-gray-300">
      <p class="text-gray-500 text-lg">未找到符合条件的房源</p>
      <button @click="() => {
        Object.assign(filters, { keyword: '', minPrice: null, maxPrice: null, minSquare: null, maxSquare: null, auditStatus: '', tagIds: [] });
        handleSearch();
      }" class="mt-4 text-blue-600 hover:underline">
        清除所有筛选
      </button>
    </div>

    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <HouseCard v-for="house in houseStore.houseList" :key="house.id" :data="house" />
    </div>
  </PageContainer>
</template>
