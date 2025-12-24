<!-- src/App.vue -->
<script setup>
import { ref, watch } from 'vue'
import { RouterView, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'

import NavBar from './components/NavBar.vue'
import SiteFooter from './components/SiteFooter.vue'
import SidebarLayout from './layouts/SidebarLayout.vue'

const route = useRoute()
const userStore = useUserStore()
const collapsed = ref(true)

// Hide sidebar on login/register pages or when not logged in
watch(
  [() => route.path, () => userStore.isLoggedIn],
  ([path, isLoggedIn]) => {
    if (!isLoggedIn || ['/login', '/register'].includes(path)) {
      collapsed.value = true
    } else {
      collapsed.value = false
    }
  },
  { immediate: true }
)
</script>

<template>
  <NavBar @toggle-sidebar="collapsed = !collapsed" />
  <SidebarLayout v-model="collapsed" :width="240" :fixed="true">
    <template #sidebar></template>
    <main>
      <RouterView />
    </main>
    <SiteFooter />
  </SidebarLayout>
</template>
