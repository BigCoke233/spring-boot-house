<script setup>
import { ref } from 'vue'
import { RouterView } from 'vue-router'

import NavBar from './components/NavBar.vue'
import SiteFooter from './components/SiteFooter.vue'
import SidebarLayout from './layouts/SidebarLayout.vue'

const collapsed = ref(false)
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
