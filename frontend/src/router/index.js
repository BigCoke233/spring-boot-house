import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '../views/HomeView.vue'
import AccountView from '../views/AccountView.vue'
import ContrastView from '../views/ContrastView.vue'
import AccountProfileView from '../views/AccountProfileView.vue'
import HouseListView from '@/views/HouseListView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/account',
      name: 'account',
      component: AccountView,
    },
    {
      path: '/account/profile',
      name: 'account-profile',
      component: AccountProfileView,
    },
    {
      path: '/contrast',
      name: 'contrast',
      component: ContrastView,
    },
    {
      path: '/houses',
      name: 'houses',
      component: HouseListView,
    },
  ],
})

export default router
