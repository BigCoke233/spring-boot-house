<script setup>
const nav = [
  { name: "基本设置", link: "/account" },
  { name: "个人资料", link: "/account/profile" },
]

const handleLogout = () => {
  // Implement logout logic here
}
</script>

<template>
  <div class="flex flex-col gap-4">
    <div>
      <img class="w-30 rd-full block" src="/img/default-profile.png" />
    </div>
    <ul class="my-10 p-0 list-none flex flex-col gap-4 text-center text-lg">
      <li v-for="item in nav" :key="item.name">
        <router-link :to="item.link">{{ item.name }}</router-link>
      </li>
      <li>
        <a class="cursor-pointer" @click="handleLogout">退出登录</a>
      </li>
    </ul>
  </div>
</template>
