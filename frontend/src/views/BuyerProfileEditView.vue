<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import AccountLayout from '@/layouts/AccountLayout.vue';
import PageContainer from '@/layouts/PageContainer.vue';
import AppButton from '@/components/AppButton.vue';

const router = useRouter()

const profile = ref({
  name: "张三",
  telephone: "13800138000",
  email: "zhangsan@example.com",
  workingCapital: 1000000,
  fixedCapital: 500000,
  annualIncome: 500000,
})

const handleSave = () => {
  // Logic to save profile
  console.log('Saved', profile.value)
  router.push('/account/profile')
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
