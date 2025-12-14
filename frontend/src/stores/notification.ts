import { ref } from 'vue'
import { defineStore } from 'pinia'

export type NotificationType = 'success' | 'error' | 'warning' | 'info'

export const useNotificationStore = defineStore('notification', () => {
  const message = ref<string>('')
  const type = ref<NotificationType>('info')
  const isVisible = ref<boolean>(false)
  const timerId = ref<number | null>(null)

  function showNotification(
    notificationMessage: string,
    notificationType: NotificationType = 'info',
    duration: number = 5000,
  ) {
    // Clear existing timer if any
    if (timerId.value !== null) {
      clearTimeout(timerId.value)
      timerId.value = null
    }

    // Set notification data
    message.value = notificationMessage
    type.value = notificationType
    isVisible.value = true

    // Set auto-dismiss timer
    timerId.value = window.setTimeout(() => {
      hideNotification()
    }, duration)
  }

  function hideNotification() {
    isVisible.value = false

    // Clear timer
    if (timerId.value !== null) {
      clearTimeout(timerId.value)
      timerId.value = null
    }

    // Clear message after transition
    setTimeout(() => {
      message.value = ''
    }, 300)
  }

  return {
    message,
    type,
    isVisible,
    showNotification,
    hideNotification,
  }
})
