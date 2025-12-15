<script setup>
import { ref, computed, watch } from 'vue'
import SideBar from '@/components/SideBar.vue'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  width: { type: Number, default: 240 },
  collapsedWidth: { type: Number, default: 0 },
  fixed: { type: Boolean, default: true },
})
const emit = defineEmits(['update:modelValue'])

const collapsed = ref(props.modelValue)
watch(() => props.modelValue, v => (collapsed.value = v))
watch(collapsed, v => emit('update:modelValue', v))

const sidebarWidth = computed(() => (collapsed.value ? props.collapsedWidth : props.width))
</script>

<template>
  <div :style="{ '--sidebar-offset': sidebarWidth + 'px' }" class="ml-[var(--sidebar-offset)]">
    <SideBar
      :model-value="collapsed"
      :width="width"
      :collapsed-width="collapsedWidth"
      @update:modelValue="v => (collapsed = v)"
      :class="fixed ? 'fixed left-0 top-0 h-screen' : ''"
    >
      <slot name="sidebar" />
    </SideBar>
    <main>
      <slot />
    </main>
  </div>
</template>
