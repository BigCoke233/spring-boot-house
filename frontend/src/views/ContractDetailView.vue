<script setup>
import { onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useContractStore } from '@/stores/contract.js'
import { useUserStore } from '@/stores/user.js'

import PageContainer from '@/layouts/PageContainer.vue'
import ContractCard from '@/components/ContractCard.vue'
import PurchaseContractBox from '@/components/PurchaseContractBox.vue'
import AppButton from '@/components/AppButton.vue'

const route = useRoute()
const contractStore = useContractStore()
const userStore = useUserStore()

const contract = computed(() => contractStore.currentContract)

const partyA = computed(() => {
    if (!contract.value) return {}
    return {
        name: contract.value.buyer?.name || '未知',
        id: contract.value.buyer?.idCard || '—',
        phone: contract.value.buyer?.phone || '—',
        address: '—', // Address not in mock currently
    }
})

const partyB = computed(() => {
    if (!contract.value) return {}
    return {
        name: contract.value.seller?.name || '未知',
        id: contract.value.seller?.idCard || '—',
        phone: contract.value.seller?.phone || '—',
        address: '—', // Address not in mock currently
    }
})

const notSigned = computed(() => {
    if (!contract.value) return false
    const role = userStore.user?.role
    if (role === 'buyer') return !contract.value.buyerAgree
    if (role === 'seller') return !contract.value.sellerAgree
    return false
})

async function handleSign() {
  if (!contract.value) return
  const role = userStore.user?.role
  if (!role) {
      alert('请先登录')
      return
  }
  try {
      await contractStore.signContract(contract.value.contractId, role)
  } catch (e) {
      alert('签署失败：' + e.message)
  }
}

onMounted(async () => {
    const id = route.params.id
    if (id) {
        await contractStore.fetchContractById(id)
    }
})
</script>

<template>
  <PageContainer class="my-20">
    <div v-if="contractStore.isLoading" class="text-center p-8 text-neutral-500">
        加载中...
    </div>
    <div v-else-if="contractStore.error" class="text-center p-8 text-red-500">
        {{ contractStore.error }}
    </div>
    <template v-else-if="contract">
        <ContractCard :data="contract" />
        <div class="bg-neutral-300/20 p-6 rd-xl mt-8">
            <PageContainer class="my-20 space-y-8">
                <PurchaseContractBox :partyA="partyA" :partyB="partyB" :contract="contract" />
                <div v-if="notSigned" class="flex justify-center">
                <AppButton @click="handleSign">签字确认</AppButton>
                </div>
                <div v-else class="flex justify-center text-green-600 font-bold">
                    已签署
                </div>
            </PageContainer>
        </div>
    </template>
    <div v-else class="text-center p-8 text-neutral-500">
        未找到合同
    </div>
  </PageContainer>
</template>
