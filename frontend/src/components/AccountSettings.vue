<script setup>
import { ref } from 'vue'
import AppButton from '@/components/AppButton.vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const form = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const isLoading = ref(false)
const error = ref('')
const success = ref('')

async function handleSubmit() {
  error.value = ''
  success.value = ''
  
  if (form.value.newPassword !== form.value.confirmPassword) {
    error.value = '两次输入的密码不一致'
    return
  }

  if (form.value.newPassword.length < 6) {
    error.value = '新密码长度至少为 6 位'
    return
  }

  isLoading.value = true
  
  try {
    await userStore.changePassword({
      oldPassword: form.value.oldPassword,
      newPassword: form.value.newPassword
    })
    
    success.value = '密码修改成功'
    form.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
  } catch (e) {
    error.value = e.message || '修改失败'
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="max-w-xl">
    <h2 class="text-xl font-bold mb-6">基本设置</h2>
    
    <div class="bg-white p-6 rd-lg border border-neutral-200 shadow-sm">
      <h3 class="text-lg font-bold mb-4">修改密码</h3>
      
      <form @submit.prevent="handleSubmit" class="space-y-6">
        <div v-if="error" class="p-3 bg-red-50 text-red-600 text-sm rd border border-red-200">
          {{ error }}
        </div>
        <div v-if="success" class="p-3 bg-green-50 text-green-600 text-sm rd border border-green-200">
          {{ success }}
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">当前密码</label>
          <input 
            v-model="form.oldPassword" 
            type="password" 
            required 
            placeholder="请输入当前密码"
            class="w-full px-3 py-2 border border-gray-300 rd focus:outline-none focus:ring-2 focus:ring-black transition" 
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">新密码</label>
          <input 
            v-model="form.newPassword" 
            type="password" 
            required 
            placeholder="请输入新密码（至少6位）"
            class="w-full px-3 py-2 border border-gray-300 rd focus:outline-none focus:ring-2 focus:ring-black transition" 
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">确认新密码</label>
          <input 
            v-model="form.confirmPassword" 
            type="password" 
            required 
            placeholder="请再次输入新密码"
            class="w-full px-3 py-2 border border-gray-300 rd focus:outline-none focus:ring-2 focus:ring-black transition" 
          />
        </div>

        <div class="pt-2">
          <AppButton type="submit" :disabled="isLoading">
            {{ isLoading ? '提交中...' : '修改密码' }}
          </AppButton>
        </div>
      </form>
    </div>
  </div>
</template>
