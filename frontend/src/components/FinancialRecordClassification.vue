<script setup lang="ts">
import Food from '@/components/icons/financial_classifications/FoodClassification.vue'
import Transport from '@/components/icons/financial_classifications/TransportClassification.vue'
import Leisure from '@/components/icons/financial_classifications/LeisureClassification.vue'
import Other from '@/components/icons/financial_classifications/OtherClassification.vue'
import Shopping from '@/components/icons/financial_classifications/ShoppingClassification.vue'
import Bill from '@/components/icons/financial_classifications/BillClassification.vue'
import Housing from '@/components/icons/financial_classifications/HousingClassification.vue'
import Question from '@/components/icons/financial_classifications/QuestionClassification.vue'
import { useI18n } from '@/i18n'

defineProps({
  name: { type: String, required: true },
  showName: { type: Boolean, required: true },
})

const { t } = useI18n()

const iconMap = {
  Food,
  Transport,
  Leisure,
  Other,
  Shopping,
  Bill,
  Housing,
  Question,
}
</script>

<template>
  <div class="financial-record-classification">
    <div class="icon">
      <component :is="iconMap[name as keyof typeof iconMap]"></component>
    </div>

    <div v-if="showName" class="name">{{ t(`financialClassifications.${name}`) }}</div>
  </div>
</template>

<style scoped>
.financial-record-classification {
  padding: 8px;
  width: 4.8rem;
  height: 4.8rem;
  justify-self: center;

  display: grid;

  .icon {
    align-self: center;
    text-align: center;

    svg {
      fill: var(--text);
    }
  }

  .name {
    align-self: first baseline;
    text-align: center;
  }
}
</style>
