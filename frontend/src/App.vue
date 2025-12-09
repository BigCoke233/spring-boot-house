<script setup>
import { RouterView } from 'vue-router'

import NavBar from './components/NavBar.vue'
import SiteFooter from './components/SiteFooter.vue'
</script>

<template>
  <NavBar />
  <main class="my-8">
    <RouterView />
  </main>
  <SiteFooter />
</template>
