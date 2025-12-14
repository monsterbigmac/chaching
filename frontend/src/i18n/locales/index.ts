import en from './en'
import zhTW from './zh-TW'

export const locales = {
  en,
  'zh-TW': zhTW,
}

export type Locale = keyof typeof locales
