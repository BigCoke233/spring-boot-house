<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'

import PageContainer from '@/layouts/PageContainer.vue'
import ContractCard from '@/components/ContractCard.vue'
import PurchaseContractBox from '@/components/PurchaseContractBox.vue'
import AppButton from '@/components/AppButton.vue'

const route = useRoute()

const contract = ref({
  contractId: Number(route.params.id) || 1,
  buyerId: 1,
  houseId: 1,
  sellerId: 1,
  totalPrice: 800000,
  payWay: 'installment',
  paymentStatus: false,
  completionStatus: false,
  agreeStatus: false,
  buyerAgree: true,
  sellerAgree: false,
  paid: false,
  delivered: false,
  paytimeEnding: '2026-03-31',
  paytimeActually: null,
  deliveryEnding: '2026-05-31',
  deliveryActually: null,
  totalPeriods: 12,
  paidCount: 3,
  downPaymentPaid: false,
})

const partyA = ref({
  name: '甲方姓名',
  id: '110101199001010000',
  phone: '13800000000',
  address: '甲方地址',
})

const partyB = ref({
  name: '乙方姓名',
  id: '110101199001010001',
  phone: '13900000000',
  address: '乙方地址',
})

const userRole = "seller" // or "seller"
const notSigned = (userRole == "buyer" && contract.value.buyerAgree == false)
  || (userRole == "seller" && contract.value.sellerAgree == false)

function handleSign() {
  // 签署合同逻辑……
}
</script>

<template>
  <PageContainer class="my-20">
    <ContractCard :data="contract" />
  </PageContainer>
  <div class="bg-neutral-300/20 p-6 rd-xl">
    <PageContainer class="my-20 space-y-8">
        <PurchaseContractBox :partyA="partyA" :partyB="partyB" :contract="contract" />
        <div v-if="notSigned" class="flex justify-center">
          <AppButton @click="handleSign">签字确认</AppButton>
        </div>
    </PageContainer>
  </div>
</template>
