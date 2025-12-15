<script setup>
import PageContainer from '@/layouts/PageContainer.vue';
import ContractCard from '@/components/ContractCard.vue';
</script>

<template>
  <PageContainer class="my-20">
    <h1 class="text-2xl font-bold">合同管理</h1>
    <div class="bg-neutral-100/20 p-6">
      <ContractCard />
    </div>
  </PageContainer>
</template>
