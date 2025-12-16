<script setup>
import { CircleCheckBig, Ban, Loader } from 'lucide-vue-next'
const props = defineProps({
  title: { type: String, required: true },
  status: { type: String, default: '未同意' }, // 已同意 | 未同意 | 已拒绝
})
const labelIconClass = "w-4 h-4 inline-block"
</script>

<template>
  <div>
    <div class="text-sm font-bold mb-2">{{ props.title }}</div>
    <div
      class="text-md text-white inline-flex items-center gap-2 px-3 py-1 rd-full"
      :class="{
        'bg-green-5': props.status === '已同意',
        'bg-red-5': props.status === '已拒绝',
        'bg-gray-5': props.status === '未同意',
      }"
    >
      <CircleCheckBig v-if="props.status === '已同意'" :class="labelIconClass" />
      <Ban v-else-if="props.status === '已拒绝'" :class="labelIconClass" />
      <Loader v-else :class="labelIconClass" />
      <span>{{ props.status }}</span>
    </div>
  </div>
</template>
