<script setup>
import { computed } from 'vue'
import { useHouseStore } from '@/stores/house.js'
import StatusLabel from '@/components/AgreeStatusLabel.vue'
import AppButton from '@/components/AppButton.vue'
const props = defineProps({
    data: {
        type: Object,
        default: () => ({
            contractId: 1,
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
    }
})

const houseStore = useHouseStore()
function getHouseById(id) { return houseStore.getHouseById(id) }
const houseData = computed(() => getHouseById(props.data.houseId) || {
  id: 1,
  name: '房源名称',
  price: 8000,
  square: 100,
  location: '房源地址',
})

const isInstallment = computed(() => props.data.payWay === 'installment')
const steps = computed(() => {
  const created = true
  const agreed = !!(props.data.buyerAgree && props.data.sellerAgree)
  const paid = !!(props.data.paymentStatus || props.data.paid || (isInstallment.value && props.data.paidCount > 0))
  const delivered = !!props.data.delivered
  const completed = !!(props.data.completionStatus || (paid && delivered))
  return [
    { key: 'created', label: '已创建', active: created },
    { key: 'agreed', label: '双方同意', active: agreed },
    { key: 'paid', label: '买方付款', active: paid },
    { key: 'delivered', label: '交房', active: delivered },
    { key: 'completed', label: '完成', active: completed },
  ]
})

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
  const total = props.data.totalPeriods ?? 0
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
  const anyAgree = !!(props.data.buyerAgree || props.data.sellerAgree)
  const bothAgree = !!(props.data.buyerAgree && props.data.sellerAgree)
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

  function status(completed, inProgress = false) {
    if (completed) return 'completed'
    if (inProgress) return 'in_progress'
    return 'not_started'
  }

  return [
    { key: 'created', label: '已创建', status: status(created) },
    { key: 'agreed', label: '双方同意', status: status(bothAgree, anyAgree && !bothAgree) },
    { key: 'paid', label: '买方付款', status: status(paidCompleted, paidInProgress) },
    { key: 'delivered', label: '交房', status: status(deliveredCompleted, deliveredInProgress) },
    { key: 'completed', label: '完成', status: status(completedCompleted) },
  ]
})

function statusDotClass(s) {
  if (s === 'completed') return 'w-2 h-2 rd-full bg-green-5 inline-block'
  if (s === 'in_progress') return 'w-2 h-2 rd-full bg-yellow-5 inline-block'
  return 'w-2 h-2 rd-full bg-neutral-300 inline-block'
}

function statusTextClass(s) {
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
</script>

<template>
    <div class="bg-white rd shadow-md">
        <header class="flex justify-between items-center b-b-1 b-neutral-200 px-6 py-3">
            <h3 class="font-light m-0">{{ houseData.name }}</h3>
            <p class="text-neutral">#{{ props.data.contractId }}</p>
        </header>

        <section class="px-6 py-3">
          <div class="flex items-center justify-between gap-4">
            <template v-for="(s, idx) in stepStatuses" :key="s.key">
              <div class="flex items-center gap-2 flex-shrink-0">
                <span :class="statusDotClass(s.status)"></span>
                <span :class="statusTextClass(s.status)">{{ s.label }}</span>
              </div>
              <span v-if="idx < stepStatuses.length - 1" class="w-full h-[1px] bg-neutral-200"></span>
            </template>
          </div>
        </section>

        <section class="grid grid-cols-4">
            <!-- 付款状态 -->
            <section class="px-6 py-4 grid-col-span-2">
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
            <section class="px-6 pb-6">
                <div class="grid grid-rows-2 gap-4">
                    <StatusLabel title="买方" :status="props.data.buyerAgree ? '已同意' : '未同意'" />
                    <StatusLabel title="卖方" :status="props.data.sellerAgree ? '已同意' : '未同意'" />
                </div>
            </section>
            <!-- 操作按钮 -->
             <section class="flex flex-col items-start gap-2 px-6 pb-6">
                <h4 class="font-bold text-sm">操作</h4>
                <AppButton size="full">查看详情</AppButton>
                <AppButton v-if="!downPaid" size="full">支付首付</AppButton>
                <AppButton v-if="paidInProgress" size="full">支付下一期</AppButton>
             </section>
        </section>

        <section class="b-t-1 b-neutral-200 p-6 text-neutral-500 mt-4 grid grid-cols-2 gap-2 text-sm">
            <div>付款截止：{{ props.data.paytimeEnding ?? '—' }}</div>
            <div>实际付款：{{ props.data.paytimeActually ?? '—' }}</div>
            <div>交房截止：{{ props.data.deliveryEnding ?? '—' }}</div>
            <div>实际交房：{{ props.data.deliveryActually ?? '—' }}</div>
        </section>
    </div>
</template>
