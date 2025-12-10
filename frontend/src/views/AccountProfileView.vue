<script setup>
import AccountLayout from '@/layouts/AccountLayout.vue';
import PageContainer from '@/layouts/PageContainer.vue';
import { UserRound, PhoneCall, Mail, DollarSign, Home } from 'lucide-vue-next'

const profileStructure = [
  { label: "姓名", icon: UserRound, name: "name" },
  { label: "电话", icon: PhoneCall, name: "telephone" },
  { label: "邮箱", icon: Mail, name: "email" },
  { label: "流动资金", icon: DollarSign, name: "workingCapital" },
  { label: "固定资产", icon: Home, name: "fixedCapital" },
  { label: "年收入", icon: DollarSign, name: "annualIncome" },
]

const profile = {
  name: "张三",
  telephone: "13800138000",
  email: "zhangsan@example.com",
  workingCapital: 1000000,
  fixedCapital: 500000,
  annualIncome: 500000,
}
</script>

<template>
  <AccountLayout>
    <PageContainer>
      <div class="grid grid-cols-2 gap-4 md:gap-10">
        <div v-for="item in profileStructure" :key="item.label"
          class="bg-neutral-100/20 p-4 md:p-8 rd-xl  shadow relative overflow-hidden">
          <div class="flex items-center text-extrabold text-neutral-500 text-sm">
            <span>{{ item.label }}</span>
          </div>
          <div class="text-lg mt-2">
            {{ profile[item.name] }}
          </div>
          <component :is="item.icon"
            class="w-25 h-25 absolute -bottom-4 -right-2 text-neutral-200 -z-1" />
        </div>
      </div>
    </PageContainer>
  </AccountLayout>
</template>
