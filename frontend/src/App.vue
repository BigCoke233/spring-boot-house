<!-- src/App.vue -->
<script setup>
import { RouterLink, RouterView, useRoute } from 'vue-router'
import { ref, computed } from 'vue'

// 获取当前路由
const route = useRoute()
</script>

<template>
  <!-- 这里添加导航栏（如果需要的话） -->
  <nav v-if="route.path !== '/admin'">
    <!-- 普通页面导航 -->
    <RouterLink to="/">Home</RouterLink>
    <RouterLink to="/about">About</RouterLink>
  </nav>
  
  <!-- 这是必须的：路由视图容器 -->
  <RouterView />
</template>

<style scoped>
/* 样式 */
</style>