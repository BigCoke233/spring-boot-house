<script setup>
import { computed } from 'vue'
import { HeartPlus } from 'lucide-vue-next'

const props = defineProps({
  name: { type: String, default: "房源名称" },
  address: { type: String, default: "房源地址" },
  number: { type: String, default: "门牌号" },
  square: { type: Number, default: 100 },
  price: { type: Number, default: 8000 },
  image: { type: String, default: "" },
  id: { type: Number, default: 1 }
})

const link = computed(() =>
  `/house/${props.id}`
)

const totalPrice = computed(() =>
  props.square * props.price
)
</script>

<template>
  <router-link :to="link" class="bg-white shadow rd overflow-hidden
    transition hover:shadow-md hover:-translate-y-1">
    <div class="relative bg-neutral-100 flex items-center justify-center h-40"
      :style="{ backgroundImage: props.image }">
      <p class="text-neutral">暂无图片</p>
      <button class="absolute top-2 right-2 b-none outline-none
        bg-white rd-full w-8 h-8 inline-flex items-center justify-center
        cursor-pointer transition hover:bg-red hover:text-white">
        <HeartPlus />
      </button>
    </div>
    <div class="p-4">
      <h3 class="font-light text-sm m-0">{{ props.name }}</h3>
      <p class="m-0 text-lg">
        <span class="font-bold text-red-5">¥{{ totalPrice }}</span>
        <span class="text-neutral"> / </span>
        <span class="text-neutral font-light">{{ props.square }} m<sup>2</sup></span>
      </p>
    </div>
  </router-link>
</template>
