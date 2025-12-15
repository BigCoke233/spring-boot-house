<script setup>
import { ref, computed, watch } from 'vue'
import SideBarList from './SideBarList.vue'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  width: { type: Number, default: 240 },
  collapsedWidth: { type: Number, default: 0 },
})

const emit = defineEmits(['update:modelValue', 'toggle'])

const collapsed = ref(props.modelValue)
watch(() => props.modelValue, v => (collapsed.value = v))
watch(collapsed, v => emit('update:modelValue', v))

const currentWidth = computed(() => (collapsed.value ? props.collapsedWidth : props.width))
const visibility = computed(() => (collapsed.value ? 'none' : 'block'))

function toggle() {
  collapsed.value = !collapsed.value
  emit('toggle', collapsed.value)
}

defineExpose({ collapsed, currentWidth, toggle })
</script>

<template>
  <aside class="bg-white shadow"
  :style="{ width: currentWidth + 'px', display: visibility }" :aria-expanded="!collapsed">
    <SideBarList />
  </aside>
</template>
