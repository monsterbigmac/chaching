<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import axios from '@/config/axios.config'
import FinancialRecord from '@/components/FinancialRecord.vue'

const router = useRouter()

function formatDate(date: Date): string {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

const now = new Date()
const startOfMonth = new Date(now.getFullYear(), now.getMonth(), 1)
const endOfMonth = new Date(now.getFullYear(), now.getMonth() + 1, 0)
const searchParams = ref({
  startDate: formatDate(startOfMonth),
  endDate: formatDate(endOfMonth),
  page: 0,
  size: 10,
  sort: 'recordDate,desc',
})

function generateMonthOptions() {
  const options = []
  for (let month = 1; month <= 12; month++) {
    const date = new Date(2000, month - 1, 1)
    const label = date.toLocaleString(window.navigator.language, {
      month: 'long',
    })
    options.push({ value: month, label })
  }
  return options
}
const monthOptions = generateMonthOptions()

const selectedYear = ref(now.getFullYear())
const selectedMonth = ref(now.getMonth() + 1)

function previousYear() {
  selectedYear.value--
}

function nextYear() {
  selectedYear.value++
}
watch([selectedYear, selectedMonth], ([year, month]) => {
  const start = new Date(year, month - 1, 1)
  const end = new Date(year, month, 0)

  searchParams.value.startDate = formatDate(start)
  searchParams.value.endDate = formatDate(end)

  searchFinancialRecords()
})

interface FinancialRecord {
  id: number
  amount: number
  cashFlowType: string
  description: string
  recordDate: string
  classification: {
    name: string
  }
}
interface FinancialRecordsByDate {
  date: string
  financialRecords: Array<FinancialRecord>
}
const financialRecordsByDateInfos = ref<FinancialRecordsByDate[]>([])
async function searchFinancialRecords() {
  try {
    const { data: responseBody } = await axios({
      method: 'get',
      url: '/api/v1/financial-records/search',
      params: searchParams.value,
    })
    financialRecordsByDateInfos.value = responseBody.data.listByDate
  } catch (e) {
    console.log(e)
  }
}

function generateWeekDay(date: string) {
  return new Date(date).toLocaleString(window.navigator.language, {
    weekday: 'long',
  })
}

function navigateToEdit(financialRecordId: number) {
  router.push({ name: 'edit', params: { id: financialRecordId } })
}

function navigateToCreate() {
  router.push({ name: 'edit' })
}

searchFinancialRecords()
</script>

<template>
  <div class="financial-records-index">
    <div class="year-month-selector">
      <div class="year-navigation">
        <button @click="previousYear" class="year-arrow">&lt;</button>
        <span class="year-display">{{ selectedYear }}</span>
        <button @click="nextYear" class="year-arrow">&gt;</button>
      </div>
      <select v-model="selectedMonth" class="month-selector">
        <option v-for="option in monthOptions" :key="option.value" :value="option.value">
          {{ option.label }}
        </option>
      </select>
    </div>
    <div
      v-for="financialRecordsByDate in financialRecordsByDateInfos"
      :key="financialRecordsByDate.date"
      class="financial-records-by-date"
    >
      <div class="date">
        <div class="yyyy-mm-dd">{{ financialRecordsByDate.date }}</div>
        <div class="week">
          {{ generateWeekDay(financialRecordsByDate.date) }}
        </div>
      </div>
      <div class="financial-records">
        <FinancialRecord
          class="financial-record"
          v-for="financialRecord in financialRecordsByDate.financialRecords"
          :key="financialRecord.id"
          :amount="financialRecord.amount"
          :cashFlowType="financialRecord.cashFlowType"
          :description="financialRecord.description"
          :classification="financialRecord.classification.name"
          @click="navigateToEdit(financialRecord.id)"
        />
      </div>
    </div>
    <button class="fab-button" @click="navigateToCreate">+</button>
  </div>
</template>

<style scoped>
.financial-records-index {
  display: grid;
  position: relative;
  padding-bottom: 5rem;
}

.year-month-selector {
  display: flex;
  gap: 0.75rem;
  justify-content: center;
  margin: 1.5rem auto 0 auto;
  max-width: 20rem;

  .year-navigation {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.5rem;
    border: 2px solid var(--highlight);
    border-radius: 8px;
    background: var(--bg);

    .year-arrow {
      width: 2rem;
      height: 2rem;
      border: none;
      border-radius: 4px;
      background: var(--highlight);
      color: var(--text);
      font-size: 1.2rem;
      font-weight: bold;
      cursor: pointer;
      transition: all 0.2s ease;
      display: flex;
      align-items: center;
      justify-content: center;

      &:hover {
        transform: scale(1.1);
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
      }

      &:active {
        transform: scale(0.95);
      }
    }

    .year-display {
      min-width: 4rem;
      text-align: center;
      font-size: 1rem;
      font-weight: 600;
      color: var(--text);
    }
  }

  .month-selector {
    flex: 1;
    padding: 0.75rem 1rem;
    font-size: 1rem;
    font-weight: 600;
    border: 2px solid var(--highlight);
    border-radius: 8px;
    background: var(--bg);
    color: var(--text);
    cursor: pointer;
    transition: all 0.2s ease;

    &:hover {
      border-color: var(--highlight);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    &:focus {
      outline: none;
      border-color: var(--highlight);
      box-shadow: 0 0 0 3px rgba(var(--highlight-rgb), 0.1);
    }
  }
}

.financial-records-by-date {
  display: grid;
  gap: 1.5rem;
  margin: 1.5rem auto 1.5rem auto;
  max-width: 20rem;

  .date {
    display: flex;
    gap: 5px;

    .yyyy-mm-dd {
      font-weight: 900;
      font-size: 1.5rem;
      align-self: center;
    }

    .week {
      font-weight: 900;
      font-size: 1.5rem;
      align-self: center;
    }
  }

  .financial-records {
    display: grid;
    gap: 1rem;

    .financial-record {
      cursor: pointer;

      &:hover {
        border-radius: 10px;
        background: var(--bg);
      }
    }
  }
}

.fab-button {
  position: fixed;
  bottom: 2rem;
  right: max(2rem, calc(50vw - 180px + 2rem));
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: var(--highlight);
  border: none;
  color: var(--text);
  font-size: 2rem;
  font-weight: 300;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  transition: all 0.3s ease;

  &:hover {
    transform: scale(1.1);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.4);
  }

  &:active {
    transform: scale(0.95);
  }
}
</style>
