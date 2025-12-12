<script setup>
import PageContainer from '@/layouts/PageContainer.vue';
import HouseCard from '@/components/HouseCard.vue';

const items = [1,2,3,4,5,6,7,8]

const categories = [
  { name: "decorated", label: "精装房", active: true },
  { name: "unfinished", label: "毛胚房", active: false }
]
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
    <div class="grid grid-cols-3 gap-4 md:gap-6">
      <HouseCard v-for="item in items" :key="item" />
    </div>
  </PageContainer>
</template>
