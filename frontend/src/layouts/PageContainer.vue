<script setup>
import { computed } from 'vue'
const props = defineProps({
  class: { type: String, default: "" }
})

const className = computed(() => {
  return `max-w-5xl mx-auto ${props.class}`
})
</script>

<template>
  <div :class="className">
    <slot />
  </div>
</template>
