<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useMessage } from '@/composables/useMessage'
import AccountLayout from '@/layouts/AccountLayout.vue';
import PageContainer from '@/layouts/PageContainer.vue';
import AppButton from '@/components/AppButton.vue';

const router = useRouter()
const userStore = useUserStore()
const { showSuccess, showError } = useMessage()

const profile = ref({
  name: "",
  telephone: "",
  email: "",
  workingCapital: 0,
  fixedCapital: 0,
  annualIncome: 0,
})

onMounted(async () => {
  if (!userStore.isLoggedIn) {
      await userStore.fetchUserInfo()
  }
  const user = userStore.userInfo
  // Map backend fields to form
  profile.value = {
      name: user.b_name || user.name || '',
      telephone: user.b_phone || user.phone || '',
      email: user.b_email || user.email || '',
      workingCapital: user.b_mobile_assets || user.b_working_capital || user.workingCapital || 0,
      fixedCapital: user.b_fixed_assets || user.b_fixed_capital || user.fixedCapital || 0,
      annualIncome: user.b_annual_income || user.annualIncome || 0
  }
})

const handleSave = async () => {
  try {
      // Map form to backend fields
      const updateData = {
          b_name: profile.value.name,
          b_phone: profile.value.telephone,
          b_email: profile.value.email,
          b_mobile_assets: profile.value.workingCapital,
          b_fixed_assets: profile.value.fixedCapital,
          b_annual_income: profile.value.annualIncome
      }
      await userStore.updateBuyerProfile(userStore.currentUserId, updateData)
      showSuccess('保存成功')
      router.push('/account/profile')
  } catch (e) {
      showError('保存失败: ' + e.message)
  }
}
</script>

<template>
  <AccountLayout>
    <PageContainer>
      <h1 class="text-xl font-bold mb-6">编辑个人信息</h1>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div class="flex flex-col gap-2">
            <label class="text-sm font-bold text-neutral-600">姓名</label>
            <input v-model="profile.name" class="p-2 border border-neutral-300 rounded focus:outline-none focus:border-neutral-500 transition" />
        </div>
        <div class="flex flex-col gap-2">
            <label class="text-sm font-bold text-neutral-600">电话</label>
            <input v-model="profile.telephone" class="p-2 border border-neutral-300 rounded focus:outline-none focus:border-neutral-500 transition" />
        </div>
         <div class="flex flex-col gap-2">
            <label class="text-sm font-bold text-neutral-600">邮箱</label>
            <input v-model="profile.email" class="p-2 border border-neutral-300 rounded focus:outline-none focus:border-neutral-500 transition" />
        </div>
         <div class="flex flex-col gap-2">
            <label class="text-sm font-bold text-neutral-600">流动资金</label>
            <input v-model="profile.workingCapital" type="number" class="p-2 border border-neutral-300 rounded focus:outline-none focus:border-neutral-500 transition" />
        </div>
         <div class="flex flex-col gap-2">
            <label class="text-sm font-bold text-neutral-600">固定资产</label>
            <input v-model="profile.fixedCapital" type="number" class="p-2 border border-neutral-300 rounded focus:outline-none focus:border-neutral-500 transition" />
        </div>
         <div class="flex flex-col gap-2">
            <label class="text-sm font-bold text-neutral-600">年收入</label>
            <input v-model="profile.annualIncome" type="number" class="p-2 border border-neutral-300 rounded focus:outline-none focus:border-neutral-500 transition" />
        </div>
      </div>
      <div class="mt-8 flex gap-4">
        <AppButton @click="handleSave">保存修改</AppButton>
        <AppButton variant="secondary" @click="router.back()">取消</AppButton>
      </div>
    </PageContainer>
  </AccountLayout>
</template>
