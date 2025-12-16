<script setup>
import { computed } from 'vue'

const props = defineProps({
  variant: { type: String, default: 'primary' }, // primary, secondary, outline, danger
  size: { type: String, default: 'md' }, // sm, md, lg
  block: { type: Boolean, default: false },
  disabled: { type: Boolean, default: false },
  type: { type: String, default: 'button' },
})

const base = 'rd cursor-pointer b-none outline-none transition select-none'

const variantClass = computed(() => {
  switch (props.variant) {
    case 'secondary':
      return 'bg-neutral-300/50 hover:bg-neutral-400/50 text-neutral-900'
    case 'danger':
      return 'bg-red-5 text-white hover:bg-red-6'
    default:
      return 'b-1 b-neutral-300 b-solid'
  }
})

const sizeClass = computed(() => {
  switch (props.size) {
    case 'sm':
      return 'px-3 py-1 text-sm'
    case 'lg':
      return 'px-6 py-2 text-lg'
    case 'full':
      return 'px-4 py-2 text-base w-full'
    default:
      return 'px-4 py-2 text-base'
  }
})

const disabledClass = computed(() => (props.disabled ? 'opacity-50 cursor-not-allowed' : ''))

const classes = computed(() => [base, variantClass.value, sizeClass.value, disabledClass.value, props.block ? 'w-full' : ''].join(' '))
</script>

<template>
  <button :type="props.type" :disabled="props.disabled" :class="classes">
    <slot />
  </button>
</template>
