<script setup>
import HouseCard from '@/components/HouseCard.vue';
import PageContainer from '@/layouts/PageContainer.vue';

import { ref } from 'vue'

const favorites = ref([])
// generate testing fake data
for (let i = 0; i < 10; i++) {
  favorites.value.push({
      id: 1,
      name: `房源 ${i}`,
      price: 8000,
      square: 100,
      location: "房源地址",
      image: "",
    })
}
</script>

<template>
  <PageContainer class="my-20">
    <div class="p-4">
      <h1 class="text-2xl font-bold">收藏房源</h1>
    </div>
    <div class="grid grid-cols-3 gap-4">
        <HouseCard v-for="house in favorites" :key="house.id" :data="house" /> 
    </div>
  </PageContainer>
</template>
