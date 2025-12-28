<script setup>
import { computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import AccountLayout from '@/layouts/AccountLayout.vue';
import PageContainer from '@/layouts/PageContainer.vue';
import { PhoneCall, Mail, Building, Globe } from 'lucide-vue-next'

const userStore = useUserStore()

const profileStructure = [
  { label: "公司名称", icon: Building, name: "name" },
  { label: "电话", icon: PhoneCall, name: "phone" },
  { label: "邮箱", icon: Mail, name: "email" },
  { label: "官网", icon: Globe, name: "website" },
]

onMounted(async () => {
    if (!userStore.isLoggedIn || !userStore.userInfo?.s_id) {
        await userStore.fetchUserInfo()
    }
})

const profile = computed(() => {
  const info = userStore.userInfo || {}
  return {
    name: info.s_name || info.name || "未填写",
    description: info.s_describe || info.description || "暂无描述",
    phone: info.s_phone || info.phone || "未填写",
    email: info.s_email || info.email || "未填写",
    website: info.s_website || info.website || "未填写",
  }
})
</script>

<template>
  <AccountLayout>
    <PageContainer class="grid grid-cols-2 gap-4 md:gap-10">
      <div class="col-span-2">
        <h1 class="text-xl font-bold">资料</h1>
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
      <div class="col-span-2 bg-neutral-100/20 p-4 md:p-8 rd-xl shadow">
        <div class="text-extrabold text-neutral-500 text-sm mb-2">公司描述</div>
        <div class="text-lg">{{ profile.description }}</div>
      </div>
      <p class="col-span-2">
        <router-link to="/account/profile/edit" class="cursor-pointer underline underline-offset-4">编辑资料</router-link>
      </p>
    </PageContainer>
  </AccountLayout>
</template>
