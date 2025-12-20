import { createRouter, createWebHistory } from 'vue-router'

import AccountView from '../views/AccountView.vue'
import ContractView from '../views/ContractView.vue'
import AccountProfileView from '../views/AccountProfileView.vue'
import HouseListView from '@/views/HouseListView.vue'
import HouseDetailView from '@/views/HouseDetailView.vue'
import FavoritesView from '@/views/FavoritesView.vue'
import ContractDetailView from '@/views/ContractDetailView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HouseListView,
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
      path: '/contract/:id',
      name: 'contract-detail',
      component: ContractDetailView,
    },
    {
      path: '/favorites',
      name: 'favorites',
      component: FavoritesView,
    },
    {
      path: '/house/:id',
      name: 'house-detail',
      component: HouseDetailView,
    }
  ],
})

export default router
