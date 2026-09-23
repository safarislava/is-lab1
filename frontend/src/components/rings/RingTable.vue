<script setup lang="ts">
import type { RingResponse } from '@/types/ring';

defineProps<{
  rings: RingResponse[];
  loading?: boolean;
}>();

const emit = defineEmits<{
  (e: 'edit', ring: RingResponse): void;
}>();
</script>

<template>
  <div class="table-container">
    <table class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Название артефакта</th>
          <th>Магическая сила</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="loading && rings.length === 0">
          <td colspan="3" class="empty-cell">
            <div class="spinner-center">
              <span class="spinner" />
              <span>Загрузка колец...</span>
            </div>
          </td>
        </tr>

        <tr v-else-if="rings.length === 0">
          <td colspan="3" class="empty-cell">
            <p>Кольца не найдены. Создайте первое кольцо всевластия!</p>
          </td>
        </tr>

        <tr
          v-for="ring in rings"
          :key="ring.id"
          class="clickable-row"
          title="Нажмите для редактирования"
          @click="emit('edit', ring)"
        >
          <td>
            <span class="id-badge">#{{ ring.id }}</span>
          </td>
          <td>
            <span class="ring-name">💍 {{ ring.name }}</span>
          </td>
          <td>
            <span class="power-badge">⚡ {{ ring.power.toLocaleString() }}</span>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>
.clickable-row {
  cursor: pointer;
  transition: background-color 0.15s ease;
}

.clickable-row:hover {
  background: var(--bg-card-hover) !important;
}

.id-badge {
  font-family: monospace;
  color: var(--text-dim);
  font-weight: 600;
}

.ring-name {
  font-weight: 600;
  color: #f59e0b;
}

.power-badge {
  background: rgba(245, 158, 11, 0.15);
  color: #fbbf24;
  border: 1px solid rgba(245, 158, 11, 0.3);
  padding: 0.2rem 0.6rem;
  border-radius: var(--radius-sm);
  font-weight: 600;
  font-size: 0.85rem;
}

.empty-cell {
  text-align: center;
  padding: 3rem 1rem !important;
  color: var(--text-muted);
}

.spinner-center {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
}
</style>
