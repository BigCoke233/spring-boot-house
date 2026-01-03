<script setup>
import { computed, ref } from 'vue'
import { useHouseStore } from '@/stores/house.js'
import { useUserStore } from '@/stores/user.js'
import { useContractStore } from '@/stores/contract.js'
import { useMessage } from '@/composables/useMessage'
import StatusLabel from '@/components/AgreeStatusLabel.vue'
import AppButton from '@/components/AppButton.vue'

const props = defineProps({
    data: {
        type: Object,
        required: true
    }
})

const houseStore = useHouseStore()
const userStore = useUserStore()
const contractStore = useContractStore()
const { showSuccess, showError, showConfirm } = useMessage()

const loading = ref(false)

// Use computed to reactively get house data
const houseData = computed(() => {
    // Priority: Props data.house > Store house > Default
    if (props.data.house) {
        return props.data.house
    }
    const house = houseStore.getHouseById(props.data.houseId)
    return house || {
        id: props.data.houseId,
        name: '未知房源',
        price: 0,
        square: 0,
        location: '未知地址',
    }
})

const isInstallment = computed(() => props.data.payWay === 'installment')

const paymentSummary = computed(() => {
  if (!isInstallment.value) {
    return {
      title: '全款',
      lines: [
        `总金额：¥${props.data.totalPrice}`,
        `支付状态：${props.data.paymentStatus || props.data.paid ? '已支付' : '未支付'}`,
        `截止日期：${props.data.paytimeEnding ?? '—'}`,
      ],
    }
  }
  const paidCount = props.data.paidCount ?? 0
  const downPaid = !!props.data.downPaymentPaid || paidCount > 0
  return {
    title: '分期',
    lines: [
      `总金额：¥${props.data.totalPrice}`,
      `首付：${downPaid ? '已支付' : '未支付'}`,
      `下一次缴费截止：${props.data.paytimeEnding ?? '—'}`,
    ],
  }
})

const installmentPercent = computed(() => {
  if (!isInstallment.value) return 0
  const total = Number(props.data.totalPeriods || 0)
  const paid = Number(props.data.paidCount || 0)
  if (!total) return 0
  return Math.min(100, Math.round((paid / total) * 100))
})

const stepStatuses = computed(() => {
  const created = true
  const bAgree = Number(props.data.buyerAgree) === 1
  const sAgree = Number(props.data.sellerAgree) === 1
  const bReject = Number(props.data.buyerAgree) === -1
  const sReject = Number(props.data.sellerAgree) === -1
  const anyAgree = bAgree || sAgree
  const bothAgree = bAgree && sAgree
  const anyReject = bReject || sReject
  const total = Number(props.data.totalPeriods || 0)
  const paidCount = Number(props.data.paidCount || 0)
  const fullPaid = !!(props.data.paymentStatus || props.data.paid)
  const installmentCompleted = isInstallment.value && total > 0 && paidCount >= total
  const installmentStarted = isInstallment.value && paidCount > 0
  const paidCompleted = isInstallment.value ? installmentCompleted : fullPaid
  const paidInProgress = isInstallment.value ? (installmentStarted && !installmentCompleted) : false
  const deliveredCompleted = !!props.data.delivered
  const deliveredInProgress = paidCompleted && !deliveredCompleted
  const completedCompleted = !!(props.data.completionStatus || (paidCompleted && deliveredCompleted))

  function status(completed, inProgress = false, rejected = false) {
    if (rejected) return 'rejected'
    if (completed) return 'completed'
    if (inProgress) return 'in_progress'
    return 'not_started'
  }

  return [
    { key: 'created', label: '已创建', status: status(created) },
    { key: 'agreed', label: anyReject ? '已拒绝' : '双方同意', status: status(bothAgree, anyAgree && !bothAgree, anyReject) },
    { key: 'paid', label: '买方付款', status: status(paidCompleted, paidInProgress) },
    { key: 'delivered', label: '交房', status: status(deliveredCompleted, deliveredInProgress) },
    { key: 'completed', label: '完成', status: status(completedCompleted) },
  ]
})

function statusDotClass(s) {
  if (s === 'rejected') return 'w-2 h-2 rd-full bg-red-5 inline-block'
  if (s === 'completed') return 'w-2 h-2 rd-full bg-green-5 inline-block'
  if (s === 'in_progress') return 'w-2 h-2 rd-full bg-yellow-5 inline-block'
  return 'w-2 h-2 rd-full bg-neutral-300 inline-block'
}

function statusTextClass(s) {
  if (s === 'rejected') return 'text-red-6'
  if (s === 'completed') return 'text-green-6'
  if (s === 'in_progress') return 'text-yellow-6'
  return 'text-neutral'
}

const downPaid = computed(() => {
  const paidCount = Number(props.data.paidCount || 0)
  return !!props.data.downPaymentPaid || paidCount > 0
})

const paidInProgress = computed(() => {
  if (!isInstallment.value) return false
  const total = Number(props.data.totalPeriods || 0)
  const paidCount = Number(props.data.paidCount || 0)
  return total > 0 && paidCount > 0 && paidCount < total
})

const fullPaid = computed(() => {
  return !!(props.data.paymentStatus || props.data.paid)
})

async function handlePay() {
    try {
        await showConfirm('确认支付？')
        loading.value = true
        const msg = await contractStore.payContract(props.data.contractId, userStore.currentUserId)
        showSuccess(msg)
    } catch (e) {
        if (e !== 'cancel') showError(e.message || e)
    } finally {
        loading.value = false
    }
}

async function handlePayInstallment() {
    try {
        await showConfirm('确认支付下一期？')
        loading.value = true
        const nextPeriod = (props.data.paidCount || 0) + 1
        const msg = await contractStore.payInstallment(props.data.contractId, userStore.currentUserId, nextPeriod)
        showSuccess(msg)
    } catch (e) {
        if (e !== 'cancel') showError(e.message || e)
    } finally {
        loading.value = false
    }
}
function getStatusText(val) {
    if (Number(val) === 1) return '已同意'
    if (Number(val) === -1) return '已拒绝'
    return '未同意'
}
</script>

<template>
    <div class="bg-white rd shadow-md">
        <header class="flex justify-between items-center b-b-1 b-neutral-200 px-6 py-3">
            <h3 class="font-light m-0">{{ houseData.name }}</h3>
            <p class="text-neutral">#{{ props.data.contractId }}</p>
        </header>

        <section class="px-6 py-3 overflow-x-auto">
          <div class="flex items-center justify-between gap-4 min-w-[500px]">
            <template v-for="(s, idx) in stepStatuses" :key="s.key">
              <div class="flex items-center gap-2 flex-shrink-0">
                <span :class="statusDotClass(s.status)"></span>
                <span :class="statusTextClass(s.status)">{{ s.label }}</span>
              </div>
              <span v-if="idx < stepStatuses.length - 1" class="w-full h-[1px] bg-neutral-200"></span>
            </template>
          </div>
        </section>

        <section class="grid grid-cols-1 lg:grid-cols-4 gap-4">
            <!-- 付款状态 -->
            <section class="px-6 py-4 lg:col-span-2">
                <div class="text-lg font-bold">{{ paymentSummary.title }}</div>
                <div v-if="isInstallment" class="mt-3">
                    <div class="flex justify-between items-center text-sm">
                    <span>已付期数</span>
                    <span>{{ props.data.paidCount }}/{{ props.data.totalPeriods }}</span>
                    </div>
                    <div class="w-full h-2 bg-neutral-200 rd overflow-hidden mt-1">
                    <div class="h-2 bg-green-5 rd" :style="{ width: installmentPercent + '%' }"></div>
                    </div>
                </div>
                <ul class="m-0 p-0 list-none mt-2 space-y-1">
                    <li v-for="line in paymentSummary.lines" :key="line"
                        class="text-sm">{{ line }}</li>
                </ul>
            </section>
            <!-- 双方进度跟踪 -->
            <section class="px-6 pb-6 pt-4 lg:pt-6">
                <div class="grid grid-rows-2 gap-4">
                    <StatusLabel title="买方" :status="getStatusText(props.data.buyerAgree)" />
                    <StatusLabel title="卖方" :status="getStatusText(props.data.sellerAgree)" />
                </div>
            </section>
            <!-- 操作按钮 -->
             <section class="flex flex-col items-start gap-2 px-6 pb-6 pt-4 lg:pt-6">
                <h4 class="font-bold text-sm m-0">操作</h4>
                <div class="flex flex-row lg:flex-col gap-2 w-full">
                    <AppButton size="full" :to="`/contract/${props.data.contractId}`">查看详情</AppButton>
                    <template v-if="userStore.role === 'buyer'">
                        <AppButton size="full" variant="secondary" :to="`/seller/${props.data.seller?.id || props.data.sellerId || houseData?.sellerId || houseData?.h_seller_id}`">查看卖家资料</AppButton>
                    </template>
                    <template v-if="userStore.role === 'seller'">
                        <AppButton size="full" variant="secondary" :to="`/buyer/${props.data.buyer?.id || props.data.buyerId || props.data.c_buyer_id}`">查看买家资料</AppButton>
                    </template>
                    <template v-if="userStore.role === 'buyer' && !fullPaid && Number(props.data.buyerAgree) === 1 && Number(props.data.sellerAgree) === 1">
                        <template v-if="!downPaid">
                            <AppButton size="full" variant="secondary" @click="handlePay" :disabled="loading">
                                {{ isInstallment ? '支付首付' : '支付全款' }}
                            </AppButton>
                        </template>
                        <template v-else-if="paidInProgress">
                            <AppButton size="full" variant="secondary" @click="handlePayInstallment" :disabled="loading">
                                支付下一期 ({{ (props.data.paidCount || 0) + 1 }})
                            </AppButton>
                        </template>
                    </template>
                </div>
             </section>
        </section>

        <section class="b-t-1 b-neutral-200 p-6 text-neutral-500 mt-0 grid grid-cols-1 sm:grid-cols-2 gap-2 text-sm">
            <div>付款截止：{{ props.data.paytimeEnding ?? '—' }}</div>
            <div>实际付款：{{ props.data.paytimeActually ?? '—' }}</div>
            <div>交房截止：{{ props.data.deliveryEnding ?? '—' }}</div>
            <div>实际交房：{{ props.data.deliveryActually ?? '—' }}</div>
        </section>
    </div>
</template>
