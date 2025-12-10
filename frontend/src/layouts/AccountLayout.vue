<script setup>
import AccountLeftBar from '@/components/AccountLeftBar.vue';
</script>

<template>
  <div class="flex gap-10 md:gap-20 my-20 md:my-30 mx-10">
    <AccountLeftBar />
    <div class="content">
      <slot />
    </div>
  </div>
</template>
