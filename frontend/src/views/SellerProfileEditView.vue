<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import AccountLayout from '@/layouts/AccountLayout.vue';
import PageContainer from '@/layouts/PageContainer.vue';
import AppButton from '@/components/AppButton.vue';

const router = useRouter()

const profile = ref({
  name: "宏达地产",
  description: "一家专注于高端商业地产开发的领军企业，致力于为客户提供优质的房产资源和服务。",
  phone: "021-88888888",
  email: "contact@hongda.com",
  website: "www.hongda.com",
})

const handleSave = () => {
  // Logic to save profile
  console.log('Saved seller profile', profile.value)
  router.push('/account/profile')
}
</script>

<template>
  <AccountLayout>
    <PageContainer>
      <h1 class="text-xl font-bold mb-6">编辑资料 (卖方)</h1>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
         <div class="flex flex-col gap-2">
            <label class="text-sm font-bold text-neutral-600">公司名称</label>
            <input v-model="profile.name" class="p-2 border border-neutral-300 rounded focus:outline-none focus:border-neutral-500 transition" />
        </div>
        <div class="flex flex-col gap-2">
            <label class="text-sm font-bold text-neutral-600">电话</label>
            <input v-model="profile.phone" class="p-2 border border-neutral-300 rounded focus:outline-none focus:border-neutral-500 transition" />
        </div>
         <div class="flex flex-col gap-2">
            <label class="text-sm font-bold text-neutral-600">邮箱</label>
            <input v-model="profile.email" class="p-2 border border-neutral-300 rounded focus:outline-none focus:border-neutral-500 transition" />
        </div>
         <div class="flex flex-col gap-2">
            <label class="text-sm font-bold text-neutral-600">官网</label>
            <input v-model="profile.website" class="p-2 border border-neutral-300 rounded focus:outline-none focus:border-neutral-500 transition" />
        </div>
        <div class="flex flex-col gap-2 md:col-span-2">
            <label class="text-sm font-bold text-neutral-600">公司描述</label>
            <textarea v-model="profile.description" rows="4" class="p-2 border border-neutral-300 rounded focus:outline-none focus:border-neutral-500 transition"></textarea>
        </div>
      </div>
      <div class="mt-8 flex gap-4">
        <AppButton @click="handleSave">保存修改</AppButton>
        <AppButton variant="secondary" @click="router.back()">取消</AppButton>
      </div>
    </PageContainer>
  </AccountLayout>
</template>
