<script setup>
import { onMounted, computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useContractStore } from '@/stores/contract.js'
import { useUserStore } from '@/stores/user.js'

import PageContainer from '@/layouts/PageContainer.vue'
import ContractCard from '@/components/ContractCard.vue'
import PurchaseContractBox from '@/components/PurchaseContractBox.vue'
import AppButton from '@/components/AppButton.vue'
import PaymentStatusCard from '@/components/PaymentStatusCard.vue'

const route = useRoute()
const contractStore = useContractStore()
const userStore = useUserStore()

const contract = computed(() => contractStore.currentContract)

const isInstallment = computed(() => contract.value?.payWay === 'installment')

const fullPaid = computed(() => !!(contract.value?.paymentStatus || contract.value?.paid))

const downPaid = computed(() => {
  const paidCount = Number(contract.value?.paidCount || 0)
  return !!contract.value?.downPaymentPaid || paidCount > 0
})

const paidInProgress = computed(() => {
  if (!isInstallment.value) return false
  const total = Number(contract.value?.totalPeriods || 0)
  const paidCount = Number(contract.value?.paidCount || 0)
  return total > 0 && paidCount > 0 && paidCount < total
})

const paying = ref(false)

async function handlePay() {
    if (!contract.value) return
    if (!confirm('确认支付？')) return
    paying.value = true
    try {
        const msg = await contractStore.payContract(contract.value.contractId, userStore.currentUserId)
        alert(msg)
    } catch (e) {
        alert(e.message)
    } finally {
        paying.value = false
    }
}

async function handlePayInstallment() {
    if (!contract.value) return
    if (!confirm('确认支付下一期？')) return
    paying.value = true
    try {
        const nextPeriod = (contract.value.paidCount || 0) + 1
        const msg = await contractStore.payInstallment(contract.value.contractId, userStore.currentUserId, nextPeriod)
        alert(msg)
    } catch (e) {
        alert(e.message)
    } finally {
        paying.value = false
    }
}

const partyA = computed(() => {
    if (!contract.value) return {}
    return {
        name: contract.value.seller?.name || '未知',
        id: contract.value.seller?.idCard || '—',
        phone: contract.value.seller?.phone || '—',
        address: '—', // Address not in mock currently
    }
})

const partyB = computed(() => {
    if (!contract.value) return {}
    return {
        name: contract.value.buyer?.name || '未知',
        id: contract.value.buyer?.idCard || '—',
        phone: contract.value.buyer?.phone || '—',
        address: '—', // Address not in mock currently
    }
})

const needsToSign = computed(() => {
    if (!contract.value) return false
    const role = userStore.role
    const agreeVal = role === 'buyer' ? contract.value.buyerAgree :
                     role === 'seller' ? contract.value.sellerAgree : 0
    // Check if explicitly 1 (Agree)
    return Number(agreeVal) !== 1
})

const hasSigned = computed(() => {
    if (!contract.value) return false
    const role = userStore.role
    const agreeVal = role === 'buyer' ? contract.value.buyerAgree :
                     role === 'seller' ? contract.value.sellerAgree : 0
    return Number(agreeVal) === 1
})

async function handleSign() {
  if (!contract.value) return
  const role = userStore.role
  if (!role) {
      alert('请先登录')
      return
  }
  if (!confirm('确认签署合同？')) return

  try {
      const msg = await contractStore.signContract(contract.value.contractId, role)
      alert(msg)
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
        <div class="my-6">
          <PaymentStatusCard :contract="contract" />
        </div>
        <div class="bg-neutral-300/20 p-6 rd-xl mt-8">
            <PageContainer class="my-20 space-y-8">
                <div class="flex justify-end">
                    <AppButton v-if="userStore.role === 'buyer'" variant="secondary" :to="`/seller/${contract.seller?.id}`">查看卖家资料</AppButton>
                    <AppButton v-if="userStore.role === 'seller'" variant="secondary" :to="`/buyer/${contract.buyer?.id}`">查看买家资料</AppButton>
                </div>
                <PurchaseContractBox :partyA="partyA" :partyB="partyB" :contract="contract" />
                <div v-if="needsToSign" class="flex justify-center">
                <AppButton @click="handleSign">签字确认</AppButton>
                </div>
                <div v-else-if="hasSigned" class="flex flex-col items-center gap-4">
                    <div class="text-green-600 font-bold">已签署</div>
                    <template v-if="userStore.role === 'buyer' && !fullPaid">
                        <template v-if="!downPaid">
                            <AppButton variant="secondary" @click="handlePay" :disabled="paying">
                                {{ isInstallment ? '支付首付' : '支付全款' }}
                            </AppButton>
                        </template>
                        <template v-else-if="paidInProgress">
                            <AppButton variant="secondary" @click="handlePayInstallment" :disabled="paying">
                                支付下一期 ({{ (contract.paidCount || 0) + 1 }})
                            </AppButton>
                        </template>
                    </template>
                </div>
            </PageContainer>
        </div>
    </template>
    <div v-else class="text-center p-8 text-neutral-500">
        未找到合同
    </div>
  </PageContainer>
</template>
