<!-- src/components/admin/UserDialog.vue -->
<template>
  <div class="modal-overlay" @click.self="handleClose">
    <div class="modal-dialog">
      <div class="modal-header">
        <h3>{{ mode === 'add' ? '添加用户' : '编辑用户' }}</h3>
        <button class="close-btn" @click="handleClose">×</button>
      </div>
      <div class="modal-body">
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label>用户类型 <span class="required">*</span></label>
            <select v-model="formData.type" required @change="handleTypeChange">
              <option value="">请选择用户类型</option>
              <option value="buyer">买方</option>
              <option value="seller">卖方</option>
              <option value="admin">管理员</option>
            </select>
          </div>

          <div class="form-group">
            <label>用户名 <span class="required">*</span></label>
            <input
              v-model="formData.username"
              type="text"
              placeholder="请输入用户名"
              required
              :maxlength="20"
            />
          </div>

          <div class="form-group">
            <label>密码 <span v-if="mode === 'add'" class="required">*</span></label>
            <input
              v-model="formData.password"
              :type="showPassword ? 'text' : 'password'"
              :placeholder="mode === 'add' ? '请输入密码' : '留空则不修改密码'"
              :required="mode === 'add'"
              :minlength="6"
            />
            <button
              type="button"
              class="toggle-password"
              @click="showPassword = !showPassword"
            >
              {{ showPassword ? '👁️' : '👁️‍🗨️' }}
            </button>
          </div>

          <!-- 买方特定字段 -->
          <div v-if="formData.type === 'buyer'" class="buyer-fields">
            <div class="section-title">买方信息</div>
            <div class="form-group">
              <label>真实姓名 <span class="required">*</span></label>
              <input
                v-model="formData.name"
                type="text"
                placeholder="请输入真实姓名"
                required
              />
            </div>

            <div class="form-group">
              <label>电话号码 <span class="required">*</span></label>
              <input
                v-model="formData.phone"
                type="tel"
                placeholder="请输入电话号码"
                required
              />
            </div>

            <div class="form-group">
              <label>电子邮箱 <span class="required">*</span></label>
              <input
                v-model="formData.email"
                type="email"
                placeholder="请输入电子邮箱"
                required
              />
            </div>

            <div class="form-group">
              <label>流动资产（元）</label>
              <input
                v-model="formData.mobileAssets"
                type="number"
                placeholder="流动资产金额"
                min="0"
                step="1000"
              />
            </div>

            <div class="form-group">
              <label>固定资产（元）</label>
              <input
                v-model="formData.fixedAssets"
                type="number"
                placeholder="固定资产金额"
                min="0"
                step="1000"
              />
            </div>

            <div class="form-group">
              <label>年收入（元）</label>
              <input
                v-model="formData.annualIncome"
                type="number"
                placeholder="年收入金额"
                min="0"
                step="1000"
              />
            </div>
          </div>

          <!-- 卖方特定字段 -->
          <div v-else-if="formData.type === 'seller'" class="seller-fields">
            <div class="section-title">卖方信息</div>
            <div class="form-group">
              <label>公司/个人名称 <span class="required">*</span></label>
              <input
                v-model="formData.name"
                type="text"
                placeholder="请输入公司或个人名称"
                required
              />
            </div>

            <div class="form-group">
              <label>电话号码 <span class="required">*</span></label>
              <input
                v-model="formData.phone"
                type="tel"
                placeholder="请输入电话号码"
                required
              />
            </div>

            <div class="form-group">
              <label>电子邮箱 <span class="required">*</span></label>
              <input
                v-model="formData.email"
                type="email"
                placeholder="请输入电子邮箱"
                required
              />
            </div>

            <div class="form-group">
              <label>公司描述</label>
              <textarea
                v-model="formData.describe"
                placeholder="请输入公司或个人描述"
                rows="3"
              ></textarea>
            </div>

            <div class="form-group">
              <label>官方网站</label>
              <input
                v-model="formData.website"
                type="url"
                placeholder="请输入官方网站地址"
              />
            </div>
          </div>

          <div class="form-actions">
            <button type="button" class="cancel-btn" @click="handleClose">
              取消
            </button>
            <button type="submit" class="save-btn" :disabled="isSubmitting">
              <span v-if="isSubmitting" class="loading-spinner"></span>
              {{ mode === 'add' ? '创建用户' : '保存修改' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useMessage } from '@/composables/useMessage'

const props = defineProps({
  // 用户数据（编辑时传入）
  user: {
    type: Object,
    default: null
  },
  // 模式：add 添加 / edit 编辑
  mode: {
    type: String,
    default: 'add',
    validator: (value) => ['add', 'edit'].includes(value)
  }
})

const emit = defineEmits(['save', 'close'])
const { showWarning } = useMessage()

// 表单数据
const formData = ref({
  type: '',
  username: '',
  password: '',
  phone: '',
  email: '',
  // 买方字段
  mobileAssets: 0,
  fixedAssets: 0,
  annualIncome: 0,
  // 卖方字段
  name: '',
  describe: '',
  website: ''
})

const showPassword = ref(false)
const isSubmitting = ref(false)

const isFormValid = computed(() => {
  const requiredFields = ['type', 'username']

  for (const field of requiredFields) {
    if (!formData.value[field]?.trim()) {
      return false
    }
  }

  // 添加用户时密码必填
  if (props.mode === 'add' && !formData.value.password.trim()) {
    return false
  }

  // 根据类型校验必填字段
  if (formData.value.type === 'buyer') {
    if (!formData.value.name?.trim() || !formData.value.phone?.trim() || !formData.value.email?.trim()) {
      return false
    }
  } else if (formData.value.type === 'seller') {
    if (!formData.value.name?.trim() || !formData.value.phone?.trim() || !formData.value.email?.trim()) {
      return false
    }
  }

  return true
})

const resetForm = () => {
  formData.value = {
    type: '',
    username: '',
    password: '',
    phone: '',
    email: '',
    mobileAssets: 0,
    fixedAssets: 0,
    annualIncome: 0,
    name: '',
    describe: '',
    website: ''
  }
  showPassword.value = false
  isSubmitting.value = false
}

// 监听props变化
watch(() => props.user, (newUser) => {
  if (newUser) {
    // 拷贝用户数据到表单
    formData.value = {
      type: newUser.type || '',
      username: newUser.username || '',
      password: '', // 编辑时不显示密码
      phone: newUser.phone || '',
      email: newUser.email || '',
      mobileAssets: newUser.mobileAssets || 0,
      fixedAssets: newUser.fixedAssets || 0,
      annualIncome: newUser.annualIncome || 0,
      name: newUser.name || '',
      describe: newUser.describe || '',
      website: newUser.website || ''
    }
  } else {
    // 重置表单
    resetForm()
  }
}, { immediate: true })

// 方法
const handleTypeChange = () => {
  // 切换用户类型时，清空相关字段
  if (formData.value.type === 'buyer') {
    formData.value.name = ''
    formData.value.description = ''
    formData.value.website = ''
  } else if (formData.value.type === 'seller') {
    formData.value.mobileAssets = 0
    formData.value.fixedAssets = 0
    formData.value.annualIncome = 0
  }
}

const handleClose = () => {
  emit('close')
}

const handleSubmit = async () => {
  if (!isFormValid.value) {
    showWarning('请填写完整的表单信息')
    return
  }

  try {
    isSubmitting.value = true

    // 准备要提交的数据
    const submitData = { ...formData.value }

    // Map fields to match backend expected keys (optional but good for consistency)
    if (submitData.type === 'buyer') {
      submitData.b_name = submitData.name
      submitData.b_phone = submitData.phone
      submitData.b_email = submitData.email
      submitData.b_mobile_assets = submitData.mobileAssets
      submitData.b_fixed_assets = submitData.fixedAssets
      submitData.b_annual_income = submitData.annualIncome

      // Clean up generic keys if desired, or keep them as fallback
    } else if (submitData.type === 'seller') {
      submitData.s_name = submitData.name
      submitData.s_phone = submitData.phone
      submitData.s_email = submitData.email
      submitData.s_describe = submitData.describe
      submitData.s_website = submitData.website
    }

    // 如果是编辑模式且密码为空，则移除密码字段
    if (props.mode === 'edit' && !submitData.password.trim()) {
      delete submitData.password
    }

    // 格式化数字字段
    if (submitData.type === 'buyer') {
      submitData.mobileAssets = parseFloat(submitData.mobileAssets) || 0
      submitData.fixedAssets = parseFloat(submitData.fixedAssets) || 0
      submitData.annualIncome = parseFloat(submitData.annualIncome) || 0
    }

    // 移除不需要的字段
    if (submitData.type !== 'buyer') {
      delete submitData.mobileAssets
      delete submitData.fixedAssets
      delete submitData.annualIncome
    }

    if (submitData.type !== 'seller') {
      delete submitData.name
      delete submitData.description
      delete submitData.website
    }

    // 发送保存事件
    emit('save', submitData)

  } catch (error) {
    console.error('表单提交错误:', error)
  } finally {
    isSubmitting.value = false
  }
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
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.modal-dialog {
  background: white;
  border-radius: 8px;
  width: 500px;
  max-width: 90vw;
  max-height: 85vh;
  overflow-y: auto;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  animation: slideUp 0.3s ease;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #e4e7ed;
  position: sticky;
  top: 0;
  background: white;
  z-index: 1;
  border-radius: 8px 8px 0 0;
}

.modal-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  font-size: 28px;
  cursor: pointer;
  color: #909399;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: all 0.3s;
}

.close-btn:hover {
  background: #f5f7fa;
  color: #606266;
}

.modal-body {
  padding: 24px;
}

.form-group {
  margin-bottom: 20px;
  position: relative;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #606266;
  font-size: 14px;
  font-weight: 500;
}

.required {
  color: #f56c6c;
  margin-left: 2px;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  font-family: inherit;
  transition: all 0.3s;
  box-sizing: border-box;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
}

.form-group input[type="password"] {
  padding-right: 40px;
}

.toggle-password {
  position: absolute;
  right: 12px;
  top: 35px;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 18px;
  padding: 4px;
  color: #909399;
  transition: color 0.3s;
}

.toggle-password:hover {
  color: #409eff;
}

.error-tip {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 4px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #e4e7ed;
}

.buyer-fields,
.seller-fields {
  background: #f8f9fa;
  border-radius: 6px;
  padding: 16px;
  margin-top: 16px;
  border: 1px solid #e4e7ed;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 32px;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
}

.cancel-btn,
.save-btn {
  padding: 10px 24px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s;
  min-width: 100px;
  text-align: center;
  border: none;
}

.cancel-btn {
  background: white;
  border: 1px solid #dcdfe6;
  color: #606266;
}

.cancel-btn:hover {
  border-color: #c0c4cc;
  background: #f5f7fa;
}

.save-btn {
  background: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.save-btn:hover:not(:disabled) {
  background: #66b1ff;
}

.save-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.loading-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 响应式调整 */
@media (max-width: 600px) {
  .modal-dialog {
    width: 95vw;
    max-height: 90vh;
  }

  .form-actions {
    flex-direction: column;
  }

  .cancel-btn,
  .save-btn {
    width: 100%;
  }
}
</style>
