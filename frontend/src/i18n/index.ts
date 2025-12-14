import { ref, computed } from 'vue'
import { locales, type Locale } from './locales'

const currentLocale = ref<Locale>('zh-TW')

export function useI18n() {
  const locale = computed({
    get: () => currentLocale.value,
    set: (value: Locale) => {
      currentLocale.value = value
    },
  })

  const messages = computed(() => locales[currentLocale.value])

  function t(key: string): string {
    const keys = key.split('.')
    let result: unknown = messages.value

    for (const k of keys) {
      if (result && typeof result === 'object' && k in result) {
        result = (result as Record<string, unknown>)[k]
      } else {
        return key
      }
    }

    return typeof result === 'string' ? result : key
  }

  return {
    locale,
    t,
  }
}
