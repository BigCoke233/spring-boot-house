<script setup>
import AccountLayout from '@/layouts/AccountLayout.vue';
import PageContainer from '@/layouts/PageContainer.vue';
import { UserRound, PhoneCall, Mail, DollarSign, Home, Building } from 'lucide-vue-next'

const profileStructure = [
  { label: "姓名", icon: UserRound, name: "name" },
  { label: "电话", icon: PhoneCall, name: "telephone" },
  { label: "邮箱", icon: Mail, name: "email" },
  { label: "公司名称", icon: Building, name: "company" },
  { label: "注册资金", icon: DollarSign, name: "registeredCapital" },
  { label: "年营业额", icon: DollarSign, name: "annualRevenue" },
]

const profile = {
  name: "李四",
  telephone: "13900139000",
  email: "lisi@example.com",
  company: "宏达地产",
  registeredCapital: 5000000,
  annualRevenue: 20000000,
}
</script>

<template>
  <AccountLayout>
    <PageContainer class="grid grid-cols-2 gap-4 md:gap-10">
      <div class="col-span-2">
        <h1 class="text-xl font-bold">个人资料 (卖方)</h1>
      </div>
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
      <p class="col-span-2">
        <router-link to="/account/profile/edit" class="cursor-pointer underline underline-offset-4">编辑个人信息</router-link>
      </p>
    </PageContainer>
  </AccountLayout>
</template>
