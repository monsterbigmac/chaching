import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  build: {
    outDir: 'dist',
    assetsDir: 'assets',
    manifest: false,
    emptyOutDir: true,
  },
  base: '/',
  server: {
    host: '0.0.0.0',
    port: 1212,
    proxy: {
      '^/api/.*': {
        target: 'http://127.0.0.1:1332',
        changeOrigin: true,
        secure: false,
      },
    }
  }
})
