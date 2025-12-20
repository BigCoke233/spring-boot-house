<script setup>
import HouseCard from '@/components/HouseCard.vue';
import PageContainer from '@/layouts/PageContainer.vue';
import { useHouseStore } from '@/stores/house.js';

const houseStore = useHouseStore();
</script>

<template>
  <PageContainer class="my-20">
    <div class="p-4">
      <h1 class="text-2xl font-bold">收藏房源</h1>
    </div>
    <div v-if="houseStore.favorites.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <HouseCard v-for="house in houseStore.favorites" :key="house.id" :data="house" /> 
    </div>
    <div v-else class="p-4 text-center text-neutral-500">
      暂无收藏房源
    </div>
  </PageContainer>
</template>
