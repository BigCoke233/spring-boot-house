<script setup>
import { useUserStore } from '@/stores/user'
import BuyerProfileView from './BuyerProfileView.vue'
import SellerProfileView from './SellerProfileView.vue'

const userStore = useUserStore()
</script>

<template>
  <BuyerProfileView v-if="userStore.role === 'buyer'" />
  <SellerProfileView v-else />
</template>
