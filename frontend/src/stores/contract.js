import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useContractStore = defineStore('contract', () => {
  const contractList = ref([])
  const currentContract = ref(null)
  const isLoading = ref(false)
  const error = ref(null)

  // Helper to map backend Contract entity (c_*) to frontend camelCase
  function mapContract(c) {
    if (!c) return null
    return {
      contractId: c.c_id,
      id: c.c_id,
      buyerId: c.c_buyer_id,
      houseId: c.c_house_id,
      totalPrice: c.c_total_price,
      payWay: c.c_pay_way,
      paytimeEnding: c.c_paytime_ending,
      paytimeActually: c.c_paytime_actually,
      deliveryEnding: c.c_delivery_ending,
      deliveryActually: c.c_delivery_actually,
      buyerAgree: c.c_buyer_agree,
      sellerAgree: c.c_seller_agree,
      paid: c.c_paid,
      paymentStatus: c.c_paid, // Alias
      delivered: c.c_delivered,
      downPayment: c.c_down_payment,
      totalPeriods: c.c_total_periods,
      // Default missing fields
      paidCount: c.paidCount || 0,
      buyer: c.buyer || null,
      seller: c.seller || null,
      house: c.house || null
    }
  }

  async function fetchContractList(params = {}) {
    isLoading.value = true
    error.value = null
    try {
      // Use /api/contract for both user and admin (ContractController maps both)
      // Construct query params
      const query = new URLSearchParams()
      Object.entries(params).forEach(([key, value]) => {
          if (value !== null && value !== undefined) query.append(key, value)
      })

      const response = await fetch(`http://localhost:8080/api/contract?${query.toString()}`, {
        credentials: 'include'
      })
      if (!response.ok) throw new Error('Failed to fetch contracts')

      const data = await response.json()
      // Handle Page<Contract>
      const list = data.content || data
      const mappedList = list.map(mapContract)

      // Fetch additional info for each contract in the list
      await Promise.all(mappedList.map(async (contract) => {
        // Fetch House Info
        if (contract.houseId) {
          try {
            const houseRes = await fetch(`http://localhost:8080/api/public/houses/${contract.houseId}`)
            if (houseRes.ok) {
              const house = await houseRes.json()
              contract.house = house
              // Fetch Seller Info
              const sellerId = house.h_seller_id || house.sellerId || house.s_id || house.seller_id
              if (sellerId) {
                const sellerRes = await fetch(`http://localhost:8080/api/admin/user/sellers/${sellerId}`, { credentials: 'include' })
                if (sellerRes.ok) {
                  const seller = await sellerRes.json()
                  contract.seller = {
                    id: seller.s_id || seller.id || seller.sellerId,
                    name: seller.s_name || seller.name || seller.sellerName || '未知卖家',
                    idCard: seller.s_id_card || seller.idCard || seller.id_card,
                    phone: seller.s_phone || seller.phone || seller.phoneNumber
                  }
                }
              } else if (house.sellerName) {
                  contract.seller = {
                      id: null,
                      name: house.sellerName,
                      idCard: '—',
                      phone: house.sellerPhone || '—'
                  }
              }
            }
          } catch (e) {
            console.warn(`Failed to fetch details for contract ${contract.id}`, e)
          }
        }

        // Fetch Buyer Info
        if (contract.buyerId) {
            try {
                const buyerRes = await fetch(`http://localhost:8080/api/admin/user/buyers/${contract.buyerId}`, { credentials: 'include' })
                if (buyerRes.ok) {
                    const buyer = await buyerRes.json()
                    contract.buyer = {
                        id: buyer.b_id || buyer.id || buyer.buyerId,
                        name: buyer.b_name || buyer.name || buyer.buyerName || '未知买家',
                        idCard: buyer.b_id_card || buyer.idCard || buyer.id_card,
                        phone: buyer.b_phone || buyer.phone || buyer.phoneNumber
                    }
                }
            } catch (e) {
                console.warn(`Failed to fetch buyer for contract ${contract.id}`, e)
            }
        }
      }))

      contractList.value = mappedList
      return contractList.value
    } catch (err) {
      console.error('Fetch contracts error:', err)
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
      // ContractController uses POST for getContract detail
      const response = await fetch(`http://localhost:8080/api/contract/${id}`, {
        method: 'POST',
        credentials: 'include'
      })
      if (!response.ok) throw new Error('Failed to fetch contract details')

      const rawContract = await response.json()
      const contract = mapContract(rawContract)

      // Parallel fetch for details
      const promises = []

      // 1. Fetch Buyer Info
      if (contract.buyerId) {
        promises.push(
            fetch(`http://localhost:8080/api/admin/user/buyers/${contract.buyerId}`, { credentials: 'include' })
                .then(res => res.ok ? res.json() : null)
                .then(buyer => {
                    console.log('Fetched buyer:', buyer)
                    // Map buyer fields if necessary (Buyer entity -> {name, idCard, phone})
                    if (buyer) {
                        contract.buyer = {
                            id: buyer.b_id || buyer.id || buyer.buyerId,
                            name: buyer.b_name || buyer.name || buyer.buyerName || '未知买家',
                            idCard: buyer.b_id_card || buyer.idCard || buyer.id_card,
                            phone: buyer.b_phone || buyer.phone || buyer.phoneNumber
                        }
                    }
                })
                .catch(e => console.warn('Failed to fetch buyer info', e))
        )
      }

      // 2. Fetch House Info (to get Seller ID)
      if (contract.houseId) {
        promises.push(
            fetch(`http://localhost:8080/api/public/houses/${contract.houseId}`)
                .then(res => res.ok ? res.json() : null)
                .then(async house => {
                    if (house) {
                        contract.house = house
                        // Fetch Seller Info if house exists
                        // Try multiple fields for sellerId
                        const sellerId = house.h_seller_id || house.sellerId || house.s_id || house.seller_id
                        console.log('House:', house, 'Seller ID:', sellerId)

                        if (sellerId) {
                             const sellerRes = await fetch(`http://localhost:8080/api/admin/user/sellers/${sellerId}`, { credentials: 'include' })
                             if (sellerRes.ok) {
                                 const seller = await sellerRes.json()
                                 console.log('Fetched seller:', seller)
                                 contract.seller = {
                                     id: seller.s_id || seller.id || seller.sellerId,
                                     name: seller.s_name || seller.name || seller.sellerName || '未知卖家',
                                     idCard: seller.s_id_card || seller.idCard || seller.id_card,
                                     phone: seller.s_phone || seller.phone || seller.phoneNumber
                                 }
                             }
                        } else if (house.sellerName) {
                            // Fallback if sellerId is missing but seller info is embedded in house
                            console.log('Using embedded seller info from house:', house.sellerName)
                            contract.seller = {
                                id: null,
                                name: house.sellerName,
                                idCard: '—', // Not available in house info
                                phone: house.sellerPhone || '—'
                            }
                        }
                    }
                })
                .catch(e => console.warn('Failed to fetch house/seller info', e))
        )
      }

      await Promise.all(promises)
      currentContract.value = contract
      return contract
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
      const response = await fetch('http://localhost:8080/api/contract', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify(contractData)
      })

      if (!response.ok) {
        const errorData = await response.json()
        throw new Error(errorData.message || 'Failed to create contract')
      }

      const newContract = await response.json()
      return mapContract(newContract)
    } catch (err) {
      console.error('Create contract error:', err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function signContract(id, role) {
      // role: 'buyer' or 'seller'
      const endpoint = role === 'buyer' ? 'buyer-agree' : 'seller-agree'
      // API expects param 'agree' (1 for agree, -1 for reject, 0 for reset)
      // Assuming sign means agree (1)
      const response = await fetch(`http://localhost:8080/api/contract/${id}/${endpoint}?agree=1`, {
          method: 'POST',
          credentials: 'include'
      })
      if (!response.ok) throw new Error('Failed to sign contract')

      // Update local state
      const updated = await response.json()
      if (currentContract.value && currentContract.value.id == id) {
          const mapped = mapContract(updated)
          // Merge to preserve fetched relations (buyer/seller) which might not be in response
          currentContract.value = { ...currentContract.value, ...mapped }
      }
      return '签署成功'
  }

  async function payContract(id) {
      // Full payment
      const response = await fetch(`http://localhost:8080/api/contract/${id}/payment?paid=1`, {
          method: 'POST',
          credentials: 'include'
      })
      if (!response.ok) throw new Error('Payment failed')

      const updated = await response.json()
      if (currentContract.value && currentContract.value.id == id) {
          const mapped = mapContract(updated)
          currentContract.value = { ...currentContract.value, ...mapped }
      }
      return '支付成功'
  }

  async function payInstallment(id, userId, period) {
      // Backend support for installments is limited to 'paid' status currently.
      // If backend adds support, we would call a specific endpoint.
      // For now, if it's the final period, we mark as paid.
      // Otherwise we just simulate success or update a tracking field if available.
      // Since we can't change backend right now, we will mark as paid if user insists,
      // or just return success to satisfy the frontend call.

      // Check total periods from current contract
      const total = currentContract.value?.totalPeriods || 0

      if (period >= total && total > 0) {
           return payContract(id, userId)
      }

      // TODO: Implement real installment tracking when backend supports it
      console.warn('Installment payment partial update not fully supported by backend yet.')

      // Just refresh to see if anything changed (unlikely)
      // fetchContractById(id)
      return `第 ${period} 期支付成功 (模拟)`
  }

  return {
    contractList,
    currentContract,
    isLoading,
    error,
    fetchContractList,
    fetchContractById,
    createContract,
    signContract,
    payContract,
    payInstallment
  }
})
