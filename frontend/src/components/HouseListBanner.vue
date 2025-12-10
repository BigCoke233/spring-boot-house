<script setup>
import PageContainer from '@/layouts/PageContainer.vue';
</script>

<template>
  <PageContainer>
    <div class="shadow rd-lg w-full h-60 overflow-hidden flex items-center relative">
      <img src="/img/house-1.jpg" class="block w-full h-auto" />
      <router-link to="/house/1"
        class="absolute right-4 bottom-4 bg-white py-1 px-2 rd text-sm">
        查看详情 →
      </router-link>
    </div>
  </PageContainer>
</template>
