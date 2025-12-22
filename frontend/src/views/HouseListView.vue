<script setup>
import HouseListAll from '@/components/HouseListAll.vue';
import HouseListBanner from '@/components/HouseListBanner.vue';
import HouseListBySeller from '@/components/HouseListBySeller.vue';
import PageContainer from '@/layouts/PageContainer.vue';


</script>

<template>
  <PageContainer class="my-20">
    <HouseListBanner />
    <HouseListAll />
    <HouseListBySeller />
  </PageContainer>
</template>
