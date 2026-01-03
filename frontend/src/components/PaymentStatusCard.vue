<script setup>
import { computed } from 'vue'

const props = defineProps({
  contract: {
    type: Object,
    required: true
  }
})

const isInstallment = computed(() => props.contract.payWay === 'installment')

const status = computed(() => {
  if (!isInstallment.value) {
    return (props.contract.paymentStatus || props.contract.paid) ? '已支付' : '未支付'
  }
  const total = Number(props.contract.totalPeriods || 0)
  const paid = Number(props.contract.paidCount || 0)
  if (paid >= total && total > 0) return '已完成'
  if (paid > 0) return '进行中'
  return '未开始'
})

const progressPercent = computed(() => {
  if (!isInstallment.value) return (props.contract.paymentStatus || props.contract.paid) ? 100 : 0
  const total = Number(props.contract.totalPeriods || 0)
  const paid = Number(props.contract.paidCount || 0)
  if (!total) return 0
  return Math.min(100, Math.round((paid / total) * 100))
})

const paidAmount = computed(() => {
    // This is an estimation since we don't have exact transaction records in the contract object
    // Assuming equal installments or standard breakdown
    const total = Number(props.contract.totalPrice || 0)
    if (!isInstallment.value) {
        return (props.contract.paymentStatus || props.contract.paid) ? total : 0
    }
    // Simple estimation for display
    const periods = Number(props.contract.totalPeriods || 0)
    const paidCount = Number(props.contract.paidCount || 0)
    if (!periods) return 0
    return Math.round((total / periods) * paidCount)
})

const lastPaymentDate = computed(() => {
    return props.contract.paytimeActually || '—'
})
</script>

<template>
  <div class="bg-white rd shadow-sm border border-neutral-200 overflow-hidden">
    <div class="bg-neutral-50 px-6 py-4 border-b border-neutral-200 flex justify-between items-center">
      <h3 class="font-bold text-lg m-0 text-neutral-800">付款详情</h3>
      <span class="px-3 py-1 rounded-full text-sm font-medium"
            :class="status === '已支付' || status === '已完成' ? 'bg-green-100 text-green-700' : 'bg-yellow-100 text-yellow-700'">
        {{ status }}
      </span>
    </div>

    <div class="p-6">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
        <!-- Left Column: Basic Info -->
        <div class="space-y-4">
          <div>
            <div class="text-sm text-neutral-500 mb-1">总金额</div>
            <div class="text-2xl font-bold text-neutral-900">¥{{ Number(contract.totalPrice).toLocaleString() }}</div>
          </div>

          <div>
            <div class="text-sm text-neutral-500 mb-1">付款方式</div>
            <div class="font-medium">{{ isInstallment ? '分期付款' : '全款支付' }}</div>
            <div v-if="isInstallment" class="text-sm text-neutral-500 mt-1">
              共 {{ contract.totalPeriods }} 期
            </div>
          </div>
        </div>

        <!-- Right Column: Progress & Dates -->
        <div class="space-y-4">
          <div v-if="isInstallment">
            <div class="flex justify-between text-sm mb-2">
              <span class="text-neutral-500">还款进度</span>
              <span class="font-medium">{{ contract.paidCount }}/{{ contract.totalPeriods }} 期</span>
            </div>
            <div class="w-full bg-neutral-100 rounded-full h-2.5 overflow-hidden">
              <div class="bg-green-500 h-2.5 rounded-full transition-all duration-500"
                   :style="{ width: progressPercent + '%' }"></div>
            </div>
            <div class="flex justify-between text-xs text-neutral-400 mt-1">
              <span>已付: ~¥{{ paidAmount.toLocaleString() }}</span>
              <span>{{ progressPercent }}%</span>
            </div>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <div class="text-sm text-neutral-500 mb-1">最近付款时间</div>
              <div class="text-neutral-900">{{ lastPaymentDate }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
