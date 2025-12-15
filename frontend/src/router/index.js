import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '../views/HomeView.vue'
import AccountView from '../views/AccountView.vue'
import ContractView from '../views/ContractView.vue'
import AccountProfileView from '../views/AccountProfileView.vue'
import HouseListView from '@/views/HouseListView.vue'
import HouseDetailView from '@/views/HouseDetailView.vue'

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
      path: '/contract',
      name: 'contract',
      component: ContractView,
    },
    {
      path: '/houses',
      name: 'houses',
      component: HouseListView,
    },
    {
      path: '/house/:id',
      name: 'house-detail',
      component: HouseDetailView,
    }
  ],
})

export default router
