<script setup>
import { computed, ref, watch } from 'vue'
import { Heart } from 'lucide-vue-next'
import { useFavoriteStore } from '@/stores/favorite.js'
import { useUserStore } from '@/stores/user.js'
import { getImageUrl } from '@/utils/imageUrl.js'

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

const displayName = computed(() => props.data.name || props.data.h_name || '')
const displaySquare = computed(() => props.data.square ?? props.data.h_square ?? 0)
const displayPrice = computed(() => props.data.price ?? props.data.h_price ?? 0)
const totalPrice = computed(() => {
  const square = Number(displaySquare.value) || 0
  const price = Number(displayPrice.value) || 0
  return (square * price).toLocaleString()
})

// Use h_id or id depending on object structure
const houseId = computed(() => props.data.h_id || props.data.id)

// Track which image from the array we are currently showing
const currentImageIndex = ref(0)
// State to track if all images failed to load
const imageError = ref(false)

// Reset state when data changes
watch(() => props.data.id, () => {
  currentImageIndex.value = 0
  imageError.value = false
})

const mainImage = computed(() => {
  if (props.data.picturePaths && Array.isArray(props.data.picturePaths) && props.data.picturePaths.length > 0) {
    // Try to find a valid image, currently we just return the one at current index
    if (currentImageIndex.value < props.data.picturePaths.length) {
      const url = props.data.picturePaths[currentImageIndex.value]
      return getImageUrl(url)
    }
  }
  const img = props.data.image || ''
  if (!img) return ''
  return getImageUrl(img)
})

const isFav = computed(() => favoriteStore.isFavorite(houseId.value))

function handleImageError() {
  const paths = props.data.picturePaths
  if (paths && Array.isArray(paths) && paths.length > 0) {
    if (currentImageIndex.value < paths.length - 1) {
      // Try next image
      currentImageIndex.value++
    } else {
      // No more images to try
      imageError.value = true
    }
  } else {
    // Legacy image failed
    imageError.value = true
  }
}

function handleFavorite(e) {
  e.preventDefault() // Prevent navigation
  favoriteStore.toggleFavorite(houseId.value, userStore.currentUserId)
}
</script>

<template>
  <router-link :to="link" class="bg-white shadow rd overflow-hidden
    transition hover:shadow-md hover:-translate-y-1 block text-neutral-900 no-underline">
    <div class="relative bg-neutral-100 flex items-center justify-center h-40 overflow-hidden">
       <!-- Use img tag for better error handling -->
       <img
        v-if="mainImage && !imageError"
        :src="mainImage"
        alt="House Image"
        class="w-full h-full object-cover"
        @error="handleImageError"
      />
      <div v-else class="flex flex-col items-center justify-center text-neutral-400">
         <p class="text-sm">暂无图片</p>
      </div>

      <button @click="handleFavorite" class="absolute top-2 right-2 b-none outline-none
        rd-full w-8 h-8 inline-flex items-center justify-center
        cursor-pointer transition shadow-sm"
        :class="isFav ? 'bg-red-500 text-white hover:bg-red-600' : 'bg-white hover:bg-neutral-100 text-neutral-400'">
        <Heart v-if="isFav" fill="currentColor" size="18" />
        <Heart v-else size="18" />
      </button>
    </div>
    <div class="p-4">
      <h3 class="font-light text-sm m-0 truncate">{{ displayName }}</h3>
      <p class="m-0 text-lg mt-1">
        <span class="font-bold text-red-6">¥{{ totalPrice }}</span>
        <span class="text-neutral-400 mx-1">/</span>
        <span class="text-neutral-500 font-light text-sm">{{ displaySquare }} m<sup>2</sup></span>
      </p>
    </div>
  </router-link>
</template>
