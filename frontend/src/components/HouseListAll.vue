<script setup>
import { onMounted, computed } from 'vue'
import PageContainer from '@/layouts/PageContainer.vue';
import HouseCard from '@/components/HouseCard.vue';
import { useHouseStore } from '@/stores/house.js'

const props = defineProps({
  limit: {
    type: Number,
    default: 0
  }
})

const houseStore = useHouseStore()
const houseList = computed(() => {
  const list = houseStore.houseList || []
  if (props.limit && props.limit > 0) {
    return list.slice(0, props.limit)
  }
  return list
})

onMounted(() => {
  houseStore.fetchHouseList()
})
</script>

<template>
  <PageContainer>
    <header class="flex justify-between items-center my-6">
      <h2 class="text-3xl font-serif font-extrabold">所有房源</h2>
      <div class="flex gap-2">
        <router-link to="/houses"
          class="text-lg py-1 px-3 b-none rd-lg cursor-pointer bg-black text-white">
          查看全部
        </router-link>
      </div>
    </header>

    <div v-if="houseStore.isLoading && !houseList.length" class="text-center py-10">
      Loading...
    </div>

    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 md:gap-6">
      <HouseCard v-for="house in houseList" :key="house.h_id || house.id" :data="house" />
    </div>
  </PageContainer>
</template>
