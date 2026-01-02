<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAdminStore } from '@/stores/admin'
import PageContainer from '@/layouts/PageContainer.vue'
import { PhoneCall, Mail, Building, Globe } from 'lucide-vue-next'

const route = useRoute()
const loading = ref(true)
const error = ref(null)
const profile = ref({})

const profileStructure = [
  { label: "公司名称", icon: Building, name: "name" },
  { label: "电话", icon: PhoneCall, name: "phone" },
  { label: "邮箱", icon: Mail, name: "email" },
  { label: "官网", icon: Globe, name: "website" },
]

// Mock Data
const MOCK_SELLERS = {
  201: {
    s_name: "万科地产",
    s_phone: "020-88888888",
    s_email: "contact@vanke.com",
    s_website: "www.vanke.com",
    s_describe: "万科企业股份有限公司成立于1984年，经过三十余年的发展，已成为国内领先的城乡建设与生活服务商。"
  },
  202: {
    s_name: "恒大地产",
    s_phone: "0755-88888888",
    s_email: "service@evergrande.com",
    s_website: "www.evergrande.com",
    s_describe: "恒大集团是“世界500强”企业，恒大地产在全国280多个城市拥有1300多个项目。"
  },
  203: {
    s_name: "绿地集团",
    s_phone: "021-88888888",
    s_email: "info@greenland.com",
    s_website: "www.greenland.com",
    s_describe: "绿地控股集团有限公司是一家全球经营的多元化企业集团，创立于1992年7月18日。"
  }
}

onMounted(async () => {
  const id = route.params.id
  if (!id) {
    error.value = "未提供ID"
    loading.value = false
    return
  }

  // Simulate API call
  try {
      // Use adminStore helper to fetch seller by ID (using public admin endpoint)
      const sellerData = await useAdminStore().fetchSellerById(id)
      if (sellerData) {
          profile.value = {
            name: sellerData.s_name,
            phone: sellerData.s_phone,
            email: sellerData.s_email,
            website: sellerData.s_website,
            description: sellerData.s_describe
          }
      } else {
          throw new Error('Seller not found')
      }
  } catch (e) {
      console.error(e)
      // Fallback to mock if API fails or for specific mock IDs
      const data = MOCK_SELLERS[id]
      if (data) {
          profile.value = {
            name: data.s_name,
            phone: data.s_phone,
            email: data.s_email,
            website: data.s_website,
            description: data.s_describe
        }
      } else {
        profile.value = {
            name: "未知开发商",
            phone: "000-00000000",
            email: "unknown@example.com",
            website: "www.example.com",
            description: "该开发商信息暂未录入。"
        }
      }
  } finally {
      loading.value = false
  }
})
</script>

<template>
  <PageContainer class="my-20">
    <div v-if="loading" class="text-center p-10">加载中...</div>
    <div v-else-if="error" class="text-center p-10 text-red-500">{{ error }}</div>
    <div v-else class="grid grid-cols-2 gap-4 md:gap-10">
      <div class="col-span-2">
        <h1 class="text-xl font-bold">资料</h1>
      </div>
      <div v-for="item in profileStructure" :key="item.label"
        class="bg-neutral-100/20 p-4 md:p-8 rd-xl  shadow relative overflow-hidden">
        <div class="flex items-center text-extrabold text-neutral-500 text-sm">
          <span>{{ item.label }}</span>
        </div>
        <div class="text-lg mt-2">
          {{ profile[item.name] || '—' }}
        </div>
        <component :is="item.icon"
          class="w-25 h-25 absolute -bottom-4 -right-2 text-neutral-200 -z-1" />
      </div>
      <div class="col-span-2 bg-neutral-100/20 p-4 md:p-8 rd-xl shadow">
        <div class="text-extrabold text-neutral-500 text-sm mb-2">公司描述</div>
        <div class="text-lg">{{ profile.description || '暂无描述' }}</div>
      </div>
    </div>
  </PageContainer>
</template>
