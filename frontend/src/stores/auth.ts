import { ref } from 'vue'
import { defineStore } from 'pinia'
import axios from '@/config/axios.config'
import { AxiosError } from 'axios'
import { useCsrfStore } from './csrf'

export const useAuthStore = defineStore('auth', () => {
  const isAuthenticated = ref<boolean>(false)
  const username = ref<string>('')
  const isLoading = ref<boolean>(false)
  const error = ref<string>('')

  async function login(loginUsername: string, loginPassword: string): Promise<boolean> {
    error.value = ''
    isLoading.value = true

    try {
      const csrfStore = useCsrfStore()
      if (!csrfStore.isInitialized) {
        await csrfStore.initCsrfToken()
      }

      await axios.post(
        '/api/auth/login',
        {},
        {
          auth: {
            username: loginUsername,
            password: loginPassword
          }
        }
      )

      isAuthenticated.value = true
      username.value = loginUsername
      return true
    } catch (e) {
      if (e instanceof AxiosError) {
        error.value = e.response?.data?.message || 'Login failed. Please check your credentials.'
        isAuthenticated.value = false
        username.value = ''
      }
      return false
    } finally {
      isLoading.value = false
    }
  }

  function logout() {
    isAuthenticated.value = false
    username.value = ''
    error.value = ''
  }

  function clearError() {
    error.value = ''
  }

  async function checkAuth(): Promise<boolean> {
    try {
      await axios.get('/api/v1/financial-records/search', {
        params: { page: 0, size: 1 }
      })
      isAuthenticated.value = true
      return true
    } catch (e) {
      if (e instanceof AxiosError && e.response?.status === 401) {
        isAuthenticated.value = false
        username.value = ''
      }
      return false
    }
  }

  return {
    isAuthenticated,
    username,
    isLoading,
    error,
    login,
    logout,
    clearError,
    checkAuth
  }
})
