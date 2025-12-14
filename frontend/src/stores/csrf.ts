import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from '@/config/axios.config'

export const useCsrfStore = defineStore('csrf', () => {
  const token = ref<string>('')
  const isLoading = ref<boolean>(false)
  const isInitialized = ref<boolean>(false)

  const csrfToken = computed(() => token.value)

  async function initCsrfToken() {
    if (isInitialized.value) {
      return
    }

    isLoading.value = true
    try {
      const { data } = await axios.get('/api/csrf')
      token.value = data.token
      isInitialized.value = true
    } catch (e) {
      console.error('Failed to fetch CSRF token:', e)
    } finally {
      isLoading.value = false
    }
  }

  function setToken(newToken: string) {
    token.value = newToken
    isInitialized.value = true
  }

  return { csrfToken, isLoading, isInitialized, initCsrfToken, setToken }
})
