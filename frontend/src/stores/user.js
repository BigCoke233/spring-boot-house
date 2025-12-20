import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', () => {
  const role = ref('buyer') // 'buyer' or 'seller'

  function setRole(newRole) {
    role.value = newRole
  }

  return { role, setRole }
})
