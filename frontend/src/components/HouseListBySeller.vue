<script setup>
import { ref, onMounted } from 'vue'
import PageContainer from '@/layouts/PageContainer.vue';
import { useSellerStore } from '@/stores/seller.js'

const sellerStore = useSellerStore()
const sellers = ref([])

onMounted(async () => {
  sellers.value = await sellerStore.fetchSellers()
})
</script>

<template>
  <div class="bg-neutral-100/50 my-20 py-10">
    <PageContainer>
      <h2 class="text-3xl mb-6 font-extrabold font-serif">精选卖家</h2>
      <div class="grid grid-cols-2 gap-4 md:gap-8">
        <div v-for="seller in sellers" :key="seller.name"
          class="bg-white shadow-md rd-lg p-6">
          <h3 class="text-xl">{{ seller.name }}</h3>
          <p>{{ seller.description }}</p>
        </div>
      </div>
    </PageContainer>
  </div>
</template>
