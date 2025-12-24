<script setup>
import { onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useHouseStore } from '@/stores/house.js'
import LeafletMap from '@/components/LeafletMap.vue';
import PageContainer from '@/layouts/PageContainer.vue';

const route = useRoute()
const houseStore = useHouseStore()

const detail = computed(() => houseStore.currentHouse || {})
const totalPrice = computed(() => {
  if (!detail.value.price || !detail.value.square) return 0
  return (detail.value.price * detail.value.square).toLocaleString()
})

onMounted(() => {
  const id = route.params.id
  if (id) {
    houseStore.fetchHouseById(id)
  }
})
</script>

<template>
  <PageContainer class="my-30">
    <div v-if="houseStore.isLoading" class="text-center py-20">
      Loading...
    </div>
    <div v-else-if="houseStore.error" class="text-center py-20 text-red-500">
      {{ houseStore.error }}
    </div>
    <header v-else-if="houseStore.currentHouse" class="grid grid-cols-2 gap-4 md:gap-10">
      <!-- Leaflet 地图 -->
      <LeafletMap :center="detail.coordinates || [47.41322, -1.219482]" />
      <div class>
        <!-- 房源基本信息 -->
        <header class="bg-neutral-200/20 p-4 md:p-6 rd-lg">
          <div class="mb-3">
            <h1 class="text-xl font-bold">{{ detail.name }}</h1>
            <p class="text-neutral text-sm">{{ detail.address }}</p>
          </div>
          <div class="flex gap-1 items-end">
            <p class="text-3xl font-serif font-extrabold text-red-6">¥{{ totalPrice }}</p>
            <p class="text-neutral text-sm mb-1"> (单价: ¥{{ detail.price }}/m²)</p>
          </div>
          <div class="mt-2 text-lg">
             <p>面积: {{ detail.square }} m<sup>2</sup></p>
          </div>
          <div class="text-neutral-600 mt-3">
            <p>{{ detail.description }}</p>
          </div>
        </header>
        <!--  开发商信息 -->
        <section class="flex items-center gap-6 bg-neutral-200/20 p-4 md:p-6 rd-lg my-4">
          <div class="h-15 w-15 bg-neutral-300 rd-full flex items-center justify-center text-xl font-bold text-white">
            {{ detail.sellerName ? detail.sellerName[0] : 'S' }}
          </div>
          <div>
            <h3 class="font-bold">{{ detail.sellerName || '未知卖家' }}</h3>
            <p class="text-sm text-neutral-500">{{ detail.sellerPhone || detail.sellerEmail }}</p>
          </div>
        </section>
        <!-- 按钮组 -->
        <section class="my-4 flex gap-4">
          <button class="p-6 py-2 text-xl bg-black shadow rd text-white hover:opacity-90 transition">咨询购买</button>
          <button class="p-6 py-2 text-xl bg-neutral-300/50 rd hover:bg-neutral-400/50 transition">加入收藏</button>
        </section>
      </div>
    </header>
    <div v-else class="text-center py-20">
      未找到房源信息
    </div>
  </PageContainer>
</template>
