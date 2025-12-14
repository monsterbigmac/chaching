<script setup lang="ts">
import { useThemeStore } from '@/stores/theme'
import VueDatePicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'
import LeftArrow from '@/components/icons/LeftArrow.vue'
import RightArrow from '@/components/icons/RightArrow.vue'

const themeStore = useThemeStore()

const selectedDate = defineModel<Date>('selectedDate', { required: true })

function goToDate(days: number) {
  const jumpToDate = new Date(selectedDate.value)
  jumpToDate.setDate(selectedDate.value.getDate() + days)
  selectedDate.value = jumpToDate
}
</script>

<template>
  <div class="custom-date-picker">
    <LeftArrow class="arrow" @click="goToDate(-1)" />
    <VueDatePicker
      ref="datepicker"
      v-model="selectedDate"
      :format="'yyyy-MM-dd'"
      :dark="themeStore.isDarkMode"
      :vertical="true"
      :enable-time-picker="false"
      :clearable="false"
      :auto-apply="true"
      :teleport-center="true"
    >
      <template #dp-input="{ value }">
        <input class="custom-input-class" :value="value" />
      </template>
    </VueDatePicker>
    <RightArrow class="arrow" @click="goToDate(1)" />
  </div>
</template>

<style scoped>
.custom-date-picker {
  display: flex;
  box-sizing: border-box;
  background: var(--bg-light);
  border: var(--border-card);
  border-radius: 10px;

  cursor: pointer;

  .arrow {
    min-height: 2.5rem;
    fill: var(--text);
  }

  .custom-input-class {
    width: 90%;
    font-size: 1.125rem;
    min-height: 2.5rem;
    text-align: center;
    background: var(--bg-light);
    border: none;
    color: var(--text-muted);

    cursor: pointer;

    &:focus {
      outline: none;
    }
  }
}

.custom-date-picker div:last-child {
  margin-left: auto;
}
</style>
