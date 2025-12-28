<!-- components/UserManagement.vue -->
<template>
  <div class="user-management">
    <!-- 搜索和操作栏 -->
    <div class="toolbar">
      <div class="search-box">
        <input
          v-model="searchKeyword"
          type="text"
          placeholder="搜索用户名、电话、邮箱..."
          class="search-input"
          @keyup.enter="handleSearch"
        />
        <button class="search-btn" @click="handleSearch">
          🔍
        </button>
      </div>

      <button class="add-btn" @click="showAddDialog = true">
        <span>+</span> 添加用户
      </button>
    </div>

    <!-- 用户表格 -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>用户类型</th>
            <th>电话</th>
            <th>邮箱</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>
              <span class="user-type" :class="user.type">
                {{ formatUserType(user.type) }}
              </span>
            </td>
            <td>{{ user.phone || '--' }}</td>
            <td>{{ user.email || '--' }}</td>
            <td>
              <span class="status-badge active">正常</span>
            </td>
            <td>
              <div class="action-buttons">
                <button class="action-btn edit-btn" @click="handleEdit(user)">
                  编辑
                </button>
                <button class="action-btn delete-btn" @click="handleDelete(user.id)">
                  删除
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading">
        <div class="spinner"></div>
        加载中...
      </div>

      <!-- 空状态 -->
      <div v-if="!loading && users.length === 0" class="empty-state">
        暂无用户数据
      </div>

      <!-- 分页 -->
      <div v-if="users.length > 0" class="pagination">
        <button class="page-btn" :disabled="page === 1" @click="handlePrevPage">
          ← 上一页
        </button>
        <span class="page-info">第 {{ page }} 页</span>
        <button class="page-btn" @click="handleNextPage">
          下一页 →
        </button>
      </div>
    </div>

    <!-- 添加/编辑对话框 -->
    <UserDialog
      v-if="showAddDialog || showEditDialog"
      :user="editingUser"
      :mode="showEditDialog ? 'edit' : 'add'"
      @save="handleSaveUser"
      @close="closeDialog"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { userApi } from '../../api/adminApi'
import { useMessage } from '@/composables/useMessage'
import UserDialog from './UserDialog.vue'

const users = ref([])
const loading = ref(false)
const searchKeyword = ref('')
const page = ref(1)
// const pageSize = 10

const showAddDialog = ref(false)
const showEditDialog = ref(false)
const editingUser = ref(null)

const { showSuccess, showError, showConfirm } = useMessage()

// 生命周期
onMounted(() => {
  fetchUsers()
})

// API调用
const fetchUsers = async () => {
  try {
    loading.value = true
    const response = await userApi.getUsers()
    users.value = response || []
  } catch (error) {
    console.error('获取用户列表失败:', error)
    users.value = []
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  page.value = 1
  fetchUsers()
}

const handlePrevPage = () => {
  if (page.value > 1) {
    page.value--
    fetchUsers()
  }
}

const handleNextPage = () => {
  page.value++
  fetchUsers()
}

const handleEdit = (user) => {
  editingUser.value = { ...user }
  showEditDialog.value = true
}

const handleDelete = async (id) => {
  try {
    await showConfirm('确定要删除这个用户吗？')
    await userApi.deleteUser(id)
    await fetchUsers()
    showSuccess('删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除用户失败:', error)
      showError('删除失败')
    }
  }
}

const handleSaveUser = async (userData) => {
  try {
    if (userData.id) {
      await userApi.updateUser(userData.id, userData)
    } else {
      await userApi.createUser(userData)
    }
    await fetchUsers()
    closeDialog()
    showSuccess('保存成功')
  } catch (error) {
    console.error('保存用户失败:', error)
    showError('保存失败')
  }
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  editingUser.value = null
}

const formatUserType = (type) => {
  const types = {
    buyer: '买方',
    seller: '卖方',
    admin: '管理员'
  }
  return types[type] || type
}
</script>

<style scoped>
.user-management {
  background: white;
  border-radius: 8px;
  padding: 20px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-box {
  display: flex;
  gap: 8px;
}

.search-input {
  width: 300px;
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
}

.search-input:focus {
  outline: none;
  border-color: #409eff;
}

.search-btn {
  padding: 8px 16px;
  background: #f5f7fa;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.add-btn {
  padding: 8px 16px;
  background: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.add-btn:hover {
  background: #66b1ff;
}

.table-container {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th {
  background: #f5f7fa;
  padding: 12px;
  text-align: left;
  color: #303133;
  font-weight: 500;
  border-bottom: 1px solid #ebeef5;
}

.data-table td {
  padding: 12px;
  border-bottom: 1px solid #ebeef5;
  color: #606266;
}

.user-type {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.user-type.buyer {
  background: #f0f9eb;
  color: #67c23a;
}

.user-type.seller {
  background: #ecf5ff;
  color: #409eff;
}

.user-type.admin {
  background: #fdf6ec;
  color: #e6a23c;
}

.status-badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.status-badge.active {
  background: #f0f9eb;
  color: #67c23a;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.action-btn {
  padding: 4px 12px;
  border: 1px solid #dcdfe6;
  background: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s;
}

.edit-btn {
  color: #409eff;
  border-color: #409eff;
}

.edit-btn:hover {
  background: #ecf5ff;
}

.delete-btn {
  color: #f56c6c;
  border-color: #f56c6c;
}

.delete-btn:hover {
  background: #fef0f0;
}

.loading {
  padding: 40px;
  text-align: center;
  color: #909399;
}

.spinner {
  border: 3px solid #f3f3f3;
  border-top: 3px solid #409eff;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  animation: spin 1s linear infinite;
  margin: 0 auto 10px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.empty-state {
  padding: 40px;
  text-align: center;
  color: #909399;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
}

.page-btn {
  padding: 6px 12px;
  border: 1px solid #dcdfe6;
  background: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  color: #606266;
  font-size: 14px;
}
</style>
