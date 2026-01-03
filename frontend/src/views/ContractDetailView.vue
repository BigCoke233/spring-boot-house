<script setup>
import { onMounted, computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useContractStore } from '@/stores/contract.js'
import { useUserStore } from '@/stores/user.js'
import { useMessage } from '@/composables/useMessage'

import PageContainer from '@/layouts/PageContainer.vue'
import ContractCard from '@/components/ContractCard.vue'
import PurchaseContractBox from '@/components/PurchaseContractBox.vue'
import AppButton from '@/components/AppButton.vue'
import PaymentStatusCard from '@/components/PaymentStatusCard.vue'

const route = useRoute()
const router = useRouter()
const contractStore = useContractStore()
const userStore = useUserStore()
const { showSuccess, showError, showConfirm, showWarning } = useMessage()

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
    try {
        await showConfirm('确认支付？')
        paying.value = true
        const msg = await contractStore.payContract(contract.value.contractId, userStore.currentUserId)
        showSuccess(msg)
    } catch (e) {
        if (e !== 'cancel') showError(e.message || e)
    } finally {
        paying.value = false
    }
}

async function handlePayInstallment() {
    if (!contract.value) return
    try {
        await showConfirm('确认支付下一期？')
        paying.value = true
        const nextPeriod = (contract.value.paidCount || 0) + 1
        const msg = await contractStore.payInstallment(contract.value.contractId, userStore.currentUserId, nextPeriod)
        showSuccess(msg)
    } catch (e) {
        if (e !== 'cancel') showError(e.message || e)
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

async function handleSign() {
  if (!contract.value) return
  const role = userStore.role
  if (!role) {
      showWarning('请先登录')
      return
  }

  try {
      await showConfirm('确认签署合同？')
      const msg = await contractStore.signContract(contract.value.contractId, role, 1)
      showSuccess(msg)
  } catch (e) {
      if (e !== 'cancel') showError('签署失败：' + (e.message || e))
  }
}

async function handleReject() {
    if (!contract.value) return
    const role = userStore.role
    if (!role) {
        showWarning('请先登录')
        return
    }

    try {
        await showConfirm('确定要拒绝这份合同吗？此操作不可撤销。')
        const msg = await contractStore.signContract(contract.value.contractId, role, -1)
        showSuccess(msg)
    } catch (e) {
        if (e !== 'cancel') showError('操作失败：' + (e.message || e))
    }
}

async function handleDelivery() {
    if (!contract.value) return
    try {
        await showConfirm('确认已交房？')
        await contractStore.updateDelivery(contract.value.contractId, 1)
        showSuccess('交房确认成功')
    } catch (e) {
        if (e !== 'cancel') showError('交房确认失败：' + (e.message || e))
    }
}

async function handleCancel() {
    if (!contract.value) return
    try {
        await showConfirm('确认要取消（删除）此合同申请吗？此操作无法撤销。')
        await contractStore.deleteContract(contract.value.contractId)
        showSuccess('合同已取消')
        router.push('/contract')
    } catch (e) {
        if (e !== 'cancel') showError('取消失败：' + (e.message || e))
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
                <PurchaseContractBox :partyA="partyA" :partyB="partyB" :contract="contract" />
      <div class="mt-8 flex gap-4">
        <!-- Seller Actions -->
        <template v-if="userStore.role === 'seller'">
             <template v-if="Number(contract.sellerAgree) !== 1 && Number(contract.sellerAgree) !== -1">
                <div class="flex gap-4 justify-center">
                    <AppButton @click="handleSign">
                        签署合同
                    </AppButton>
                    <AppButton variant="danger" @click="handleReject">
                        拒绝合同
                    </AppButton>
                </div>
             </template>
             <template v-if="Number(contract.buyerAgree) === 1 && Number(contract.sellerAgree) === 1 && !contract.delivered">
                <div class="flex justify-center">
                    <AppButton variant="secondary" @click="handleDelivery">
                        确认交房
                    </AppButton>
                </div>
             </template>
        </template>

        <!-- Buyer Actions -->
        <template v-if="userStore.role === 'buyer'">
             <!-- Sign Contract Logic (if not yet agreed by buyer) -->
             <template v-if="Number(contract.buyerAgree) !== 1 && Number(contract.buyerAgree) !== -1">
                 <div class="flex gap-4 mb-4 justify-center">
                    <AppButton @click="handleSign">
                        签署合同
                    </AppButton>
                    <AppButton variant="danger" @click="handleReject">
                        拒绝合同
                    </AppButton>
                 </div>
             </template>

             <!-- Payment Logic -->
             <template v-if="Number(contract.buyerAgree) === 1 && Number(contract.sellerAgree) === 1 && !fullPaid">
                 <template v-if="!downPaid">
                     <AppButton @click="handlePay" :disabled="paying">
                         {{ isInstallment ? '支付首付' : '支付全款' }}
                     </AppButton>
                 </template>
                 <template v-else-if="paidInProgress">
                     <AppButton @click="handlePayInstallment" :disabled="paying">
                         支付下一期 ({{ (contract.paidCount || 0) + 1 }})
                     </AppButton>
                 </template>
             </template>

             <!-- Cancel Option for Buyer if not yet signed by seller -->
             <template v-if="contract.status == 1 && Number(contract.sellerAgree) !== 1 && Number(contract.buyerAgree) !== 1">
                 <div class="flex gap-4">
                    <AppButton variant="secondary" @click="handleCancel">
                        取消申请
                    </AppButton>
                 </div>
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
