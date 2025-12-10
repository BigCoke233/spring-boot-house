<script setup>
import PageContainer from '@/layouts/PageContainer.vue';

const sellers = [
  {
    name: "地产商A",
    description: "地产商A是一家专注于城市更新和住宅开发的公司，致力于为客户提供高品质的居住环境。"
  },
  {
    name: "地产商B",
    description: "地产商B是一家专注于商业地产和旅游地产的公司，致力于为客户提供多元化和个性化的居住体验。"
  },
  {
    name: "地产商C",
    description: "地产商C是一家专注于绿色建筑和可持续发展的公司，致力于为客户提供环保和健康的生活方式。"
  }
]
</script>

<template>
  <div class="bg-neutral-100/50 my-20 py-10">
    <PageContainer>
      <h2 class="text-3xl">精选地产商</h2>
      <div class="grid grid-cols-2 gap-4 md:gap-8">
        <div v-for="seller in sellers" :key="seller.name"
          class="bg-white shadow-md rd-lg p-6 py-2">
          <h3 class="text-xl">{{ seller.name }}</h3>
          <p>{{ seller.description }}</p>
        </div>
      </div>
    </PageContainer>
  </div>
</template>
