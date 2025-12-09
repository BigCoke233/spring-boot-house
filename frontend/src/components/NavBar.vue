<script setup>

</script>

<template>
  <nav class="fixed top-0 left-0 right-0 py-2 px-6 bg-slate-1/20 backdrop-blur-md z-10 shadow-sm m-0">
    <div class="flex justify-between items-center">
      <a>菜单</a>
      <router-link to="/" class="text-xl font-bold">商品房售卖</router-link>
      <ul class="flex space-x-4 list-none">
        <li><router-link to="/account">账号</router-link></li>
        <li><router-link to="/contrasts">合同</router-link></li>
      </ul>
    </div>
  </nav>
</template>
