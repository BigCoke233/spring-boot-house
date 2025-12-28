<!-- src/views/AdminDashboard.vue -->
<template>
  <div class="admin-dashboard">
    <!-- 顶部导航 -->
    <AdminHeader @logout="handleLogout" />

    <!-- 功能选项卡 -->
    <div class="function-tabs">
      <button
        class="tab-btn"
        :class="{ 'active': activeTab === 'users' }"
        @click="activeTab = 'users'"
      >
        <span class="tab-icon">👥</span>
        <span class="tab-text">用户管理</span>
      </button>

      <button
        class="tab-btn"
        :class="{ 'active': activeTab === 'contracts' }"
        @click="activeTab = 'contracts'"
      >
        <span class="tab-icon">📄</span>
        <span class="tab-text">合同管理</span>
      </button>
    </div>

    <!-- 内容区域 -->
    <div class="content-area">
      <UserManagement v-if="activeTab === 'users'" />
      <ContractManagement v-else />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import AdminHeader from '../components/admin/AdminHeader.vue'
import UserManagement from '../components/admin/UserManagement.vue'
import ContractManagement from '../components/admin/ContractManagement.vue'

const router = useRouter()
const userStore = useUserStore()
const activeTab = ref('users')

const handleLogout = async () => {
  try {
    await userStore.logout()
  } catch (e) {
    console.error('Logout failed', e)
  } finally {
    userStore.clearState()
    localStorage.removeItem('admin_token')
    router.push('/')
  }
}
</script>

<style scoped>
.admin-dashboard {
  min-height: 100vh;
  background: #f5f7fa;
}

.function-tabs {
  background: white;
  border-bottom: 1px solid #e4e7ed;
  padding: 0 20px;
  display: flex;
  gap: 4px;
}

.tab-btn {
  padding: 12px 24px;
  border: none;
  background: none;
  cursor: pointer;
  font-size: 14px;
  color: #606266;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s;
  position: relative;
}

.tab-btn:hover {
  color: #409eff;
}

.tab-btn.active {
  color: #409eff;
  font-weight: 500;
}

.tab-btn.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  right: 0;
  height: 2px;
  background: #409eff;
}

.tab-icon {
  font-size: 16px;
}

.content-area {
  padding: 20px;
}
</style>
