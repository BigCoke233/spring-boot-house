<script setup>
import { onMounted } from 'vue'
import PageContainer from '@/layouts/PageContainer.vue';
import HouseCard from '@/components/HouseCard.vue';
import { useHouseStore } from '@/stores/house.js'

const houseStore = useHouseStore()

const categories = [
  { name: "decorated", label: "精装房", active: true },
  { name: "unfinished", label: "毛胚房", active: false }
]

onMounted(() => {
  houseStore.fetchHouseList()
})
</script>

<template>
  <PageContainer>
    <header class="flex justify-between items-center my-6">
      <h2 class="text-3xl font-serif font-extrabold">所有房源</h2>
      <div class="flex gap-2">
        <button class="text-lg py-1 px-3 b-none rd-lg cursor-pointer"
          :class="category.active
            ? 'bg-black text-white'
            : 'bg-neutral-200/20 b-1 b-solid b-neutral-200/50'"
          v-for="category in categories" :key="category.name">{{ category.label }}</button>
      </div>
    </header>
    
    <div v-if="houseStore.isLoading && !houseStore.houseList.length" class="text-center py-10">
      Loading...
    </div>
    
    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 md:gap-6">
      <HouseCard v-for="house in houseStore.houseList" :key="house.id" :data="house" />
    </div>
  </PageContainer>
</template>
