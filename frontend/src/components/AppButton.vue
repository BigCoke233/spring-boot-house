<script setup>
import { computed } from 'vue'

const props = defineProps({
  variant: { type: String, default: 'primary' }, // primary, secondary, outline, danger
  size: { type: String, default: 'md' }, // sm, md, lg
  block: { type: Boolean, default: false },
  disabled: { type: Boolean, default: false },
  type: { type: String, default: 'button' },
  to: { type: [String, Object], default: null },
})

const base = 'rd cursor-pointer b-none outline-none transition select-none text-center'

const variantClass = computed(() => {
  switch (props.variant) {
    case 'secondary':
      return 'bg-neutral-300/50 hover:bg-neutral-400/50 text-neutral-900'
    case 'danger':
      return 'b-1 b-red-5 b-solid text-red-6 hover:bg-red-6 hover:text-white'
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

const emit = defineEmits(['click'])

function handleClick(e) {
  e.stopPropagation()
  if (props.disabled) {
    e.preventDefault()
    return
  }
  emit('click', e)
}
</script>

<template>
  <component
    :is="props.to ? 'router-link' : 'button'"
    :to="props.to || undefined"
    :type="props.to ? undefined : props.type"
    :disabled="props.disabled"
    :class="classes"
    @click="handleClick"
  >
    <slot />
  </component>
</template>
