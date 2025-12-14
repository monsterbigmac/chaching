<script setup lang="ts">
import { useNotificationStore } from '@/stores/notification'

const notificationStore = useNotificationStore()

function handleClose() {
  notificationStore.hideNotification()
}
</script>

<template>
  <Transition name="notification">
    <div
      v-if="notificationStore.isVisible"
      :class="['notification', `notification--${notificationStore.type}`]"
      @click="handleClose"
    >
      <div class="notification__content">
        <span class="notification__message">{{ notificationStore.message }}</span>
        <button
          class="notification__close"
          type="button"
          aria-label="Close notification"
          @click.stop="handleClose"
        >
          ×
        </button>
      </div>
    </div>
  </Transition>
</template>

<style scoped>
.notification {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 9999;
  min-width: 300px;
  max-width: 500px;
  padding: 1rem 1.5rem;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  cursor: pointer;
  font-size: 0.875rem;
}

.notification__content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
}

.notification__message {
  flex: 1;
  color: white;
  line-height: 1.5;
}

.notification__close {
  background: none;
  border: none;
  color: white;
  font-size: 1.5rem;
  line-height: 1;
  cursor: pointer;
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: background-color 0.2s;
  flex-shrink: 0;
}

.notification__close:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

/* Notification type variants */
.notification--success {
  background-color: #10b981;
}

.notification--error {
  background-color: #ef4444;
}

.notification--warning {
  background-color: #f59e0b;
}

.notification--info {
  background-color: #3b82f6;
}

/* Transition animations */
.notification-enter-active,
.notification-leave-active {
  transition: all 0.3s ease;
}

.notification-enter-from {
  opacity: 0;
  transform: translateX(-50%) translateY(-20px);
}

.notification-leave-to {
  opacity: 0;
  transform: translateX(-50%) translateY(-20px);
}
</style>
