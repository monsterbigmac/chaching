<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')

async function handleLogin() {
  if (!username.value || !password.value) {
    authStore.error = 'Please enter both username and password'
    return
  }

  authStore.clearError()
  const success = await authStore.login(username.value, password.value)

  if (success) {
    router.push('/')
  }
}

function handleKeyPress(event: KeyboardEvent) {
  if (event.key === 'Enter') {
    handleLogin()
  }
}
</script>

<template>
  <div class="login-container">
    <div class="login-card">
      <h1 class="login-title">ChaChing</h1>
      <p class="login-subtitle">Sign in to your account</p>

      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="username">Username</label>
          <input
            id="username"
            v-model="username"
            type="text"
            autocomplete="username"
            placeholder="Enter your username"
            :disabled="authStore.isLoading"
            @keypress="handleKeyPress"
            required
          />
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <input
            id="password"
            v-model="password"
            type="password"
            autocomplete="current-password"
            placeholder="Enter your password"
            :disabled="authStore.isLoading"
            @keypress="handleKeyPress"
            required
          />
        </div>

        <div v-if="authStore.error" class="error-message">
          {{ authStore.error }}
        </div>

        <button
          type="submit"
          class="login-button"
          :disabled="authStore.isLoading || !username || !password"
        >
          {{ authStore.isLoading ? 'Signing in...' : 'Sign in' }}
        </button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1rem;
  background-color: var(--bg-dark);
}

.login-card {
  width: 100%;
  max-width: 400px;
  padding: 2rem;
  background-color: var(--bg-light);
  border-radius: 8px;
  border: var(--border-card);
  box-shadow: var(--shadow);
}

.login-title {
  margin: 0 0 0.5rem 0;
  font-size: 2rem;
  font-weight: 700;
  text-align: center;
  color: var(--text);
}

.login-subtitle {
  margin: 0 0 2rem 0;
  text-align: center;
  color: var(--text-muted);
  font-size: 0.875rem;
}

.loading-message {
  text-align: center;
  padding: 2rem;
  color: var(--text-muted);
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--text);
}

.form-group input {
  padding: 0.75rem;
  font-size: 1rem;
  border: 1px solid var(--border);
  border-radius: 6px;
  background-color: var(--bg);
  color: var(--text);
  transition: border-color 0.2s;
}

.form-group input:focus {
  outline: none;
  border-color: var(--primary);
}

.form-group input:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.error-message {
  padding: 0.75rem;
  background-color: var(--bg);
  border: 1px solid var(--danger);
  border-radius: 6px;
  color: var(--danger);
  font-size: 0.875rem;
  text-align: center;
}

.login-button {
  padding: 0.875rem;
  font-size: 1rem;
  font-weight: 600;
  color: var(--bg-dark);
  background-color: var(--success);
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: opacity 0.2s;
}

.login-button:hover:not(:disabled) {
  opacity: 0.8;
}

.login-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
