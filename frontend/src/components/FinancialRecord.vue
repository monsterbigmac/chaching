<script setup lang="ts">
import FinancialRecordClassification from '@/components/FinancialRecordClassification.vue'
import { useI18n } from '@/i18n'

defineProps<{
  amount: number
  cashFlowType: string
  description: string
  classification: string
}>()

const { t } = useI18n()
</script>

<template>
  <div class="financial-record">
    <div class="icon">
      <FinancialRecordClassification :name="classification" :showName="false" />
    </div>
    <div class="info">
      <div class="description">{{ description }}</div>
      <div class="classification">{{ t(`financialClassifications.${classification}`) }}</div>
    </div>
    <div class="amount">{{ cashFlowType === 'expense' ? '-' : '+' }}${{ amount }}</div>
  </div>
</template>

<style scoped>
.financial-record {
  display: grid;
  grid-template-columns: 1fr 3fr 1fr;
  gap: 10px;
  min-height: 5rem;

  .icon {
    border-radius: 10px;
    background: var(--bg);
    align-self: center;
    text-align: center;
  }

  .info {
    display: grid;

    .description {
      font-size: 1.25rem;
      color: var(--text);
      align-self: first baseline;
      margin-top: 5px;
    }

    .classification {
      font-size: 1.125rem;
      color: var(--text-muted);
      align-self: last baseline;
      margin-bottom: 5px;
    }
  }

  .amount {
    font-size: 1.125rem;
    align-self: center;
    text-align: right;
  }
}
</style>
