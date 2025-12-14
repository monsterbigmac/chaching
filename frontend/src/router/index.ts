import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      name: 'index',
      component: () => import('../views/FinancialRecordsIndex.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/edit/:id?',
      name: 'edit',
      component: () => import('../views/FinancialRecordEdit.vue'),
      meta: { requiresAuth: true }
    }
  ],
})


let authChecked = false
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()
  if (!authChecked && to.name !== 'login') {
    authChecked = true
    await authStore.checkAuth()
  }

  const requiresAuth = to.meta.requiresAuth !== false
  if (requiresAuth && !authStore.isAuthenticated) {
    next({ name: 'login' })
  } else if (to.name === 'login' && authStore.isAuthenticated) {
    next({ name: 'index' })
  } else {
    next()
  }
})

export default router
