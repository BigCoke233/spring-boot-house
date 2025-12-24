<script setup>
import { Menu } from 'lucide-vue-next';
import { useUserStore } from '@/stores/user';
import { storeToRefs } from 'pinia';
import { useRoute } from 'vue-router';
import { computed } from 'vue';

const emit = defineEmits(['toggle-sidebar']);
const userStore = useUserStore();
const { isLoggedIn } = storeToRefs(userStore);
const route = useRoute();

const showMenuButton = computed(() => {
  return isLoggedIn.value && !['/login', '/register'].includes(route.path);
});
</script>

<template>
  <nav class="fixed top-0 left-0 right-0 py-4 px-6 bg-slate-1/20 backdrop-blur-md z-500 shadow-sm m-0">
    <div class="flex justify-between items-center">
      <a v-if="showMenuButton" class="cursor-pointer" @click="emit('toggle-sidebar')"><Menu /></a>
      <div v-else class="w-6 h-6"></div> <!-- Spacer to keep alignment if needed, or just remove -->
      <router-link to="/" class="text-xl font-bold">商品房售卖</router-link>
      <ul class="flex space-x-4 list-none items-center">
        <template v-if="isLoggedIn">
          <li><router-link to="/account">账号</router-link></li>
        </template>
        <template v-else>
          <li><router-link to="/login" class="px-4 py-2 text-slate-600 hover:text-slate-900">登录</router-link></li>
          <li><router-link to="/register" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">注册</router-link></li>
        </template>
      </ul>
    </div>
  </nav>
</template>
