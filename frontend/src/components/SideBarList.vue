<script setup>
import { House, ReceiptText, FolderHeart, Warehouse, PlusCircle, List } from 'lucide-vue-next'
import { RouterLink } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { computed } from 'vue'

const userStore = useUserStore()

const buyerNav = [
  { name: "首页", icon: House, link: '/' },
  { name: "房源", icon: Warehouse, link: '/houses' },
  { name: "收藏", icon: FolderHeart, link: '/favorites' },
  { name: "合同", icon: ReceiptText, link: '/contract' }
]

const sellerNav = [
  { name: "新建房源", icon: PlusCircle, link: '/seller/house/create' },
  { name: "我的房源", icon: List, link: '/seller/houses' },
  { name: "合同", icon: ReceiptText, link: '/contract' }
]

const nav = computed(() => {
  if (userStore.role === 'seller') {
    return sellerNav
  }
  return buyerNav
})
</script>

<template>
  <nav>
    <ul class="list-none m-0 p-4 mt-18">
      <li v-for="item in nav" :key="item.name">
        <RouterLink :to="item.link"
        class="flex items-center px-4 py-2 hover:bg-gray-100 rd gap-4">
          <component :is="item.icon" class="w-5 h-5" />
          {{ item.name }}
        </RouterLink>
      </li>
    </ul>
  </nav>
</template>
