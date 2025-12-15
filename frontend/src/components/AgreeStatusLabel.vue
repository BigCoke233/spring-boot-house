<script setup>
import { CircleCheckBig, Ban, Loader } from 'lucide-vue-next'
const props = defineProps({
  title: { type: String, required: true },
  status: { type: String, default: '未同意' }, // 已同意 | 未同意 | 已拒绝
})
</script>

<template>
  <div>
    <div class="text-sm font-bold mb-2">{{ props.title }}</div>
    <div
      class="text-md text-white inline-flex items-center gap-2 px-2 py-1 rounded-md"
      :class="{
        'bg-green-5': props.status === '已同意',
        'bg-red-5': props.status === '已拒绝',
        'bg-gray-5': props.status === '未同意',
      }"
    >
      <span v-if="props.status === '已同意'">
        <CircleCheckBig class="w-5 h-5 inline-block" />
      </span>
      <span v-else-if="props.status === '已拒绝'">
        <Ban class="w-5 h-5 inline-block" />
      </span>
      <span v-else>
        <Loader class="w-5 h-5 inline-block" />
      </span>
      {{ props.status }}
    </div>
  </div>
</template>
