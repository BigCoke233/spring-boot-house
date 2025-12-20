import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

// Mock Data based on the structure used in ContractView.vue
const MOCK_CONTRACTS = [
  {
    contractId: 1001,
    buyerId: 501,
    houseId: 1,
    sellerId: 201,
    totalPrice: 980000,
    payWay: 'installment',
    paymentStatus: false,
    completionStatus: false,
    agreeStatus: false,
    buyerAgree: true,
    sellerAgree: true,
    paid: false,
    delivered: false,
    paytimeEnding: '2026-03-31',
    paytimeActually: null,
    deliveryEnding: '2026-05-31',
    deliveryActually: null,
    totalPeriods: 12,
    paidCount: 3,
    downPaymentPaid: true,
    buyer: { id: 501, name: '张三', idCard: '110101199001010000', phone: '13800000000' },
    seller: { id: 201, name: '万科地产', idCard: '91440000MA5G3U1234', phone: '020-88888888' },
    legalClauses: {
        liabilityBuyer: '买方需按约定时间支付款项，逾期按日万分之五支付违约金。',
        liabilitySeller: '卖方需按约定时间交房，逾期按日万分之五支付违约金。',
        disputeResolution: '双方协商不成，可向房屋所在地人民法院提起诉讼。'
    }
  },
  {
    contractId: 1002,
    buyerId: 502,
    houseId: 2,
    sellerId: 202,
    totalPrice: 1200000,
    payWay: 'full',
    paymentStatus: true,
    completionStatus: false,
    agreeStatus: true,
    buyerAgree: true,
    sellerAgree: true,
    paid: true,
    delivered: false,
    paytimeEnding: '2025-12-31',
    paytimeActually: '2025-10-20',
    deliveryEnding: '2026-02-28',
    deliveryActually: null,
    totalPeriods: 0,
    paidCount: 0,
    downPaymentPaid: false,
    buyer: { id: 502, name: '李四', idCard: '110101199202020000', phone: '13900000000' },
    seller: { id: 202, name: '恒大地产', idCard: '91440000MA5G3U5678', phone: '0755-88888888' },
    legalClauses: {
        liabilityBuyer: '买方需按约定时间支付款项。',
        liabilitySeller: '卖方需按约定时间交房。',
        disputeResolution: '双方协商解决。'
    }
  },
  {
    contractId: 1003,
    buyerId: 503,
    houseId: 3,
    sellerId: 203,
    totalPrice: 860000,
    payWay: 'installment',
    paymentStatus: false,
    completionStatus: false,
    agreeStatus: false,
    buyerAgree: false,
    sellerAgree: true,
    paid: false,
    delivered: false,
    paytimeEnding: '2026-04-15',
    paytimeActually: null,
    deliveryEnding: '2026-06-30',
    deliveryActually: null,
    totalPeriods: 24,
    paidCount: 0,
    downPaymentPaid: false,
    buyer: { id: 503, name: '王五', idCard: '110101199505050000', phone: '13700000000' },
    seller: { id: 203, name: '绿地集团', idCard: '91310000MA5G3U9012', phone: '021-88888888' },
    legalClauses: {
        liabilityBuyer: '买方违约需支付合同总价20%的违约金。',
        liabilitySeller: '卖方违约需双倍返还定金。',
        disputeResolution: '提交仲裁委员会仲裁。'
    }
  },
  {
    contractId: 1004,
    buyerId: 504,
    houseId: 4,
    sellerId: 204,
    totalPrice: 1500000,
    payWay: 'full',
    paymentStatus: true,
    completionStatus: true,
    agreeStatus: true,
    buyerAgree: true,
    sellerAgree: true,
    paid: true,
    delivered: true,
    paytimeEnding: '2024-12-31',
    paytimeActually: '2024-11-15',
    deliveryEnding: '2025-01-31',
    deliveryActually: '2025-01-10',
    totalPeriods: 0,
    paidCount: 0,
    downPaymentPaid: false,
    buyer: { id: 504, name: '赵六', idCard: '110101198808080000', phone: '13600000000' },
    seller: { id: 204, name: '碧桂园', idCard: '91440000MA5G3U3456', phone: '0757-88888888' },
    legalClauses: {
        liabilityBuyer: '逾期付款需支付滞纳金。',
        liabilitySeller: '逾期交房需支付违约金。',
        disputeResolution: '向法院起诉。'
    }
  },
  {
    contractId: 1005,
    buyerId: 505,
    houseId: 5,
    sellerId: 205,
    totalPrice: 2000000,
    payWay: 'installment',
    paymentStatus: false,
    completionStatus: false,
    agreeStatus: true,
    buyerAgree: true,
    sellerAgree: true,
    paid: false,
    delivered: false,
    paytimeEnding: '2027-06-30',
    paytimeActually: null,
    deliveryEnding: '2027-12-31',
    deliveryActually: null,
    totalPeriods: 36,
    paidCount: 12,
    downPaymentPaid: true,
    buyer: { id: 505, name: '孙七', idCard: '110101199303030000', phone: '13500000000' },
    seller: { id: 205, name: '保利地产', idCard: '91440000MA5G3U7890', phone: '020-66666666' },
    legalClauses: {
        liabilityBuyer: '分期付款违约将导致合同终止。',
        liabilitySeller: '房屋质量问题需在30天内整改。',
        disputeResolution: '双方协商或仲裁。'
    }
  }
]

export const useContractStore = defineStore('contract', () => {
  // State
  const contractList = ref([])
  const currentContract = ref(null)
  const isLoading = ref(false)
  const error = ref(null)
  const useMock = ref(true)

  // Getters
  const getContractById = computed(() => (id) => {
    return contractList.value.find(c => c.contractId == id) || MOCK_CONTRACTS.find(c => c.contractId == id)
  })

  // Actions
  async function fetchContractList(filters = {}) {
    isLoading.value = true
    error.value = null
    try {
      if (useMock.value) {
        await new Promise(resolve => setTimeout(resolve, 500))
        contractList.value = MOCK_CONTRACTS
        return MOCK_CONTRACTS
      }

      const query = new URLSearchParams(filters).toString()
      const response = await fetch(`/api/contract?${query}`)
      if (!response.ok) throw new Error('Failed to fetch contract list')
      const data = await response.json()
      contractList.value = data
      return data
    } catch (err) {
      console.error('Fetch contract list error:', err)
      error.value = err.message
      if (!useMock.value) {
          console.warn('Falling back to mock data')
          contractList.value = MOCK_CONTRACTS
          return MOCK_CONTRACTS
      }
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function fetchContractById(id) {
    isLoading.value = true
    error.value = null
    try {
      if (useMock.value) {
        await new Promise(resolve => setTimeout(resolve, 300))
        const contract = MOCK_CONTRACTS.find(c => c.contractId == id)
        if (!contract) throw new Error('Contract not found')
        
        // Ensure nested objects exist for mock data if missing (safety check)
        if (!contract.buyer) contract.buyer = { id: contract.buyerId, name: '未知买方', idCard: '—', phone: '—' }
        if (!contract.seller) contract.seller = { id: contract.sellerId, name: '未知卖方', idCard: '—', phone: '—' }
        if (!contract.legalClauses) contract.legalClauses = { liabilityBuyer: '—', liabilitySeller: '—', disputeResolution: '—' }

        currentContract.value = contract
        return contract
      }

      const response = await fetch(`/api/contract/${id}`)
      if (!response.ok) throw new Error('Failed to fetch contract details')
      const data = await response.json()
      currentContract.value = data
      return data
    } catch (err) {
      console.error(`Fetch contract ${id} error:`, err)
      error.value = err.message
       if (!useMock.value) {
           const contract = MOCK_CONTRACTS.find(c => c.contractId == id)
           if (contract) {
               console.warn('Falling back to mock data')
               currentContract.value = contract
               return contract
           }
       }
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function signContract(contractId, role) {
      // role: 'buyer' or 'seller'
      try {
          if (useMock.value) {
              await new Promise(resolve => setTimeout(resolve, 500))
              const contract = MOCK_CONTRACTS.find(c => c.contractId == contractId)
              if (contract) {
                  if (role === 'buyer') contract.buyerAgree = true
                  if (role === 'seller') contract.sellerAgree = true
                  // Update current if matches
                  if (currentContract.value && currentContract.value.contractId == contractId) {
                       if (role === 'buyer') currentContract.value.buyerAgree = true
                       if (role === 'seller') currentContract.value.sellerAgree = true
                  }
              }
              return true
          }
          
          const response = await fetch(`/api/contract/${contractId}/sign`, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({ role })
          })
          if (!response.ok) throw new Error('Sign failed')
          
          // Refresh data
          await fetchContractById(contractId)
          return true
      } catch (e) {
          console.error('Sign contract error:', e)
          throw e
      }
  }

  return {
    contractList,
    currentContract,
    isLoading,
    error,
    useMock,
    fetchContractList,
    fetchContractById,
    getContractById,
    signContract
  }
})
