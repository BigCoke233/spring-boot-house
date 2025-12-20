<script setup>
import { onMounted } from 'vue'
import PageContainer from '@/layouts/PageContainer.vue'
import ContractCard from '@/components/ContractCard.vue'
import { useHouseStore } from '@/stores/house.js'
import { useContractStore } from '@/stores/contract.js'

const houseStore = useHouseStore()
const contractStore = useContractStore()

onMounted(async () => {
  // Ensure house data is available for mapping
  await houseStore.fetchHouses()
  // Fetch contracts
  await contractStore.fetchContractList()
})
</script>

<template>
  <PageContainer class="my-20">
    <header>
      <h1 class="text-2xl font-bold">合同管理</h1>
    </header>
    <div v-if="contractStore.isLoading" class="text-center p-8 text-neutral-500">
        加载中...
    </div>
    <div v-else-if="contractStore.error" class="text-center p-8 text-red-500">
        {{ contractStore.error }}
    </div>
    <div v-else class="space-y-6">
      <div v-if="contractStore.contractList.length === 0" class="text-center p-8 text-neutral-500">
          暂无合同
      </div>
      <div v-for="c in contractStore.contractList" :key="c.contractId" class="bg-neutral-100/20 p-6 rd">
        <ContractCard :data="c" />
      </div>
    </div>
  </PageContainer>
</template>
