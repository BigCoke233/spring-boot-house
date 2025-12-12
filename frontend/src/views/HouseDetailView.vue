<script setup>
import LeafletMap from '@/components/LeafletMap.vue';
import PageContainer from '@/layouts/PageContainer.vue';

const detail = {
  name: 'House Detail',
  description: 'Detailed information about the house',
  address: '123 Main St',
  price: '500,000',
  sqm: 1500,
  image: '/house.jpg'
}
</script>

<template>
  <PageContainer class="my-30">
    <header class="grid grid-cols-2 gap-4 md:gap-10">
      <!-- Leaflet 地图 -->
      <LeafletMap :center="[47.41322, -1.219482]" />
      <div class>
        <!-- 房源基本信息 -->
        <header class="bg-neutral-200/20 p-4 md:p-6 rd-lg">
          <div class="mb-3">
            <h1 class="text-xl font-bold">{{ detail.name }}</h1>
            <p class="text-neutral text-sm">{{ detail.address }}</p>
          </div>
          <div class="flex gap-1 items-end">
            <p class="text-3xl font-serif font-extrabold">¥{{ detail.price }}</p>
            <p>/</p>
            <p>{{ detail.sqm }} m<sup>2</sup></p>
          </div>
          <div class="text-lg mt-3">
            <p>{{ detail.description }}</p>
          </div>
        </header>
        <!--  开发商信息 -->
        <section class="flex items-center gap-6 bg-neutral-200/20 p-4 md:p-6 rd-lg my-4">
          <div class="h-15 w-15 bg-neutral rd-full"></div>
          <div>
            <h3 class="font-bold">开发商名称</h3>
            <p>开发商介绍</p>
          </div>
        </section>
        <!-- 按钮组 -->
        <section class="my-4 flex gap-4">
          <button class="p-6 py-2 text-xl bg-black shadow rd text-white">咨询购买</button>
          <button class="p-6 py-2 text-xl bg-neutral-300/50 rd">加入收藏</button>
        </section>
      </div>
    </header>
  </PageContainer>
</template>
