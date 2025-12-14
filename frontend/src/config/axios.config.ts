import axios from 'axios';
import { useCsrfStore } from '@/stores/csrf';
import { useAuthStore } from '@/stores/auth';
import router from '@/router';

const instance = axios.create({
  baseURL: import.meta.env.VITE_AXIOS_BASE_URL
});

instance.interceptors.request.use(
  (config) => {
    if (config.url === '/api/csrf') {
      return config;
    }

    const methodsRequiringCsrf = ['post', 'patch', 'put', 'delete'];
    const method = config.method?.toLowerCase();

    if (method && methodsRequiringCsrf.includes(method)) {
      const csrfStore = useCsrfStore();
      if (csrfStore.csrfToken) {
        config.headers['X-CSRF-TOKEN'] = csrfStore.csrfToken;
      }
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

instance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response?.status === 401) {
      const authStore = useAuthStore();
      authStore.logout();

      if (router.currentRoute.value.name !== 'login') {
        router.push({ name: 'login' });
      }
    }
    return Promise.reject(error);
  }
);

export default instance;
