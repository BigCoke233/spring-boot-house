<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import AccountLayout from '@/layouts/AccountLayout.vue';
import PageContainer from '@/layouts/PageContainer.vue';
import AppButton from '@/components/AppButton.vue';

const router = useRouter()
const userStore = useUserStore()

const profile = ref({
  name: "",
  description: "",
  phone: "",
  email: "",
  website: "",
})

onMounted(async () => {
  if (!userStore.isLoggedIn) {
      await userStore.fetchUserInfo()
  }
  const user = userStore.userInfo
  // Map backend fields to form (Seller entity: s_name, s_describe, etc.)
  profile.value = {
      name: user.s_name || user.name || '',
      description: user.s_describe || user.description || '',
      phone: user.s_phone || user.phone || '',
      email: user.s_email || user.email || '',
      website: user.s_website || user.website || '',
  }
})

const handleSave = async () => {
  try {
      const updateData = {
          s_id: userStore.currentUserId,
          s_name: profile.value.name,
          s_describe: profile.value.description,
          s_phone: profile.value.phone,
          s_email: profile.value.email,
          s_website: profile.value.website
      }
      await userStore.updateSellerProfile(updateData)
      alert('保存成功')
      router.push('/account/profile')
  } catch (e) {
      alert('保存失败: ' + e.message)
  }
}
</script>

<template>
  <AccountLayout>
    <PageContainer>
      <h1 class="text-xl font-bold mb-6">编辑资料</h1>
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
