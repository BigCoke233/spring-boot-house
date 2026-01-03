<!-- src/components/admin/ContractDialog.vue -->
<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-dialog">
      <div class="modal-header">
        <h3>{{ mode === 'add' ? '新建合同' : mode === 'edit' ? '编辑合同' : '合同详情' }}</h3>
        <button class="close-btn" @click="$emit('close')">×</button>
      </div>
      <div class="modal-body">
        <form @submit.prevent="handleSubmit" v-if="mode !== 'view'">
          <div class="form-group">
            <label>买方ID</label>
            <input v-model.number="formData.buyerId" type="number" placeholder="输入买方ID" required />
          </div>

          <div class="form-group">
            <label>房屋ID</label>
            <input v-model.number="formData.houseId" type="number" placeholder="输入房屋ID" required />
          </div>

          <div class="form-group">
            <label>总金额</label>
            <input v-model.number="formData.totalPrice" type="number" placeholder="输入总金额" required step="0.01" />
          </div>

          <div class="form-group">
            <label>付款方式</label>
            <select v-model="formData.payWay" required>
              <option value="full">全款</option>
              <option value="installment">分期</option>
            </select>
          </div>

          <div class="form-group">
            <label>付款截止日期</label>
            <input v-model="formData.paytimeEnding" type="date" required />
          </div>

          <div class="form-group">
            <label>交房截止日期</label>
            <input v-model="formData.deliveryEnding" type="date" required />
          </div>

          <div class="form-actions">
            <button type="button" class="cancel-btn" @click="$emit('close')">
              取消
            </button>
            <button type="submit" class="save-btn">
              {{ mode === 'add' ? '创建' : '保存' }}
            </button>
          </div>
        </form>

        <div v-else class="view-mode">
          <div class="info-item">
            <label>合同ID:</label>
            <span>{{ contract.id }}</span>
          </div>
          <div class="info-item">
            <label>买方ID:</label>
            <span>{{ contract.buyerId || contract.buyer?.id }}</span>
          </div>
          <div class="info-item">
            <label>买方姓名:</label>
            <span>{{ contract.buyerName || contract.buyer?.name }}</span>
          </div>
          <div class="info-item">
            <label>房屋ID:</label>
            <span>{{ contract.houseId || contract.house?.id }}</span>
          </div>
          <div class="info-item">
            <label>房屋名称:</label>
            <span>{{ contract.houseName || contract.house?.name }}</span>
          </div>
          <div class="info-item">
            <label>总金额:</label>
            <span>¥{{ formatNumber(contract.totalPrice) }}</span>
          </div>
          <div class="info-item">
            <label>付款方式:</label>
            <span>{{ formatPayWay(contract.payWay) }}</span>
          </div>
          <div class="info-item">
            <label>付款截止:</label>
            <span>{{ formatDate(contract.paytimeEnding) }}</span>
          </div>
          <div class="info-item">
            <label>交房截止:</label>
            <span>{{ formatDate(contract.deliveryEnding) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  contract: {
    type: Object,
    default: () => ({})
  },
  mode: {
    type: String,
    default: 'add'
  }
})

const emit = defineEmits(['save', 'close'])

const formData = ref({
  buyerId: '',
  houseId: '',
  totalPrice: '',
  payWay: 'full',
  paytimeEnding: '',
  deliveryEnding: ''
})

// 监听props变化
watch(() => props.contract, (newVal) => {
  if (newVal) {
    // Format dates for input[type="date"]
    const formatDateForInput = (dateStr) => {
      if (!dateStr || dateStr === '—' || dateStr === '--') return ''
      // Try to parse non-standard formats like "2025/1/3" or handle standard Date objects
      const d = new Date(dateStr)
      if (isNaN(d.getTime())) return ''
      return d.toISOString().split('T')[0]
    }

    formData.value = {
      ...newVal,
      buyerId: newVal.buyerId || newVal.buyer?.id,
      houseId: newVal.houseId || newVal.house?.id,
      paytimeEnding: formatDateForInput(newVal.paytimeEnding),
      deliveryEnding: formatDateForInput(newVal.deliveryEnding)
    }
  }
}, { immediate: true })

const handleSubmit = () => {
  // Construct a clean payload with only the fields we want to send
  const payload = {
    id: formData.value.id, // Ensure ID is present if editing
    buyerId: formData.value.buyerId,
    houseId: formData.value.houseId,
    totalPrice: formData.value.totalPrice,
    payWay: formData.value.payWay,
    paytimeEnding: formData.value.paytimeEnding || null,
    deliveryEnding: formData.value.deliveryEnding || null
  }
  emit('save', payload)
}

const formatNumber = (num) => {
  if (!num) return '0'
  return num.toLocaleString('zh-CN')
}

const formatDate = (dateString) => {
  if (!dateString || dateString === '—' || dateString === '--') return '--'
  const d = new Date(dateString)
  if (isNaN(d.getTime())) return dateString // Return original if invalid
  return d.toLocaleDateString('zh-CN')
}

const formatPayWay = (way) => {
  const map = {
    full: '全款',
    installment: '分期'
  }
  return map[way] || way
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-dialog {
  background: white;
  border-radius: 8px;
  width: 500px;
  max-width: 90vw;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #e4e7ed;
}

.modal-header h3 {
  margin: 0;
  color: #303133;
  font-size: 16px;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #909399;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-btn:hover {
  color: #606266;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  color: #606266;
  font-size: 14px;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #409eff;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 24px;
}

.cancel-btn,
.save-btn {
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.cancel-btn {
  background: white;
  border: 1px solid #dcdfe6;
  color: #606266;
}

.save-btn {
  background: #409eff;
  border: 1px solid #409eff;
  color: white;
}

.view-mode .info-item {
  margin-bottom: 12px;
  display: flex;
}

.view-mode .info-item label {
  width: 100px;
  color: #909399;
  font-size: 14px;
}

.view-mode .info-item span {
  flex: 1;
  color: #303133;
  font-size: 14px;
}
</style>
