<script setup>
import { ref, watch } from 'vue'
import { RouterView, useRoute } from 'vue-router'

import NavBar from './components/NavBar.vue'
import SiteFooter from './components/SiteFooter.vue'
import SidebarLayout from './layouts/SidebarLayout.vue'

const route = useRoute()
const collapsed = ref(false)

// Hide sidebar on login/register pages
watch(() => route.path, (path) => {
  if (path === '/login' || path === '/register') {
    collapsed.value = true
  } else {
    collapsed.value = false
  }
})
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
