<script setup>
import { House, ReceiptText, FolderHeart, Warehouse } from 'lucide-vue-next'
import { RouterLink } from 'vue-router'

const nav = [
  { name: "首页", icon: House, link: '/' },
  { name: "房源", icon: Warehouse, link: '/houses' },
  { name: "收藏", icon: FolderHeart, link: '/favorites' },
  { name: "合同", icon: ReceiptText, link: '/contract' }
]
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
