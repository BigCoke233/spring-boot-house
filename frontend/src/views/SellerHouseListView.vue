<script setup>
import { onMounted, computed } from 'vue'
import PageContainer from '@/layouts/PageContainer.vue'
import { useHouseStore } from '@/stores/house.js'
import { useUserStore } from '@/stores/user.js'
import AppButton from '@/components/AppButton.vue'
import { useRouter } from 'vue-router'

const houseStore = useHouseStore()
const userStore = useUserStore()
const router = useRouter()

// Assume sellerId is part of user info. 
// For mock purposes, if no ID, default to 1 (or handle appropriately).
const sellerId = computed(() => userStore.userInfo?.id || 1)

const myHouses = computed(() => {
  return houseStore.houseList.filter(h => h.sellerId === sellerId.value)
})

function getStatusLabel(status) {
  switch(status) {
    case 'approved': return { text: '已审核', class: 'bg-green-100 text-green-700' }
    case 'pending': return { text: '审核中', class: 'bg-yellow-100 text-yellow-700' }
    case 'rejected': return { text: '未通过', class: 'bg-red-100 text-red-700' }
    default: return { text: '未知', class: 'bg-gray-100 text-gray-700' }
  }
}

async function handleDelete(id) {
    if (!confirm('确定要删除这个房源吗？此操作无法撤销。')) return
    
    try {
        await houseStore.deleteHouse(id)
        alert('删除成功')
    } catch (e) {
        alert('删除失败: ' + e.message)
    }
}

onMounted(() => {
    houseStore.fetchHouseList()
})
</script>

<template>
  <PageContainer class="my-20">
    <header class="flex justify-between items-center mb-8">
      <h1 class="text-2xl font-bold">我的房源管理</h1>
      <AppButton variant="primary" @click="router.push('/seller/house/create')">发布新房源</AppButton>
    </header>

    <div v-if="houseStore.isLoading" class="text-center py-10 text-neutral-500">
      加载中...
    </div>
    
    <div v-else-if="myHouses.length === 0" class="text-center py-10 bg-neutral-100 rd-lg text-neutral-500">
      暂无发布的房源
    </div>

    <div v-else class="space-y-4">
      <div v-for="house in myHouses" :key="house.id" class="bg-white p-4 rd-lg shadow-sm border border-neutral-200 flex flex-col md:flex-row gap-6">
        <!-- Image Placeholder -->
        <div class="w-full md:w-48 h-32 bg-neutral-200 rd overflow-hidden flex-shrink-0">
             <img v-if="house.image" :src="house.image" class="w-full h-full object-cover" />
             <div v-else class="w-full h-full flex items-center justify-center text-neutral-400">暂无图片</div>
        </div>

        <div class="flex-grow flex flex-col justify-between">
            <div>
                <div class="flex justify-between items-start">
                    <h3 class="text-lg font-bold">{{ house.name }}</h3>
                    <span :class="['px-2 py-1 text-xs rd', getStatusLabel(house.auditStatus).class]">
                        {{ getStatusLabel(house.auditStatus).text }}
                    </span>
                </div>
                <p class="text-neutral-500 text-sm mt-1">{{ house.address }}</p>
                <div class="mt-2 text-sm">
                    <span class="mr-4">¥{{ house.price }}/m²</span>
                    <span>{{ house.square }}m²</span>
                </div>
            </div>
            
            <div class="flex justify-end gap-3 mt-4 md:mt-0">
                <AppButton size="sm" variant="secondary" :to="`/house/${house.id}`">查看</AppButton>
                <AppButton size="sm" @click="router.push(`/seller/house/${house.id}/edit`)">编辑</AppButton>
                <AppButton size="sm" variant="danger" @click="handleDelete(house.id)">删除</AppButton>
            </div>
        </div>
      </div>
    </div>
  </PageContainer>
</template>
