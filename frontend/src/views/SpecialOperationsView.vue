<script setup lang="ts">
import SpecialOperationsCards from '@/components/special/SpecialOperationsCards.vue';
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <div>
        <h1 class="page-title"><span>⚡</span> Специальные операции</h1>
        <p class="page-subtitle">
          Бизнес-операции и расчеты, выполняемые на уровне хранимых процедур PostgreSQL
        </p>
      </div>
    </div>

    <SpecialOperationsCards />
  </div>
</template>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
}
</style>
