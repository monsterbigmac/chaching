<script setup lang="ts">
import { RouterLink, RouterView, useRoute } from 'vue-router'
import { computed, onMounted } from 'vue'
import { useCsrfStore } from '@/stores/csrf'
import { useAuthStore } from '@/stores/auth'
import { useI18n } from '@/i18n'
import AppNotification from '@/components/AppNotification.vue'

document.body.classList.toggle('light')

const { t } = useI18n()

const csrfStore = useCsrfStore()
onMounted(() => {
  csrfStore.initCsrfToken()
})

const route = useRoute()
interface BreadcrumbItem {
  label: string
  path: string
}
const breadcrumbs = computed<BreadcrumbItem[]>(() => {
  const items: BreadcrumbItem[] = []

  if (route.path === '/') {
    items.push({ label: t('general.index'), path: '/' })
  } else if (route.path === '/edit' || route.path.startsWith('/edit/')) {
    items.push({ label: t('general.index'), path: '/' })
    items.push({ label: t('general.edit'), path: route.path })
  }

  return items
})

const authStore = useAuthStore()
const showNavigation = computed(() => authStore.isAuthenticated && route.name !== 'login')
</script>

<template>
  <div class="app-container">
    <AppNotification />

    <nav v-if="showNavigation && breadcrumbs.length > 0" class="breadcrumb" aria-label="Breadcrumb">
      <ol>
        <li v-for="(item, index) in breadcrumbs" :key="item.path">
          <RouterLink v-if="index < breadcrumbs.length - 1" :to="item.path">
            {{ item.label }}
          </RouterLink>
          <span v-else class="current">{{ item.label }}</span>
          <span v-if="index < breadcrumbs.length - 1" class="separator">/</span>
        </li>
      </ol>
    </nav>

    <RouterView />
  </div>
</template>

<style scoped>
.app-container {
  width: 100%;
  max-width: 360px;
  min-height: 100vh;
  margin: 0 auto;
  border-radius: 10px;
  background-color: var(--bg-dark);
}

@media screen and (max-width: 480px) {
  .app-container {
    max-width: 100%;
    background-color: var(--bg-dark);
  }
}

.breadcrumb {
  padding: 0.75rem 0;
  margin: 0 1rem;
  border-bottom: 1px solid var(--color-border);
}

.breadcrumb ol {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 0.5rem;
}

.breadcrumb li {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
}

.breadcrumb a {
  color: var(--color-text);
  text-decoration: none;
  opacity: 0.7;
  transition: opacity 0.2s;
}

.breadcrumb a:hover {
  opacity: 1;
  text-decoration: underline;
}

.breadcrumb .current {
  color: var(--color-text);
  font-weight: 500;
}

.breadcrumb .separator {
  opacity: 0.5;
  user-select: none;
}
</style>
