<script setup>
import { useUserStore } from '@/stores/user'
import BuyerProfileEditView from './BuyerProfileEditView.vue'
import SellerProfileEditView from './SellerProfileEditView.vue'

const userStore = useUserStore()
</script>

<template>
  <BuyerProfileEditView v-if="userStore.role === 'buyer'" />
  <SellerProfileEditView v-else />
</template>
