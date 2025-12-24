<script setup>
import { computed } from 'vue'
import { Heart, HeartOff } from 'lucide-vue-next'
import { useFavoriteStore } from '@/stores/favorite.js'
import { useUserStore } from '@/stores/user.js'

const props = defineProps({
  data: {
    type: Object,
    default: () => ({
      id: 1,
      name: "房源名称",
      price: 8000,
      square: 100,
      location: "房源地址",
      image: "",
    })
  }
})

const favoriteStore = useFavoriteStore()
const userStore = useUserStore()

const link = computed(() =>
  `/house/${props.data.h_id || props.data.id}`
)

const totalPrice = computed(() =>
  (props.data.square * props.data.price).toLocaleString()
)

// Use h_id or id depending on object structure
const houseId = computed(() => props.data.h_id || props.data.id)

const isFav = computed(() => favoriteStore.isFavorite(houseId.value))

function handleFavorite(e) {
  e.preventDefault() // Prevent navigation
  favoriteStore.toggleFavorite(houseId.value, userStore.currentUserId)
}
</script>

<template>
  <router-link :to="link" class="bg-white shadow rd overflow-hidden
    transition hover:shadow-md hover:-translate-y-1 block text-neutral-900 no-underline">
    <div class="relative bg-neutral-100 flex items-center justify-center h-40 bg-cover bg-center"
      :style="{ backgroundImage: props.data.image ? `url(${props.data.image})` : 'none' }"> 
      <p v-if="!props.data.image" class="text-neutral">暂无图片</p>
      <button @click="handleFavorite" class="absolute top-2 right-2 b-none outline-none
        rd-full w-8 h-8 inline-flex items-center justify-center
        cursor-pointer transition shadow-sm"
        :class="isFav ? 'bg-red-500 text-white hover:bg-red-600' : 'bg-white hover:bg-neutral-100 text-neutral-400'">
        <Heart v-if="isFav" fill="currentColor" size="18" />
        <Heart v-else size="18" />
      </button>
    </div>
    <div class="p-4">
      <h3 class="font-light text-sm m-0 truncate">{{ props.data.name }}</h3>
      <p class="m-0 text-lg mt-1">
        <span class="font-bold text-red-6">¥{{ totalPrice }}</span>
        <span class="text-neutral-400 mx-1">/</span>
        <span class="text-neutral-500 font-light text-sm">{{ props.data.square }} m<sup>2</sup></span>
      </p>
    </div>
  </router-link>
</template>
