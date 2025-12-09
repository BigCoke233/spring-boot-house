<script setup>
import { RouterView } from 'vue-router'

import NavBar from './components/NavBar.vue'
</script>

<template>
  <NavBar />
  <main class="my-8">
    <RouterView />
  </main>
</template>
