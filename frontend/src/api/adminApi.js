// api/adminApi.js
import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true
})

// 请求拦截器
api.interceptors.request.use(config => {
  return config
}, error => {
  return Promise.reject(error)
})

// 响应拦截器 - 错误处理
api.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response?.status === 401) {
      // 未登录或Session过期
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// 用户管理API
export const userApi = {
  // 获取用户列表
  getUsers(params) {
    return api.get('/admin/users', { params })
  },

  // 获取单个用户
  getUser(id) {
    return api.get(`/admin/user/${id}`)
  },

  // 创建用户
  createUser(userData) {
    return api.post('/admin/user', userData)
  },

  // 更新用户
  updateUser(id, userData) {
    return api.post(`/admin/user/${id}`, userData)
  },

  // 删除用户
  deleteUser(id) {
    return api.delete(`/admin/user/${id}`)
  },

  // 搜索用户
  searchUsers(params) {
    // 这里根据后端API调整
    return api.get('/admin/users', { params })
  }
}

// 合同管理API
export const contractApi = {
  // 获取合同列表
  getContracts(params) {
    return api.get('/admin/contracts', { params })
  },

  // 获取单个合同
  getContract(id) {
    return api.post(`/contract/${id}`)
  },

  // 创建合同
  createContract(contractData) {
    return api.post('/contract', contractData)
  },

  // 更新合同
  updateContract(id, contractData) {
    return api.post(`/contract/${id}/update`, contractData)
  },

  // 删除合同
  deleteContract(id) {
    return api.delete(`/contract/${id}`)
  },

  // 买方同意/拒绝
  buyerAgree(id, agree) {
    return api.post(`/contract/${id}/buyer-agree`, null, {
      params: { agree }
    })
  },

  // 卖方同意/拒绝
  sellerAgree(id, agree) {
    return api.post(`/contract/${id}/seller-agree`, null, {
      params: { agree }
    })
  },

  // 更新付款状态
  updatePayment(id, paid) {
    return api.post(`/contract/${id}/payment`, null, {
      params: { paid }
    })
  },

  // 更新交房状态
  updateDelivery(id, delivered) {
    return api.post(`/contract/${id}/delivery`, null, {
      params: { delivered }
    })
  }
}

export default api
