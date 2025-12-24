import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

// Mock Data based on the structure used in ContractView.vue
const MOCK_CONTRACTS = [
  {
    c_id: 1001,
    c_buyer_id: 501,
    c_house_id: 1,
    c_total_price: 980000,
    c_pay_way: 'installment',
    c_buyer_agree: 1,
    c_seller_agree: 1,
    c_paid: 0,
    c_delivered: 0,
    c_paytime_ending: '2026-03-31',
    c_paytime_actually: null,
    c_delivery_ending: '2026-05-31',
    c_delivery_actually: null,
    buyer: { b_id: 501, b_name: '张三', b_phone: '13800000000' },
    house: { h_id: 1, h_name: '映月城 · A1' }
  }
]

export const useContractStore = defineStore('contract', () => {
  // State
  const contractList = ref([])
  const currentContract = ref(null)
  const isLoading = ref(false)
  const error = ref(null)
  const useMock = ref(false) // Default to false to use real API

  // Getters
  const getContractById = computed(() => (id) => {
    return contractList.value.find(c => c.c_id == id) || (useMock.value ? MOCK_CONTRACTS.find(c => c.c_id == id) : null)
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
      // Supports /api/contract or /api/admin/contracts depending on usage,
      // but Controller maps both to same method. We use /api/contract for simplicity.
      const response = await fetch(`http://localhost:8080/api/contract?${query}`, {
        credentials: 'include'
      })
      if (!response.ok) throw new Error('Failed to fetch contract list')
      const data = await response.json()
      // API returns Page<Contract>, we need content
      contractList.value = data.content || data
      return contractList.value
    } catch (err) {
      console.error('Fetch contract list error:', err)
      error.value = err.message
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
        const contract = MOCK_CONTRACTS.find(c => c.c_id == id)
        if (!contract) throw new Error('Contract not found')
        currentContract.value = contract
        return contract
      }

      // Backend requires POST for detail per Controller: @PostMapping("/contract/{id}")
      const response = await fetch(`http://localhost:8080/api/contract/${id}`, {
          method: 'POST',
          credentials: 'include'
      })
      if (!response.ok) throw new Error('Failed to fetch contract details')
      const data = await response.json()
      currentContract.value = data
      return data
    } catch (err) {
      console.error(`Fetch contract ${id} error:`, err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function createContract(contractData) {
      isLoading.value = true
      error.value = null
      try {
          if (useMock.value) {
              // Mock creation
              return contractData
          }
          const response = await fetch('http://localhost:8080/api/contract', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          credentials: 'include',
          body: JSON.stringify(contractData)
      })
          if (!response.ok) throw new Error('Failed to create contract')
          return await response.json()
      } catch (err) {
          error.value = err.message
          throw err
      } finally {
          isLoading.value = false
      }
  }

  async function updateContract(id, updateData) {
      isLoading.value = true
      try {
          const response = await fetch(`http://localhost:8080/api/contract/${id}/update`, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              credentials: 'include',
              body: JSON.stringify(updateData)
          })
          if (!response.ok) throw new Error('Failed to update contract')
          return await response.json()
      } catch (err) {
          error.value = err.message
          throw err
      } finally {
          isLoading.value = false
      }
  }

  async function deleteContract(id) {
      isLoading.value = true
      try {
          const response = await fetch(`http://localhost:8080/api/contract/${id}`, {
              method: 'DELETE',
              credentials: 'include'
          })
          if (!response.ok) throw new Error('Failed to delete contract')
          return true
      } catch (err) {
          error.value = err.message
          throw err
      } finally {
          isLoading.value = false
      }
  }

  async function signContract(contractId, role, agree) {
      // role: 'buyer' or 'seller'
      // agree: -1 (reject), 1 (agree)
      try {
          const endpoint = role === 'buyer'
              ? `/api/contract/${contractId}/buyer-agree?agree=${agree}`
              : `/api/contract/${contractId}/seller-agree?agree=${agree}`

          const response = await fetch(endpoint, {
              method: 'POST'
          })
          if (!response.ok) throw new Error('Sign failed')

          // Refresh data
          const updatedContract = await response.json()
          if (currentContract.value && currentContract.value.c_id == contractId) {
              currentContract.value = updatedContract
          }
          return updatedContract
      } catch (e) {
          console.error('Sign contract error:', e)
          throw e
      }
  }

  async function updatePayment(contractId, paid) {
      try {
        const response = await fetch(`http://localhost:8080/api/contract/${contractId}/payment?paid=${paid}`, {
            method: 'POST'
        })
        if (!response.ok) throw new Error('Payment update failed')
        const updatedContract = await response.json()
        if (currentContract.value && currentContract.value.c_id == contractId) {
            currentContract.value = updatedContract
        }
        return updatedContract
      } catch (e) {
          console.error(e)
          throw e
      }
    }

    async function updateDelivery(contractId, delivered) {
        try {
          const response = await fetch(`/api/contract/${contractId}/delivery?delivered=${delivered}`, {
              method: 'POST'
          })
          if (!response.ok) throw new Error('Delivery update failed')
          const updatedContract = await response.json()
          if (currentContract.value && currentContract.value.c_id == contractId) {
              currentContract.value = updatedContract
          }
          return updatedContract
        } catch (e) {
            console.error(e)
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
    createContract,
    updateContract,
    deleteContract,
    signContract,
    updatePayment,
    updateDelivery
  }
})
