import { createRouter, createWebHistory } from 'vue-router'

import AccountView from '../views/AccountView.vue'
import ContractView from '../views/ContractView.vue'
import ProfileDispatcher from '@/views/ProfileDispatcher.vue'
import ProfileEditDispatcher from '@/views/ProfileEditDispatcher.vue'
import HouseListView from '@/views/HouseListView.vue'
import AllHousesView from '@/views/AllHousesView.vue'
import HouseDetailView from '@/views/HouseDetailView.vue'
import FavoritesView from '@/views/FavoritesView.vue'
import ContractDetailView from '@/views/ContractDetailView.vue'
import SellerHouseListView from '@/views/SellerHouseListView.vue'
import SellerHouseEditView from '@/views/SellerHouseEditView.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import PublicSellerProfileView from '@/views/public/PublicSellerProfileView.vue'
import PublicBuyerProfileView from '@/views/public/PublicBuyerProfileView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  },
  routes: [
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
    },
    {
      path: '/houses',
      name: 'houses',
      component: AllHousesView,
    },
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
      component: ProfileDispatcher,
    },
    {
      path: '/account/profile/edit',
      name: 'account-profile-edit',
      component: ProfileEditDispatcher,
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
    },
    {
      path: '/seller/houses',
      name: 'seller-houses',
      component: SellerHouseListView,
    },
    {
      path: '/seller/house/create',
      name: 'seller-house-create',
      component: SellerHouseEditView,
    },
    {
      path: '/seller/house/:id/edit',
      name: 'seller-house-edit',
      component: SellerHouseEditView,
    },
    {
      path: '/seller/:id',
      name: 'public-seller-profile',
      component: PublicSellerProfileView,
    },
    {
      path: '/buyer/:id',
      name: 'public-buyer-profile',
      component: PublicBuyerProfileView,
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('../views/AdminDashboard.vue'),
      meta: { requiresAuth: true }
    }
  ],
})

import { useUserStore } from '@/stores/user'

router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()

  // Skip check for public routes if not logged in locally
  const publicRoutes = ['login', 'register', 'home', 'houses', 'house-detail']
  if (publicRoutes.includes(to.name) && !userStore.isLoggedIn) {
    next()
    return
  }

  // If local state says logged in, or route requires auth, verify with backend
  if (userStore.isLoggedIn || !publicRoutes.includes(to.name)) {
    try {
      await userStore.fetchUserInfo()
      // If verification succeeds and user was going to login/register, redirect based on role
      if (['login', 'register'].includes(to.name)) {
        if (userStore.role === 'admin') {
          next({ name: 'admin' })
        } else {
          next({ name: 'home' })
        }
        return
      }
      next()
    } catch (error) {
      console.error('Session verification failed:', error)
      // If verification fails (session expired/invalid)
      if (!publicRoutes.includes(to.name)) {
        next({ name: 'login', query: { redirect: to.fullPath } })
      } else {
        // If public route but verification failed, just proceed as guest
        next()
      }
    }
  } else {
    next()
  }
})

export default router
