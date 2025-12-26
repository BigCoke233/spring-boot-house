<script setup>
import { onMounted, computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useHouseStore } from '@/stores/house.js'
import { useFavoriteStore } from '@/stores/favorite.js'
import { useUserStore } from '@/stores/user.js'
import { useContractStore } from '@/stores/contract.js'
import LeafletMap from '@/components/LeafletMap.vue';
import PageContainer from '@/layouts/PageContainer.vue';
import AppButton from '@/components/AppButton.vue';

const route = useRoute()
const router = useRouter()
const houseStore = useHouseStore()
const favoriteStore = useFavoriteStore()
const userStore = useUserStore()
const contractStore = useContractStore()

const detail = computed(() => houseStore.currentHouse || {})
const totalPrice = computed(() => {
  const price = detail.value.price || detail.value.h_price || 0
  const square = detail.value.square || detail.value.h_square || 0
  if (!price || !square) return 0
  return (price * square).toLocaleString()
})

const showPaymentDialog = ref(false)
const selectedPayWay = ref('full')
const selectedPeriods = ref(12)

async function handleBuy() {
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }
  if (userStore.role !== 'buyer') {
    alert('只有买家可以购买房源')
    return
  }
  showPaymentDialog.value = true
}

async function confirmBuy() {
  try {
    const price = detail.value.price || detail.value.h_price || 0
    const square = detail.value.square || detail.value.h_square || 0

    const contractData = {
      buyerId: userStore.currentUserId,
      houseId: detail.value.id || detail.value.h_id,
      totalPrice: Number(price) * Number(square),
      payWay: selectedPayWay.value,
      totalPeriods: selectedPayWay.value === 'installment' ? selectedPeriods.value : 0,
      paytimeEnding: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString(), // Default 7 days later
      deliveryEnding: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString() // Default 30 days later
    }

    await contractStore.createContract(contractData)
    alert('购买申请已提交，请等待卖家确认。')
    router.push('/contract')
  } catch (error) {
    console.error('Failed to create contract:', error)
    alert('提交购买申请失败：' + (error.message || '未知错误'))
  } finally {
    showPaymentDialog.value = false
  }
}

async function handleFavorite() {
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }
  if (userStore.role !== 'buyer') {
    alert('只有买家可以收藏房源')
    return
  }
  try {
    await favoriteStore.toggleFavorite(detail.value.id, userStore.currentUserId)
  } catch (error) {
    console.error(error)
    if (!userStore.isLoggedIn) {
        router.push('/login')
    }
  }
}

onMounted(() => {
  const id = route.params.id
  if (id) {
    houseStore.fetchHouseById(id)
  }
})
</script>

<template>
  <PageContainer class="my-30">
    <div v-if="houseStore.isLoading" class="text-center py-20">
      Loading...
    </div>
    <div v-else-if="houseStore.error" class="text-center py-20 text-red-500">
      {{ houseStore.error }}
    </div>
    <header v-else-if="houseStore.currentHouse" class="grid grid-cols-2 gap-4 md:gap-10">
      <!-- Leaflet 地图 -->
      <LeafletMap :center="[detail.latitude, detail.longitude]" />
      <div class>
        <!-- 房源基本信息 -->
        <header class="bg-neutral-200/20 p-4 md:p-6 rd-lg">
          <div class="mb-3">
            <h1 class="text-xl font-bold">{{ detail.name }}</h1>
            <p class="text-neutral text-sm">{{ detail.address }}</p>
          </div>
          <div class="flex gap-1 items-end">
            <p class="text-3xl font-serif font-extrabold text-red-6">¥{{ totalPrice }}</p>
            <p class="text-neutral text-sm mb-1"> (单价: ¥{{ detail.price }}/m²)</p>
          </div>
          <div class="mt-2 text-lg">
             <p>面积: {{ detail.square }} m<sup>2</sup></p>
          </div>
          <div class="text-neutral-600 mt-3">
            <p>{{ detail.description }}</p>
          </div>
        </header>
        <!--  开发商信息 -->
        <section class="flex items-center gap-6 bg-neutral-200/20 p-4 md:p-6 rd-lg my-4">
          <div class="h-15 w-15 bg-neutral-300 rd-full flex items-center justify-center text-xl font-bold text-white">
            {{ detail.sellerName ? detail.sellerName[0] : 'S' }}
          </div>
          <div>
            <h3 class="font-bold">{{ detail.sellerName || '未知卖家' }}</h3>
            <p class="text-sm text-neutral-500">{{ detail.sellerPhone || detail.sellerEmail }}</p>
          </div>
        </section>
        <!-- 按钮组 -->
        <section class="my-4 flex gap-4">
          <button @click="handleBuy" class="p-6 py-2 text-xl bg-black shadow rd text-white hover:opacity-90 transition">咨询购买</button>
          <button @click="handleFavorite" class="p-6 py-2 text-xl bg-neutral-300/50 rd hover:bg-neutral-400/50 transition">
            {{ favoriteStore.isFavorite(detail.id) ? '取消收藏' : '加入收藏' }}
          </button>
        </section>
      </div>
    </header>
    <div v-else class="text-center py-20">
      未找到房源信息
    </div>

    <!-- Payment Method Dialog -->
    <div v-if="showPaymentDialog" class="fixed inset-0 bg-black/50 flex items-center justify-center z-1000">
      <div class="bg-white p-8 rd-lg shadow-xl w-full max-w-md">
        <h3 class="text-xl font-bold mb-4">确认购买</h3>
        <p class="mb-4 text-neutral-600">房源：{{ detail.name || detail.h_name }}</p>
        <p class="mb-6 text-xl font-bold text-red-6">总价：¥{{ totalPrice }}</p>

        <div class="mb-6">
          <label class="block font-bold mb-2">选择付款方式</label>
          <div class="space-y-2">
            <label class="flex items-center gap-2 p-3 border rounded cursor-pointer hover:bg-neutral-50"
                   :class="{'border-black bg-neutral-50': selectedPayWay === 'full'}">
              <input type="radio" v-model="selectedPayWay" value="full">
              <span>全款支付</span>
            </label>
            <label class="flex items-center gap-2 p-3 border rounded cursor-pointer hover:bg-neutral-50"
                   :class="{'border-black bg-neutral-50': selectedPayWay === 'installment'}">
              <input type="radio" v-model="selectedPayWay" value="installment">
              <span>分期付款 (首付 + 分期)</span>
            </label>
          </div>
        </div>

        <div v-if="selectedPayWay === 'installment'" class="mb-6">
          <label class="block font-bold mb-2">分期期数 (月)</label>
          <select v-model="selectedPeriods" class="w-full p-2 border rounded">
            <option :value="12">12期 (1年)</option>
            <option :value="24">24期 (2年)</option>
            <option :value="36">36期 (3年)</option>
          </select>
        </div>

        <div class="flex gap-4 justify-end">
          <AppButton variant="secondary" @click="showPaymentDialog = false">取消</AppButton>
          <AppButton @click="confirmBuy">确认提交</AppButton>
        </div>
      </div>
    </div>
  </PageContainer>
</template>
