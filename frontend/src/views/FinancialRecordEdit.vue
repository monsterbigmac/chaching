<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from '@/config/axios.config'
import { AxiosError } from 'axios'
import FinancialRecordClassification from '@/components/FinancialRecordClassification.vue'
import DatePicker from '@/components/CustomDatePicker.vue'
import { useI18n } from '@/i18n'
import { useNotificationStore } from '@/stores/notification'

const route = useRoute()
const router = useRouter()
const { t } = useI18n()
const notificationStore = useNotificationStore()

const financialClassifications = ref<Array<{ id: number; name: string }>>([])

const selectedClassificationId = ref<number | null>(null)
const description = ref<string>('')
const amount = ref<number | null>(null)
const recordDate = ref<Date>(new Date())
const cashFlowType = ref<string>('expense')

const isEditMode = computed(() => {
  const recordId = route.params.id
  return recordId && typeof recordId === 'string'
})

const selectedClassificationName = computed(() => {
  const classification = financialClassifications.value.find(
    (c) => c.id === selectedClassificationId.value,
  )
  return classification ? t(`financialClassifications.${classification.name}`) : ''
})

function selectClassification(id: number) {
  selectedClassificationId.value = id
}

async function loadFinancialClassifications() {
  try {
    const { data: responseBody } = await axios({
      method: 'get',
      url: '/api/v1/financial-classifications/search',
      params: {
        page: 0,
        size: 100,
      },
    })
    financialClassifications.value = responseBody.data.financialClassifications || []

    if (selectedClassificationId.value === null && financialClassifications.value.length > 0) {
      selectedClassificationId.value = financialClassifications.value[0].id
    }
  } catch (e) {
    console.log(e)
  }
}

async function loadFinancialRecord(id: string) {
  try {
    const { data: responseBody } = await axios({
      method: 'get',
      url: `/api/v1/financial-records/${id}`,
    })
    const financialRecord = responseBody.data.financialRecord

    description.value = financialRecord.description
    amount.value = financialRecord.amount
    recordDate.value = new Date(financialRecord.recordDate)
    cashFlowType.value = financialRecord.cashFlowType

    const matchingClassification = financialClassifications.value.find(
      (c) => c.name === financialRecord.classification.name,
    )
    if (matchingClassification) {
      selectedClassificationId.value = matchingClassification.id
    }
  } catch (e) {
    console.log(e)
  }
}

async function submitRecord() {
  try {
    const recordId = route.params.id
    const requestBody = {
      description: description.value || selectedClassificationName.value,
      amount: amount.value || 0,
      recordDate: recordDate.value.toISOString(),
      classificationId: selectedClassificationId.value,
      cashFlowType: cashFlowType.value,
    }

    if (isEditMode.value) {
      await axios({
        method: 'patch',
        url: `/api/v1/financial-records/${recordId}`,
        data: requestBody,
      })
      notificationStore.showNotification(t('notifications.updated'), 'success')
    } else {
      await axios({
        method: 'post',
        url: '/api/v1/financial-records',
        data: requestBody,
      })
      notificationStore.showNotification(t('notifications.created'), 'success')
    }

    router.push('/')
  } catch (e) {
    if (e instanceof AxiosError) {
      const errorMessage = e.response?.data?.message || t('notifications.operationFailed')
      notificationStore.showNotification(errorMessage, 'error')
    } else {
      console.log(e)
    }
  }
}

async function deleteRecord() {
  try {
    const recordId = route.params.id
    if (isEditMode.value) {
      await axios({
        method: 'delete',
        url: `/api/v1/financial-records/${recordId}`,
      })
      notificationStore.showNotification(t('notifications.deleted'), 'success')
      router.push('/')
    }
  } catch (e) {
    if (e instanceof AxiosError) {
      const errorMessage = e.response?.data?.message || t('notifications.operationFailed')
      notificationStore.showNotification(errorMessage, 'error')
    } else {
      console.log(e)
    }
  }
}

async function init() {
  await loadFinancialClassifications()

  const recordId = route.params.id
  if (isEditMode.value && typeof recordId === 'string') {
    await loadFinancialRecord(recordId)
  }
}

init()
</script>

<template>
  <div class="financial-record-edit">
    <div class="record-classifications">
      <FinancialRecordClassification
        class="record-classification"
        v-for="classification in financialClassifications"
        :class="{ active: selectedClassificationId === classification.id }"
        :key="classification.id"
        :name="classification.name"
        :showName="true"
        @click="selectClassification(classification.id)"
      />
    </div>
    <div class="details">
      <div class="date-info">
        <DatePicker v-model:selectedDate="recordDate" />
      </div>
      <div class="description-and-amount">
        <div class="input-group">
          <label for="description">{{ t('general.description') }}</label>
          <input
            id="description"
            name="description"
            type="text"
            :placeholder="selectedClassificationName"
            v-model="description"
          />
        </div>
        <div class="input-group">
          <label for="amount">{{ t('general.amount') }}</label>
          <input
            id="amount"
            name="amount"
            type="number"
            inputmode="numeric"
            min="0"
            placeholder="0"
            v-model="amount"
          />
        </div>
      </div>
      <button @click="submitRecord" class="submit-button">{{ t('general.submit') }}</button>
      <button v-if="isEditMode" @click="deleteRecord" class="delete-button">
        {{ t('general.delete') }}
      </button>
    </div>
  </div>
</template>

<style scoped>
.financial-record-edit {
  width: 20rem;
  margin-left: auto;
  margin-right: auto;

  user-select: none;
  -webkit-user-select: none;
  -ms-user-select: none;

  .record-classifications {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    column-gap: 1rem;
    row-gap: 1rem;

    cursor: pointer;

    .record-classification {
      background: var(--gradient);
      border-radius: 10px;
      border: var(--border-card);
      border-top: solid 1px var(--highlight);

      &:hover {
        background: var(--gradient-hover);
      }

      &.active {
        background: var(--highlight);
        box-shadow: var(--shadow);
        border: none;
      }
    }
  }

  .details {
    margin-top: 15px;

    display: grid;
    gap: 15px;

    .date-info {
      width: 100%;
    }

    .description-and-amount {
      width: 100%;
      display: grid;
      gap: 15px;

      -webkit-tap-highlight-color: rgba(0, 0, 0, 0);

      .input-group {
        display: flex;
        flex-direction: column;
        gap: 0.5rem;

        label {
          font-size: 18px;
          color: var(--text);
          padding-left: 0.25rem;
        }
      }

      input {
        font-size: 18px;
        min-height: 2.5rem;
        width: 100%;
        box-sizing: border-box;
        background: var(--bg-light);
        border-radius: 10px;
        border: var(--border-card);
        caret-color: var(--text);
        color: var(--text);
        padding-left: 0.5rem;

        &:focus {
          outline: none;
        }

        &::placeholder {
          color: var(--text-muted);
        }
      }
    }

    .submit-button {
      width: 100%;
      padding: 0.75rem 1.5rem;
      font-size: 18px;
      background: var(--highlight);
      color: var(--text);
      border: none;
      border-radius: 10px;
      cursor: pointer;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 16px rgba(0, 0, 0, 0.4);
      }

      &:active {
        transform: translateY(0);
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
      }
    }

    .delete-button {
      width: 40px;
      background: none;
      border: none;
      color: var(--text-muted);
      font-size: 14px;
      cursor: pointer;
      padding: 0.5rem 0;
      text-align: left;

      &:hover {
        color: var(--text);
      }
    }
  }
}
</style>
