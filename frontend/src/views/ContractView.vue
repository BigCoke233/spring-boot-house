<script setup>
import { ref, onMounted } from 'vue'
import PageContainer from '@/layouts/PageContainer.vue'
import ContractCard from '@/components/ContractCard.vue'
import { useHouseStore } from '@/stores/house.js'

const contracts = ref([
  {
    contractId: 1001,
    buyerId: 501,
    houseId: 1,
    sellerId: 201,
    totalPrice: 980000,
    payWay: 'installment',
    paymentStatus: false,
    completionStatus: false,
    agreeStatus: false,
    buyerAgree: true,
    sellerAgree: true,
    paid: false,
    delivered: false,
    paytimeEnding: '2026-03-31',
    paytimeActually: null,
    deliveryEnding: '2026-05-31',
    deliveryActually: null,
    totalPeriods: 12,
    paidCount: 3,
    downPaymentPaid: true,
  },
  {
    contractId: 1002,
    buyerId: 502,
    houseId: 2,
    sellerId: 202,
    totalPrice: 1200000,
    payWay: 'full',
    paymentStatus: true,
    completionStatus: false,
    agreeStatus: true,
    buyerAgree: true,
    sellerAgree: true,
    paid: true,
    delivered: false,
    paytimeEnding: '2025-12-31',
    paytimeActually: '2025-10-20',
    deliveryEnding: '2026-02-28',
    deliveryActually: null,
    totalPeriods: 0,
    paidCount: 0,
    downPaymentPaid: false,
  },
  {
    contractId: 1003,
    buyerId: 503,
    houseId: 3,
    sellerId: 203,
    totalPrice: 860000,
    payWay: 'installment',
    paymentStatus: false,
    completionStatus: false,
    agreeStatus: false,
    buyerAgree: false,
    sellerAgree: true,
    paid: false,
    delivered: false,
    paytimeEnding: '2026-04-15',
    paytimeActually: null,
    deliveryEnding: '2026-06-30',
    deliveryActually: null,
    totalPeriods: 24,
    paidCount: 0,
    downPaymentPaid: false,
  },
])

const houseStore = useHouseStore()
onMounted(() => {
  houseStore.setHouses([
    { id: 1, name: '映月城 · A1', price: 9800, square: 92, location: '广州市天河区' },
    { id: 2, name: '星河湾 · B2', price: 12500, square: 105, location: '深圳市南山区' },
    { id: 3, name: '绿地花园 · C3', price: 8800, square: 89, location: '上海市浦东新区' },
  ])
})
</script>

<template>
  <PageContainer class="my-20">
    <header>
      <h1 class="text-2xl font-bold">合同管理</h1>
    </header>
    <div class="">
      <div v-for="c in contracts" :key="c.contractId" class="bg-neutral-100/20 p-6 rd">
        <ContractCard :data="c" />
      </div>
    </div>
  </PageContainer>
</template>
