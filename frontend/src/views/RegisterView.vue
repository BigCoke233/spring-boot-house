<template>
  <div class="register-container">
    <div class="register-box">
      <h2>注册账号</h2>
      <form @submit.prevent="handleRegister">
        <!-- Account Info -->
        <div class="section-title">账号信息</div>
        <div class="form-group">
          <label for="username">用户名</label>
          <input
            type="text"
            id="username"
            v-model="formData.username"
            required
            placeholder="请输入用户名"
          >
        </div>

        <div class="form-group">
          <label for="password">密码</label>
          <input
            type="password"
            id="password"
            v-model="formData.password"
            required
            placeholder="请输入密码"
            @keyup.enter="handleRegister"
          >
        </div>

        <div class="form-group">
          <label>账户类型</label>
          <div class="radio-group">
            <label class="radio-label">
              <input type="radio" v-model="formData.type" value="buyer">
              我是买家
            </label>
            <label class="radio-label">
              <input type="radio" v-model="formData.type" value="seller">
              我是房产中介
            </label>
          </div>
        </div>

        <!-- Buyer Info -->
        <template v-if="formData.type === 'buyer'">
          <div class="section-title">个人信息</div>
          <div class="form-group">
            <label for="buyer-name">真实姓名</label>
            <input type="text" id="buyer-name" v-model="buyerInfo.name" required>
          </div>
          <div class="form-group">
            <label for="buyer-phone">手机号码</label>
            <input type="tel" id="buyer-phone" v-model="buyerInfo.phone" required>
          </div>
          <div class="form-group">
            <label for="buyer-email">电子邮箱</label>
            <input type="email" id="buyer-email" v-model="buyerInfo.email" required>
          </div>
          <div class="form-group">
            <label for="mobile-assets">流动资产 (元)</label>
            <input type="number" id="mobile-assets" v-model="buyerInfo.mobileAssets">
          </div>
          <div class="form-group">
            <label for="fixed-assets">固定资产 (元)</label>
            <input type="number" id="fixed-assets" v-model="buyerInfo.fixedAssets">
          </div>
          <div class="form-group">
            <label for="annual-income">年收入 (元)</label>
            <input type="number" id="annual-income" v-model="buyerInfo.annualIncome">
          </div>
        </template>

        <!-- Seller Info -->
        <template v-if="formData.type === 'seller'">
          <div class="section-title">中介信息</div>
          <div class="form-group">
            <label for="seller-name">公司名称</label>
            <input type="text" id="seller-name" v-model="sellerInfo.name" required @keyup.enter="handleRegister">
          </div>
          <div class="form-group">
            <label for="seller-phone">联系电话</label>
            <input type="tel" id="seller-phone" v-model="sellerInfo.phone" required @keyup.enter="handleRegister">
          </div>
          <div class="form-group">
            <label for="seller-email">联系邮箱</label>
            <input type="email" id="seller-email" v-model="sellerInfo.email" required @keyup.enter="handleRegister">
          </div>
          <div class="form-group">
            <label for="seller-website">公司网站</label>
            <input type="url" id="seller-website" v-model="sellerInfo.website" @keyup.enter="handleRegister">
          </div>
          <div class="form-group">
            <label for="seller-describe">公司简介</label>
            <textarea
              id="seller-describe"
              v-model="sellerInfo.describe"
              rows="3"
            ></textarea>
          </div>
        </template>

        <div class="error-message" v-if="error">{{ error }}</div>

        <button type="submit" class="register-btn" :disabled="loading">
          {{ loading ? '注册中...' : '立即注册' }}
        </button>
      </form>

      <div class="login-link">
        已有账号？ <router-link to="/login">立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const error = ref('')

const formData = reactive({
  username: '',
  password: '',
  type: 'buyer'
})

const buyerInfo = reactive({
  name: '',
  phone: '',
  email: '',
  mobileAssets: null,
  fixedAssets: null,
  annualIncome: null
})

const sellerInfo = reactive({
  name: '',
  phone: '',
  email: '',
  website: '',
  describe: ''
})

const handleRegister = async () => {
  loading.value = true
  error.value = ''

  try {
    const info = formData.type === 'buyer' ? buyerInfo : sellerInfo

    await userStore.register({
      username: formData.username,
      password: formData.password,
      type: formData.type,
      info: info
    })

    await userStore.login({
      username: formData.username,
      password: formData.password
    })

    router.push('/')
  } catch (err) {
    error.value = err.message || '注册失败，请重试'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  min-height: calc(100vh - 60px);
  padding: 2rem 0;
  background-color: #f5f7fa;
}

.register-box {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 500px;
}

h2 {
  text-align: center;
  margin-bottom: 2rem;
  color: #2c3e50;
}

.section-title {
  font-weight: bold;
  margin: 1.5rem 0 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #eee;
  color: #2c3e50;
}

.form-group {
  margin-bottom: 1.2rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: #606266;
}

input, textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.2s;
}

input:focus, textarea:focus {
  outline: none;
  border-color: #409eff;
}

.radio-group {
  display: flex;
  gap: 2rem;
}

.radio-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
}

.radio-label input {
  width: auto;
}

.register-btn {
  width: 100%;
  padding: 0.75rem;
  background-color: #67c23a;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s;
  margin-top: 1rem;
}

.register-btn:hover {
  background-color: #85ce61;
}

.register-btn:disabled {
  background-color: #c2e7b0;
  cursor: not-allowed;
}

.error-message {
  color: #f56c6c;
  margin-bottom: 1rem;
  font-size: 0.9rem;
}

.login-link {
  margin-top: 1rem;
  text-align: center;
  font-size: 0.9rem;
  color: #606266;
}

.login-link a {
  color: #409eff;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
