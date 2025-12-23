<!-- components/ContractManagement.vue -->
<template>
  <div class="contract-management">
    <!-- 筛选和操作栏 -->
    <div class="filter-bar">
      <div class="filter-group">
        <select v-model="filters.status" class="filter-select">
          <option value="">全部状态</option>
          <option value="pending">待处理</option>
          <option value="approved">已同意</option>
          <option value="rejected">已拒绝</option>
          <option value="completed">已完成</option>
        </select>
        
        <input 
          v-model="filters.buyerName" 
          type="text" 
          placeholder="买方名称" 
          class="filter-input"
        />
        
        <input 
          v-model="filters.houseName" 
          type="text" 
          placeholder="房屋名称" 
          class="filter-input"
        />
        
        <button class="filter-btn" @click="handleFilter">
          筛选
        </button>
        <button class="reset-btn" @click="resetFilters">
          重置
        </button>
      </div>
      
      <button class="add-btn" @click="showAddDialog = true">
        <span>+</span> 新建合同
      </button>
    </div>

    <!-- 合同表格 -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>合同ID</th>
            <th>买方</th>
            <th>房屋信息</th>
            <th>总金额</th>
            <th>付款方式</th>
            <th>付款截止</th>
            <th>交房截止</th>
            <th>买方同意</th>
            <th>卖方同意</th>
            <th>付款状态</th>
            <th>交房状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="contract in contracts" :key="contract.id">
            <td>{{ contract.id }}</td>
            <td>{{ contract.buyer?.name || '--' }}</td>
            <td>
              <div class="house-info">
                <div class="house-name">{{ contract.house?.name || '--' }}</div>
                <div v-if="contract.house?.price" class="house-price">
                  ¥{{ formatNumber(contract.house.price) }}/㎡
                </div>
              </div>
            </td>
            <td class="total-price">¥{{ formatNumber(contract.totalPrice) }}</td>
            <td>
              <span class="pay-way" :class="contract.payWay">
                {{ contract.payWay === 'full' ? '全款' : '分期' }}
              </span>
            </td>
            <td>{{ formatDate(contract.paytimeEnding) }}</td>
            <td>{{ formatDate(contract.deliveryEnding) }}</td>
            <td>
              <span class="agree-status" :class="getAgreeClass(contract.buyerAgree)">
                {{ getAgreeText(contract.buyerAgree) }}
              </span>
            </td>
            <td>
              <span class="agree-status" :class="getAgreeClass(contract.sellerAgree)">
                {{ getAgreeText(contract.sellerAgree) }}
              </span>
            </td>
            <td>
              <span class="status-badge" :class="contract.paid ? 'paid' : 'unpaid'">
                {{ contract.paid ? '已付款' : '未付款' }}
              </span>
            </td>
            <td>
              <span class="status-badge" :class="contract.delivered ? 'delivered' : 'undelivered'">
                {{ contract.delivered ? '已交房' : '未交房' }}
              </span>
            </td>
            <td>
              <div class="action-buttons">
                <button class="action-btn view-btn" @click="handleView(contract)">
                  详情
                </button>
                <button class="action-btn edit-btn" @click="handleEdit(contract)">
                  编辑
                </button>
                <button class="action-btn delete-btn" @click="handleDelete(contract.id)">
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
      <div v-if="!loading && contracts.length === 0" class="empty-state">
        暂无合同数据
      </div>
      
      <!-- 分页 -->
      <div v-if="contracts.length > 0" class="pagination">
        <button class="page-btn" :disabled="page === 1" @click="handlePrevPage">
          ← 上一页
        </button>
        <span class="page-info">第 {{ page }} 页</span>
        <button class="page-btn" @click="handleNextPage">
          下一页 →
        </button>
      </div>
    </div>

    <!-- 合同对话框 -->
    <ContractDialog 
      v-if="showAddDialog || showEditDialog || showViewDialog"
      :contract="currentContract"
      :mode="showViewDialog ? 'view' : (showEditDialog ? 'edit' : 'add')"
      @save="handleSaveContract"
      @close="closeDialog"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { contractApi } from '../../api/adminApi'
import ContractDialog from './ContractDialog.vue'

const contracts = ref([])
const loading = ref(false)
const page = ref(1)
const filters = ref({
  status: '',
  buyerName: '',
  houseName: ''
})

const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showViewDialog = ref(false)
const currentContract = ref(null)

// 生命周期
onMounted(() => {
  fetchContracts()
})

// API调用
const fetchContracts = async () => {
  try {
    loading.value = true
    const params = {
      page: page.value,
      ...filters.value
    }
    const response = await contractApi.getContracts(params)
    contracts.value = response?.content || response || []
  } catch (error) {
    console.error('获取合同列表失败:', error)
    contracts.value = []
  } finally {
    loading.value = false
  }
}

const handleFilter = () => {
  page.value = 1
  fetchContracts()
}

const resetFilters = () => {
  filters.value = {
    status: '',
    buyerName: '',
    houseName: ''
  }
  page.value = 1
  fetchContracts()
}

const handlePrevPage = () => {
  if (page.value > 1) {
    page.value--
    fetchContracts()
  }
}

const handleNextPage = () => {
  page.value++
  fetchContracts()
}

const handleView = (contract) => {
  currentContract.value = { ...contract }
  showViewDialog.value = true
}

const handleEdit = (contract) => {
  currentContract.value = { ...contract }
  showEditDialog.value = true
}

const handleDelete = async (id) => {
  if (!confirm('确定要删除这个合同吗？')) return
  
  try {
    await contractApi.deleteContract(id)
    await fetchContracts()
  } catch (error) {
    console.error('删除合同失败:', error)
    alert('删除失败')
  }
}

const handleSaveContract = async (contractData) => {
  try {
    if (contractData.id) {
      await contractApi.updateContract(contractData.id, contractData)
    } else {
      await contractApi.createContract(contractData)
    }
    await fetchContracts()
    closeDialog()
  } catch (error) {
    console.error('保存合同失败:', error)
    alert('保存失败')
  }
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  showViewDialog.value = false
  currentContract.value = null
}

// 工具函数
const getAgreeClass = (agree) => {
  return agree === 1 ? 'agreed' : agree === -1 ? 'rejected' : 'pending'
}

const getAgreeText = (agree) => {
  return agree === 1 ? '同意' : agree === -1 ? '拒绝' : '待处理'
}

const formatNumber = (num) => {
  if (!num) return '0'
  return num.toLocaleString('zh-CN')
}

const formatDate = (dateString) => {
  if (!dateString) return '--'
  return new Date(dateString).toLocaleDateString('zh-CN')
}
</script>

<style scoped>
.contract-management {
  background: white;
  border-radius: 8px;
  padding: 20px;
}

.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 10px;
}

.filter-group {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.filter-select,
.filter-input {
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  min-width: 120px;
}

.filter-input {
  flex: 1;
  max-width: 200px;
}

.filter-btn,
.reset-btn {
  padding: 8px 16px;
  border: 1px solid #dcdfe6;
  background: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.filter-btn {
  background: #409eff;
  color: white;
  border-color: #409eff;
}

.filter-btn:hover {
  background: #66b1ff;
}

.reset-btn:hover {
  border-color: #409eff;
  color: #409eff;
}

.house-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.house-name {
  font-weight: 500;
}

.house-price {
  font-size: 12px;
  color: #909399;
}

.total-price {
  font-weight: 600;
  color: #e6a23c;
}

.pay-way {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.pay-way.full {
  background: #f0f9eb;
  color: #67c23a;
}

.pay-way.installment {
  background: #ecf5ff;
  color: #409eff;
}

.agree-status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.agree-status.agreed {
  background: #f0f9eb;
  color: #67c23a;
}

.agree-status.rejected {
  background: #fef0f0;
  color: #f56c6c;
}

.agree-status.pending {
  background: #fdf6ec;
  color: #e6a23c;
}

.status-badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.status-badge.paid {
  background: #f0f9eb;
  color: #67c23a;
}

.status-badge.unpaid {
  background: #fef0f0;
  color: #f56c6c;
}

.status-badge.delivered {
  background: #f0f9eb;
  color: #67c23a;
}

.status-badge.undelivered {
  background: #fdf6ec;
  color: #e6a23c;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .data-table {
    min-width: 1200px;
  }
  
  .table-container {
    overflow-x: auto;
  }
}
</style>