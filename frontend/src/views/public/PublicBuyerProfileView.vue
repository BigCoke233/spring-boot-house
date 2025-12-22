<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import PageContainer from '@/layouts/PageContainer.vue'
import { UserRound, PhoneCall, Mail, DollarSign, Home } from 'lucide-vue-next'

const route = useRoute()
const loading = ref(true)
const error = ref(null)
const profile = ref({})

const profileStructure = [
  { label: "姓名", icon: UserRound, name: "name" },
  { label: "电话", icon: PhoneCall, name: "phone" },
  { label: "邮箱", icon: Mail, name: "email" },
  { label: "流动资金", icon: DollarSign, name: "mobileAssets" },
  { label: "固定资产", icon: Home, name: "fixedAssets" },
  { label: "年收入", icon: DollarSign, name: "annualIncome" },
]

// Mock Data
const MOCK_BUYERS = {
  501: {
    b_name: "张三",
    b_phone: "13800000000",
    b_email: "zhangsan@example.com",
    b_mobile_assets: 5000000,
    b_fixed_assets: 12000000,
    b_annual_income: 800000
  },
  502: {
    b_name: "李四",
    b_phone: "13900000000",
    b_email: "lisi@example.com",
    b_mobile_assets: 3000000,
    b_fixed_assets: 8000000,
    b_annual_income: 500000
  },
  503: {
    b_name: "王五",
    b_phone: "13700000000",
    b_email: "wangwu@example.com",
    b_mobile_assets: 8000000,
    b_fixed_assets: 20000000,
    b_annual_income: 1500000
  }
}

onMounted(async () => {
  const id = route.params.id
  if (!id) {
    error.value = "未提供ID"
    loading.value = false
    return
  }

  // Simulate API call
  await new Promise(resolve => setTimeout(resolve, 500))

  const data = MOCK_BUYERS[id]
  if (data) {
    profile.value = {
        name: data.b_name,
        phone: data.b_phone,
        email: data.b_email,
        mobileAssets: data.b_mobile_assets,
        fixedAssets: data.b_fixed_assets,
        annualIncome: data.b_annual_income
    }
    loading.value = false
  } else {
    // Default fallback
    profile.value = {
        name: "未知用户",
        phone: "000-00000000",
        email: "unknown@example.com",
        mobileAssets: 0,
        fixedAssets: 0,
        annualIncome: 0
    }
    loading.value = false
  }
})
</script>

<template>
  <PageContainer class="my-20">
    <div v-if="loading" class="text-center p-10">加载中...</div>
    <div v-else-if="error" class="text-center p-10 text-red-500">{{ error }}</div>
    <div v-else class="grid grid-cols-2 gap-4 md:gap-10">
      <div class="col-span-2">
        <h1 class="text-xl font-bold">资料 (买方)</h1>
      </div>
      <div v-for="item in profileStructure" :key="item.label"
        class="bg-neutral-100/20 p-4 md:p-8 rd-xl  shadow relative overflow-hidden">
        <div class="flex items-center text-extrabold text-neutral-500 text-sm">
          <span>{{ item.label }}</span>
        </div>
        <div class="text-lg mt-2">
          {{ profile[item.name] || '—' }}
        </div>
        <component :is="item.icon"
          class="w-25 h-25 absolute -bottom-4 -right-2 text-neutral-200 -z-1" />
      </div>
    </div>
  </PageContainer>
</template>
