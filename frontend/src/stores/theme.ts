import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useThemeStore = defineStore('theme', () => {
  const theme = ref('light')
  const isDarkMode = computed(() => theme.value === 'dark')
  const isLightMode = computed(() => theme.value === 'light')

  function setTheme(selectedTheme: string) {
    switch (selectedTheme) {
      case 'light':
      case 'dark':
        theme.value = selectedTheme
        break
      case 'OS':
        if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
          theme.value = 'dark'
        } else {
          theme.value = 'light'
        }
      default:
        theme.value = 'dark'
    }
  }

  function initTheme() {
    if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
      theme.value = 'dark'
    } else {
      theme.value = 'light'
    }
  }

  return { theme, isDarkMode, isLightMode, setTheme, initTheme }
})
